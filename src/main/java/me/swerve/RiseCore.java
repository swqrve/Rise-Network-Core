package me.swerve;

import me.swerve.command.*;
import me.swerve.file.FileManager;
import me.swerve.listener.ConnectionListener;
import me.swerve.listener.MessageListener;
import lombok.Getter;
import me.swerve.manager.BungeeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class RiseCore extends JavaPlugin {
    @Getter private static RiseCore instance;
    @Override
    public void onEnable() {
        instance = this;

        registerListeners();
        registerCommands();

        FileManager.loadPermissions();
        FileManager.loadRanks();
        FileManager.loadCosmetics();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeManager());

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&3RiseCore&f] &fRiseCore has been enabled."));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&3RiseCore&f] &fRiseCore has been disabled."));

        FileManager.savePermissions();
    }

    public void registerCommands() {
        Bukkit.getPluginCommand("permissions").setExecutor(new PermissionCommand());
        Bukkit.getPluginCommand("punishments").setExecutor(new PunishmentCommand());
        Bukkit.getPluginCommand("createPermission").setExecutor(new CreatePermissionCommand());
        Bukkit.getPluginCommand("rank").setExecutor(new RankCommand());
        Bukkit.getPluginCommand("hub").setExecutor(new HubCommand());
        Bukkit.getPluginCommand("msg").setExecutor(new MessageCommand());
        Bukkit.getPluginCommand("r").setExecutor(new ReplyCommand());
        Bukkit.getPluginCommand("mute").setExecutor(new MuteCommand());
        Bukkit.getPluginCommand("ban").setExecutor(new BanCommand());
        Bukkit.getPluginCommand("warn").setExecutor(new WarnCommand());
        Bukkit.getPluginCommand("cosmetics").setExecutor(new CosmeticCommand());
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new MessageListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
    }
}
