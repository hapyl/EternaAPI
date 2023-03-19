package me.hapyl.spigotutils.registry;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import org.bukkit.util.Consumer;

import java.util.Map;
import java.util.Set;

public class KeyToConcurrentSetRegistry<K, V> extends Registry<K, V> {

    private final Map<K, Set<V>> registry;

    public KeyToConcurrentSetRegistry(EternaPlugin plugin) {
        super(plugin);
        this.registry = Maps.newConcurrentMap();
    }

    public Set<V> getSet(K k) {
        return this.registry.getOrDefault(k, Sets.newConcurrentHashSet());
    }

    private void workSet(K k, Consumer<Set<V>> consumer) {
        final Set<V> set = getSet(k);

        consumer.accept(set);
        registry.put(k, set);
    }

    @Override
    public void register(K k, V v) {
        workSet(k, set -> set.add(v));
    }

    @Override
    public void register(K k) {
        register(k, null);
    }

    @Override
    public void unregister(K k, V v) {
        workSet(k, set -> set.remove(v));
    }

    @Override
    public void unregister(K k) {
        registry.remove(k);
    }
}
