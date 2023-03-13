package me.hapyl.spigotutils.module.util;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Indicates that the class contains viewers of some type, usually players.
 *
 * @param <T> - The type of viewer.
 */
public class ContainsViewers<T> {

    private final Set<T> viewers;

    public ContainsViewers() {
        viewers = Sets.newHashSet();
    }

    public void addViewer(T t) {
        viewers.add(t);
    }

    public void removeViewer(T t) {
        viewers.remove(t);
    }

    public boolean isViewer(T t) {
        return viewers.contains(t);
    }

    public Set<T> getViewers() {
        return viewers;
    }

    public void forEachViewer(Consumer<T> t) {
        getViewers().forEach(t);
    }

}
