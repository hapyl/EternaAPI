package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Represents a {@link Stream} supplier.
 *
 * @param <T> - The stream type.
 */
public interface Streamer<T> {
    
    /**
     * Gets a new {@link Stream}.
     *
     * @return a new {@link Stream}.
     */
    @Nonnull
    Stream<T> stream();
    
}
