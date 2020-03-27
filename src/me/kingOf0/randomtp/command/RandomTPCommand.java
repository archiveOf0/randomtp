package me.kingOf0.randomtp.command;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import me.kingOf0.randomtp.KingOfRTP;
import me.kingOf0.randomtp.Manager;
import me.kingOf0.randomtp.file.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RandomTPCommand implements CommandExecutor {

    private final KingOfRTP main = KingOfRTP.getInstance();
    private final Manager manager = main.getManager();
    private final Messages messages = main.getMessages();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(messages.getString("noconsole"));
                return true;
            }
            Player player = (Player) commandSender;

            if (!main.isCommandListenerEnabled())
                if (manager.isTeleporting(player.getUniqueId())) {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("alreadyteleporting")));
                    return true;
                }

            if (manager.hasCooldown(player)) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("hascooldown").replace("%cooldown%", String.valueOf(manager.getCooldown(player)))));
                return true;
            }

            manager.setTeleporting(player.getUniqueId(), new BukkitRunnable() {
                private final Location location;
                private int delay;
                {
                    location = manager.randomLocation();
                    delay = manager.getDelay();

                    location.getChunk().load();
                }
                @Override
                public void run() {
                    if (delay == 0) {
                        this.cancel();
                        player.teleport(location);
                        manager.addCooldown(player);
                        manager.teleportingComplete(player.getUniqueId());
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("teleported")));
                        return;
                    }
                    ActionBarAPI.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', messages.getString("teleporting").replace("%delay%", String.valueOf(delay))));
                    delay -= 1;
                }
            }.runTaskTimer(main, 0,20).getTaskId());
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("kingofrtp.reload")) {
                    manager.reload();
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("reloaded")));
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("noperm")));
                }
                return true;
            }
            if (!commandSender.hasPermission("kingofrtp.teleport.other")) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("noperm")));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (manager.isTeleporting(target.getUniqueId())) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("alreadyteleporting-other")));
                return true;
            }
            target.teleport(manager.randomLocation());
            return true;
        } else if (args.length == 2) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(messages.getString("noconsole"));
                return true;
            }
            Player player = (Player) commandSender;

            if (args[0].equalsIgnoreCase("set")) {
                if (commandSender.hasPermission("kingofrtp.set")) {
                    if (args[1].equalsIgnoreCase("location")) {
                        manager.setLocation(player.getLocation());
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("successfullyset")));
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("nocommand-set")));
                    }
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("noperm")));
                }
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("nocommand")));
            }
            return true;
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                if (commandSender.hasPermission("kingofrtp.set")) {
                    int value;
                    try {
                        value = Integer.parseInt(args[2]);
                    } catch (NumberFormatException ignored) {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("notvalidarg-set")));
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("size")) {
                        manager.setSize(value);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("successfullyset")));
                    } else if (args[1].equalsIgnoreCase("delay")) {
                        manager.setDelay(value);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("successfullyset")));
                    } else if (args[1].equalsIgnoreCase("cooldown")) {
                        manager.setCooldown(value);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("successfullyset")));
                    } else if (args[1].equalsIgnoreCase("checkYMax")) {
                        manager.setCheckYMax(value);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("successfullyset")));
                    } else if (args[1].equalsIgnoreCase("checkYMin")) {
                        manager.setCheckYMin(value);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("successfullyset")));
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("nocommand-set")));
                    }
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("noperm")));
                }
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("nocommand")));
            }
            return true;
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("nocommand")));
        }
        return true;
    }

}
