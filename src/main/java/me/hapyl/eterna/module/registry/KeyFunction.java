package me.hapyl.eterna.module.registry;

import java.util.function.Function;

/**
 * A functional interface that extends {@link Function} to create elements of type {@code T}
 * based on a {@link Key}.
 *
 * @param <T> - The type of elements that this function produces.
 */
@FunctionalInterface
public interface KeyFunction<T> extends Function<Key, T> {
}
