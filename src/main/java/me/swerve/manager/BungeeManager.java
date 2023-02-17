package me.swerve.manager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import me.swerve.RiseCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BungeeManager implements PluginMessageListener {

    @Getter private static BungeeManager instance;

    @Getter private final HashMap<String, Map<Long, Boolean>> serversStatus = new HashMap<>();
    @Getter private final HashMap<String, Integer> serversOnline = new HashMap<>();
    @Getter private final List<String> servers = new ArrayList<>();

    public BungeeManager() {
        instance = this;
        Bukkit.getScheduler().runTaskTimerAsynchronously(RiseCore.getInstance(), () -> getOnline("ALL"), 0L, 20L);
    }

    public String getStatus(String ip) {
        Map<Long, Boolean> cached = this.serversStatus.get(ip);

        long lastUsed = 0L;
        boolean online = false;

        if (cached != null) {
            lastUsed = cached.keySet().iterator().next();
            online = cached.values().iterator().next();
        }

        if (System.currentTimeMillis() - lastUsed > 1000L)
            ping(ip);

        if (online)
            return "&aOnline";

        return "&cOffline";
    }

    public void ping(String fullIp) {
        Bukkit.getScheduler().runTaskAsynchronously(RiseCore.getInstance(), () -> {
            try {
                String ip = fullIp.split(":")[0];
                int port = Integer.parseInt(fullIp.split(":")[1]);
                (new Socket()).connect(new InetSocketAddress(ip, port), 1000);
                Map<Long, Boolean> map = new HashMap<>();
                map.put(Long.valueOf(System.currentTimeMillis()), Boolean.valueOf(true));
                this.serversStatus.put(fullIp, map);
            } catch (IOException exception) {
                Map<Long, Boolean> map = new HashMap<>();
                map.put(Long.valueOf(System.currentTimeMillis()), Boolean.valueOf(false));
                this.serversStatus.put(fullIp, map);
            }
        });
    }
    public int getOnline(String server) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(server);
            Bukkit.getServer().sendPluginMessage(RiseCore.getInstance(), "BungeeCord", out.toByteArray());
            return this.serversOnline.getOrDefault(server, 0);
        } catch (Exception ex) { return 0; }
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] messages) {
        if (!channel.equals("BungeeCord")) return;

        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(messages);
            String action = in.readUTF();
            if (action.equals("PlayerCount")) this.serversOnline.put(in.readUTF(), Integer.valueOf(in.readInt()));
        } catch (Exception exception) {}
    }
}
