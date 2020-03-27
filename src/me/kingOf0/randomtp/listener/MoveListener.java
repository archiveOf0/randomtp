package me.kingOf0.randomtp.listener;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import me.kingOf0.randomtp.KingOfRTP;
import me.kingOf0.randomtp.Manager;
import me.kingOf0.randomtp.file.Messages;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final Manager manager = KingOfRTP.getInstance().getManager();
    private final Messages messages = KingOfRTP.getInstance().getMessages();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlock().getLocation().equals(event.getTo().getBlock().getLocation()))
            return;
        if (manager.isTeleporting(event.getPlayer().getUniqueId())) {
            manager.cancelTeleporting(event.getPlayer().getUniqueId());
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("teleportingcancelled-move")));
            ActionBarAPI.sendActionBar(event.getPlayer(), messages.getString(ChatColor.translateAlternateColorCodes('&',"teleportingcancelled-move")));
        }
    }
}
