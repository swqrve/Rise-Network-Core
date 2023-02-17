package me.swerve.command;

import me.swerve.RiseCore;
import me.swerve.menu.board.CosmeticsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CosmeticCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be a player to use this command."));
            return false;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aOpening Cosmetics Menu"));
        Bukkit.getPluginManager().registerEvents(new CosmeticsMenu((Player) sender), RiseCore.getInstance());

        return false;
    }
}
