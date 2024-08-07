package me.hapyl.eterna.registry;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiConsumer;

public interface IRegistry<K, V> {

    /**
     * Gets the {@link JavaPlugin} that owns this {@link Registry}.
     *
     * @return the java plugin that owns this registry.
     */
    @Nonnull
    JavaPlugin getPlugin();

    /**
     * Registers <code>K</code> to a <code>V</code>.
     * <br>
     * The default behavior does <b>not</b> allow re-registering items.
     *
     * @param k - Key.
     * @param v - Value.
     */
    void register(@Nonnull K k, @Nullable V v);

    /**
     * Registers a <code>K</code> to a <code>null element</code>.
     * <br>
     * In case if registry does not care about <code>V</code>.
     *
     * @param k - Key.
     */
    default void register(@Nonnull K k) {
        register(k, null);
    }

    /**
     * Unregisters value from a key if value matches <code>V</code>.
     *
     * @param k - Key.
     * @param v - Value.
     * @return true if successfully unregistered, false otherwise.
     */
    boolean unregister(@Nonnull K k, @Nonnull V v);

    /**
     * Unregisters values from a key.
     *
     * @param k - Key.
     */
    boolean unregister(@Nonnull K k);

    /**
     * Gets a <b>copy</b> of the registry.
     *
     * @return a copy of the registry.
     */
    @Nonnull
    Map<K, V> getRegistryCopy();

    /**
     * Performs a for-each iterations on each key-value element in this registry.
     *
     * @param consumer - Consumer to apply to each element.
     */
    void forEach(@Nonnull BiConsumer<K, V> consumer);

    /**
     * Returns true if this key is registered; false otherwise.
     *
     * @param k - Key
     * @return true if this key is registered; false otherwise.
     */
    boolean isRegistered(@Nonnull K k);

    /**
     * Returns true if this key is registered to the given value; false otherwise.
     *
     * @param k - Key.
     * @param v - Value.
     * @return true if this key is registered to the given value; false otherwise.
     */
    boolean isRegistered(@Nonnull K k, @Nonnull V v);

    /**
     * Gets a <code>V</code> by the given <code>K</code> or null, if not registered.
     *
     * @param k - Key
     * @return the registered value or null.
     */
    @Nullable
    V byKey(@Nonnull K k);

    /**
     * Gets a <code>K</code> by the given <code>V</code> or null, if not registered.
     *
     * @param v - Value.
     * @return the registered key or null.
     */
    @Nullable
    K byValue(@Nonnull V v);

    /**
     * Gets the size of this registry.
     *
     * @return the size of this registry.
     */
    int size();
}
