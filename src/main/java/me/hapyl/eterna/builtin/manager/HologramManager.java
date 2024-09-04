package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.util.Disposable;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public final class HologramManager extends EternaManager<Hologram, Boolean> implements Disposable {

    HologramManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
    }

    @Nonnull
    public Set<Hologram> getHolograms() {
        return new HashSet<>(managing.keySet());
    }

    @Override
    public void dispose() {
        managing.keySet().forEach(Hologram::dispose);
    }
}
