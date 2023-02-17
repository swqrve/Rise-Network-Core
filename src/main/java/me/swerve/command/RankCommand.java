package me.swerve.command;

import me.swerve.RiseCore;
import me.swerve.menu.board.PermissionMenu;
import me.swerve.menu.board.RankMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be a player to use this command."));
            return false;
        }

        if (!sender.hasPermission("core.permissions")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo permission."));
            return false;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /SetRank <Player>"));
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null && Bukkit.getOfflinePlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by that name."));
            return false;
        }

        UUID uuid = null;
        if (Bukkit.getPlayer(args[0]) != null) uuid = Bukkit.getPlayer(args[0]).getUniqueId();
        if (Bukkit.getOfflinePlayer(args[0]) != null) uuid = Bukkit.getPlayer(args[0]).getUniqueId();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aOpening Permissions Menu"));
        Bukkit.getPluginManager().registerEvents(new RankMenu((Player) sender, uuid), RiseCore.getInstance());

        return false;
    }
}
