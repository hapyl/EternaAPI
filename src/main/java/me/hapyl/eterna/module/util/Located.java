package me.hapyl.eterna.module.util;

import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * An interface representing objects that exist in the world at a specific location.
 */
public interface Located {

    /**
     * Gets the location of this object in the world.
     *
     * @return The {@link Location} of this object.
     */
    @Nonnull
    Location getLocation();

    /**
     * Gets the world in which this object is located.
     * <p>
     * This is a convenience method that retrieves the world from the location.
     * <p>The default implementation is as follows:
     * <pre>{@code
     * @Nonnull
     * default World getWorld() {
     *     return getLocation().getWorld();
     * }
     * }</pre></p>
     *
     * @return The {@link World} where this object is located.
     */
    @Nonnull
    default World getWorld() {
        return getLocation().getWorld();
    }

}
