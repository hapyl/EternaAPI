package kz.hapyl.spigotutils.module.locaiton;

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
        offset = validateOffset(offset);
        return offsetLocation(location, -offset);
    }

    /**
     * Returns location in front of center.
     *
     * @param location - Center.
     * @param offset   - Offset. 1.0 is exactly one block in front.
     * @return location in front of center.
     */
    public static Location getInFront(Location location, double offset) {
        offset = validateOffset(offset);
        return offsetLocation(location, offset);
    }

    /**
     * Returns vector with offset to the left of the center.
     *
     * @param location - Center.
     * @return vector with offset to the left of the center.
     */
    public static Vector getToTheLeft(Location location) {
        final Vector vector = normalizeVector(location);
        return new Vector(vector.getZ(), 0.0d, -vector.getX()).normalize();
    }

    /**
     * Returns vector with offset to the right of the center.
     *
     * @param location - Center.
     * @return vector with offset to the right of the center.
     */
    public static Vector getToTheRight(Location location) {
        final Vector vector = normalizeVector(location);
        return new Vector(-vector.getZ(), 0.0d, vector.getX()).normalize();
    }

    /**
     * Returns location with offset to the left of the center.
     *
     * @param location - Center.
     * @param offset   - Offset.
     * @return location with offset to the left of the center.
     */
    public static Location getToTheLeft(Location location, double offset) {
        offset = validateOffset(offset);
        location.add(getToTheLeft(location).multiply(offset));
        return location;
    }

    /**
     * Returns location with offset to the right of the center.
     *
     * @param location - Center.
     * @param offset   - Offset.
     * @return location with offset to the right of the center.
     */
    public static Location getToTheRight(Location location, double offset) {
        offset = validateOffset(offset);
        location.add(getToTheRight(location).multiply(offset));
        return location;
    }

    private static Vector normalizeVector(Location location) {
        return location.getDirection().normalize();
    }

    private static double validateOffset(double offset) {
        return Math.max(offset, 0.0d);
    }

    private static Location offsetLocation(Location location, double offset) {
        final Vector vector = normalizeVector(location).multiply(offset);
        return location.add(vector);
    }

}
