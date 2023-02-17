package me.swerve.command;

import me.swerve.RiseCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class HubCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be a player to use this command."));
            return false;
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF("hub");
        } catch (Exception ignored) {}

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSending you to the hub."));
        Bukkit.getPlayer(((Player) sender).getUniqueId()).sendPluginMessage(RiseCore.getInstance(), "BungeeCord", b.toByteArray());

        return false;
    }
}
