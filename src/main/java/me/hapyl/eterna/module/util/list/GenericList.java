package me.hapyl.eterna.module.util.list;

import me.hapyl.eterna.module.annotate.SelfReturn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents a generic list implementation, with builder-like {@link #append(Object)} methods.
 * <p>It mainly exists for builder-like methods and {@link IndexOutOfBoundsException} safety.</p>
 * <p>The implementing lists should generally have the following factory methods:</p>
 * <ul>
 *     <li><pre>{@code
 *     // Factory method for instantiation.
 *     public static GenericList of(E...)
 *     }</pre>
 *     <li><pre>{@code
 *     // Factory method for instantiation of an empty instance.
 *     public static GenericList empty()
 *     }</pre>
 *     <li><pre>{@code
 *     // [Optional] Factory method for Stream collector.
 *     public static Collector colletor()
 *     }</pre>
 * </ul>
 *
 * @param <E> - The list type.
 */
public interface GenericList<E> extends Iterable<E> {
    
    /**
     * Appends the given element to this list.
     *
     * @param e - The element to append.
     */
    @SelfReturn
    GenericList<E> append(@Nonnull E e);
    
    /**
     * Appends the given list to this list.
     *
     * @param other - The list to append.
     */
    @SelfReturn
    GenericList<E> append(@Nonnull GenericList<E> other);
    
    /**
     * Retrieves the element at the given index.
     * <p>If the index is out of bounds, {@code null} is returned.</p>
     *
     * @param index - The index to retrieve the element at.
     * @return the retrieved element, or {@code null} if index is out of bounds.
     */
    @Nullable
    E get(int index);
    
    /**
     * Gets the size of this list.
     *
     * @return the size of this list.
     */
    int size();
    
    /**
     * Gets an immutable copy of this list.
     *
     * @return an immutable copy of this list.
     */
    @Nonnull
    List<E> toList();
    
    /**
     * Gets a mutable defensive copy of this list.
     *
     * @return a mutable defensive copy of this list.
     */
    @Nonnull
    E[] toArray();
    
    /**
     * Gets the {@link Iterator} of this list.
     *
     * @return the {@link Iterator} of this list.
     */
    @Nonnull
    @Override
    Iterator<E> iterator();
    
    /**
     * Gets the {@link Stream} of this list.
     *
     * @return the {@link Stream} of this list.
     */
    @Nonnull
    Stream<E> stream();
    
    /**
     * A factory method to instantiate a {@link GenericList}.
     *
     * @param supplier - The instance supplier.
     * @param values   - The values to instantiate with.
     * @return a new {@link GenericList}.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    static <E, L extends GenericList<E>> L of(@Nonnull Function<E[], L> supplier, @Nonnull E... values) {
        return supplier.apply(Arrays.copyOf(values, values.length));
    }
    
}
