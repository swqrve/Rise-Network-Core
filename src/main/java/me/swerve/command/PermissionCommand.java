package me.swerve.command;

import me.swerve.RiseCore;
import me.swerve.menu.board.PermissionMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionCommand implements CommandExecutor {

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

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aOpening Permissions Menu"));
        Bukkit.getPluginManager().registerEvents(new PermissionMenu((Player) sender), RiseCore.getInstance());

        return false;
    }
}
