package me.kingOf0.randomtp;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import me.kingOf0.randomtp.command.RandomTPCommand;
import me.kingOf0.randomtp.file.Config;
import me.kingOf0.randomtp.file.Messages;
import me.kingOf0.randomtp.listener.CommandListener;
import me.kingOf0.randomtp.listener.DamageListener;
import me.kingOf0.randomtp.listener.MoveListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class KingOfRTP extends JavaPlugin {

    private static KingOfRTP instance;
    private Config config;
    private Messages messages;
    private Manager manager;
    private Listener commandListener;

    @Override
    public void onEnable() {
        if (instance != null)
            throw new IllegalStateException("The plugin cannot enabled twice!");
        instance = this;
        new ActionBarAPI(this);

        config = new Config();
        messages = new Messages();
        manager = new Manager();
        manager.load();


        if (config.getBoolean("commandListener", true)) {
            getServer().getPluginManager().registerEvents(new CommandListener(), this);
        }
        if (config.getBoolean("moveListener", true)) {
            getServer().getPluginManager().registerEvents(new MoveListener(), this);
        }
        if (config.getBoolean("damageListener", true)) {
            getServer().getPluginManager().registerEvents(new DamageListener(), this);
        }

        getCommand("randomtp").setExecutor(new RandomTPCommand());
    }

    @Override
    public void onDisable() {
        if (config != null && manager != null)
            manager.forceSave();
    }

    public static KingOfRTP getInstance() {
        return instance;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public Messages getMessages() {
        return messages;
    }

    public Manager getManager() {
        return manager;
    }

    public boolean isCommandListenerEnabled() {
        return commandListener != null;
    }
}
