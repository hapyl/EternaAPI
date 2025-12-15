package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.SelfReturn;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a builder-like {@link List} maker.
 *
 * @param <T> - The element type.
 * @param <L> - The {@link List} type.
 */
public class ListMaker<T, L extends List<T>> implements Builder<L> {
    
    private final L list;
    
    ListMaker(@Nonnull L list) {
        this.list = list;
    }
    
    /**
     * Adds the given element to the {@link List}.
     *
     * @param t - The element to add.
     */
    @SelfReturn
    public ListMaker<T, L> add(@Nonnull T t) {
        this.list.add(t);
        return this;
    }
    
    /**
     * Remove the given element from the {@link List}.
     *
     * @param t - The element to remove.
     */
    @SelfReturn
    public ListMaker<T, L> remove(@Nonnull T t) {
        this.list.remove(t);
        return this;
    }
    
    /**
     * Puts the given element at the start of the {@link List}.
     *
     * <p>If the element is in the list, it will be moved to the start.</p>
     * <p>Behavior is undefined for multiple elements in the list.</p>
     *
     * @param t - The element to put at the start.
     */
    @SelfReturn
    public ListMaker<T, L> putFirst(@Nonnull T t) {
        this.list.remove(t);
        this.list.addFirst(t);
        return this;
    }
    
    /**
     * Puts the given element at the end of the {@link List}.
     *
     * <p>If the element is in the list, it will be moved to the end.</p>
     * <p>Behavior is undefined for multiple elements in the list.</p>
     *
     * @param t - The element to put at the end.
     */
    @SelfReturn
    public ListMaker<T, L> putLast(@Nonnull T t) {
        this.list.remove(t);
        this.list.addLast(t);
        return this;
    }
    
    /**
     * Adds the given elements to the {@link List}.
     *
     * @param t - The elements to add.
     */
    @SafeVarargs
    public final ListMaker<T, L> addAll(@Nonnull T... t) {
        list.addAll(List.of(t));
        return this;
    }
    
    /**
     * Adds the given collection to the {@link List}.
     *
     * @param t - The collection to add.
     */
    @SelfReturn
    public ListMaker<T, L> addAll(@Nonnull Collection<T> t) {
        list.addAll(t);
        return this;
    }
    
    /**
     * Builds the {@link List}.
     *
     * @return the build {@link List} containing the elements.
     */
    @Nonnull
    @Override
    public L build() {
        return list;
    }
    
    /**
     * Creates a new {@link ListMaker} using a {@link List} as underlying collection.
     *
     * @return a new {@link ListMaker}.
     */
    @Nonnull
    public static <T> ListMaker<T, List<T>> ofList() {
        return makeBuilder(Lists.newArrayList());
    }
    
    /**
     * Creates a new {@link ListMaker} using a {@link LinkedList} as underlying collection.
     *
     * @return a new {@link ListMaker}.
     */
    @Nonnull
    public static <T> ListMaker<T, LinkedList<T>> ofLinkedList() {
        return makeBuilder(Lists.newLinkedList());
    }
    
    protected static <T, L extends List<T>> ListMaker<T, L> makeBuilder(L c) {
        return new ListMaker<>(c);
    }
    
    
}
