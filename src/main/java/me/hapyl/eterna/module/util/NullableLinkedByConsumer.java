package me.hapyl.eterna.module.util;

import javax.annotation.Nullable;

/**
 * A functional interface for accepting entries with nullable values.
 */
@FunctionalInterface
public interface NullableLinkedByConsumer<K> {
    void accept(int index, @Nullable K k, @Nullable Integer integer);
}
