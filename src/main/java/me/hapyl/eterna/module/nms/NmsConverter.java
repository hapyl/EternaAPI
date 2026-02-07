package me.hapyl.eterna.module.nms;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a converter between {@code bukkit} and {@code nms}.
 *
 * @param <B> - The {@code bukkit} value type.
 * @param <N> - The {@code nms} value type.
 */
@ApiStatus.Internal
public interface NmsConverter<B, N> {
    
    /**
     * Converts this object into a {@code bukkit} object.
     *
     * @param n - The {@code nms} object.
     * @return this object as {@code bukkit}.
     */
    @NotNull
    B toBukkit(@NotNull N n);
    
    /**
     * Converts this object into a {@code nms} object.
     *
     * @param b - The {@code bukkit} object.
     * @return this object as {@code nms}.
     */
    @NotNull
    N toNms(@NotNull B b);
}
