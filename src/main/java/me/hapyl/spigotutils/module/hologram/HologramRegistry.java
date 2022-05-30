package me.hapyl.spigotutils.module.hologram;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.Registry;

import java.util.Set;

public class HologramRegistry extends Registry<Hologram> {

    public final Set<Hologram> holograms;

    public HologramRegistry(EternaPlugin plugin) {
        super(plugin);
        holograms = Sets.newHashSet();
    }

    public void removeAll() {
        for (Hologram hologram : holograms) {
            hologram.removeStands();
        }
    }

    public Set<Hologram> getHolograms() {
        return holograms;
    }

    @Override
    public void register(Hologram hologram) {
        holograms.add(hologram);
    }

    @Override
    public void unregister(Hologram hologram) {
        holograms.remove(hologram);
    }
}
