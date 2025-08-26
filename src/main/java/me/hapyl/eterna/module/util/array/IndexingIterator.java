package me.hapyl.eterna.module.util.array;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} that provides access to the current zero-based index of the element being iterated.
 *
 * @param <E> - Type of elements returned by this iterator.
 */
public interface IndexingIterator<E> extends Iterator<E> {
    /**
     * {@inheritDoc}
     *
     * @return {@code true} if the iteration has more elements, {@code false} otherwise.
     */
    @Override
    boolean hasNext();
    
    /**
     * {@inheritDoc}
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException if the iteration has no more elements.
     */
    @Override
    E next();
    
    /**
     * Gets the zero-based index of the element that will be returned by the next call to {@link #next()}.
     * <p>Calling this method will <b>not</b> increment the index.</p>
     *
     * @return current index position.
     */
    int index();
}
