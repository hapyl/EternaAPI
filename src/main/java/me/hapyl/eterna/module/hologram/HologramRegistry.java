package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.registry.Registry;

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
