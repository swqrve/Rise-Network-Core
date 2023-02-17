package me.swerve.command;

import me.swerve.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReplyCommand implements CommandExecutor {
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

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /r <message>"));
            return false;
        }

        if (CorePlayer.getCorePlayers().get(p.getUniqueId()).getLastSpokenUser() == null || Bukkit.getPlayer(CorePlayer.getCorePlayers().get(p.getUniqueId()).getLastSpokenUser()) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou haven't spoken to anyone recently!"));
            return false;
        }

        String cleanedString = Arrays.asList(args).toString().replaceAll(", ", " ").replaceAll("\\[", "").replaceAll("]", "");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(to &6"  + Bukkit.getPlayer(CorePlayer.getCorePlayers().get(p.getUniqueId()).getLastSpokenUser()).getDisplayName() + "&7) " + cleanedString));
        Bukkit.getPlayer(CorePlayer.getCorePlayers().get(p.getUniqueId()).getLastSpokenUser()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(from &6"  + ((Player) sender).getDisplayName() + "&7) " + cleanedString));
        CorePlayer.getCorePlayers().get(Bukkit.getPlayer(CorePlayer.getCorePlayers().get(p.getUniqueId()).getLastSpokenUser()).getUniqueId()).setLastSpokenUser(((Player) sender).getUniqueId());
        return false;
    }

}
