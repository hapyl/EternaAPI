package me.hapyl.spigotutils.module.entity;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.registry.Registry;

import javax.annotation.Nullable;

public class RopeRegistry extends Registry<Integer, Rope> {

    private int freeId;

    public RopeRegistry(EternaPlugin plugin) {
        super(plugin);
        freeId = 0;
    }

    public void register(Rope rope) {
        rope.setId(freeId);
        register(freeId, rope);
        freeId++;
    }

    @Nullable
    public Rope getById(int id) {
        return byKey(id);
    }

    public void removeAll() {
        forEachValues(rope -> {
            if (rope.isRemoveAtRestart()) {
                rope.remove();
            }
        });
    }

}
