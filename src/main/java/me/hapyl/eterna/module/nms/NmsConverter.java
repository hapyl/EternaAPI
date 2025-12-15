package me.hapyl.eterna.module.nms;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

/**
 * Represents a converter between a Bukkit and Nms.
 *
 * @param <B> - The Bukkit type.
 * @param <N> - The Nms type.
 */
@ApiStatus.Internal
public interface NmsConverter<B, N> {
    /**
     * Gets the bukkit object from the given Nms object.
     *
     * @param n - The Nms object.
     * @return the bukkit object from the given Nms object.
     */
    @Nonnull
    B toBukkit(@Nonnull N n);
    
    /**
     * Gets the Nms object from the given bukkit type.
     *
     * @param b - The bukkit type.
     * @return the Nms object from the given bukkit type.
     */
    @Nonnull
    N toNms(@Nonnull B b);
}
