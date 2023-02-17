package me.swerve.listener;

import me.swerve.cosmetic.Cosmetic;
import me.swerve.permission.Permission;
import me.swerve.player.CorePlayer;
import me.swerve.punishment.Punishment;
import me.swerve.rank.Rank;
import me.swerve.util.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageListener implements Listener {

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent e) {
        CorePlayer p = CorePlayer.getCorePlayers().get(e.getPlayer().getUniqueId());

        Punishment activePunishment = p.getPunishmentProfile().getActive(Punishment.PunishmentType.MUTE);
        if (activePunishment != null) {
            e.getPlayer().sendMessage(activePunishment.getMuteMessage());
            e.setCancelled(true);
            return;
        }


        if (p.isWaitingForInput()) {
            e.setCancelled(true);
            p.setWaitingForInput(false);

            Permission toEdit = p.getPermissionToEdit();
            p.setPermissionToEdit(null);

            String message = e.getMessage().split(" ")[0].toLowerCase();

            if (message.contains("cancel")) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have canceled this action."));
                return;
            }

            if (message.contains("delete")) {
                Permission.getPermissions().remove(toEdit);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have deleted permission: " + toEdit.getPermissionName()));
                return;
            }

            if (NumberUtil.isInteger(message, 10)) {
                int newPermissionLevel = Integer.parseInt(message);

                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have updated the permission level from " + toEdit.getPermissionLevel() + " to " + newPermissionLevel));
                toEdit.setPermissionLevel(newPermissionLevel);

                return;
            }

            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have updated the permission " + toEdit.getPermissionName() + " to " + message));
            toEdit.setPermissionName(message);

            return;
        }

        String prefix = "";
        if (p.getCosmeticsProfile().getCurrentPrefixID() != 0) prefix = Cosmetic.getCosmetic(Cosmetic.CosmeticType.PREFIX, p.getCosmeticsProfile().getCurrentPrefixID()).getPrefix() + " ";

        String rankPrefix = Rank.getRanks().get(p.getRankProfile().getRankName()).getPrefix() + " ";
        if (rankPrefix.length() == 1) rankPrefix = "";

        String nameColor = Rank.getRanks().get(p.getRankProfile().getRankName()).getNameColor();
        if (p.getCosmeticsProfile().getCurrentNameColorID() != 0) nameColor = Cosmetic.getCosmetic(Cosmetic.CosmeticType.NAME_COLOR, p.getCosmeticsProfile().getCurrentNameColorID()).getNameColor();

        String suffix = "";
        if (p.getCosmeticsProfile().getCurrentSuffixID() != 0) suffix = " " + Cosmetic.getCosmetic(Cosmetic.CosmeticType.SUFFIX, p.getCosmeticsProfile().getCurrentSuffixID()).getSuffix();

        String chatColor = "";
        if (p.getCosmeticsProfile().getCurrentChatColorID() != 0) chatColor = Cosmetic.getCosmetic(Cosmetic.CosmeticType.CHAT_COLOR, p.getCosmeticsProfile().getCurrentChatColorID()).getChatColor();

        e.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + rankPrefix + nameColor + "%s" + suffix + "&f: " + chatColor + "%s"));
    }
}
