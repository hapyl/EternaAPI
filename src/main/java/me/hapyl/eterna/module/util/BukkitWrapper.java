package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;

/**
 * Represents a class that wraps a bukkit value in some way.
 *
 * @param <T> - The bukkit value.
 */
public interface BukkitWrapper<T> {

    /**
     * Gets the bukkit value.
     *
     * @return the bukkit value.
     */
    @Nonnull
    T bukkit();

}
