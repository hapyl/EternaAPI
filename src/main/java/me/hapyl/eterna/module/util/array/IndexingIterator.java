package me.hapyl.eterna.module.util.array;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents an {@link Iterator} that provides access to the current zero-based index of the element being iterated.
 *
 * @param <E> - The type of elements returned by this iterator.
 */
public interface IndexingIterator<E> extends Iterator<E> {
    
    /**
     * Gets whether the iterator has more elements.
     *
     * @return {@code true} if the iteration has more elements, {@code false} otherwise.
     */
    @Override
    boolean hasNext();
    
    /**
     * Gets the next {@link E} in the iterator.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException if the iteration has no more elements.
     */
    @Override
    E next();
    
    /**
     * Gets the zero-based index of the element that will be returned by the next call to {@link #next()}.
     *
     * <p>
     * Calling this method does <b>not</b> increment the index.
     * </p>
     *
     * @return current index position.
     */
    int index();
}
