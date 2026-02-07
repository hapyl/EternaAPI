package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.component.ComponentListSupplier;
import me.hapyl.eterna.module.location.LocationHelper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an abstract {@link Hologram} implementation.
 */
public abstract class AbstractHologram implements Hologram {
    
    @NotNull protected Location location;
    @NotNull protected ComponentListSupplier supplier;
    
    @ApiStatus.Internal
    AbstractHologram(@NotNull Location location) {
        this.location = LocationHelper.copyOf(location);
        this.supplier = player -> ComponentList.empty();
    }
    
    /**
     * Sets the lines of this {@link Hologram}.
     *
     * <p>Each line is generated dynamically for each {@link Player} using the provided {@link ComponentListSupplier}.</p>
     *
     * @param supplier - The component list supplier.
     */
    @Override
    public void setLines(@NotNull ComponentListSupplier supplier) {
        this.supplier = supplier;
    }
    
    /**
     * Gets a copy of the {@link Location} of this {@link Hologram}.
     *
     * @return a copy of the location of this hologram.
     */
    @NotNull
    @Override
    public Location getLocation() {
        return LocationHelper.copyOf(location);
    }
    
    /**
     * Gets the {@link World} of this {@link Hologram}.
     *
     * @return the world of this hologram.F
     */
    @NotNull
    @Override
    public World getWorld() {
        return location.getWorld();
    }
    
}
