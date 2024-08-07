package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.eterna.module.annotate.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * A weighted collection.
 */
public class WeightedCollection<E> implements Iterable<E> {

    private final TreeMap<Integer, List<E>> elements;
    private final Random random;

    public WeightedCollection() {
        this.elements = new TreeMap<>();
        this.random = new Random();
    }

    /**
     * Add an element to the collection.
     *
     * @param e      - Element to add.
     * @param weight - Weight of the element.
     *               The probability of selecting an element is proportional to its weight.
     * @throws IllegalArgumentException if weight is negative
     */
    public void add(@Nonnull E e, @Range int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("weight cannot be negative");
        }

        elements.computeIfAbsent(weight, fn -> Lists.newArrayList()).add(e);
    }

    /**
     * Adds all elements to the collection.
     *
     * @param weight - Weight of the element.
     *               The probability of selecting an element is proportional to its weight.
     * @param first  - First element to add.
     * @param rest   - Rest of the elements
     */
    @SafeVarargs
    public final void addAll(@Range int weight, @Nonnull E first, @Nonnull E... rest) {
        add(first, weight);

        for (E e : rest) {
            add(e, weight);
        }
    }

    /**
     * Gets a random element from this collection based on the weight.
     *
     * @return a random element from this collection based on the weight.
     * @throws NullPointerException if the collection is empty.
     */
    @Nonnull
    public E get() {
        final E e = getOrNull();

        if (e != null) {
            return e;
        }

        throw new NullPointerException("collection is empty");
    }

    /**
     * Gets a random element from this collection based on the weight; or def if the collection is empty.
     *
     * @param def - Default element.
     * @return a random element from this collection based on the weight; or def if the collection is empty.
     */
    @Nonnull
    public E getOrDefault(@Nonnull E def) {
        final E e = getOrNull();

        return e != null ? e : def;
    }

    /**
     * Gets a random element from this collection based on the weight; or null if the collection is empty.
     *
     * @return a random element from this collection based on the weight; or null if the collection is empty.
     */
    @Nullable
    public E getOrNull() {
        int totalWeight = elements.lastKey();
        int randomWeight = random.nextInt(totalWeight) + 1;

        for (Map.Entry<Integer, List<E>> entry : elements.entrySet()) {
            int weight = entry.getKey();
            if (randomWeight <= weight) {
                List<E> elements = entry.getValue();
                return elements.get(random.nextInt(elements.size()));
            }
            randomWeight -= weight;
        }

        return null;
    }

    /**
     * Gets a copy of all values associated with the given weight; or empty list if none.
     *
     * @param weight - Weight.
     * @return a copy of all values associated with the given weight; or empty list if none.
     */
    @Nonnull
    public List<E> getByWeight(@Range int weight) {
        return Lists.newArrayList(elements.getOrDefault(weight, Lists.newArrayList()));
    }

    /**
     * Returns true if this collection contains the given element; false otherwise.
     *
     * @param e - Element.
     * @return true if this collection contains the given element; false otherwise.
     */
    public boolean contains(@Nonnull E e) {
        for (E e1 : this) {
            if (e1.equals(e)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if there is at least one element with the given weight; false otherwise.
     *
     * @param weight - Weight.
     * @return true if there is at least one element with the given weight; false otherwise.
     */
    public boolean containsWeight(int weight) {
        return elements.containsKey(weight);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Iterator<E> iterator() {
        final Set<Map.Entry<Integer, List<E>>> entries = Sets.newHashSet(elements.entrySet());
        final Set<E> iterator = new HashSet<>();

        for (Map.Entry<Integer, List<E>> entry : entries) {
            iterator.addAll(entry.getValue());
        }

        entries.clear();
        return iterator.iterator();
    }

    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
