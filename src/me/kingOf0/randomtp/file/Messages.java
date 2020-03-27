package me.kingOf0.randomtp.file;

import me.kingOf0.randomtp.KingOfRTP;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages extends YamlConfiguration {

	private final File file;

	public Messages() {
		file = new File(KingOfRTP.getInstance().getDataFolder(), "messages.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			saveDefaults();
		}
		reload();
	}

	private void saveDefaults() {
		KingOfRTP.getInstance().saveResource("messages.yml", false);
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