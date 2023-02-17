package me.swerve.command;

import me.swerve.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MessageCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if(!p.hasPermission("uhc.message")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /msg <User> <Message>"));
            return false;
        }

        String playerName = args[0];

        if (Bukkit.getPlayer(playerName) == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by that name."));
            return false;
        }

        if (!Bukkit.getPlayer(playerName).isOnline()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player is not available!"));
            return false;
        }

        args[0] = "";
        String cleanedString = Arrays.asList(args).toString().replaceAll(", ", " ").replaceAll("\\[", "").replaceAll("]", "");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(to &6"  + Bukkit.getPlayer(playerName).getDisplayName() + "&7) " + cleanedString));
        Bukkit.getPlayer(playerName).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(from &6"  + ((Player) sender).getDisplayName() + "&7) " + cleanedString));
        CorePlayer.getCorePlayers().get(Bukkit.getPlayer(playerName).getUniqueId()).setLastSpokenUser(((Player) sender).getUniqueId());
        return false;
    }

}
