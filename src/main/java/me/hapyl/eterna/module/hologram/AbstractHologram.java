package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.component.ComponentList;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

public abstract class AbstractHologram implements Hologram {
    
    @Nonnull protected Location location;
    @Nonnull protected ComponentSupplier supplier;
    
    public AbstractHologram(@Nonnull Location location) {
        this.location = BukkitUtils.newLocation(location);
        this.supplier = player -> ComponentList.empty();
    }
    
    @Override
    public void setLines(@Nonnull ComponentSupplier supplier) {
        this.supplier = supplier;
    }
    
    @Nonnull
    @Override
    public Location getLocation() {
        return BukkitUtils.newLocation(location);
    }
    
    @Nonnull
    @Override
    public World getWorld() {
        return location.getWorld();
    }
    
}
