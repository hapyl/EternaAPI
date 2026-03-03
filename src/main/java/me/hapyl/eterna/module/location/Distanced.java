package me.hapyl.eterna.module.location;

import me.hapyl.eterna.module.math.Numbers;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that has {@link Coordinates} and provides distance measurement.
 *
 * <p>
 *
 * </p>
 */
public interface Distanced extends Coordinates {
    
    /**
     * Gets the {@code X} coordinate.
     *
     * @return the {@code X} coordinate.
     */
    @Override
    double x();
    
    /**
     * Gets the {@code Y} coordinate.
     *
     * @return the {@code Y} coordinate.
     */
    @Override
    double y();
    
    /**
     * Gets the {@code Z} coordinate.
     *
     * @return the {@code Z} coordinate.
     */
    @Override
    double z();
    
    /**
     * Calculates the squared distance between this object to the given coordinates.
     *
     * @param x - The {@code X} coordinate.
     * @param y - The {@code X} coordinate.
     * @param z - The {@code X} coordinate.
     * @return the squared distance between this object and the given coordinates.
     */
    default double distanceToSquared(double x, double y, double z) {
        return distanceToSquared(this.x(), this.y(), this.z(), x, y, z);
    }
    
    /**
     * Calculates the squared distance between this object to the given {@link Coordinates}.
     *
     * @param coordinates - The coordinates to measure the distance to.
     * @return the squared distance between this object and the given coordinates.
     */
    default double distanceToSquared(@NotNull Coordinates coordinates) {
        return this.distanceToSquared(coordinates.x(), coordinates.y(), coordinates.z());
    }
    
    /**
     * Calculates the squared distance between this object to the given {@link Location}.
     *
     * @param location - The location to measure the distance to.
     * @return the squared distance between this object and the given location.
     */
    default double distanceToSquared(@NotNull Location location) {
        return this.distanceToSquared(location.x(), location.y(), location.z());
    }
    
    /**
     * Calculates the distance between this object to the given coordinates.
     *
     * @param x - The {@code X} coordinate.
     * @param y - The {@code X} coordinate.
     * @param z - The {@code X} coordinate.
     * @return the distance between this object and the given coordinates.
     */
    default double distanceTo(double x, double y, double z) {
        return Math.sqrt(this.distanceToSquared(x, y, z));
    }
    
    /**
     * Calculates the distance between this object to the given {@link Coordinates}.
     *
     * @param coordinates - The coordinates to measure the distance to.
     * @return the distance between this object and the given coordinates.
     */
    default double distanceTo(@NotNull Coordinates coordinates) {
        return Math.sqrt(this.distanceToSquared(coordinates.x(), coordinates.y(), coordinates.z()));
    }
    
    /**
     * Calculates the distance between this object to the given {@link Location}.
     *
     * @param location - The location to measure the distance to.
     * @return the distance between this object and the given location.
     */
    default double distanceTo(@NotNull Location location) {
        return Math.sqrt(this.distanceToSquared(location.x(), location.y(), location.z()));
    }
    
    /**
     * A static factory method to measure squared distance between two sets of coordinates.
     *
     * @param x1 - The first {@code X} coordinate.
     * @param y1 - The first {@code Y} coordinate.
     * @param z1 - The first {@code Z} coordinate.
     * @param x2 - The second {@code X} coordinate.
     * @param y2 - The second {@code Y} coordinate.
     * @param z2 - The second {@code Z} coordinate.
     * @return the squared distance between the two sets of coordinates.
     */
    static double distanceToSquared(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Numbers.square(x1 - x2) + Numbers.square(y1 - y2) + Numbers.square(z1 - z2);
    }
    
}
