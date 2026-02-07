package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a {@link Buffer}, which is a data structure that stores a specific number of elements.
 */
public abstract class Buffer<E> implements List<E> {
    
    private final int maxCapacity;
    private final LinkedList<E> linkedList;
    
    /**
     * Creates a new {@link Buffer} with the maximum capacity.
     *
     * @param maxCapacity - The maximum capacity.
     */
    public Buffer(final int maxCapacity) {
        this.maxCapacity = Math.max(1, maxCapacity);
        this.linkedList = new LinkedList<>();
    }
    
    /**
     * Retrieves, but does not remove the first (the oldest) {@link E}.
     *
     * @return the first (the oldest) element.
     */
    @Nullable
    public E peekFirst() {
        return linkedList.peekFirst();
    }
    
    /**
     * Retrieves and removes the first (the oldest) {@link E}.
     *
     * @return the first (the oldest) element.
     */
    @Nullable
    public E pollFirst() {
        return linkedList.pollFirst();
    }
    
    /**
     * Retrieves, but does not remove the last (the youngest) {@link E}.
     *
     * @return the last (the youngest) element.
     */
    @Nullable
    public E peekLast() {
        return linkedList.peekLast();
    }
    
    /**
     * Retrieves and removes the last (the youngest) {@link E}.
     *
     * @return the last (the youngest) element.
     */
    @Nullable
    public E pollLast() {
        return linkedList.pollLast();
    }
    
    /**
     * Gets the size of this {@link Buffer}.
     *
     * @return the size of this buffer.
     */
    @Override
    public int size() {
        return linkedList.size();
    }
    
    /**
     * Gets whether this {@link Buffer} is empty.
     *
     * @return {@code true} if this buffer is empty; {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return linkedList.isEmpty();
    }
    
    /**
     * Gets whether the given {@link Object} is in this {@link Buffer}.
     *
     * @param object - The element to check.
     * @return {@code true} if the given object is in this buffer; {@code false} otherwise.
     */
    @Override
    public boolean contains(Object object) {
        return linkedList.contains(object);
    }
    
    /**
     * Gets an {@link Iterator} for this {@link Buffer}.
     *
     * @return an iterator for this buffer.
     */
    @NotNull
    @Override
    public Iterator<E> iterator() {
        return linkedList.iterator();
    }
    
