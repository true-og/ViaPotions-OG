package plugin.ViaPotionsOG.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import net.trueog.utilitiesog.UtilitiesOG;
import plugin.ViaPotionsOG.ViaPotionsOG;
import plugin.ViaPotionsOG.utils.PotionTranslator;
import plugin.ViaPotionsOG.utils.TranslationData;
import plugin.ViaPotionsOG.utils.VersionUtil;

public class SpawnEntityListener extends PacketAdapter {

	private final VersionUtil versionUtil;

	public SpawnEntityListener(final Plugin plugin, final VersionUtil versionUtil) {

		super(plugin, PacketType.Play.Server.SPAWN_ENTITY);

		this.versionUtil = versionUtil;

	}

	@Override
	public void onPacketSending(final PacketEvent event) {

		final PacketContainer packet = event.getPacket();
		final Entity entity = packet.getEntityModifier(event).read(0);
		if (packet.getIntegers().read(6) == 73 && entity instanceof ThrownPotion) {

			final Player player = event.getPlayer();
			final int version = versionUtil.getVersion(player);
			final PacketContainer edited = packet.deepClone();
			final ThrownPotion potion = (ThrownPotion) entity;
			for (final PotionEffect effect : potion.getEffects()) {

				for (final PotionTranslator translator : PotionTranslator.values()) {

					if (effect.getType().equals(translator.getPotionEffectType())) {

						for (TranslationData data : translator.getDatas()) {

							if (data.getLowestVersion() <= version && data.getHighestVersion() >= version) {

								edited.getIntegers().write(7, data.getRemap());

							}

						}

					}

				}

			}

			try {

				ProtocolLibrary.getProtocolManager().sendServerPacket(player, edited, false);

			}
			catch (Exception error) {

				UtilitiesOG.logToConsole(ViaPotionsOG.getPrefix(), error.getMessage());

			}

			event.setCancelled(true);

		}

	}

}