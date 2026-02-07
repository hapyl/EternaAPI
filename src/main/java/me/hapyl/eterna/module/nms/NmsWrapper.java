package me.hapyl.eterna.module.nms;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a wrapper for an {@code object} that may be treated as an {@code nms} object.
 *
 * @param <N> - The {@code nms} type.
 */
public interface NmsWrapper<N> {
    
    /**
     * Gets the {@code nms} object.
     *
     * @return the {@code nms} object.
     */
    @NotNull
    N toNms();
    
}
