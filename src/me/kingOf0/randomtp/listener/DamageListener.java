package me.kingOf0.randomtp.listener;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import me.kingOf0.randomtp.KingOfRTP;
import me.kingOf0.randomtp.Manager;
import me.kingOf0.randomtp.file.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final Manager manager = KingOfRTP.getInstance().getManager();
    private final Messages messages = KingOfRTP.getInstance().getMessages();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (manager.isTeleporting(event.getEntity().getUniqueId())) {
            manager.cancelTeleporting(event.getEntity().getUniqueId());
            event.getEntity().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("teleportingcancelled-command")));
            ActionBarAPI.sendActionBar((Player) event.getEntity(), ChatColor.translateAlternateColorCodes('&', messages.getString("teleportingcancelled-move")));
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (manager.isTeleporting(event.getEntity().getUniqueId())) {
            manager.cancelTeleporting(event.getEntity().getUniqueId());
            ActionBarAPI.sendActionBar((Player) event.getEntity(), "teleportingcancelled-move");
            event.getEntity().sendMessage(messages.getString(messages.getString("teleportingcancelled-move")));
        }
        if (manager.isTeleporting(event.getDamager().getUniqueId())) {
            manager.cancelTeleporting(event.getDamager().getUniqueId());
            event.getDamager().sendMessage(messages.getString("teleportingcancelled-damage"));
            ActionBarAPI.sendActionBar((Player) event.getDamager(), messages.getString("teleportingcancelled-move"));
        }
    }

}
