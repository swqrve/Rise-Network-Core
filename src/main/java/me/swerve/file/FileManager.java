package me.swerve.file;

import me.swerve.RiseCore;
import me.swerve.cosmetic.Cosmetic;
import me.swerve.permission.Permission;
import me.swerve.player.CorePlayer;
import me.swerve.punishment.Punishment;
import me.swerve.rank.Rank;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Collections;
import java.util.UUID;

public class FileManager {

    public static Document getPlayerDocument(UUID uuid) {
        File dataFolder = getDataFolder();

        File playerFile = new File(dataFolder.getPath() + "/" + uuid.toString() + "-info.json");
        Document document = new Document("uuid", uuid.toString()).append("uuid", uuid.toString());

        if(playerFile.exists()) document = FileUtility.readFromFile(playerFile);

        if(document.get("rank") == null) {
            document.append("rank", "Default");

            document.append("prefixID", 0);
            document.append("suffixID", 0);
            document.append("chatColorID", 0);
            document.append("nameColorID", 0);

            document.append("punishments", 0);

            document.append("coins", 0);

            document.append("owned-prefixes", Collections.emptyList());
            document.append("owned-suffixes", Collections.emptyList());
            document.append("owned-chatColors", Collections.emptyList());
            document.append("owned-nameColors", Collections.emptyList());
        }

        return document;
    }

    public static void savePlayerDocument(UUID uuid) {
        Document document = new Document("uuid", uuid.toString()).append("uuid", uuid.toString());

        File dataFolder = getDataFolder();

        CorePlayer player = CorePlayer.getCorePlayers().get(uuid);

        player.getRankProfile().save(document);
        player.getCosmeticsProfile().save(document);
        player.getPunishmentProfile().save(document);

        document.append("coins", player.getCoins());

        File playerFile = new File(dataFolder.getPath() + "/" + uuid + "-info.json");
        FileUtility.write(playerFile, document);
    }

    public static void savePlayerDocument(UUID uuid, String newRank) {
        Document document = getPlayerDocument(uuid);
        File dataFolder = getDataFolder();

        document.remove("rank");
        document.append("rank", newRank);

        File playerFile = new File(dataFolder.getPath() + "/" + uuid.toString() + "-info.json");
        FileUtility.write(playerFile, document);
    }

    public static void savePlayerDocument(UUID uuid, Punishment newPunishment) {
        Document document = getPlayerDocument(uuid);
        File dataFolder = getDataFolder();

        int punishments = document.getInteger("punishments") + 1;
        document.remove("punishments");
        document.append("punishments", punishments);

        String punishmentTitle = "punishment-" + punishments + "-";

        document.append(punishmentTitle + "type", newPunishment.getType().toString().toLowerCase());
        document.append(punishmentTitle + "date", newPunishment.getPunishmentDate());
        document.append(punishmentTitle + "timeformat", newPunishment.getTimeFormat());
        document.append(punishmentTitle + "reason", newPunishment.getReason());
        document.append(punishmentTitle + "expires", newPunishment.isExpires());
        document.append(punishmentTitle + "time", newPunishment);
        document.append(punishmentTitle + "punisher", newPunishment.getPunisherName());
        document.append(punishmentTitle + "expired", newPunishment.isExpired());

        File playerFile = new File(dataFolder.getPath() + "/" + uuid.toString() + "-info.json");
        FileUtility.write(playerFile, document);
    }


    public static void loadPermissions() {
        File dataFolder = getDataFolder();

        File permissionsFile = new File(dataFolder + "/permissions.json");
        Document permissionsDoc = new Document("permissions", "permissions");

        if (permissionsFile.exists()) permissionsDoc = FileUtility.readFromFile(permissionsFile);

        int permissionsCount = 0;
        if (permissionsDoc.getInteger("permissionCount") != null) permissionsCount = permissionsDoc.getInteger("permissionCount");

        for (int i = 0; i < permissionsCount; i++) {
            String title = "permission-" + i + "-";

            new Permission(
                    permissionsDoc.getInteger(title + "level"),
                    permissionsDoc.getString(title + "name")
            );
        }
    }

    public static void savePermissions() {
        File dataFolder = getDataFolder();

        Document permissionsDoc = new Document("permissions", "permissions");
        File permissionsFile = new File(dataFolder + "/permissions.json");

        int permissionCount = Permission.getPermissions().size();

        for (int i = 0; i < permissionCount; i++) {
            String title = "permission-" + i + "-";

            permissionsDoc.append(title + "level", Permission.getPermissions().get(i).getPermissionLevel());
            permissionsDoc.append(title + "name", Permission.getPermissions().get(i).getPermissionName());
        }

        permissionsDoc.append("permissionCount", permissionCount);


        FileUtility.write(permissionsFile, permissionsDoc);
    }

    public static void loadRanks() {
        File dataFolder = getDataFolder();

        File rankFile = new File(dataFolder + "/ranks.json");
        Document rankDoc = new Document("rank", "rank");

        if (rankFile.exists()) rankDoc = FileUtility.readFromFile(rankFile);

        int rankCount = 0;
        if (rankDoc.getInteger("rankCount") != null) rankCount = rankDoc.getInteger("rankCount");

        for (int i = 0; i < rankCount; i++) {
            String title = "rank-" + i + "-";

            new Rank(
                    rankDoc.getString(title + "prefix"),
                    rankDoc.getString(title + "nameColor"),
                    rankDoc.getString(title + "name"),
                    rankDoc.getInteger(title + "level")
            );
        }
    }
    public static void loadCosmetics() {
        File dataFolder = getDataFolder();

        File cosmeticFile = new File(dataFolder + "/cosmetics.json");
        Document cosmeticDoc = new Document("cosmetics", "cosmetics");

        if (cosmeticFile.exists()) cosmeticDoc = FileUtility.readFromFile(cosmeticFile);

        int cosmeticCount = 0;
        if (cosmeticDoc.getInteger("cosmeticCount") != null) cosmeticCount = cosmeticDoc.getInteger("cosmeticCount");

        for (int i = 0; i < cosmeticCount; i++) {
            String title = "cosmetic-" + i + "-";

            new Cosmetic(
                    Cosmetic.CosmeticType.valueOf (
                            cosmeticDoc.getString(title + "type")),
                            cosmeticDoc.getString(title + "value"),
                            cosmeticDoc.getInteger(title + "id")
            );
        }
    }

    private static File getDataFolder() {
        File dataFolder = new File("C:\\Users\\Administrator\\Desktop\\Rise Network\\rSpigot Servers");
        dataFolder = new File(dataFolder.getPath() + "/data/");

        if(!dataFolder.exists()) dataFolder.mkdirs();

        return dataFolder;
    }

}
