package me.hapyl.eterna.module.registry;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Validate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.*;

/**
 * Represents a simple {@link Registry} implementation with an underlying {@link LinkedHashMap}.
 *
 * @param <K> - The keyed object type.
 */
public class SimpleRegistry<K extends Keyed> implements Registry<K>, Iterable<K> {
    
    protected final Map<Key, K> registered;
    
    /**
     * Creates a new {@link SimpleRegistry}.
     */
    public SimpleRegistry() {
        this.registered = Maps.newLinkedHashMap(); // Actually, keep the order.
    }
    
    
    @Override
    @NotNull
    public Optional<K> get(@NotNull Key key) {
        return Optional.ofNullable(registered.get(key));
    }
    
    @Override
    @NotNull
    @OverridingMethodsMustInvokeSuper
    public K register(@NotNull K k) {
        final Key key = k.getKey();
        Validate.isTrue(!registered.containsKey(key), "Duplicate registration of '%s'!".formatted(key));
        
        registered.put(key, k);
        return k;
    }
    
    @Override
    public boolean unregister(@NotNull K k) {
        return registered.remove(k.getKey()) != null;
    }
    
    @Override
    public boolean isRegistered(@NotNull Key key) {
        return registered.containsKey(key);
    }
    
    @Override
    public boolean isRegistered(@NotNull K k) {
        return registered.containsValue(k);
    }
    
    @Override
    public boolean isEmpty() {
        return registered.isEmpty();
    }
    
    @NotNull
    @Override
    public List<Key> keys() {
        return List.copyOf(registered.keySet());
    }
    
    @NotNull
    @Override
    public List<K> values() {
        return List.copyOf(registered.values());
    }
    
    @NotNull
    @Override
    public Iterator<K> iterator() {
        // Get iterator through immutable list copy
        return values().iterator();
    }
}
