package me.hapyl.eterna.module.util.array;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * A generic wrapper around a standard Java array.
 * <p>Differs from {@link List} in a way being stricter and unresizable.</p>
 *
 * @param <T> - The array type.
 * @see #of(Object[])
 * @see #ofImmutable(Object[])
 */
public interface Array<T> extends Iterable<T> {
    
    /**
     * Retrieves the element at the given index.
     *
     * @param index – The index of the element to return.
     * @return The element at the specified index, or {@code null} if none is present.
     * @implNote Implementations may perform explicit bounds checking or throw a {@link ArrayIndexOutOfBoundsException} if the index is out of range.
     */
    @Nullable
    T get(int index);
    
    /**
     * Sets the element at the specified index.
     *
     * @param index – The index of the element to set.
     * @param t     – The element to set at the specified index; may be {@code null}.
     * @implNote Implementations may perform explicit bounds checking or throw a {@link ArrayIndexOutOfBoundsException} if the index is out of range.
     */
    void set(int index, @Nullable T t);
    
    /**
     * Gets the length of this array.
     *
     * @return the length of this array.
     */
    int length();
    
    /**
     * Gets whether this array is empty.
     * <p>This does not exclude {@code null} elements; meaning an array containing a {@code null} element will <b>not</b> be empty.</p>
     *
     * @return {@code true} if this array is empty, {@code false} otherwise.
     */
    default boolean isEmpty() {
        return length() == 0;
    }
    
    /**
     * Gets whether this array contains the given element.
     *
     * @param element - The element to check.
     * @return {@code true} if this array contains the given element, {@code false} otherwise.
     */
    default boolean contains(@Nonnull T element) {
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
     * Gets an iterator for this array.
     *
     * @return an iterator for this array.
     */
    @Nonnull
    @Override
    default Iterator<T> iterator() {
        return new ArrayIterator<>(this);
    }
    
    /**
     * Gets an immutable {@link List} containing all the elements from this {@link Array}.
     *
     * @return immutable {@link List} containing all the elements from this {@link Array}.
     */
    @Nonnull
    List<T> asList();
    
    /**
     * Gets a {@link Stream} of the elements in this {@link Array}.
     *
     * @return {@link Stream} of the elements in this {@link Array}.
     */
    @Nonnull
    Stream<T> stream();
    
    /**
     * Gets a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Nonnull
    String toString();
    
    /**
     * Creates an empty {@link Array} of the given length.
     *
     * @param length - The length of the array.
     * @param <T>    - The element type.
     * @return A newly created {@link Array} instance of the given length.
     */
    @Nonnull
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
    @Nonnull
    static <T> Array<T> of(@Nonnull T... array) {
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
    @Nonnull
    static <T> ImmutableArray<T> ofImmutable(@Nonnull T... array) {
        return new ImmutableArray<>(Arrays.copyOf(array, array.length));
    }
    
    /**
     * Represents an {@link Iterator} for the given {@link Array}.
     *
     * @param <T> - The element type.
     */
    final class ArrayIterator<T> implements Iterator<T> {
        
        private final Array<T> array;
        private int index;
        
        ArrayIterator(@Nonnull Array<T> array) {
            this.array = array;
            this.index = 0;
        }
        
        @Override
        public boolean hasNext() {
            return index < array.length();
        }
        
        @Override
        public T next() {
            return array.get(index++);
        }
    }
}
