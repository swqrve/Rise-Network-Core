package me.swerve;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import me.swerve.command.*;
import me.swerve.file.FileManager;
import me.swerve.listener.ConnectionListener;
import me.swerve.listener.MessageListener;
import lombok.Getter;
import me.swerve.manager.BungeeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.text.Document;
import java.util.Collection;


public class RiseCore extends JavaPlugin {
    @Getter private static RiseCore instance;

    @Getter private MongoDatabase mongoDatabase;
    @Override
    public void onEnable() {
        instance = this;

        registerListeners();
        registerCommands();

        setupMongoDB();

        FileManager.loadPermissions();
        FileManager.loadRanks();
        FileManager.loadCosmetics();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeManager());

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&3RiseCore&f] &fRiseCore has been enabled."));
    }

    private void setupMongoDB() {
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        MongoCredential credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        System.out.println("Connected to the rise database successfully");

        mongoDatabase = mongo.getDatabase("myDb");
        System.out.println("Credentials :"+ credential);

        boolean found = false;
        for (String name : mongoDatabase.listCollectionNames()) if (name.equalsIgnoreCase("maincollection")) found = true;

        if (!found) {
            mongoDatabase.createCollection("mainCollection");
            System.out.println("Created Database Collection successfully");
        }
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
        Bukkit.getPluginCommand("unmute").setExecutor(new UnMuteCommand());
        Bukkit.getPluginCommand("unban").setExecutor(new UnBanCommand());
        Bukkit.getPluginCommand("warn").setExecutor(new WarnCommand());
        Bukkit.getPluginCommand("cosmetics").setExecutor(new CosmeticCommand());
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new MessageListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
    }
}
