package me.hapyl.eterna.builtin.manager;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EternaManager<K, V> {
    
    protected final EternaPlugin eterna;
    protected final Map<K, V> managing;
    
    EternaManager(@Nonnull EternaPlugin eterna) {
        this.eterna = eterna;
        this.managing = makeNewMap();
        
        // Register the listener if applicable
        if (this instanceof Listener listener) {
            Bukkit.getPluginManager().registerEvents(listener, eterna);
        }
    }
    
    public void register(@Nonnull K k, @Nonnull V v) {
        managing.put(k, v);
    }
    
    @Nullable
    public V unregister(@Nonnull K k) {
        return managing.remove(k);
    }
    
    public boolean isManaging(@Nonnull K k) {
        return managing.containsKey(k);
    }
    
    @Nullable
    public V get(@Nonnull K k) {
        return managing.get(k);
    }
    
    @Nonnull
    public V getOrThrow(@Nonnull K k) {
        return Objects.requireNonNull(get(k), "value == null");
    }
    
    public void forEach(@Nonnull BiConsumer<K, V> consumer) {
        managing.forEach(consumer);
    }
    
    public void forEach(@Nonnull Consumer<V> consumer) {
        managing.values().forEach(consumer);
    }
    
    @Nonnull
    protected Map<K, V> makeNewMap() {
        return Maps.newHashMap();
    }
    
}
