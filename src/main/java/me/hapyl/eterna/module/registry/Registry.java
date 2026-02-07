package me.hapyl.eterna.module.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a {@link Registry} that maps {@link Key} to a {@link Keyed} object.
 *
 * @param <K> - The keyed object type.
 */
public interface Registry<K extends Keyed> extends Iterable<K> {
    
    /**
     * Retrieves the {@link K} object from this {@link Registry} wrapped in an {@link Optional}.
     *
     * <p>
     * This method returns {@link Optional#empty()} is there are no elements mapped by the given {@link Key}.
     * </p>
     *
     * @param key - The key retrieve the element at.
     * @return the keyed object wrapped in an optional.
     */
    @NotNull
    Optional<K> get(@NotNull Key key);
    
    /**
     * Retrieves the {@link K} object from this {@link Registry} wrapped in an {@link Optional}.
     *
     * <p>
     * This is a convenience method that converts the given {@link String} into a {@link Key} before retrieves an object.
     * If the given {@link String} isn't a valid {@link Key}, an {@link Optional#empty()} is returned.
     * </p>
     *
     * @param key - The string key to retrieve the element at.
     * @return the keyed object wrapped in an optional.
     */
    @NotNull
    default Optional<K> get(@NotNull String key) {
        final Key keyObject = Key.ofStringOrNull(key);
        
        return keyObject != null ? get(keyObject) : Optional.empty();
    }
    
    /**
     * Registers the given {@link K} in this {@link Registry}.
     *
     * <p>
     * Attempting to register a duplicate element will result in a {@link IllegalArgumentException}.
     * </p>
     *
     * @param k - The object to register.
     * @return the same object.
     */
    @NotNull
    K register(@NotNull K k);
    
    /**
     * Creates and registers an instance of {@link K} using the given {@link Key} in this {@link Registry}.
     *
     * <p>
     * Attempting to register a duplicate element will result in a {@link IllegalArgumentException}.
     * </p>
     *
     * @param key      - The string to create the key from.
     * @param function - The function how to create the {@link K}.
     * @param <E>      - The function key.
     * @return the registered element capture.
     * @throws IllegalArgumentException if the given string does not match the key pattern.
     */
    default <E extends K> E register(@NotNull String key, @NotNull KeyFunction<E> function) {
        final E e = function.apply(Key.ofString(key));
        
        this.register(e);
        return e;
    }
    
    /**
     * Unregisters the given {@link K} from this {@link Registry}.
     *
     * @param k - The element to unregister.
     * @return {@code true} if the element was unregistered; {@code false} otherwise.
     */
    boolean unregister(@NotNull K k);
    
    /**
     * Gets whether the given {@link Key} is registered in this {@link Registry}.
     *
     * @param key - The key to check.
     * @return {@code true} if the given key is registered in this registry; {@code false} otherwise.
     */
    default boolean isRegistered(@NotNull Key key) {
        return get(key).isPresent();
    }
    
    /**
     * Gets whether the given {@link K} is registered in this {@link Registry}.
     *
     * @param k - The object to check.
     * @return {@code true} if the given object is registered in this registry; {@code false} otherwise.
     */
    default boolean isRegistered(@NotNull K k) {
        return isRegistered(k.getKey());
    }
    
    /**
     * Gets whether this {@link Registry} is empty, meaning no elements are registered.
     *
     * @return {@code true} if this registry is empty; {@code false} otherwise.
     */
    boolean isEmpty();
    
    /**
     * Gets an <b>immutable</b> copy of {@link Key} objects in this {@link Registry}.
     *
     * @return an <b>immutable</b> copy of keys objects in this registry.
     */
    @NotNull
    List<Key> keys();
    
    /**
     * Gets an <b>immutable</b> copy of {@link K} objects in this {@link Registry}.
     *
     * @return an <b>immutable</b> copy of {@link K} objects in this registry.
     */
    @NotNull
    List<K> values();
    
    /**
     * Gets an <b>immutable</b> {@link List} of {@link Key} string values in this {@link Registry}.
     *
     * @return an <b>immutable</b> list of key string values in this registry.
     */
    @NotNull
    default List<String> keysAsString() {
        return values().stream().map(Keyed::getKeyAsString).collect(Collectors.toList());
    }
    
    /**
     * Gets an {@link Iterator} over the {@link K} objects in this {@link Registry}.
     *
     * <p>
     * The iterator is over an <b>immutable</b> copy of the elements, meaning it does <b>not</b> support removal operations.
     * </p>
     *
     * @return an iterator over the {@link K} objects in this registry.
     */
    @NotNull
    @Override
    Iterator<K> iterator();
}
