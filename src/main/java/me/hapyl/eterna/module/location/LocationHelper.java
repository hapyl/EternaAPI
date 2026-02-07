package me.hapyl.eterna.module.location;

import me.hapyl.eterna.module.annotate.DefensiveCopy;
import me.hapyl.eterna.module.annotate.Mutates;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.math.Vectors;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Represents a useful helper utility class for {@link Location} operations.
 */
@UtilityClass
public final class LocationHelper {
    
    /**
     * Defines all the {@link Axis}.
     */
    public static final Set<Axis> allAxis = Set.of(Axis.X, Axis.Y, Axis.Z);
    
    private LocationHelper() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Offsets the given {@link Location} with the given {@code offset} forward.
     *
     * @param origin - The origin location.
     * @param offset - The offset.
     * @return a new location offset forward by the given {@code offset}.
     */
    @NotNull
    public static Location getInFront(@NotNull @DefensiveCopy Location origin, double offset) {
        return copyOf(origin).add(origin.getDirection().normalize().multiply(maxOffset(offset)));
    }
    
    /**
     * Offset the given {@link Location} with the given {@code offset} backwards.
     *
     * @param origin - The origin location.
     * @param offset - The offset.
     * @return a new location offset backwards by the given {@code offset}.
     */
    @NotNull
    public static Location getBehind(@NotNull @DefensiveCopy Location origin, double offset) {
        return copyOf(origin).add(origin.getDirection().normalize().multiply(-maxOffset(offset)));
    }
    
    /**
     * Offsets the given {@link Location} with the given {@code offset} to the left.
     *
     * @param origin - The origin location.
     * @param offset - The offset.
     * @return a new location offset to the left by the given {@code offset}.
     */
    @NotNull
    public static Location getToTheLeft(@NotNull @DefensiveCopy Location origin, double offset) {
        return copyOf(origin).add(Vectors.left(origin.getDirection(), maxOffset(offset)));
    }
    
    /**
     * Offsets the given {@link Location} with the given {@code offset} to the right.
     *
     * @param origin - The origin location.
     * @param offset - The offset.
     * @return a new location offset to the right by the given {@code offset}.
     */
    @NotNull
    public static Location getToTheRight(@NotNull @DefensiveCopy Location origin, double offset) {
        return copyOf(origin).add(Vectors.right(origin.getDirection(), maxOffset(offset)));
    }
    
    /**
     * Gets whether the two {@link Location} block coordinates are equal.
     *
     * @param location1 - The first location.
     * @param location2 - The second location.
     * @return {@code true} if the two location block coordinates are equal; {@code false} otherwise.
     */
    public static boolean blockLocationEquals(@NotNull Location location1, @NotNull Location location2) {
        if (!Objects.equals(location1.getWorld(), location2.getWorld())) {
            return false;
        }
        
        final int block1x = location1.getBlockX();
        final int block1y = location1.getBlockY();
        final int block1z = location1.getBlockZ();
        
        final int block2x = location2.getBlockX();
        final int block2y = location2.getBlockY();
        final int block2z = location2.getBlockZ();
        
        return block1x == block2x && block1y == block2y && block1z == block2z;
    }
    
    /**
     * Linearly interpolates between the two {@link Location}.
     *
     * @param from - The starting location.
     * @param to   - The target location.
     * @param mu   - The interpolation factor, where {@code 0} returns {@code from} and {@code 1} returns {@code to}.
     * @return A new interpolated location.
     */
    @NotNull
    public static Location lerp(@NotNull @DefensiveCopy Location from, @NotNull Location to, double mu) {
        final double x = lerp(from.getX(), to.getX(), mu);
        final double y = lerp(from.getY(), to.getY(), mu);
        final double z = lerp(from.getZ(), to.getZ(), mu);
        
        return new Location(from.getWorld(), x, y, z);
    }
    
