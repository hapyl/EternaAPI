package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that may be represented as a {@code bukkit} object in some way.
 *
 * @param <B> - The bukkit object type.
 */
public interface BukkitWrapper<B> {
    
    /**
     * Gets the {@code bukkit} object.
     *
     * @return the bukkit object.
     */
    @NotNull
    B bukkit();
    
}
