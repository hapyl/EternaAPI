package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;

/**
 * A functional interface for accepting entries with non-null values.
 */
public interface LinkedBiConsumer<K> {
    void accept(int index, @Nonnull K k, @Nonnull Integer integer);
}