    /**
     * Cosine-interpolates between the two {@link Location}, producing a smoother transition than {@link #lerp(Location, Location, double)}.
     *
     * @param from - The starting location.
     * @param to   - The target location.
     * @param mu   - The interpolation factor, where {@code 0} returns {@code from} and {@code 1} returns {@code to}.
     * @return A new smoothly interpolated location.
     */
    @NotNull
    public static Location clerp(@NotNull @DefensiveCopy Location from, @NotNull Location to, double mu) {
        mu = (1 - Math.cos(mu * 2)) / 2;
        
        return lerp(from, to, mu);
    }
    
    /**
     * Linearly interpolates between two {@code double}.
     *
     * @param min - The starting value.
     * @param max - The target value.
     * @param mu  - The interpolation factor, where {@code 0} returns {@code min} and {@code 1} returns {@code max}.
     * @return The interpolated value.
     */
    public static double lerp(double min, double max, double mu) {
        return min + mu * (max - min);
    }
    
    /**
     * Offsets the given {@link Location} by the given {@code coordinates}, accepts the given {@link Consumer} on the modified {@link Location}
     * before restoring the {@code location} to the original values.
     *
     * <p>Example usage:</p>
     *
     * <pre>{@code
     * LocationHelper.offset(location, 1, 1, 1, _location -> {
     *     _location.getWorld().spawnParticle(Particle.FLAME, _location, 1);
     * });
     * }</pre>
     *
     * @param location - The location to offset.
     * @param x        - The {@code x} coordinate offset.
     * @param y        - The {@code y} coordinate offset.
     * @param z        - The {@code z} coordinate offset.
     * @param consumer - The consumer to accept on offset location.
     */
    public static void offset(@NotNull Location location, final double x, final double y, final double z, @NotNull Consumer<Location> consumer) {
        location.add(x, y, z);
        consumer.accept(location);
        location.subtract(x, y, z);
    }
    
    /**
     * Offsets the given {@link Location} by the given {@code coordinates}, runs the given {@link Runnable} before restoring the {@code location} to the original values.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * LocationHelper.offset(location, 1, 1, 1, () -> {
     *     // The method mutates the original object, so we can reference the origin
     *     // location, but should not modify it!
     *     location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 1);
     * });
     * }</pre>
     *
     * @param location - The location to offset.
     * @param x        - The {@code x} coordinate offset.
     * @param y        - The {@code y} coordinate offset.
     * @param z        - The {@code z} coordinate offset.
     * @param runnable - The runnable to run.
     */
    public static void offset(@NotNull Location location, final double x, final double y, final double z, @NotNull Runnable runnable) {
        offset(location, x, y, z, _location -> runnable.run());
    }
    
    /**
     * Gets the squared distance between the two {@link Location} along the given {@link Axis}.
     *
     * @param from - The first location.
     * @param to   - The second location.
     * @param axis - The axis along which to measure the distance.
     * @return the squared distance between the two location along the given axis.
     */
    public static double distanceSquared(@NotNull Location from, @NotNull Location to, @Nullable @Range(from = 1, to = 3) Axis... axis) {
        if (!from.getWorld().equals(to.getWorld())) {
            return Double.MAX_VALUE;
        }
        
        final Set<Axis> axisSet = (axis != null && axis.length != 0) ? Set.of(axis) : allAxis;
        double distance = 0.0;
        
        if (axisSet.contains(Axis.X)) {
            distance += Numbers.square(from.getX() - to.getX());
        }
        
        if (axisSet.contains(Axis.Y)) {
            distance += Numbers.square(from.getY() - to.getY());
        }
        
        if (axisSet.contains(Axis.Z)) {
            distance += Numbers.square(from.getZ() - to.getZ());
        }
        
        return distance;
    }
    
    /**
     * Gets the distance between the two {@link Location} along the given {@link Axis}.
     *
     * @param from - The first location.
     * @param to   - The second location.
     * @param axis - The axis along which to measure the distance.
     * @return the distance between the two location along the given axis.
     */
    public static double distance(@NotNull Location from, @NotNull Location to, @NotNull @Range(from = 1, to = 3) Axis... axis) {
        return Math.sqrt(distanceSquared(from, to, axis));
    }
    
