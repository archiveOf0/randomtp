package me.kingOf0.randomtp;

import me.kingOf0.randomtp.file.Config;
import me.kingOf0.randomtp.file.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Manager {

    private Location location;
    private int size;
    private int delay;
    private int cooldown;
    private int checkYMax;
    private int checkYMin;

    private final KingOfRTP main = KingOfRTP.getInstance();
    private Messages messages = main.getMessages();
    private final Config config = main.getConfig();

    private final Map<UUID, Integer> teleporting = new HashMap<>();

    public void save() {
        ConfigurationSection location = config.createSection("location");
        location.set("world", this.location.getWorld().getName());
        location.set("x", this.location.getX());
        location.set("y", this.location.getY());
        location.set("z", this.location.getZ());

        config.set("size", size);
        config.set("delay", delay);
        config.set("cooldown", cooldown);
        config.set("checkYMax", checkYMax);
        config.set("checkYMin", checkYMin);
    }

    public void load() {
        ConfigurationSection location = config.getConfigurationSection("location");
        this.location = new Location(Bukkit.getWorld(location.getString("world")), location.getDouble("x"), location.getDouble("y"),location.getDouble("z"));

        size = config.getInt("size");
        delay = config.getInt("delay");
        cooldown = config.getInt("cooldown");
        checkYMax = config.getInt("checkYMax", 200);
        checkYMin = config.getInt("checkYMin", 20);
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getDelay() {
        return delay;
    }

    public int getSize() {
        return size;
    }

    public Location getLocation() {
        return location;
    }

    public Location randomLocation() {
        Random random = new Random();
        int x = random.nextInt(size);
        int z = random.nextInt(size);
        if (random.nextBoolean())
            x = -x;
        if (random.nextBoolean())
            z = -z;
        Location location = this.location.clone().add(x, 0, z);

        for (int i = checkYMax; i > checkYMin; i--) {
            location.setY(i);
            if (location.getBlock() != null) {
                Material type = location.getBlock().getType();
                if (type != Material.AIR && type.isBlock()) {
                    Bukkit.getConsoleSender().sendMessage("breaked" + location.getBlock());
                    break;
                }
            }
        }
        location.setX(location.getBlockX() + 0.5);
        location.setY(location.getBlockY() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);

        if (!location.getBlock().getType().isBlock() || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER || location.getBlock().getType() == Material.LAVA || location.getBlock().getType() == Material.STATIONARY_LAVA)
            return randomLocation();
        if (!location.add(0,1,0).getBlock().getType().isBlock() || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER || location.getBlock().getType() == Material.LAVA || location.getBlock().getType() == Material.STATIONARY_LAVA)
            return randomLocation();
        if (!location.add(0,1,0).getBlock().getType().isBlock() || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER || location.getBlock().getType() == Material.LAVA || location.getBlock().getType() == Material.STATIONARY_LAVA)
            return randomLocation();
        if (!location.getBlock().getType().isBlock() || location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER || location.getBlock().getType() == Material.LAVA || location.getBlock().getType() == Material.STATIONARY_LAVA)
            return randomLocation();

        return location;
    }

    public void reload() {
        config.reload();
        messages.reload();
        load();
    }

    public void forceSave() {
        save();
        config.save();
    }

    public boolean hasCooldown(Player player) {
        if (player.isOp() || player.hasPermission("kingofrtp.bypass.cooldown"))
            return false;
        if (!player.hasMetadata("kingofrtp.cooldown"))
            return false;
        return ((int) ((player.getMetadata("kingofrtp.cooldown").get(0).asLong() - System.currentTimeMillis()) / 1000)) > 0;
    }

    public void addCooldown(Player player) {
        player.setMetadata("kingofrtp.cooldown", new FixedMetadataValue(main, System.currentTimeMillis() + (cooldown * 1000)));
    }

    public int getCooldown(Player player) {
        return (int) ((player.getMetadata("kingofrtp.cooldown").get(0).asLong() - System.currentTimeMillis()) / 1000);
    }

    //todo: add delay while teleporting. Note: find the location before teleportation.

    public void removeCooldown(Player player) {
        player.removeMetadata("kingofrtp.cooldown", main);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSize(int value) {
        this.size = value;
    }

    public void setDelay(int value) {
        this.delay = value;
    }

    public void setCooldown(int value) {
        this.cooldown = value;
    }

    public void setCheckYMin(int value) {
        this.checkYMin = value;
    }

    public void setCheckYMax(int value) {
        this.checkYMax = value;
    }

    public void setTeleporting(UUID uuid, Integer taskId) {
        teleporting.put(uuid, taskId);
    }

    public boolean isTeleporting(UUID uuid) {
        return teleporting.containsKey(uuid);
    }

    public void cancelTeleporting(UUID uuid) {
        main.getServer().getScheduler().cancelTask(teleporting.get(uuid));
        teleporting.remove(uuid);
    }

    public void teleportingComplete(UUID uuid) {
        teleporting.remove(uuid);
    }
}
