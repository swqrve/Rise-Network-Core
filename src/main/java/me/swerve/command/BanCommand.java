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

public class BanCommand implements CommandExecutor {
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

        UUID toBan = null;
        String playerName = null;
        if (Bukkit.getPlayer(args[0]) != null) {
            toBan = Bukkit.getPlayer(args[0]).getUniqueId();
            playerName = Bukkit.getPlayer(args[0]).getName();
        }

        if (Bukkit.getOfflinePlayer(args[0]) != null) {
            toBan = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            playerName = Bukkit.getOfflinePlayer(args[0]).getName();
        }

        if (!(NumberUtil.isInteger(args[1].substring(0, args[1].length() - 1), 10))) {
            return sendUsageMessage(sender);
        }

        int time = Integer.parseInt(args[1].substring(0, args[1].length() - 1));
        String format = String.valueOf(args[1].charAt(args[1].length() - 1));

        int toSubtract = 1;
        if (args[args.length - 1].equalsIgnoreCase("-s")) toSubtract++;

        StringBuilder reason = new StringBuilder();
        for (int i = 2; i <= (args.length - toSubtract); i++) reason.append(" ").append(args[i]);

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

        if (!(args[args.length - 1].equalsIgnoreCase("-s"))) {
            String message = " " + time + format;
            if (!expires) message = "ever";
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Rise&f] &c" + playerName + " has been banned for" + message + " for reason: " + reason.toString()));
        }

        Punishment toAdd = new Punishment(
                Punishment.PunishmentType.BAN,
                new Date(),
                finalFormat,
                reason.toString(),
                expires,
                time,
                sender.getName(),
                false
        );

        if (CorePlayer.getCorePlayers().get(toBan) != null) {
            CorePlayer p = CorePlayer.getCorePlayers().get(toBan);
            p.getPunishmentProfile().getPunishments().add(toAdd);

            Bukkit.getPlayer(toBan).kickPlayer(toAdd.getBanMessage());
            return false;
        }

        FileManager.savePlayerDocument(toBan, toAdd);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have banned " + playerName + " for " + time + format + " for reason " + reason.toString()));

        return false;
    }

    private boolean sendUsageMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /Ban <Player> <Time> <Reason> <-S>"));
        return true;
    }
}
