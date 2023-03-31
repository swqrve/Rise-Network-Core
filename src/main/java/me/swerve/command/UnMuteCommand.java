package me.swerve.command;

import me.swerve.file.FileManager;
import me.swerve.player.CorePlayer;
import me.swerve.profile.type.PunishmentProfile;
import me.swerve.punishment.Punishment;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class UnMuteCommand implements CommandExecutor {
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

        UUID toUnMute = null;
        String playerName = null;
        if (Bukkit.getPlayer(args[0]) != null) {
            toUnMute = Bukkit.getPlayer(args[0]).getUniqueId();
            playerName = Bukkit.getPlayer(args[0]).getName();
        }

        if (Bukkit.getOfflinePlayer(args[0]) != null) {
            toUnMute = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            playerName = Bukkit.getOfflinePlayer(args[0]).getName();
        }

        if (CorePlayer.getCorePlayers().get(toUnMute) != null) {
            CorePlayer p = CorePlayer.getCorePlayers().get(toUnMute);
            if (p.getPunishmentProfile().getActive(Punishment.PunishmentType.MUTE) == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThey don't have an active mute!"));
                return false;
            }

            p.getPunishmentProfile().getActive(Punishment.PunishmentType.MUTE).setExpired(true);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + playerName + " has been unmuted!"));
            return false;
        }

        Document mutedPlayerDoc = FileManager.getPlayerDocument(toUnMute);
        int punishmentCount = mutedPlayerDoc.getInteger("punishments");

        PunishmentProfile bannedPlayerProfile = new PunishmentProfile();
        for (int punishmentID = 1; punishmentID <= punishmentCount; punishmentID++) {
            String punishmentTitle = "punishment-" + punishmentID + "-";

            bannedPlayerProfile.getPunishments().add(new Punishment(
                    Punishment.PunishmentType.valueOf(mutedPlayerDoc.getString(punishmentTitle + "type").toUpperCase()),
                    mutedPlayerDoc.getDate(punishmentTitle + "date"),
                    mutedPlayerDoc.getInteger(punishmentTitle + "timeformat"),
                    mutedPlayerDoc.getString(punishmentTitle + "reason"),
                    mutedPlayerDoc.getBoolean(punishmentTitle + "expires"),
                    mutedPlayerDoc.getInteger(punishmentTitle + "time"),
                    mutedPlayerDoc.getString(punishmentTitle + "punisher"),
                    mutedPlayerDoc.getBoolean(punishmentTitle + "expired")
            ));
        }

        if (bannedPlayerProfile.getActive(Punishment.PunishmentType.MUTE) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThey don't have an active mute!"));
            return false;
        }

        bannedPlayerProfile.getActive(Punishment.PunishmentType.MUTE).setExpired(true);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + playerName + " has been unmuted!"));

        bannedPlayerProfile.save(mutedPlayerDoc);

        FileManager.savePlayerDocument(toUnMute, mutedPlayerDoc);

        return false;
    }
}

