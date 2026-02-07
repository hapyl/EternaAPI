package me.hapyl.eterna.module.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * An interface representing objects that exist in the {@link World} at a specific {@link Location}.
 */
public interface Located {
    
    /**
     * Gets the {@link Location} of this object.
     *
     * @return The location of this object.
     */
    @NotNull
    Location getLocation();
    
    /**
     * Sets the {@link Location} of this object.
     * <p>The default implementation throws an {@link UnsupportedOperationException}.</p>
     *
     * @param location - The new location to set.
     */
    default void setLocation(@NotNull Location location) {
        throw new UnsupportedOperationException("setLocation()");
    }
    
    /**
     * Gets the {@link World} in which this object is location.
     * <p>This is a convenience method that retrieves the {@link World} from the {@link Location}.</p>
     *
     * @return The world in which this object is located.
     */
    @NotNull
    default World getWorld() {
        return getLocation().getWorld();
    }
    
}
