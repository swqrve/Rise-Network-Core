package me.swerve.command;

import me.swerve.file.FileManager;
import me.swerve.player.CorePlayer;
import me.swerve.punishment.Punishment;
import me.swerve.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class WarnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        if (!sender.hasPermission("core.punish")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo permission."));
            return false;
        }

        if (args.length < 2) return sendUsageMessage(sender);


        if (Bukkit.getPlayer(args[0]) == null && Bukkit.getOfflinePlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by that name: "+ args[0]));
            return false;
        }

        UUID toWarn = null;
        String playerName = null;
        if (Bukkit.getPlayer(args[0]) != null) {
            toWarn = Bukkit.getPlayer(args[0]).getUniqueId();
            playerName = Bukkit.getPlayer(args[0]).getName();
        }

        if (Bukkit.getOfflinePlayer(args[0]) != null) {
            toWarn = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            playerName = Bukkit.getOfflinePlayer(args[0]).getName();
        }

        if (!(NumberUtil.isInteger(args[1].substring(0, args[1].length() - 1), 10))) {
            return sendUsageMessage(sender);
        }

        int time = Integer.parseInt(args[1].substring(0, args[1].length() - 1));
        String format = String.valueOf(args[1].charAt(args[1].length() - 1));

        int toSubtract = 1;
        if (args[args.length - 1].equalsIgnoreCase("-s")) toSubtract++;

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 2; i < args.length - toSubtract; i++) reasonBuilder.append(args[i]);

        int finalFormat;
        switch (format) {
            case "s":
                finalFormat = Calendar.SECOND;
                break;
            case "m":
                finalFormat = Calendar.MINUTE;
                break;
            case "h":
                finalFormat = Calendar.HOUR;
                break;
            case "d":
            default:
                finalFormat = Calendar.DATE;
                break;
        }

        boolean expires = !format.equalsIgnoreCase("i");

        Punishment toAdd = new Punishment(
                Punishment.PunishmentType.WARN,
                new Date(),
                finalFormat,
                reasonBuilder.toString(),
                expires,
                time,
                sender.getName(),
                false
        );

        if (!(args[args.length - 1].equalsIgnoreCase("-s"))) Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',  "&f[&6Rise&f] &c" + playerName + " has been warned for reason: " + reasonBuilder.toString()));
        if (Bukkit.getPlayer(toWarn) != null) Bukkit.getPlayer(toWarn).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have been warned for " + reasonBuilder.toString()));

        if (CorePlayer.getCorePlayers().get(toWarn) != null) {
            CorePlayer p = CorePlayer.getCorePlayers().get(toWarn);
            p.getPunishmentProfile().getPunishments().add(toAdd);

            return false;
        }

        FileManager.savePlayerDocument(toWarn, toAdd);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have warned " + playerName + " for " + time + format + " for reason " + reasonBuilder.toString()));

        return false;
    }

    private boolean sendUsageMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /Warn <Player> <Time> <Reason> <-S>"));
        return true;
    }
}