    /**
     * Converts this {@link Buffer} to an array.
     *
     * @return the array containing all the elements of this buffer.
     */
    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return linkedList.toArray();
    }
    
    /**
     * Converts this {@link Buffer} into an array of the given {@link T}.
     *
     * @param array - The array into which the elements of this list are to be stored, if it is big enough;
     *              otherwise, a new array of the same runtime type is allocated for this purpose.
     * @return an array containing the elements of this list
     */
    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] array) {
        return linkedList.toArray(array);
    }
    
    /**
     * Adds the given {@link E} to this {@link Buffer}.
     *
     * <p>
     * If addition of the element exceeds the maximum capacity of this {@link Buffer}, the first (the oldest)
     * element is removed and {@link #unbuffered(Object)} is called with that element.
     * </p>
     */
    @Override
    public final boolean add(@NotNull E e) {
        if (linkedList.size() + 1 > maxCapacity) {
            final E last = linkedList.pollFirst();
            
            if (last != null) {
                unbuffered(last);
            }
        }
        
        linkedList.addLast(e);
        return true;
    }
    
    /**
     * Removes the given {@link E} form this {@link Buffer}.
     *
     * @param o - The element to remove.
     * @return {@code true} if the element was removed; {@code false} otherwise.
     */
    @Override
    public boolean remove(Object o) {
        return linkedList.remove(o);
    }
    
    /**
     * Gets whether this {@link Buffer} contains all the elements from the given {@link Collection}.
     *
     * @param c - The collection to check.
     * @return {@code true} if this buffer contains all the elements from the given collection; {@code false} otherwise.
     */
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return linkedList.containsAll(c);
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public boolean addAll(@NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("addAll()");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("addAll()");
    }
    
    /**
     * Removes all the elements from this {@link Buffer} that are also contained in the given {@link Collection}.
     *
     * @param c - The collection of which elements to use.
     * @return {@code true} if at least one element was removed; {@code false} otherwise.
     */
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return linkedList.removeAll(c);
    }
    
    /**
     * Retains all the elements in {@link Buffer} that are also contained in the given {@link Collection}
     *
     * @param c - The collection of which elements to use.
     * @return {@code true} if at least one element was retained; {@code false} otherwise.
     */
    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return linkedList.retainAll(c);
    }
    
    /**
     * Removes all the elements from this {@link Buffer}.
     */
    @Override
    public void clear() {
        linkedList.clear();
    }
    
    /**
     * Retrieves an {@link E} at the given index.
     *
     * @param index - The index of the element.
     * @return the retrieves element.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    @Override
    public E get(int index) {
        return linkedList.get(index);
    }
    
    /**
     * Sets the given {@link E} to the given index.
     *
     * @param index   - The index of the element.
     * @param element - The element to set.
     * @return the previous element at that index.
     * @throws IndexOutOfBoundsException – if the index is out of range (index < 0 || index >= size())
     */
    @Override
    public E set(int index, E element) {
        return linkedList.set(index, element);
    }
    
    /**
     * Adds the given {@link E} to this {@link Buffer}.
     *
     * @param element - The element to add.
     * @deprecated Use {@link Buffer#add(Object)}
     */
    @Override
    @Deprecated
    public void add(int index, E element) {
        add(element);
    }
    
    /**
     * Removes an {@link E} at the given index.
     *
     * @param index - The index of the element to be removed.
     * @return the element that was removed.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    @Override
    public E remove(int index) {
        return linkedList.remove(index);
    }
    
    /**
     * Gets the first index of the given {@link E}.
     *
     * @param o - The element to retrieve the index for.
     * @return the first index of the given element, or {@code -1} if not in this buffer.
     */
    @Override
    public int indexOf(Object o) {
        return linkedList.indexOf(o);
    }
    
    /**
     * Returns the last index of the given {@link E}.
     *
     * @param o - The element to retrieve the index for.
     * @return the last index of given element, or {@code -1} if not in this buffer.
     */
    @Override
    public int lastIndexOf(Object o) {
        return linkedList.lastIndexOf(o);
    }
    
    /**
     * Gets a {@link ListIterator} for this {@link Buffer}.
     *
     * @return a list iterator.
     */
    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return linkedList.listIterator();
    }
    
    /**
     * Gets a {@link ListIterator} starting at the given index for this {@link Buffer}.
     *
     * @param index - The index to start at.
     * @return a list iterator.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return linkedList.listIterator(index);
    }
    
    /**
     * Gets a view of the portion of this list between the given {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     *
     * @param fromIndex - The low endpoint (inclusive) of the sublist.
     * @param toIndex   - The high endpoint (exclusive) of the sublist.
     * @return a sublist from the given {@code fromIndex} - {@code toIndex}.
     * @throws IndexOutOfBoundsException if an endpoint index value is out of bounds.
     * @throws IllegalArgumentException  if the endpoint indices are out of order.
     */
    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return linkedList.subList(fromIndex, toIndex);
    }
    
    /**
     * A callback method that is called whenever a {@link E} is removed from this {@link Buffer}
     * due to the {@link Buffer} overflow.
     *
     * @param e - The element that was unbuffered.
     */
    public abstract void unbuffered(@NotNull E e);
    
    /**
     * Retrieves an {@link E} wrapped in an {@link Optional} at the given index.
     *
     * @param index - The to retrieve the element at.
     * @return the element at the given index wrapped in an optional.
     */
    @NotNull
    public Optional<E> getOptional(int index) {
        return index < 0 || index >= linkedList.size() ? Optional.empty() : Optional.ofNullable(get(index));
    }
    
    /**
     * Gets the {@link String} representation of this {@link Buffer}.
     */
    @Override
    public String toString() {
        return "Buffer{" +
               "maxCapacity=" + maxCapacity +
               ", buffer=" + linkedList +
               '}';
    }
    
}
