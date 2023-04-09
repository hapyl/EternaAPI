package me.hapyl.spigotutils.module.locaiton;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Useful helper to work with locations.
 */
public class LocationHelper {

    /**
     * Returns location behind center.
     *
     * @param location - Center.
     * @param offset   - Offset. 1.0 is exactly one block behind.
     * @return location behind center.
     */
    public static Location getBehind(Location location, double offset) {
        return offsetLocation(location, -validateOffset(offset));
    }

    /**
     * Returns location in front of center.
     *
     * @param location - Center.
     * @param offset   - Offset. 1.0 is exactly one block in front.
     * @return location in front of center.
     */
    public static Location getInFront(Location location, double offset) {
        return offsetLocation(location, validateOffset(offset));
    }

    /**
     * Returns vector with offset to the left of the center.
     *
     * @param location - Center.
     * @return vector with offset to the left of the center.
     */
    public static Vector getVectorToTheLeft(Location location) {
        return normalizeVector(location).setY(0.0d).rotateAroundY(Math.PI / 2);
    }

    /**
     * Returns vector with offset to the right of the center.
     *
     * @param location - Center.
     * @return vector with offset to the right of the center.
     */
    public static Vector getVectorToTheRight(Location location) {
        return normalizeVector(location).setY(0.0d).rotateAroundY(-Math.PI / 2);
    }

    /**
     * Returns location with offset to the left of the center.
     *
     * @param location - Center.
     * @param offset   - Offset.
     * @return location with offset to the left of the center.
     */
    public static Location getToTheLeft(Location location, double offset) {
        return location.clone().add(getVectorToTheLeft(location).multiply(validateOffset(offset)));
    }

    /**
     * Returns location with offset to the right of the center.
     *
     * @param location - Center.
     * @param offset   - Offset.
     * @return location with offset to the right of the center.
     */
    public static Location getToTheRight(Location location, double offset) {
        return location.clone().add(getVectorToTheRight(location).multiply(validateOffset(offset)));
    }

    private static Vector normalizeVector(Location location) {
        return location.getDirection().clone().normalize();
    }

    private static double validateOffset(double offset) {
        return Math.max(offset, 0.0d);
    }

    private static Location offsetLocation(Location location, double offset) {
        final Vector vector = normalizeVector(location).multiply(offset);
        return location.clone().add(vector);
    }

}
