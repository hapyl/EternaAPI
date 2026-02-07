package me.hapyl.eterna.module.registry;

import java.util.function.Function;

/**
 * A functional interface that extends {@link Function} to create elements of type {@link K}
 * based on a {@link Key}.
 *
 * @param <K> - The type of elements that this function produces.
 * @see Registry#register(String, KeyFunction)
 */
@FunctionalInterface
public interface KeyFunction<K> extends Function<Key, K> {
}
