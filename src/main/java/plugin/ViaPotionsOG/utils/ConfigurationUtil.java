package plugin.ViaPotionsOG.utils;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigurationUtil {
	final private Plugin plugin;

	public ConfigurationUtil(final Plugin plugin) {

		this.plugin = plugin;

	}

	public YamlConfiguration getConfiguration(String filePath) {

		final File dataFolder = plugin.getDataFolder();
		final File file = new File(filePath.replace("%datafolder%", dataFolder.toPath().toString()));
		if (file.exists()) {

			return YamlConfiguration.loadConfiguration(file);

		}
		else {

			return new YamlConfiguration();

		}

	}

}