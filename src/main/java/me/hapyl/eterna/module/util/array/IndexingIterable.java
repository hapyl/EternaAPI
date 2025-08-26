package me.hapyl.eterna.module.util.array;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * Represents a collection of elements that can be iterated with access to the zero-based index of each element.
 *
 * @param <E> - Type of elements returned by the iterator.
 */
public interface IndexingIterable<E> extends Iterable<E> {
    
    /**
     * Gets an {@link IndexingIterator} over the elements in this collection.
     *
     * @return iterator providing indexed access to elements.
     */
    @Nonnull
    @Override
    IndexingIterator<E> iterator();
    
    /**
     * Performs the given action for each element in this collection, supplying both the zero-based index and the element.
     *
     * @param consumer - The action to be performed for each index-element pair.
     */
    default void forEach(@Nonnull BiConsumer<Integer, E> consumer) {
        final IndexingIterator<E> iterator = iterator();
        
        while (iterator.hasNext()) {
            consumer.accept(iterator.index(), iterator.next());
        }
    }
    
}
