package me.hapyl.spigotutils.module.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CollectionBuilder<T, C extends Collection<T>> implements Builder<C> {

    private final C collection;

    protected CollectionBuilder(C collection) {
        this.collection = collection;
    }

    public CollectionBuilder<T, C> add(@Nonnull T t) {
        collection.add(t);
        return this;
    }

    public CollectionBuilder<T, C> putLast(@Nonnull T t) {
        collection.remove(t);
        collection.add(t);
        return this;
    }

    @SafeVarargs
    public final CollectionBuilder<T, C> addAll(@Nonnull T... t) {
        collection.addAll(List.of(t));
        return this;
    }

    public CollectionBuilder<T, C> addAll(@Nonnull Collection<T> t) {
        collection.addAll(t);
        return this;
    }

    @Nonnull
    @Override
    public C build() {
        return collection;
    }

    @Nonnull
    public static <T> CollectionBuilder<T, List<T>> ofList() {
        return makeBuilder(Lists.newArrayList());
    }

    @Nonnull
    public static <T> CollectionBuilder<T, LinkedList<T>> ofLinkedList() {
        return makeBuilder(Lists.newLinkedList());
    }

    @Nonnull
    public static <T> CollectionBuilder<T, Set<T>> ofSet() {
        return makeBuilder(Sets.newHashSet());
    }

    protected static <T, C extends Collection<T>> CollectionBuilder<T, C> makeBuilder(C c) {
        return new CollectionBuilder<>(c);
    }


}
