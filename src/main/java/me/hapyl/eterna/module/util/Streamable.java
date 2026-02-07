package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Represents an object that may return a {@link Stream}.
 *
 * @param <T> - The stream type.
 */
public interface Streamable<T> {
    
    /**
     * Gets a {@link Stream} of this object.
     *
     * @return the stream of this object.
     */
    @NotNull
    Stream<T> stream();
    
}
