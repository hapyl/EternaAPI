package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.list.StringList;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AbstractHologram implements Hologram {
    
    @Nonnull protected Location location;
    @Nonnull protected LineSupplier supplier;
    
    public AbstractHologram(@Nonnull Location location) {
        this.location = BukkitUtils.newLocation(location);
        this.supplier = player -> StringList.empty();
    }
    
    @Override
    @OverridingMethodsMustInvokeSuper
    public void setLines(@Nonnull LineSupplier supplier) {
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
