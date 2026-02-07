package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a handle, or a wrapper of something.
 *
 * @param <T> - The handle type.
 */
public interface Handle<T> {
    
    /**
     * Gets the handle.
     *
     * @return the handle.
     */
    @NotNull
    T getHandle();
    
}
