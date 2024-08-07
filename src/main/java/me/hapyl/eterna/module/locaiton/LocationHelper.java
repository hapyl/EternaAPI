package me.hapyl.eterna.module.locaiton;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Useful helper to work with locations.
 */
public final class LocationHelper {

    private LocationHelper() {
    }

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

    /**
     * Returns true if both locations are within the same block.
     *
     * @param location1 - First location.
     * @param location2 - Second location.
     * @return true if both locations are within the same block.
     */
    public static boolean blockLocationEquals(@Nonnull Location location1, @Nonnull Location location2) {
        final World world = location1.getWorld();

        if (world == null || world != location2.getWorld()) {
            return false;
        }

        final int blockX = location1.getBlockX();
        final int blockY = location1.getBlockY();
        final int blockZ = location1.getBlockZ();

        final int blockX2 = location2.getBlockX();
        final int blockY2 = location2.getBlockY();
        final int blockZ2 = location2.getBlockZ();

        return blockX == blockX2 && blockY == blockY2 && blockZ == blockZ2;
    }

    /**
     * Calculates a new location with a linear interpolation.
     *
     * @param from - Start location.
     * @param to   - End location.
     * @param mu   - The "strength" of the interpolations.
     * @return a new interpolated location.
     */
    @Nonnull
    public static Location lerp(@Nonnull Location from, @Nonnull Location to, double mu) {
        final double x = lerp(from.getX(), to.getX(), mu);
        final double y = lerp(from.getY(), to.getY(), mu);
        final double z = lerp(from.getZ(), to.getZ(), mu);

        return new Location(from.getWorld(), x, y, z);
    }

    /**
     * Calculates a new location with a cosine interpolation.
     * <br>
     * Cosine interpolation is slower but much smoother than the linear interpolation.
     *
     * @param from - Start location.
     * @param to   - End location.
     * @param mu   - The "strength" of the interpolations.
     * @return a new interpolated location.
     */
    @Nonnull
    public static Location clerp(@Nonnull Location from, @Nonnull Location to, double mu) {
        mu = (1 - Math.cos(mu * 2)) / 2;

        return lerp(from, to, mu);
    }

    /**
     * Lerps the number between <code>min</code> and <code>max</code> with the given <code>mu</code>.
     *
     * @param min - Min.
     * @param max - Max.
     * @param mu  - Strength.
     * @return an interpolated number.
     */
    public static double lerp(double min, double max, double mu) {
        return min + mu * (max - min);
    }

    /**
     * Modifies the {@link Location}, then calls {@link Consumer#accept(Object)} with the modified location before restoring the location.
     * <br>
     * This does mutate the location, but restores it back instantly.
     *
     * @param location - Location.
     * @param x        - X.
     * @param y        - Y.
     * @param z        - Z.
     * @param consumer - Consumer
     */
    public static void modify(@Nonnull Location location, double x, double y, double z, @Nonnull Consumer<Location> consumer) {
        location.add(x, y, z);
        consumer.accept(location);
        location.subtract(x, y, z);
    }

    /**
     * Modifies the {@link Location}, then calls {@link Consumer#accept(Object)} with the modified location before restoring the location.
     * <br>
     * This does mutate the location, but restores it back instantly.
     *
     * @param location - Location.
     * @param x        - X.
     * @param y        - Y.
     * @param z        - Z.
     * @param fn       - Function.
     */
    @Nonnull
    public static <T> T modify(@Nonnull Location location, double x, double y, double z, @Nonnull Function<Location, T> fn) {
        location.add(x, y, z);
        final T t = fn.apply(location);
        location.subtract(x, y, z);

        return t;
    }

    /**
     * Creates a copy of a given {@link Location} and adds the given coordinates to it.
     *
     * @param location - Location.
     * @param x        - X
     * @param y        - Y.
     * @param z        - Z.
     * @return a new location offset with the given coordinates.
     */
    @Nonnull
    public static Location addAsNew(@Nonnull Location location, double x, double y, double z) {
        return new Location(
                location.getWorld(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        ).add(x, y, z);
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
