package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * A buffer that can store up to a specified number of elements before removing the oldest one.
 */
public abstract class Buffer<E> implements List<E> {

    private final int maxCapacity;
    private final LinkedList<E> linkedList;

    /**
     * Creates a new {@link Buffer} with a maximum capacity.
     *
     * @param maxCapacity - Maximum capacity.
     */
    public Buffer(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.linkedList = new LinkedList<>();
    }

    /**
     * Gets the first (oldest) element.
     *
     * @return the first (oldest) element.
     */
    @Nullable
    public E peekFirst() {
        return linkedList.peekFirst();
    }

    /**
     * Gets and removes the first (oldest) element.
     *
     * @return the first (oldest) element.
     */
    @Nullable
    public E pollFirst() {
        return linkedList.pollFirst();
    }

    /**
     * Gets the last (youngest) element.
     *
     * @return the last (youngest) element.
     */
    @Nullable
    public E peekLast() {
        return linkedList.peekLast();
    }

    /**
     * Gets and removes the last (youngest) element.
     *
     * @return the last (youngest) element.
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
     * Returns true if this {@link Buffer} is empty.
     *
     * @return true if this buffer is empty.
     */
    @Override
    public boolean isEmpty() {
        return linkedList.isEmpty();
    }

    /**
     * Returns true if the given element is in the {@link Buffer}, false otherwise.
     *
     * @param o - Element.
     * @return true if the given element is in the {@link Buffer}, false otherwise.
     */
    @Override
    public boolean contains(Object o) {
        return linkedList.contains(o);
    }

    /**
     * Gets an {@link Iterator} for this {@link Buffer}.
     *
     * @return an iterator.
     */
    @Nonnull
    @Override
    public Iterator<E> iterator() {
        return linkedList.iterator();
    }

    /**
     * Converts this buffer to an array.
     *
     * @return array.
     */
    @Nonnull
    @Override
    public Object[] toArray() {
        return linkedList.toArray();
    }

    /**
     * {@inheritDoc}
     *
     * @param a - the array into which the elements of this list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of this list
     */
    @Nonnull
    @Override
    public <T> T[] toArray(@Nonnull T[] a) {
        return linkedList.toArray(a);
    }

    /**
     * Adds an element to this {@link Buffer}.
     * <br>
     * If adding of this element exceeds the maximum capacity of this {@link Buffer},
     * the first (oldest) element will be unlinked and call {@link #unbuffered(Object)} with.
     */
    @Override
    public final boolean add(@Nonnull E e) {
        if (size() + 1 > maxCapacity) {
            final E last = linkedList.pollFirst();
            if (last != null) {
                unbuffered(last);
            }
        }

        linkedList.addLast(e);
        return true;
    }

    /**
     * Called whenever the first (oldest) element is removed due to buffer size.
     *
     * @param e - Element that was removed.
     */
    public abstract void unbuffered(@Nonnull E e);

    /**
     * Removes the given element form this {@link Buffer}.
     *
     * @param o - Element to remove.
     * @return true if the element was removed, false otherwise.
     */
    @Override
    public boolean remove(Object o) {
        return linkedList.remove(o);
    }

