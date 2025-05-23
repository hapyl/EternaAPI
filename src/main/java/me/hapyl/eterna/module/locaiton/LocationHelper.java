package me.hapyl.eterna.module.locaiton;

import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Range;

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
     * Gets a {@link Location} behind the given {@link Location} with the given offset.
     *
     * @param origin - The origin.
     * @param offset - Offset in blocks.
     * @return The location behind the origin with the given offset.
     */
    @Nonnull
    public static Location getBehind(@Nonnull Location origin, double offset) {
        return offsetLocation(origin, -validateOffset(offset));
    }
    
    /**
     * Gets a {@link Location} in front of the given {@link Location} with the given offset.
     *
     * @param origin - The origin.
     * @param offset - Offset in blocks.
     * @return The location in front of the origin with the given offset.
     */
    @Nonnull
    public static Location getInFront(@Nonnull Location origin, double offset) {
        return offsetLocation(origin, validateOffset(offset));
    }
    
    /**
     * Gets a {@link Vector} that points to the left of the given {@link Location}.
     *
     * @param origin - The origin.
     * @return a vector that points to the left of the origin.
     */
    @Nonnull
    public static Vector getVectorToTheLeft(@Nonnull Location origin) {
        return normalizedDirection(origin).setY(0.0d).rotateAroundY(Math.PI / 2);
    }
    
    /**
     * Gets a {@link Vector} that points to the right of the given {@link Location}.
     *
     * @param origin - Origin.
     * @return a vector that points to the left of the origin.
     */
    public static Vector getVectorToTheRight(@Nonnull Location origin) {
        return normalizedDirection(origin).setY(0.0d).rotateAroundY(-Math.PI / 2);
    }
    
    /**
     * Gets a {@link Location} to the left of the given {@link Location}.
     *
     * @param origin - Origin.
     * @param offset - Offset in blocks.
     * @return a location to the left of the origin.
     */
    @Nonnull
    public static Location getToTheLeft(@Nonnull Location origin, double offset) {
        return origin.clone().add(getVectorToTheLeft(origin).multiply(validateOffset(offset)));
    }
    
    /**
     * Gets a {@link Location} to the right of the given {@link Location}.
     *
     * @param origin - Origin.
     * @param offset - Offset in blocks.
     * @return a location to the right of the origin.
     */
    @Nonnull
    public static Location getToTheRight(@Nonnull Location origin, double offset) {
        return origin.clone().add(getVectorToTheRight(origin).multiply(validateOffset(offset)));
    }
    
    /**
     * Returns {@code true} if both locations are within the same block; false otherwise.
     *
     * @param location1 - First location.
     * @param location2 - Second location.
     * @return true if both locations are within the same block; false otherwise.
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
     * Linearly interpolates between two {@link Location} based on the give interpolation factor.
     *
     * @param from - Start location.
     * @param to   - End location.
     * @param mu   - The interpolation factor.
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
     * Performs cosine interpolation between two {@link Location} based on the given interpolation factor.
     * The interpolation smooths the transition between the two locations, creating an easing effect.
     *
     * @param from - Start location.
     * @param to   - End location.
     * @param mu   - The interpolation factor.
     * @return a new interpolated location.
     */
    @Nonnull
    public static Location clerp(@Nonnull Location from, @Nonnull Location to, double mu) {
        mu = (1 - Math.cos(mu * 2)) / 2;
        
        return lerp(from, to, mu);
    }
    
    /**
     * Linearly interpolates between two values based on the given interpolation factor.
     *
     * @param min - Min.
     * @param max - Max.
     * @param mu  - The interpolation factor.
     * @return an interpolated number.
     */
    public static double lerp(double min, double max, double mu) {
        return min + mu * (max - min);
    }
    
    /**
     * Temporarily adds the given {@code x}, {@code y}, {@code z} values to the provided {@link Location},
     * performs the action specified by the given {@link Consumer}, and restores the location to its original
     * coordinates.
     * <pre>{@code
     * Location location = ...;
     *
     * LocationHelper.modify(location, 1, 2, 3, then -> {
     *     then.getWorld().spawnParticle(Particle.FLAME, then, 1);
     * });
     * }</pre>
     * Both {@code location} and {@code then} variables are identical within the consumer same.
     *
     * @param location - Location.
     * @param x        - X.
     * @param y        - Y.
     * @param z        - Z.
     * @param consumer - Consumer
     */
    public static void offset(@Nonnull Location location, final double x, final double y, final double z, @Nonnull Consumer<Location> consumer) {
        location.add(x, y, z);
        consumer.accept(location);
        location.subtract(x, y, z);
    }
    
    /**
     * Temporarily adds the given {@code x}, {@code y}, {@code z} values to the provided {@link Location},
     * performs the action specified by the given {@link Function}, restores the location to its original
     * coordinates, and returns the value from the given {@link Function}.
     * <pre>{@code
     * Location location = ...;
     *
     * final LivingEntity piggy = LocationHelper.modify(location, 1, 2, 3, then -> {
     *     return Entities.PIG.spawn(location);
     * });
     * }</pre>
     * Both {@code location} and {@code then} variables are identical within the consumer same.
     *
     * @param location - Location.
     * @param x        - X.
     * @param y        - Y.
     * @param z        - Z.
     * @param fn       - Function.
     */
    @Nonnull
    public static <T> T offset(@Nonnull Location location, final double x, final double y, final double z, @Nonnull Function<Location, T> fn) {
        location.add(x, y, z);
        final T t = fn.apply(location);
        location.subtract(x, y, z);
        
        return t;
    }
    
    /**
     * Temporarily adds the given {@code x}, {@code y}, {@code z} values to the provided {@link Location},
     * performs the action specified by the given {@link Consumer}, and restores the location to its original
     * coordinates.
     * <pre>{@code
     * Location location = ...;
     *
     * LocationHelper.offset(location, 1, 2, 3, () -> {
     *     location.getWorld().spawnParticle(Particle.FLAME, location, 1);
     * });
     * }</pre>
     *
     * @param location - Location.
     * @param x        - X.
     * @param y        - Y.
     * @param z        - Z.
     * @param action   - Action to perform after addition and before subtraction.
     */
    public static void offset(@Nonnull Location location, final double x, final double y, final double z, @Nonnull Runnable action) {
        location.add(x, y, z);
        action.run();
        location.subtract(x, y, z);
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
    
    /**
     * Calculates the squared distance between two {@link Location} objects along the specified axes.
     * <p>This method avoids the computational cost of square root calculation, making it suitable
     * for comparisons or scenarios where exact distances are unnecessary.</p>
     *
     * @param from - The starting {@link Location}.
     * @param to   - The target {@link Location}.
     * @param axis - The axes along which to calculate the distance.
     * @return the squared distance between the two locations along the specified axis.
     * @throws IllegalArgumentException if no axes are provided.
     */
    public static double distanceSquared(@Nonnull Location from, @Nonnull Location to, @Nonnull @Range(from = 1, to = 3) Axis... axis) {
        Validate.isTrue(axis.length > 0, "There must be at least one axis!");
        
        double distance = 0.0d;
        
        if (CollectionUtils.contains(axis, Axis.X)) {
            distance += square(from.getX() - to.getX());
        }
        
        if (CollectionUtils.contains(axis, Axis.Y)) {
            distance += square(from.getY() - to.getY());
        }
        
        if (CollectionUtils.contains(axis, Axis.Z)) {
            distance += square(from.getZ() - to.getZ());
        }
        
        return distance;
    }
    
    /**
     * <b>Attempts</b> to anchor the location, so it's directly on a block.
     *
     * @param location - The location to anchor.
     * @return A new anchored location.
     */
    @Nonnull
    public static Location anchor(@Nonnull Location location) {
        return BukkitUtils.anchorLocation(location);
    }
    
    /**
     * Finds a random {@link Location} around the given location.
     * <br>
     * This anchors the location.
     *
     * @param location - Location to find around.
     * @param max      - Max distance in each direction.
     * @return A new location.
     */
    @Nonnull
    public static Location randomAround(@Nonnull Location location, double max) {
        return BukkitUtils.findRandomLocationAround(location, max);
    }
    
    /**
     * Converts the given {@link Location} into readable string.
     *
     * @param location - The location to convert.
     * @return a readable location string.
     */
    @Nonnull
    public static String toString(@Nonnull Location location) {
        return BukkitUtils.locationToString(location);
    }
    
    /**
     * Converts the given {@link Location} into an array of coordinates, following the pattern:
     * <pre>{@code [x, y, z]}</pre>
     *
     * @param location - The location to convert.
     * @return a new array with the location's x, y, and z coordinates.
     */
    public static double[] toCoordinates(@Nonnull Location location) {
        return new double[] { location.getX(), location.getY(), location.getZ() };
    }
    
    /**
     * Converts the given {@link Location} into an array of coordinates, following the pattern:
     * <pre>{@code [x, y, z, yaw, pitch]}</pre>
     *
     * @param location - The location to convert.
     * @return a new array with the location's x, y, z, yaw, and pitch values.
     */
    public static double[] toCoordinatesWithRotation(@Nonnull Location location) {
        return new double[] { location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch() };
    }
    
    /**
     * Squares the given number.
     *
     * @param a - The number to square.
     * @return the squared number.
     */
    public static double square(double a) {
        return a * a;
    }
    
    /**
     * Gets the normalized direction of the given {@link Location}.
     *
     * @param location - The location.
     * @return the normalized direction of the given {@link Location}.
     */
    @Nonnull
    public static Vector normalizedDirection(@Nonnull Location location) {
        return location.getDirection().normalize();
    }
    
    private static double validateOffset(double offset) {
        return Math.max(offset, 0.0d);
    }
    
    private static Location offsetLocation(Location location, double offset) {
        final Vector vector = normalizedDirection(location).multiply(offset);
        return location.clone().add(vector);
    }
    
}
