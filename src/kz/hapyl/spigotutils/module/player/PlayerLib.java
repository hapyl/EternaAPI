package kz.hapyl.spigotutils.module.player;

import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerLib {

	public static void playSound(Player player, Sound sound, float pitch) {
		validateTrue(pitch >= 0.0f && pitch <= 2.0f);
		player.playSound(player.getLocation(), sound, SoundCategory.RECORDS, 2, pitch);
	}

	public static void playSound(Location location, Sound sound, float pitch) {
		validateTrue(pitch >= 0.0f && pitch <= 2.0f);
		validateTrue(location.getWorld() != null);
		location.getWorld().playSound(location, sound, SoundCategory.RECORDS, 20, pitch);
	}

	public static void stopSound(Sound sound) {
		Bukkit.getOnlinePlayers().forEach(p -> p.stopSound(sound));
	}

	public static void spawnParticle(Player player, Location location, Particle particle, int amount, double x, double y, double z, float speed) {
		player.spawnParticle(particle, location, amount, x, y, z, speed);
	}

	public static void spawnParticle(Location location, Particle particle, int amount, double x, double y, double z, float speed) {
		if (location.getWorld() != null) {
			location.getWorld().spawnParticle(particle, location, amount, x, y, z, speed);
		}
	}

	public static void addEffect(Player player, PotionEffectType type, int duration, int amplifier) {
		player.addPotionEffect(new PotionEffect(type, duration, amplifier, true, false));
	}

	public static void removeEffect(Player player, PotionEffectType type) {
		player.removePotionEffect(type);
	}

	public static void villagerYes(Player player) {
		playSound(player, Sound.ENTITY_VILLAGER_YES, 1.0f);
	}

	public static void villagerYes(Player player, String string, Object... replacements) {
		Chat.sendClickableMessage(player, string, replacements);
		villagerYes(player);
	}

	public static void lavaPop(Player player) {
		playSound(player, Sound.BLOCK_LAVA_POP, 0.0f);
	}

	public static void villagerNo(Player player) {
		playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
	}

	public static void villagerNo(Player player, String string, Object... replacements) {
		Chat.sendClickableMessage(player, string, replacements);
		villagerNo(player);
	}

	public static void plingNote(Player player, float pitch) {
		playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, pitch);
	}

	public static void playNote(Player player, Note note, float pitch) {
		player.sendMessage("this is not yet implemented");
	}

	public enum Note {

	}

	public static void playSoundMessage(Player player, Sound sound, float pitch, String message, Object... replacements) {
		playSound(player, sound, pitch);
		Chat.sendClickableMessage(player, message, replacements);
	}

	public static void playSound(Sound sound, float pitch) {
		validateTrue(pitch >= 0.0f && pitch <= 2.0f);
		for (final Player online : Bukkit.getOnlinePlayers()) {
			playSound(online, sound, pitch);
		}
	}

	private static void validateTrue(boolean a, String b) {
		if (!a) {
			throw new IllegalArgumentException(b);
		}
	}

	private static void validateTrue(boolean b) {
		validateTrue(b, "Could not parse " + b);
	}

}
