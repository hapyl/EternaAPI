package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.SelfReturn;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a builder-like {@link List} maker.
 *
 * @param <E> - The element type.
 * @param <L> - The list type.
 */
public final class ListMaker<E, L extends List<E>> {
    
    private final L list;
    
    private ListMaker(@NotNull L list) {
        this.list = list;
    }
    
    /**
     * Adds the given {@link E} element to the {@link List}.
     *
     * @param e - The element to add.
     */
    @SelfReturn
    public ListMaker<E, L> add(@NotNull E e) {
        this.list.add(e);
        return this;
    }
    
    /**
     * Remove the given {@link E} element from the {@link List}.
     *
     * @param e - The element to remove.
     */
    @SelfReturn
    public ListMaker<E, L> remove(@NotNull E e) {
        this.list.remove(e);
        return this;
    }
    
    /**
     * Puts the given element at the start of the {@link List}.
     *
     * <p>
     * If the element is in the list, it will be moved to the start.
     * </p>
     *
     * @param e - The element to put at the start.
     */
    @SelfReturn
    public ListMaker<E, L> putFirst(@NotNull E e) {
        this.list.remove(e);
        this.list.addFirst(e);
        return this;
    }
    
    /**
     * Puts the given element at the end of the {@link List}.
     *
     * <p>If the element is in the list, it will be moved to the end.</p>
     *
     * @param e - The element to put at the end.
     */
    @SelfReturn
    public ListMaker<E, L> putLast(@NotNull E e) {
        this.list.remove(e);
        this.list.addLast(e);
        return this;
    }
    
    /**
     * Adds the given {@link E} elements to the {@link List}.
     *
     * @param e - The elements to add.
     */
    @SafeVarargs
    public final ListMaker<E, L> addAll(@NotNull E... e) {
        list.addAll(List.of(e));
        return this;
    }
    
    /**
     * Adds the given {@link Collection} to the {@link List}.
     *
     * @param e - The collection to add.
     */
    @SelfReturn
    public ListMaker<E, L> addAll(@NotNull Collection<E> e) {
        list.addAll(e);
        return this;
    }
    
    /**
     * Makes the {@link List}.
     *
     * @return the list containing the elements.
     */
    @NotNull
    public L makeList() {
        return list;
    }
    
    /**
     * Makes an immutable copy of the underlying {@link List}.
     *
     * @return an immutable copy of the underlying list.
     */
    @NotNull
    public List<E> makeImmutableList() {
        return List.copyOf(list);
    }
    
    /**
     * A static factory method for creating {@link ListMaker} with an underlying {@link List}.
     *
     * @return a new list maker.
     */
    @NotNull
    public static <T> ListMaker<T, List<T>> ofList() {
        return makeBuilder(Lists.newArrayList());
    }
    
    /**
     * A static factory method for creating {@link ListMaker} with an underlying {@link LinkedList}.
     *
     * @return a new list maker.
     */
    @NotNull
    public static <T> ListMaker<T, LinkedList<T>> ofLinkedList() {
        return makeBuilder(Lists.newLinkedList());
    }
    
    @ApiStatus.Internal
    private static <T, L extends List<T>> ListMaker<T, L> makeBuilder(@NotNull L c) {
        return new ListMaker<>(c);
    }
    
    
}
