package me.hapyl.eterna.module.util.array;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Represents a collection of elements that can be iterated with access to the zero-based index of each element.
 *
 * @param <E> - The type of elements returned by the iterator.
 */
public interface IndexingIterable<E> extends Iterable<E> {
    
    /**
     * Gets an {@link IndexingIterator} over the elements in this collection.
     *
     * @return iterator providing indexed access to elements.
     */
    @NotNull
    @Override
    IndexingIterator<E> iterator();
    
    /**
     * Performs the given action for each element in this collection.
     *
     * @param consumer - The action to be performed for each index-element pair.
     */
    default void forEach(@NotNull BiConsumer<Integer, E> consumer) {
        final IndexingIterator<E> iterator = iterator();
        
        while (iterator.hasNext()) {
            consumer.accept(iterator.index(), iterator.next());
        }
    }
    
}
