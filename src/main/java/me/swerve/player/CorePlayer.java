package me.swerve.player;

import lombok.Getter;
import lombok.Setter;
import me.swerve.RiseCore;
import me.swerve.file.FileManager;
import me.swerve.permission.Permission;
import me.swerve.profile.type.CosmeticProfile;
import me.swerve.profile.type.PunishmentProfile;
import me.swerve.profile.type.RankProfile;
import me.swerve.punishment.Punishment;
import me.swerve.rank.Rank;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class CorePlayer {
    @Getter private static final Map<UUID, CorePlayer> corePlayers = new HashMap<>();

    @Getter private final UUID uuid;

    @Getter private final CosmeticProfile cosmeticsProfile = new CosmeticProfile();
    @Getter private final RankProfile rankProfile = new RankProfile();
    @Getter private final PunishmentProfile punishmentProfile = new PunishmentProfile();

    private final PermissionAttachment permissionAttachment;

    @Getter @Setter private UUID lastSpokenUser;

    @Getter @Setter private boolean waitingForInput;
    @Getter @Setter private Permission permissionToEdit;

    @Getter @Setter private int coins;

    public CorePlayer(Player p, UUID uuid) {
        this.uuid = uuid;

        Document playerDoc = FileManager.getPlayerDocument(uuid);

        updateCosmeticProfile(playerDoc);
        updateRankProfile(playerDoc);
        updatePunishmentProfile(playerDoc);

        coins = playerDoc.getInteger("coins");

        permissionAttachment = p.addAttachment(RiseCore.getInstance());

        setPermissions(Rank.getRanks().get(rankProfile.getRankName()).getPermissionLevel());

        corePlayers.put(uuid, this);
    }
    private void updateRankProfile(Document doc) {
        rankProfile.setRankName(doc.getString("rank"));
    }

    private void updateCosmeticProfile(Document doc) {
        cosmeticsProfile.setCurrentPrefixID(doc.getInteger("prefixID"));
        cosmeticsProfile.setCurrentSuffixID(doc.getInteger("suffixID"));
        cosmeticsProfile.setCurrentChatColorID(doc.getInteger("chatColorID"));
        cosmeticsProfile.setCurrentNameColorID(doc.getInteger("nameColorID"));

        cosmeticsProfile.getOwnedPrefixes().addAll(doc.getList("owned-prefixes", Integer.class));
        cosmeticsProfile.getOwnedSuffixes().addAll(doc.getList("owned-suffixes", Integer.class));
        cosmeticsProfile.getOwnedChatColors().addAll(doc.getList("owned-chatColors", Integer.class));
        cosmeticsProfile.getOwnedNameColors().addAll(doc.getList("owned-nameColors", Integer.class));
    }

    private void updatePunishmentProfile(Document doc) {
        int punishmentCount = doc.getInteger("punishments");

        for (int punishmentID = 1; punishmentID <= punishmentCount; punishmentID++) {
            String punishmentTitle = "punishment-" + punishmentID + "-";

            punishmentProfile.getPunishments().add(new Punishment(
                    Punishment.PunishmentType.valueOf(doc.getString(punishmentTitle + "type").toUpperCase()),
                    doc.getDate(punishmentTitle + "date"),
                    doc.getInteger(punishmentTitle + "timeformat"),
                    doc.getString(punishmentTitle + "reason"),
                    doc.getBoolean(punishmentTitle + "expires"),
                    doc.getInteger(punishmentTitle + "time"),
                    doc.getString(punishmentTitle + "punisher"),
                    doc.getBoolean(punishmentTitle + "expired")
                    ));
        }
    }

    public void updateRank(String rankName) {
        removePermissions(Rank.getRanks().get(rankProfile.getRankName()).getPermissionLevel());

        rankProfile.setRankName(rankName);
        setPermissions(Rank.getRanks().get(rankProfile.getRankName()).getPermissionLevel());
    }

    public void removeAttachment() {
        Bukkit.getPlayer(uuid).removeAttachment(permissionAttachment);
    }
    private void setPermissions(int permissionLevel) {
        for (Permission p : Permission.getPermissions()) if (p.getPermissionLevel() <= permissionLevel) permissionAttachment.setPermission(p.getPermissionName(), true);
    }

    private void removePermissions(int permissionLevel) {
        for (Permission p : Permission.getPermissions()) if (p.getPermissionLevel() <= permissionLevel) permissionAttachment.unsetPermission(p.getPermissionName());
    }

    public void save() {
        FileManager.savePlayerDocument(uuid);
    }
}