    /**
     * Anchors the given {@link Location} on the highest block below the {@link Location}, accounting for that {@code block} {@link BoundingBox}.
     *
     * @param original - The location to anchor.
     * @return a new location anchored on the highest block below the location.
     */
    @NotNull
    public static Location anchor(@NotNull @DefensiveCopy Location original) {
        final Location location = copyOf(original);
        final World world = location.getWorld();
        
        while (location.getY() < world.getMaxHeight() && !location.getBlock().isPassable()) {
            location.add(0, 1, 0);
        }
        
        while (location.getY() > world.getMinHeight() && location.getBlock().getRelative(BlockFace.DOWN).isPassable()) {
            location.subtract(0, 1, 0);
        }
        
        // Compensate for block shape
        location.setY(location.getBlock().getRelative(BlockFace.DOWN).getBoundingBox().getMaxY());
        
        return location;
    }
    
    /**
     * Randomly offsets the given {@link Location} around the given {@code offset} and {@link #anchor(Location)} it.
     *
     * @param location - The location to offset.
     * @param offset   - The maximum offset range.
     * @return a new randomly offset location.
     */
    @NotNull
    public static Location offsetRandomly(@NotNull @DefensiveCopy Location location, final double offset) {
        final double x = BukkitUtils.RANDOM.nextDouble(-offset, Math.nextUp(offset));
        final double z = BukkitUtils.RANDOM.nextDouble(-offset, Math.nextUp(offset));
        
        return anchor(copyOf(location).add(x, 0, z));
    }
    
    /**
     * Gets a {@link String} representation of the given {@link Location}.
     * <p>
     * The location is formatted following the pattern: {@code %.1f %.1f %.1f}
     * </p>
     *
     * @param location - The location to get the string representation for.
     * @return a string representation of the given location.
     */
    @NotNull
    public static String toString(@NotNull Location location) {
        return toString(location, false);
    }
    
    /**
     * Gets a {@link String} representation of the given {@link Location}.
     * <p>
     * The location is formatted following the pattern: {@code %.1f %.1f %.1f}
     * </p>
     *
     * @param location        - The location to get the string representation for.
     * @param includeRotation - {@code true} to include {@code yaw} and {@code pitch}; {@code false} otherwise.
     * @return a string representation of the given location.
     */
    @NotNull
    public static String toString(@NotNull Location location, boolean includeRotation) {
        final String toString = "%.1f %.1f %.1f".formatted(location.getX(), location.getY(), location.getZ());
        
        return includeRotation ? toString + " %.1f %.1f".formatted(location.getYaw(), location.getPitch()) : toString;
    }
    
    /**
     * Gets the {@code y}, {@code y} and {@code z} from the given {@link Location} as a {@code double[]}.
     *
     * @param location - The location for which to get the coordinates.
     * @return a {@code double[]} containing the given location {@code y}, {@code y} and {@code z}.
     */
    public static double[] toCoordinates(@NotNull Location location) {
        return new double[] { location.getX(), location.getY(), location.getZ() };
    }
    
    /**
     * Gets the {@code y}, {@code y}, {@code z}, {@code yaw} and {@code pitch} from the given {@link Location} as a {@code double[]}.
     *
     * @param location - The location for which to get the coordinates.
     * @return a {@code double[]} containing the given location {@code y}, {@code y}, {@code z}, {@code yaw} and {@code pitch}.
     */
    public static double[] toCoordinatesWithRotation(@NotNull Location location) {
        return new double[] { location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch() };
    }
    
