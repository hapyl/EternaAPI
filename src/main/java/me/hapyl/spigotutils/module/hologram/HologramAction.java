package me.hapyl.spigotutils.module.hologram;

import org.bukkit.entity.Player;

public abstract class HologramAction<T> {

    private final T t;
    private final Hologram hologram;

    public HologramAction(Hologram hologram, T t) {
        this.hologram = hologram;
        this.t = t;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public abstract T getObject();

    public abstract void consume(Player receiver);

}
