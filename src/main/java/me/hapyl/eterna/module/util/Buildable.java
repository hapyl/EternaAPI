package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@code builder} implementation that may {@link #build()} a {@link T}.
 *
 * <p>
 * Implementations are usually named {@code Builder} and are an inner class, but are not required to follow the pattern.
 * </p>
 *
 * @param <T> - The type of the object to build.
 */
public interface Buildable<T> {
    
    /**
     * Builds the {@link T}.
     *
     * @return the built object.
     */
    @NotNull
    T build();
    
}
