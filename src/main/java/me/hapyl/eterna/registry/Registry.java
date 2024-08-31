package me.hapyl.eterna.registry;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A generic registry interface that holds elements of type {@code T} that extend {@link Keyed}.
 * Allows for registering, retrieving, and unregistering elements by their {@link Key}.
 *
 * @param <T> - The type of elements stored in the registry, which must extend {@link Keyed}.
 */
public interface Registry<T extends Keyed> {

    /**
     * Gets the element by its {@link Key} or {@code null} if not registered.
     *
     * @param key - The {@link Key} of the element to retrieve.
     * @return the element associated with the key, or {@code null} if not registered.
     */
    @Nullable
    T get(@Nonnull Key key);

    /**
     * Gets the element by its {@link Key} derived from a string ID or returns {@code null} if not registered or the {@link String} is invalid.
     *
     * @param string - The string ID to convert to a {@link Key}.
     * @return the element associated with the key, or {@code null} if not registered.
     */
    @Nullable
    default T get(@Nonnull String string) {
        final Key key = Key.ofStringOrNull(string);

        return key != null ? get(key) : null;
    }

    /**
     * Gets an {@link Optional} containing the element associated with the given {@link Key}.
     *
     * @param key - The {@link Key} of the element to retrieve.
     * @return an {@link Optional} containing the element if found, or an empty {@link Optional}.
     */
    @Nonnull
    default Optional<T> getOptional(@Nonnull Key key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Registers the given item in the registry.
     *
     * @param t - The item to register.
     * @return the registered item.
     */
    T register(@Nonnull T t);

    /**
     * Registers an item using the given string ID as a key and a {@link KeyFunction} to create the item.
     * <br>
     * Usage example:
     * <pre>{@code
     * KeyedItem keyedItem = register("keyed_item", KeyedItem::new);
     *
     * // Considering the KeyedItem is structured as:
     * class KeyedItem implements Keyed {
     *
     *     private final Key key;
     *
     *     public KeyedItem(@Nonnull Key key) {
     *         this.key = key;
     *     }
     *
     *     @Nonnull
     *     @Override
     *     public final Key getKey() {
     *         return this.key;
     *     }
     *
     * }
     * }</pre>
     *
     * @param key - The string ID to convert to a {@link Key} and register the item under.
     * @param fn  - A {@link KeyFunction} to create the item to register.
     * @return the registered item.
     */
    @Nonnull
    default <E extends T> E register(@Nonnull String key, @Nonnull KeyFunction<E> fn) {
        final E t = fn.apply(Key.ofString(key));

        register(t);
        return t;
    }

    /**
     * Unregisters the given item from the registry. (optional operation)
     *
     * @param t - The item to unregister.
     * @return {@code true} if the item was successfully unregistered, {@code false} otherwise.
     */
    boolean unregister(@Nonnull T t);

    default boolean isRegistered(@Nonnull Key key) {
        return get(key) != null;
    }

    default boolean isRegistered(@Nonnull T t) {
        return isRegistered(t.getKey());
    }

    /**
     * Returns {@code true} if this {@link Registry} is empty, meaning nothing is registered, {@code false} otherwise.
     *
     * @return {@code true} if this {@link Registry} is empty, meaning nothing is registered, {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * Gets a copy of all registered elements.
     *
     * @return a list containing all registered elements.
     */
    @Nonnull
    List<T> values();

    /**
     * Retrieves a list of all registered {@link Key} objects.
     *
     * @return a list of all registered {@link Key} objects.
     */
    @Nonnull
    default List<Key> keys() {
        return values().stream().map(Keyed::getKey).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of string representations of all registered {@link Key}s.
     *
     * @return a list of string representations of all registered {@link Key}s.
     */
    @Nonnull
    default List<String> keysAsString() {
        return values().stream().map(Keyed::getKeyAsString).collect(Collectors.toList());
    }

}
