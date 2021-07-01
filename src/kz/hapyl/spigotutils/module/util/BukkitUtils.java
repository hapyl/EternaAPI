package kz.hapyl.spigotutils.module.util;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collection;

public class BukkitUtils {

	private static final JavaPlugin PLUGIN = SpigotUtilsPlugin.getPlugin();

	public static String locationToString(Location location) {
		return locationToString(location, "%s, %s, %s");
	}

	public static String locationToString(Location location, String format) {
		return String.format(format, location.getX(), location.getY(), location.getZ());
	}

	public static String roundTick(int tick) {
		return tick % 20 == 0 ? "" + (tick / 20d) : BukkitUtils.decimalFormat((tick / 20f));
	}

	public static String locationToString(Location location, boolean includeRotation) {
		return locationToString(location, "%s, %s, %s (%s, %s)", true);
	}

	public static String locationToString(Location location, String format, boolean includeRotation) {
		return String.format(format, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public static Location centerLocation(Location location) {
		return new Location(location.getWorld(), location.getBlockX() + 0.5d, location.getBlockY() + 0.5d, location.getBlockZ() + 0.5d);
	}

	public static Location getSpawnLocation(World world) {
		return world.getSpawnLocation();
	}

	public static Location getSpawnLocation() {
		return getSpawnLocation(Bukkit.getWorlds().get(0));
	}

	public static void mergePitchYaw(Location from, Location to) {
		to.setPitch(from.getPitch());
		to.setYaw(from.getYaw());
	}

	@Nullable
	public static Entity getClosestEntityTo(Collection<Entity> collection, Location targetLocation) {
		return getClosestEntityTo(collection, targetLocation, new EntityType[0]);
	}

	public static Entity getClosestEntityTo(Collection<Entity> collection, Location targetLocation, EntityType... allowedTypes) {
		if (collection.isEmpty()) {
			return null;
		}

		Entity current = null;
		double closest = 0;

		search:
		for (final Entity entity : collection) {
			if (allowedTypes.length > 0) {
				for (final EntityType allowedType : allowedTypes) {
					if (entity.getType() != allowedType) {
						continue search;
					}
				}
			}

			// init if first entity
			final double distance = entity.getLocation().distance(targetLocation);
			if (current == null) {
				current = entity;
				closest = distance;
			}
			else if (distance <= closest) {
				current = entity;
				closest = distance;
			}
		}

		return current;

	}

	public static void removeHeldItem(Player player, int amount) {
		final ItemStack mainItem = player.getInventory().getItemInMainHand();
		mainItem.setAmount(mainItem.getAmount() - amount);
	}

	public static void removeHeldItemIf(Player player, int amount, boolean condition) {
		if (condition) {
			removeHeldItem(player, amount);
		}
	}

	public static String decimalFormat(Number number, String format) {
		return new DecimalFormat(format).format(number);
	}

	public static String decimalFormat(Number number) {
		return decimalFormat(number, "#0.00");
	}

	public static void runAsync(Runnable runnable) {
		new BukkitRunnable() {
			@Override
			public void run() {
				runnable.run();
			}
		}.runTaskAsynchronously(PLUGIN);
	}

	public static void runSync(Runnable runnable) {
		new BukkitRunnable() {
			@Override
			public void run() {
				runnable.run();
			}
		}.runTask(PLUGIN);
	}

	public static void runLater(Runnable runnable, int ticks) {
		Validate.isTrue(ticks > 0, "ticks value mu be positive");
		new BukkitRunnable() {
			@Override
			public void run() {
				runnable.run();
			}
		}.runTaskLater(PLUGIN, ticks);
	}

	public static double random() {
		return 0.0d;
	}

	public static Location defLocation(int x, int y, int z) {
		return defLocation(x + 0.5f, y + 0.5f, z + 0.5f);
	}

	public static Location defLocation(double x, double y, double z) {
		return new Location(Bukkit.getWorlds().get(0), x, y, z);
	}
}
