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

public class MuteCommand implements CommandExecutor {
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

        UUID toMute = null;
        String playerName = null;
        if (Bukkit.getPlayer(args[0]) != null) {
            toMute = Bukkit.getPlayer(args[0]).getUniqueId();
            playerName = Bukkit.getPlayer(args[0]).getName();
        }

        if (Bukkit.getOfflinePlayer(args[0]) != null) {
            toMute = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
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

        if (!(args[args.length - 1].equalsIgnoreCase("-s"))) Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Rise&f] &c" + playerName + " has been muted for reason: " + reasonBuilder.toString()));

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

        if (Bukkit.getPlayer(toMute) != null) {
            String message = " " + time + format;
            if (!expires) message = "ever";
            Bukkit.getPlayer(toMute).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have been muted for" + message + " for the reason " + reasonBuilder.toString()));
        }

        Punishment toAdd = new Punishment(
                Punishment.PunishmentType.MUTE,
                new Date(),
                finalFormat,
                reasonBuilder.toString(),
                expires,
                time,
                sender.getName(),
                false
        );

        if (CorePlayer.getCorePlayers().get(toMute) != null) {
            CorePlayer p = CorePlayer.getCorePlayers().get(toMute);
            p.getPunishmentProfile().getPunishments().add(toAdd);

            return false;
        }


        FileManager.savePlayerDocument(toMute, toAdd);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have muted " + playerName + " for " + time + format + " for reason " + reasonBuilder.toString()));

        return false;
    }

    private boolean sendUsageMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /Mute <Player> <Time> <Reason> <-S>"));
        return true;
    }
}
