package me.kingOf0.randomtp.file;

import me.kingOf0.randomtp.KingOfRTP;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config extends YamlConfiguration {

	private final File file;

	public Config() {
		file = new File(KingOfRTP.getInstance().getDataFolder(), "config.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			saveDefaults();
		}
		reload();
	}

	private void saveDefaults() {
		KingOfRTP.getInstance().saveResource("config.yml", false);
	}

	public void reload() {
		try {
			super.load(this.file);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	public void save() {
        try {
            super.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}