    /**
     * Returns true if this {@link Buffer} contains all the elements from the given {@link Collection}.
     *
     * @param c - Collection.
     * @return true if this buffer contains all the elements from the given collection.
     */
    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return linkedList.containsAll(c);
    }

    /**
     * Adds all the elements from the given {@link Collection} to this {@link Buffer}.
     *
     * @param c - Collection.
     * @deprecated Adding an unknown amount of elements goes against the design of the buffer.
     */
    @Override
    @Deprecated
    public boolean addAll(@Nonnull Collection<? extends E> c) {
        return addAll(0, c);
    }

    /**
     * Adds all the elements from the given {@link Collection} to this {@link Buffer}.
     *
     * @param c - Collection.
     * @deprecated Adding an unknown amount of elements goes against the design of the buffer.
     */
    @Override
    @Deprecated
    public boolean addAll(int index, @Nonnull Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }

        return true;
    }

    /**
     * Removes all the elements from this {@link Buffer} that are contained in both this {@link Buffer} and the given {@link Collection}.
     *
     * @param c - Collection.
     * @return true if at least one element was removed.
     */
    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        return linkedList.removeAll(c);
    }

    /**
     * Retains only the elements that are contained in both this {@link Buffer} and the given {@link Collection}.
     *
     * @param c - Collection.
     * @return true if at least one element was retained.
     */
    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return linkedList.retainAll(c);
    }

    /**
     * Clears this {@link Buffer}.
     */
    @Override
    public void clear() {
        linkedList.clear();
    }

    /**
     * Gets an element by the given index.
     *
     * @param index - Index of the element to return
     * @return the element.
     * @throws IndexOutOfBoundsException – if the index is out of range (index < 0 || index >= size())
     */
    @Override
    public E get(int index) {
        return linkedList.get(index);
    }

    /**
     * Gets an element by the given index, or null if index is out of bounds.
     *
     * @param index - Index.
     * @return the element or null.
     */
    @Nullable
    public E getOrNull(int index) {
        return getOrDefault(index, null);
    }

    /**
     * Gets an element by the given index, or <code>def</code> if index is out of bounds.
     *
     * @param index - Index.
     * @param def   - Default element.
     * @return the element or default.
     */
    public E getOrDefault(int index, E def) {
        return index < 0 || index >= linkedList.size() ? def : get(index);
    }

    /**
     * Sets the element to the given index.
     *
     * @param index   - Index of the element to replace
     * @param element - Element.
     * @return previous element.
     * @throws IndexOutOfBoundsException – if the index is out of range (index < 0 || index >= size())
     */
    @Override
    public E set(int index, E element) {
        return linkedList.set(index, element);
    }

    /**
     * Adds an element to this {@link Buffer}.
     *
     * @param element - Element.
     * @see Buffer#add(Object).
     */
    @Override
    @Deprecated
    public void add(int index, E element) {
        add(element);
    }

    /**
     * Removes an element at the given index.
     *
     * @param index - The index of the element to be removed.
     * @return the element that was removed.
     * @throws IndexOutOfBoundsException – if the index is out of range (index < 0 || index >= size())
     */
    @Override
    public E remove(int index) {
        return linkedList.remove(index);
    }

    /**
     * Returns the index of the given element.
     *
     * @param o - Element.
     * @return the index or -1 if not in this buffer.
     */
    @Override
    public int indexOf(Object o) {
        return linkedList.indexOf(o);
    }

    /**
     * Returns the last index of the given element.
     *
     * @param o - Element.
     * @return the last index or -1 if not in this buffer.
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
    @Nonnull
    @Override
    public ListIterator<E> listIterator() {
        return linkedList.listIterator();
    }

    /**
     * Gets a {@link ListIterator} starting at the given index. for this {@link Buffer}.
     *
     * @param index - Index.
     * @return a list iterator.
     * @throws IndexOutOfBoundsException – if the index is out of range (index < 0 || index >= size())
     */
    @Nonnull
    @Override
    public ListIterator<E> listIterator(int index) {
        return linkedList.listIterator(index);
    }

    /**
     * {@inheritDoc}
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     * @return
     * @throws IndexOutOfBoundsException – if an endpoint index value is out of range (fromIndex < 0 || toIndex > size)
     * @throws IllegalArgumentException  – if the endpoint indices are out of order (fromIndex > toIndex)
     */
    @Nonnull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return linkedList.subList(fromIndex, toIndex);
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

    /**
     * Compares the given element with element at the given index from this {@link Buffer}.
     *
     * @param index   - Index.
     * @param element - Element.
     * @return true if the given element and an element at the given index in this buffer matches, false otherwise.
     */
    public boolean compare(int index, @Nullable E element) {
        if (index < 0 || index > size()) {
            return false;
        }

        final E e = getOrNull(index);
        return e != null && e.equals(element);
    }

    /**
     * Compares all the elements from this {@link Buffer} with the given array.
     * <br>
     * This only compares elements up to the size of the given array.
     *
     * @param values - Array.
     * @return true if all elements are present in this buffer in the exact order.
     */
    public boolean compareAll(@Nonnull E[] values) {
        if (isEmpty() || values.length > size()) {
            return false;
        }

        for (int i = 0; i < values.length; i++) {
            final E value = values[i];
            final E e = getOrNull(i);

            if (e != null && e != value) {
                return false;
            }
        }

        return true;
    }

}
