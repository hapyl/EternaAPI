package me.hapyl.eterna.builtin.manager;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiConsumer;

public class EternaManager<K, V> {

    protected final EternaPlugin eterna;
    protected final Map<K, V> managing;

    EternaManager(@Nonnull EternaPlugin eterna) {
        this.eterna = eterna;
        this.managing = Maps.newHashMap();
    }

    public void register(@Nonnull K k, @Nonnull V v) {
        managing.put(k, v);
    }

    public void unregister(@Nonnull K k) {
        managing.remove(k);
    }

    public boolean isManaging(@Nonnull K k) {
        return managing.containsKey(k);
    }

    @Nullable
    public V get(@Nonnull K k) {
        return managing.get(k);
    }

    public void forEach(@Nonnull BiConsumer<K, V> consumer) {
        managing.forEach(consumer);
    }

}
