package me.hapyl.eterna.module.util.collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * A generic array class that supports operations on an array with a specified size.
 * <p>
 * This class provides basic methods to get and set elements at specific indices,
 * iterate over the array, and create instances with an initial set of elements.
 *
 * @param <T> the type of elements in the array.
 */
public final class GenericArrayWithExpectedSize<T> {

    public final int length;

    private final T[] array;

    @SuppressWarnings("unchecked")
    public GenericArrayWithExpectedSize(final int size) {
        this.array = (T[]) new Object[size];
        this.length = size;
    }

    /**
     * Retrieves the element at the specified index.
     *
     * @param index - The index of the element to retrieve.
     * @return the element at the specified index, or null if the index is out of bounds.
     */
    @Nullable
    public T get(final int index) {
        if (isIndexOutOfBound(index)) {
            return null;
        }

        return array[index];
    }

    /**
     * Sets the element at the specified index and returns the previous element at that index.
     *
     * @param index - The index of the element to set.
     * @param t     - The element to set, can be null.
     * @return the previous element at the specified index, or null if the index was out of bounds.
     */
    @Nullable
    public T set(final int index, @Nullable final T t) {
        if (isIndexOutOfBound(index)) {
            return null;
        }

        final T previous = array[index];
        array[index] = t;

        return previous;
    }

    /**
     * Returns the size of the array.
     *
     * @return the size of the array
     */
    public int size() {
        return length;
    }

    /**
     * Performs the given action for each element in the array.
     *
     * @param action - The action to be performed for each element.
     */
    public void forEach(@Nonnull Consumer<? super T> action) {
        for (T t : array) {
            action.accept(t);
        }
    }

    private boolean isIndexOutOfBound(int index) {
        return index < 0 || index >= array.length;
    }

    private GenericArrayWithExpectedSize<T> set(@Nonnull T[] elements) {
        if (length != elements.length) {
            throw new IllegalArgumentException("");
        }

        System.arraycopy(elements, 0, this.array, 0, elements.length);
        return this;
    }

    /**
     * Creates a new {@link GenericArrayWithExpectedSize} instance containing the specified elements.
     *
     * @param elements - The elements to initialize the array with.
     * @return a new GenericArrayWithExpectedSize instance containing the specified elements
     * @throws IllegalArgumentException if the length of the provided elements is zero
     */
    @SafeVarargs
    @Nonnull
    public static <T> GenericArrayWithExpectedSize<T> of(final @Nonnull T... elements) {
        final int length = elements.length;

        if (length == 0) {
            throw new IllegalArgumentException("illegal length: " + length);
        }

        return new GenericArrayWithExpectedSize<T>(length).set(elements);
    }
}