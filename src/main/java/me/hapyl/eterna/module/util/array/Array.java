package me.hapyl.eterna.module.util.array;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Represents an un-resizable standard {@code array} array.
 *
 * <p>
 * The {@link Array} permits {@code null} elements.
 * </p>
 *
 * @param <E> - The array type.
 * @see #of(Object[])
 * @see #ofImmutable(Object[])
 */
public interface Array<E> extends IndexingIterable<E> {
    
    /**
     * Retrieves the element at the given index.
     *
     * @param index – The index of the element to return.
     * @return the element at the specified index, or {@code null} if none is present.
     */
    @Nullable
    E get(int index);
    
    /**
     * Sets the element at the specified index.
     *
     * @param index – The index of the element to set.
     * @param e     – The element to set at the specified index; may be {@code null}.
     */
    void set(int index, @Nullable E e);
    
    /**
     * Gets the length of this {@link Array}.
     *
     * @return the length of this array.
     */
    int length();
    
    /**
     * Gets whether this array is empty.
     *
     * <p>
     * This does not exclude {@code null} elements; meaning an array containing a {@code null} element will <b>not</b> be empty.
     * </p>
     *
     * @return {@code true} if this array is empty, {@code false} otherwise.
     */
    default boolean isEmpty() {
        return length() == 0;
    }
    
    /**
     * Gets whether this {@link Array} contains the given {@link E}.
     *
     * @param element - The element to check.
     * @return {@code true} if this array contains the given element, {@code false} otherwise.
     */
    default boolean contains(@NotNull E element) {
        if (isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < length(); i++) {
            if (element.equals(get(i))) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets an {@link IndexingIterable} for this array.
     *
     * @return an {@link IndexingIterable} for this array.
     */
    @NotNull
    @Override
    default IndexingIterator<E> iterator() {
        return new ArrayIterator<>(this);
    }
    
    /**
     * Gets an immutable {@link List} containing all the elements from this {@link Array}.
     *
     * @return immutable {@link List} containing all the elements from this {@link Array}.
     */
    @NotNull
    List<E> asList();
    
    /**
     * Gets a {@link Stream} of the elements in this {@link Array}.
     *
     * @return {@link Stream} of the elements in this {@link Array}.
     */
    @NotNull
    Stream<E> stream();
    
    /**
     * Gets a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @NotNull
    String toString();
    
    /**
     * Creates an empty {@link Array} of the given length.
     *
     * @param length - The length of the array.
     * @param <T>    - The element type.
     * @return A newly created {@link Array} instance of the given length.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    static <T> Array<T> of(int length) {
        return new ArrayImpl<>((T[]) new Object[length]);
    }
    
    /**
     * Creates an {@link Array} instance of the given generic array.
     * <p>A defensive copy of the generic array is created before instantiating a new instance; meaning modifying the underlying array will not affect the {@link Array}.</p>
     *
     * @param array - The underlying array.
     * @param <T>   - The element type.
     * @return A newly created {@link Array} instance.
     */
    @SafeVarargs
    @NotNull
    static <T> Array<T> of(@NotNull T... array) {
        return new ArrayImpl<>(Arrays.copyOf(array, array.length));
    }
    
    /**
     * Creates an immutable {@link Array} instance of the given generic array.
     * <p>A defensive copy of the generic array is created before instantiating a new instance; meaning modifying the underlying array will not affect the {@link Array}.</p>
     *
     * @param array - The underlying array.
     * @param <T>   - The element type.
     * @return A newly created {@link Array} instance.
     */
    @SafeVarargs
    @NotNull
    static <T> ImmutableArray<T> ofImmutable(@NotNull T... array) {
        return new ImmutableArray<>(Arrays.copyOf(array, array.length));
    }
    
    /**
     * Creates a {@link Collector} that collects stream elements into an {@link Array}
     * using the provided array supplier and factory function.
     *
     * <p>This collector accumulates elements into a temporary {@link List}, then
     * converts the list into an array using the given supplier, and finally applies
     * the provided function to produce the target {@link Array}.</p>
     *
     * @param arrayFactory - The function to create an array of the desired type and length.
     * @param arrayFn      - The function to construct an {@link Array} from the supplied array.
     * @param <T>          - The element type.
     * @param <A>          - The resulting {@link Array} subtype.
     */
    @NotNull
    static <T, A extends Array<T>> Collector<T, List<T>, A> collector(@NotNull IntFunction<T[]> arrayFactory, @NotNull Function<T[], A> arrayFn) {
        return Collector.of(
                Lists::newArrayList,
                List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                list -> arrayFn.apply(list.toArray(arrayFactory))
        );
    }
    
    /**
     * Represents an {@link IndexingIterator} with an underlying {@link Iterator} for the given {@link Array}.
     *
     * @param <E> - The element type.
     */
    final class ArrayIterator<E> implements IndexingIterator<E> {
        
        private final Array<E> array;
        private int index;
        
        ArrayIterator(@NotNull Array<E> array) {
            this.array = array;
            this.index = 0;
        }
        
        @Override
        public int index() {
            return index;
        }
        
        @Override
        public boolean hasNext() {
            return index < array.length();
        }
        
        @Override
        public E next() {
            return array.get(index++);
        }
    }
    
}