    /**
     * Creates a copy of the given {@link Location}.
     *
     * @param location - The location to copy.
     * @return a copied location.
     */
    @NotNull
    public static Location copyOf(@NotNull @DefensiveCopy Location location) {
        return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    /**
     * Creates a copy of the given {@link Location}.
     *
     * @param location - The location to copy.
     * @return a copied location, or {@code null} if the provided location was {@code null}.
     */
    @Nullable
    public static Location copyOfNullable(@Nullable @DefensiveCopy Location location) {
        return location != null ? copyOf(location) : null;
    }
    
    /**
     * Centers the given {@link Location}.
     *
     * @param location - The location to center.
     * @return a new centered location.
     */
    @NotNull
    public static Location center(@NotNull @DefensiveCopy Location location) {
        return copyOf(location).add(0.5, 0.5, 0.5);
    }
    
    /**
     * Gets whether the difference between the two {@link Location} is longer than a full block.
     *
     * @param from - The first location.
     * @param to   - The second location.
     * @return {@code true} if the difference between the two location is longer than a full block, {@code false} otherwise.
     */
    public static boolean hasMovedBlock(@NotNull Location from, @NotNull Location to) {
        if (from.getWorld() != to.getWorld()) {
            return false;
        }
        
        return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
    }
    
    /**
     * Gets the relative {@code x / z} offsets surrounding a given location.
     *
     * <p>
     * The returned array contains the four cardinal directions and the four diagonal directions,
     * each represented as a {@code double[2]} in the form {@code {xOffset, zOffset}}.
     * </p>
     */
    public static double[][] relativeOffsets() {
        return new double[][] {
                { -1.0, 0.0 },
                { +1.0, 0.0 },
                { 0.0, -1.0 },
                { 0.0, +1.0 },
                { +1.0, +1.0 },
                { -1.0, +1.0 },
                { +1.0, -1.0 },
                { -1.0, -1.0 }
        };
    }
    
    /**
     * Gets the default {@link World} for the server.
     *
     * @return the default world for the server.
     */
    @NotNull
    public static World defaultWorld() {
        return Bukkit.getWorlds().getFirst();
    }
    
    /**
     * Creates a new {@link Location} in the {@link #defaultWorld()} at the given coordinates.
     *
     * @param x     - The {@code x} coordinate.
     * @param y     - The {@code y} coordinate.
     * @param z     - The {@code z} coordinate.
     * @param yaw   - The {@code yaw}.
     * @param pitch - The {@code pitch}.
     * @return a new location in the default world.
     */
    @NotNull
    public static Location defaultLocation(final double x, final double y, final double z, final float yaw, final float pitch) {
        return new Location(defaultWorld(), x, y, z, yaw, pitch);
    }
    
    /**
     * Creates a new {@link Location} in the {@link #defaultWorld()} at the given coordinates.
     *
     * @param x - The {@code x} coordinate.
     * @param y - The {@code y} coordinate.
     * @param z - The {@code z} coordinate.
     * @return a new location in the default world.
     */
    @NotNull
    public static Location defaultLocation(final double x, final double y, final double z) {
        return defaultLocation(x, y, z, 0.0f, 0.0f);
    }
    
    /**
     * Creates a new {@link Location} in the {@link #defaultWorld()} at the given coordinates.
     *
     * <p>This method centres the given coordinates.</p>
     *
     * @param x - The {@code x} coordinate.
     * @param y - The {@code y} coordinate.
     * @param z - The {@code z} coordinate.
     * @return a new location in the default world.
     */
    @NotNull
    public static Location defaultLocation(final int x, final int y, final int z) {
        return defaultLocation(x + 0.5, y + 0.5, z + 0.5, 0.0f, 0.0f);
    }
    
    /**
     * Merges the {@code yaw} and {@code pitch} values of the {@code to} {@link Location} from the {@code from} {@link Location}.
     *
     * @param from - The location from which to merge the yaw and pitch.
     * @param to   - The location where to merge the yaw and pitch.
     */
    public static void mergeYawPitch(@NotNull Location from, @NotNull @Mutates Location to) {
        to.setPitch(from.getPitch());
        to.setYaw(from.getYaw());
    }
    
    @ApiStatus.Internal
    private static double maxOffset(double offset) {
        return Math.max(offset, 0.0);
    }
    
}
