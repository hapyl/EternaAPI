package me.hapyl.spigotutils.module.entity;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.registry.IdRegistry;

import javax.annotation.Nullable;

public class RopeRegistry extends IdRegistry<Rope> {

    public RopeRegistry(EternaPlugin plugin) {
        super(plugin);
    }

    @Nullable
    public Rope getById(int id) {
        return byKey(id);
    }

}
