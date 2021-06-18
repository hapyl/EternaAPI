package kz.hapyl.spigotutils.module.locaiton;

import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LocationHelper {

	public static Location getBehind(Location location, double offset) {
		validateOffset(offset);
		return offsetLocation(location, -offset);
	}

	public static Location getInFront(Location location, double offset) {
		validateOffset(offset);
		return offsetLocation(location, offset);
	}

	public static Vector getToTheLeft(Location location, double offset) {
		validateOffset(offset);
		final Vector vector = normalizeVector(location);
		return new Vector(vector.getZ(), 0.0d, -vector.getX()).normalize();
	}

	public static Vector getToTheRight(Location location, double offset) {
		validateOffset(offset);
		final Vector vector = normalizeVector(location);
		return new Vector(-vector.getZ(), 0.0d, vector.getX()).normalize();
	}

	private static Vector normalizeVector(Location location) {
		return location.getDirection().normalize();
	}

	private static void validateOffset(double offset) {
		Validate.isTrue(offset > 0.0d, "offset must be greater than 0");
	}

	private static Location offsetLocation(Location location, double offset) {
		final Vector vector = normalizeVector(location).multiply(offset);
		return location.add(vector);
	}

}
