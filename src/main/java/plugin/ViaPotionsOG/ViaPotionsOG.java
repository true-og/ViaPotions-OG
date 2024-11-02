package plugin.ViaPotionsOG;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import plugin.ViaPotionsOG.adapters.WorldEventAdapter;
import plugin.ViaPotionsOG.listeners.AreaEffectCloudListener;
import plugin.ViaPotionsOG.listeners.SpawnEntityListener;
import plugin.ViaPotionsOG.utils.ConfigurationUtil;
import plugin.ViaPotionsOG.utils.VersionUtil;

public class ViaPotionsOG extends JavaPlugin {

	public void onEnable() {

		final ConfigurationUtil configurationUtil = new ConfigurationUtil(this);
		final VersionUtil versionUtil = new VersionUtil(this);
		final PluginManager pluginManager = getServer().getPluginManager();
		final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

		if (pluginManager.isPluginEnabled("ViaRewind")) {

			getLogger().info("ViaRewind detected, enabling integration.");

		}
		else if (pluginManager.isPluginEnabled("ViaBackwards")) {

			getLogger().warning("ViaBackwards detected. In order to make ViaPotions work correctly, it is required that you have ViaRewind installed.");

		}
		else {

			getLogger().severe("No compatible plugins have been detected, disabling the plugin.");
			getLogger().severe("In order to make ViaPotions functional, ViaRewind must be installed.");

			pluginManager.disablePlugin(this);

		}

		if (pluginManager.getPlugin("ViaRewind-Legacy-Support") == null || !configurationUtil.getConfiguration("%datafolder%/ViaRewind-Legacy-Support/config.yml").getBoolean("area-effect-cloud-particles")) {

			pluginManager.registerEvents(new AreaEffectCloudListener(this, versionUtil), this);

		}

		protocolManager.addPacketListener(new SpawnEntityListener(this, versionUtil));
		protocolManager.addPacketListener(new WorldEventAdapter(this, versionUtil));

	}

	public static String getPrefix() {

		return "&8[&3ViaPotions&4-OG&8] ";

	}

}