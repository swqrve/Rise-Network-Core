package me.swerve.command;

import me.swerve.RiseCore;
import me.swerve.menu.board.PermissionMenu;
import me.swerve.menu.board.PunishmentMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PunishmentCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be a player to use this command."));
            return false;
        }

        if (!sender.hasPermission("core.punishments")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo permission."));
            return false;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /punishments <Player>"));
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null && Bukkit.getOfflinePlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by that name."));
            return false;
        }

        UUID uuid = null;
        String playerName = null;
        if (Bukkit.getPlayer(args[0]) != null) {
            uuid = Bukkit.getPlayer(args[0]).getUniqueId();
            playerName = Bukkit.getPlayer(args[0]).getName();
        }

        if (Bukkit.getOfflinePlayer(args[0]) != null) {
            uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            playerName = Bukkit.getOfflinePlayer(args[0]).getName();
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aOpening Punishments Menu"));
        Bukkit.getPluginManager().registerEvents(new PunishmentMenu((Player) sender, uuid, playerName), RiseCore.getInstance());

        return false;
    }
}
