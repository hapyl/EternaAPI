package me.hapyl.eterna.registry;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Validate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple implementation of the {@link Registry} interface that maintains the order of registration.
 * This registry stores elements of type {@code T} in a {@link LinkedHashMap} to preserve insertion order.
 *
 * @param <T> - The type of elements stored in the registry, which must extend {@link Keyed}.
 */
public class SimpleRegistry<T extends Keyed> implements Registry<T> {

    /**
     * The underlying map that holds the registered elements, keyed by their {@link Key}.
     */
    protected final Map<Key, T> registered;

    /**
     * Constructs a new empty {@link SimpleRegistry}.
     */
    public SimpleRegistry() {
        this.registered = Maps.newLinkedHashMap(); // Actually, keep the order.
    }

    @Nullable
    @Override
    public T get(@Nonnull Key key) {
        return registered.get(key);
    }

    /**
     * Registers the specified element in the registry.
     * Ensures that duplicate registrations are not allowed.
     *
     * @param t - The element to register.
     * @return the registered element.
     * @throws IllegalArgumentException if the key is already registered.
     */
    @Override
    @OverridingMethodsMustInvokeSuper
    public T register(@Nonnull T t) {
        final Key key = t.getKey();
        Validate.isTrue(!registered.containsKey(key), "Duplicate registration of '%s'!".formatted(key));

        registered.put(key, t);
        return t;
    }

    @Override
    public boolean unregister(@Nonnull T t) {
        return registered.remove(t.getKey()) != null;
    }

    @Override
    public boolean isRegistered(@NotNull Key key) {
        return registered.containsKey(key);
    }

    @Override
    public boolean isRegistered(@NotNull T t) {
        return registered.containsValue(t);
    }

    @Override
    public boolean isEmpty() {
        return registered.isEmpty();
    }

    @Nonnull
    @Override
    public List<T> values() {
        return new ArrayList<>(registered.values());
    }

}
