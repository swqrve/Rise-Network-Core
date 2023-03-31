package me.swerve.command;

import me.swerve.file.FileManager;
import me.swerve.player.CorePlayer;
import me.swerve.profile.type.PunishmentProfile;
import me.swerve.punishment.Punishment;
import me.swerve.util.NumberUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class UnBanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        if (!sender.hasPermission("core.punish")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo permission."));
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null && Bukkit.getOfflinePlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by that name: "+ args[0]));
            return false;
        }

        UUID toUnBan = null;
        String playerName = null;
        if (Bukkit.getPlayer(args[0]) != null) {
            toUnBan = Bukkit.getPlayer(args[0]).getUniqueId();
            playerName = Bukkit.getPlayer(args[0]).getName();
        }

        if (Bukkit.getOfflinePlayer(args[0]) != null) {
            toUnBan = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            playerName = Bukkit.getOfflinePlayer(args[0]).getName();
        }

        if (CorePlayer.getCorePlayers().get(toUnBan) != null) {
            CorePlayer p = CorePlayer.getCorePlayers().get(toUnBan);
            if (p.getPunishmentProfile().getActive(Punishment.PunishmentType.BAN) == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThey don't have an active ban!"));
                return false;
            }

            p.getPunishmentProfile().getActive(Punishment.PunishmentType.BAN).setExpired(true);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + playerName + " has been unbanned!"));
            return false;
        }

        Document bannedPlayerDoc = FileManager.getPlayerDocument(toUnBan);
        int punishmentCount = bannedPlayerDoc.getInteger("punishments");

        PunishmentProfile bannedPlayerProfile = new PunishmentProfile();
        for (int punishmentID = 1; punishmentID <= punishmentCount; punishmentID++) {
            String punishmentTitle = "punishment-" + punishmentID + "-";

            bannedPlayerProfile.getPunishments().add(new Punishment(
                    Punishment.PunishmentType.valueOf(bannedPlayerDoc.getString(punishmentTitle + "type").toUpperCase()),
                    bannedPlayerDoc.getDate(punishmentTitle + "date"),
                    bannedPlayerDoc.getInteger(punishmentTitle + "timeformat"),
                    bannedPlayerDoc.getString(punishmentTitle + "reason"),
                    bannedPlayerDoc.getBoolean(punishmentTitle + "expires"),
                    bannedPlayerDoc.getInteger(punishmentTitle + "time"),
                    bannedPlayerDoc.getString(punishmentTitle + "punisher"),
                    bannedPlayerDoc.getBoolean(punishmentTitle + "expired")
            ));
        }

        if (bannedPlayerProfile.getActive(Punishment.PunishmentType.BAN) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThey don't have an active ban!"));
            return false;
        }

        bannedPlayerProfile.getActive(Punishment.PunishmentType.BAN).setExpired(true);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + playerName + " has been unbanned!"));

        bannedPlayerProfile.save(bannedPlayerDoc);

        FileManager.savePlayerDocument(toUnBan, bannedPlayerDoc);

        return false;
    }
}

