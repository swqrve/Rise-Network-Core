package me.swerve.command;

import me.swerve.permission.Permission;
import me.swerve.util.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreatePermissionCommand implements CommandExecutor {

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

        if (args.length != 2 || !(NumberUtil.isInteger(args[1], 10))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /CreatePermission <Permission> <Level>"));
            return false;
        }

        String permissionName = args[0];
        int permissionLevel = Integer.parseInt(args[1]);

        new Permission(permissionLevel, permissionName);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCreated permission " + permissionName + " with a permission level of " + permissionLevel));

        return false;
    }
}
