package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * A weighted collection.
 */
public class WeightedCollection<T> implements Iterable<T> {

    protected final List<WeightedElement> elements;
    protected final Random random;

    protected int totalWeight;

    public WeightedCollection() {
        this.elements = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Adds the given element to this {@link WeightedCollection} with the given weight.
     *
     * @param t      - Element to add.
     * @param weight - Weight of the element.
     * @throws IllegalArgumentException If weight is negative.
     */
    public void add(@Nonnull T t, @Range(from = 0, to = Integer.MAX_VALUE) int weight) {
        elements.add(new WeightedElement(t, weight));
        totalWeight += weight;
    }

    /**
     * Removes the given element from this {@link WeightedCollection}.
     *
     * @param t - Element to remove.
     * @return true if the element was removed and total weight decremented, false otherwise.
     */
    public boolean remove(@Nonnull T t) {
        return elements.removeIf(item -> {
            if (item.t.equals(t)) {
                totalWeight -= item.weight;
                return true;
            }

            return false;
        });
    }

    /**
     * Adds the given elements from the given {@link Collection} to this {@link WeightedCollection} with the given weight.
     *
     * @param weight     - Weight of all elements.
     * @param collection - Collection.
     */
    public void addAll(@Range(from = 0, to = Integer.MAX_VALUE) int weight, @Nonnull Collection<T> collection) {
        for (T t : collection) {
            this.add(t, weight);
        }
    }

    /**
     * Gets a random element from this {@link WeightedCollection} or throws an {@link IllegalStateException} if the {@link WeightedCollection} {@link #isEmpty()}.
     *
     * @return a random element or throws an exception if the collection is empty.
     * @throws IllegalStateException If the {@link WeightedCollection} is empty.
     */
    @Nonnull
    public T getRandomElement() {
        final T t = getRandomElementOrNull();

        if (t != null) {
            return t;
        }

        throw new IllegalStateException("Cannot get a random element from an empty collection!");
    }

    /**
     * Gets a random element from this {@link WeightedElement}, or default element if the {@link WeightedCollection} {@link #isEmpty()}.
     *
     * @param def - Default element.
     * @return a random element or default if there are no elements.
     */
    @Nonnull
    public T getRandomElementOrDefault(@Nonnull T def) {
        final T t = getRandomElementOrNull();

        return t != null ? t : def;
    }

    /**
     * Gets a random element from this {@link WeightedCollection} or <code>null</code> if the {@link WeightedCollection} {@link #isEmpty()}.
     *
     * @return a random element or null if the collection is empty.
     */
    @Nullable
    public T getRandomElementOrNull() {
        final double randomWeight = random.nextDouble(totalWeight);
        double cumulativeWeight = 0;

        for (WeightedElement element : elements) {
            cumulativeWeight += element.weight;

            if (randomWeight < cumulativeWeight) {
                return element.t;
            }
        }

        return null;
    }

    /**
     * Gets a {@link List} of all elements from this {@link WeightedCollection} matching the given weight.
     * <br>
     * This {@link List} is <b>not</b> backed up by this {@link WeightedCollection}.
     *
     * @param weight - Weight to match.
     * @return a list of all elements from this collection matching the given weight.
     */
    @Nonnull
    public List<T> getElementsByWeight(@Range(from = 0, to = Integer.MAX_VALUE) int weight) {
        final List<T> elements = new ArrayList<>();

        for (WeightedElement element : this.elements) {
            if (element.weight == weight) {
                elements.add(element.t);
            }
        }

        return elements;
    }

    /**
     * Returns true if the given element is in this {@link WeightedCollection}, false otherwise.
     *
     * @param t - Element to check.
     * @return true if the given element is in this collection, false otherwise.
     */
    public boolean contains(@Nonnull T t) {
        for (WeightedElement element : elements) {
            if (element.t.equals(t)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if at least one element in this {@link WeightedCollection} has the given weight.
     *
     * @param weight - Weight to check.
     * @return true if at least one element in this collection has the given weight.
     */
    public boolean containsWeight(int weight) {
        for (WeightedElement element : elements) {
            if (element.weight == weight) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return getElements().iterator();
    }

    /**
     * Gets a copy of all the elements in this {@link WeightedCollection}.
     *
     * @return a copy of all elements in this collection.
     */
    @Nonnull
    public List<T> getElements() {
        final List<T> elements = new ArrayList<>();

        for (WeightedElement element : this.elements) {
            elements.add(element.t);
        }

        return elements;
    }

    /**
     * Gets a copy of {@link WeightedElement}.
     *
     * @return a copy of weighted elements.
     */
    @Nonnull
    public List<WeightedElement> getWeightedElements() {
        return new ArrayList<>(this.elements);
    }

    /**
     * Gets the size of this {@link WeightedCollection}.
     *
     * @return the size of this collection.
     */
    public int size() {
        return elements.size();
    }

    /**
     * Returns true if this {@link WeightedCollection} is empty, false otherwise.
     *
     * @return true if this collection is empty, false otherwise.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Gets the drop chance for the given element, or <code>-1</code> if the element is not in this {@link WeightedCollection}.
     *
     * @param t - Element to get the drop chance for.
     * @return the drop chance for the given element, or -1 if the element is not in this collection.
     */
    public double getDropChance(@Nonnull T t) {
        final WeightedElement element = getElement(t);

        return element != null ? element.getDropChance() : -1;
    }

    /**
     * Gets the weight for the given element, or <code>-1</code> if the element is not in this {@link WeightedCollection}.
     *
     * @param t - Element to get the weight for.
     * @return the weight for the given element, or -1 if the element is not in this collection.
     */
    public int getWeight(@Nonnull T t) {
        final WeightedElement element = getElement(t);

        return element != null ? element.getWeight() : -1;
    }

    /**
     * Gets a string representation of the object.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("{totalWeight=" + totalWeight + ", elements=[");

        for (int i = 0; i < elements.size(); i++) {
            if (i != 0) {
                builder.append(", ");
            }

            final WeightedElement element = elements.get(i);

            builder.append(element.t).append(":").append(element.weight);
        }

        return builder.append("]}").toString();
    }

    @Nullable
    protected WeightedElement getElement(T t) {
        for (WeightedElement element : elements) {
            if (element.t.equals(t)) {
                return element;
            }
        }

        return null;
    }

    public final class WeightedElement {
        private final T t;
        private final int weight;

        private WeightedElement(@Nonnull T t, int weight) {
            this.t = t;
            this.weight = weight;
        }

        /**
         * Gets the weight of this element.
         *
         * @return the weight of this element.
         */
        public int getWeight() {
            return weight;
        }

        /**
         * Gets the drop chance of this element.
         *
         * @return the drop chance of this element.
         */
        public double getDropChance() {
            return (double) weight / totalWeight;
        }

        /**
         * Gets a string representation of the object.
         * <pre>{@code
         * @Override
         * public String toString() {
         *     return "%s: %.2s%%".formatted(t.toString(), getDropChance() * 100);
         * }
         * }</pre>
         *
         * @return a string representation of this object.
         */
        @Override
        public String toString() {
            return "%s: %.2f%%".formatted(t.toString(), getDropChance() * 100);
        }
    }

}
