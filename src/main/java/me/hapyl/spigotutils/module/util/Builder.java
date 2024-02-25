package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;

/**
 * Represents a builder of something.
 */
public interface Builder<T> {

    @Nonnull
    T build();

}
