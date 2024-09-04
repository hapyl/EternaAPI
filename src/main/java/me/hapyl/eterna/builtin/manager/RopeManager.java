package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.entity.Rope;

import javax.annotation.Nonnull;

public final class RopeManager extends EternaManager<Integer, Rope> {

    private int freeId = 0;

    RopeManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
    }

    public void register(@Nonnull Rope rope) {
        register(freeId++, rope);
    }
}
