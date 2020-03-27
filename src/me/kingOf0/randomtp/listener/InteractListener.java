package me.kingOf0.randomtp.listener;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import me.kingOf0.randomtp.KingOfRTP;
import me.kingOf0.randomtp.Manager;
import me.kingOf0.randomtp.file.Messages;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class InteractListener implements Listener {

    private final Manager manager = KingOfRTP.getInstance().getManager();
    private final Messages messages = KingOfRTP.getInstance().getMessages();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (manager.isTeleporting(event.getPlayer().getUniqueId())) {
            manager.cancelTeleporting(event.getPlayer().getUniqueId());
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("teleportingcancelled-interact")));
            ActionBarAPI.sendActionBar(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', messages.getString("teleportingcancelled-interact")));
        }
    }
}
