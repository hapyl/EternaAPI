package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.registry.Registry;

import java.util.Set;

public class HologramRegistry extends Registry<Hologram, Void> {

    public HologramRegistry(EternaPlugin plugin) {
        super(plugin);
    }

    public void removeAll() {
        getKeys().forEach(Hologram::removeStands);
    }

    public Set<Hologram> getHolograms() {
        return getKeys();
    }

}
