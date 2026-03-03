package me.hapyl.eterna.module.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that may have {@code X}, {@code Y} and {@code Z} coordinates.
 */
public interface Coordinates {
    
    /**
     * Gets the {@code X} coordinate.
     *
     * @return the {@code X} coordinate.
     */
    double x();
    
    /**
     * Gets the {@code Y} coordinate.
     *
     * @return the {@code Y} coordinate.
     */
    double y();
    
    /**
     * Gets the {@code Z} coordinate.
     *
     * @return the {@code Z} coordinate.
     */
    double z();
    
    /**
     * Creates a new {@link Location} in the given {@link World} with this {@link Coordinates}.
     *
     * @param world - The world to create the location in.
     * @return a new location in the given world with this coordinates.
     */
    @NotNull
    default Location toLocation(@NotNull World world) {
        return new Location(world, this.x(), this.y(), this.z());
    }
    
}
