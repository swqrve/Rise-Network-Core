package me.swerve.listener;

import me.swerve.RiseCore;
import me.swerve.player.CorePlayer;
import me.swerve.punishment.Punishment;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onConnection(PlayerLoginEvent e) {
        CorePlayer p = new CorePlayer(e.getPlayer(), e.getPlayer().getUniqueId());

        Punishment activePunishment = p.getPunishmentProfile().getActive(Punishment.PunishmentType.BAN);
        if (activePunishment != null) {
            e.setKickMessage(activePunishment.getBanMessage());
            e.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        CorePlayer p = CorePlayer.getCorePlayers().get(e.getPlayer().getUniqueId());

        p.save();
        p.removeAttachment();

        CorePlayer.getCorePlayers().remove(e.getPlayer().getUniqueId());
    }
}
