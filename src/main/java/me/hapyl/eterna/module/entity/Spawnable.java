package me.hapyl.eterna.module.entity;

import javax.annotation.Nonnull;

public interface Spawnable<E> {

    @Nonnull
    E spawn();

    boolean isSpawned();

}
