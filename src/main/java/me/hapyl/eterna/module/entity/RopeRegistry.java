package me.hapyl.eterna.module.entity;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.registry.IdRegistry;

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
