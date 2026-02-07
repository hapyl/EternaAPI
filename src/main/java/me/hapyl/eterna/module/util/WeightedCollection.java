package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

/**
 * Represents a weighted {@link Collection}, where elements are stored and can be retrieved by their weight.
 *
 * <p>
 * The weight refers to a probability of the element to be selected, where higher weights results in higher changes of the element to be selected.
 * </p>
 */
public class WeightedCollection<E> implements Iterable<E> {
    
    protected final List<WeightedElement> elements;
    protected final Random random;
    
    protected int totalWeight;
    
    /**
     * Creates a new {@link WeightedCollection}.
     */
    public WeightedCollection() {
        this.elements = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * Adds the given {@link E} to this {@link WeightedCollection} with the given {@code weight}.
     *
     * @param e      - The element to add.
     * @param weight - The weight of the element.
     * @throws IllegalArgumentException if the weight is negative.
     */
    public void add(@NotNull E e, @Range(from = 0, to = Integer.MAX_VALUE) int weight) {
        this.elements.add(new WeightedElement(e, weight));
        this.totalWeight += weight;
    }
    
    /**
     * Removes the given {@link E} from this {@link WeightedCollection}.
     *
     * @param e - The element to remove.
     * @return {@code true} if the element was removed and total weight decremented; {@code false} otherwise.
     */
    public boolean remove(@NotNull E e) {
        return elements.removeIf(item -> {
            if (item.element.equals(e)) {
                totalWeight -= item.weight;
                return true;
            }
            
            return false;
        });
    }
    
    /**
     * Adds the given elements from the given {@link Collection} to this {@link WeightedCollection} with the given weight.
     *
     * @param weight     - The weight of each element.
     * @param collection - The collection of which elements to add.
     */
    public void addAll(@Range(from = 0, to = Integer.MAX_VALUE) int weight, @NotNull Collection<E> collection) {
        for (E e : collection) {
            this.add(e, weight);
        }
    }
    
    /**
     * Retrieves a random element from this {@link WeightedCollection}.
     *
     * <p>
     * If there are no elements in this {@link WeightedCollection}, an {@link Optional#empty()} is returned.
     * </p>
     *
     * @return a random element wrapped in an optional.
     */
    @NotNull
    public Optional<E> getRandomElement() {
        final double randomWeight = random.nextDouble(totalWeight);
        double cumulativeWeight = 0;
        
        for (WeightedElement element : elements) {
            cumulativeWeight += element.weight;
            
            if (randomWeight < cumulativeWeight) {
                return Optional.of(element.element);
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Gets an <b>immutable</b> {@link List} of all {@link E} elements in this {@link WeightedCollection} that match the given {@code weight}.
     *
     * @param weight - The weight to match.
     * @return a list of all elements from this collection matching the given weight.
     */
    @NotNull
    public List<E> getElementsByWeight(@Range(from = 0, to = Integer.MAX_VALUE) int weight) {
        return this.elements.stream()
                            .filter(element -> element.weight == weight)
                            .map(element -> element.element)
                            .toList();
    }
    
    /**
     * Gets whether the given {@link E} is in this {@link WeightedCollection}.
     *
     * @param e - The element to check.
     * @return {@code true} if the given element is in this collection; {@code false} otherwise.
     */
    public boolean contains(@NotNull E e) {
        for (WeightedElement element : elements) {
            if (element.element.equals(e)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets whether at least one element in this {@link WeightedCollection} has the given {@code weight}.
     *
     * @param weight - The weight to check.
     * @return {@code true} if at least one element in this collection has the given weight; {@code false} otherwise.
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
     * Gets an {@link Iterator} over {@link E} in this {@link WeightedCollection}.
     *
     * <p>
     * The returned {@link Iterator} is immutable and does not support removal operations.
     * </p>
     */
    @NotNull
    @Override
    public Iterator<E> iterator() {
        return getElements().iterator();
    }
    
    /**
     * Gets an <b>immutable</b> copy of all the elements in this {@link WeightedCollection}.
     *
     * @return an <b>immutable</b> copy of all the elements in this collection.
     */
    @NotNull
    public List<E> getElements() {
        return this.elements.stream()
                            .map(element -> element.element)
                            .toList();
    }
    
    /**
     * Gets an <b>immutable</b> copy of all {@link WeightedElement} in this {@link WeightedElement}.
     *
     * @return an <b>immutable</b> copy of weighted elements.
     */
    @NotNull
    public List<WeightedElement> getWeightedElements() {
        return List.copyOf(this.elements);
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
     * Gets whether this {@link WeightedCollection} is empty.
     *
     * @return {@code true} if this collection is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }
    
    /**
     * Gets the drop chance for the given {@link E}, or {@code -1} if the element is not in this {@link WeightedCollection}.
     *
     * @param e - The element for which to get the drop chance.
     * @return the drop chance for the given element, or {@code -1} if the element is not in this collection.
     */
    public double getDropChance(@NotNull E e) {
        final WeightedElement element = findElement(e);
        
        return element != null ? element.getDropChance() : -1;
    }
    
    /**
     * Gets the {@code weight} for the given {@link E}, or {@code -1} if the element is not in this {@link WeightedCollection}.
     *
     * @param e - Element to get the weight for.
     * @return the weight for the given element, or {@code -1} if the element is not in this collection.
     */
    public int getWeight(@NotNull E e) {
        final WeightedElement element = findElement(e);
        
        return element != null ? element.weight() : -1;
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
            
            builder.append(element.element).append(":").append(element.weight);
        }
        
        return builder.append("]}").toString();
    }
    
    /**
     * Gets the {@link WeightedElement} by the given {@link E}.
     *
     * @param e - The element to match.
     * @return the weighted element, or {@code null} if not in the list.
     */
    @Nullable
    protected WeightedElement findElement(@NotNull E e) {
        return this.elements.stream()
                            .filter(element -> Objects.equals(element.element, e))
                            .findFirst()
                            .orElse(null);
    }
    
    /**
     * Represents an immutable {@link WeightedElement} that contains the {@link E} reference and its {@code weight}.
     */
    public class WeightedElement {
        
        public final E element;
        public final int weight;
        
        /**
         * Creates a new {@link WeightedElement}.
         *
         * @param element - The element reference.
         * @param weight  - The element weight.
         */
        public WeightedElement(@NotNull E element, int weight) {
            this.element = element;
            this.weight = weight;
        }
        
        /**
         * Gets the object element reference.
         *
         * @return the object element reference.
         */
        @NotNull
        public E element() {
            return element;
        }
        
        /**
         * Gets the weight of this element.
         *
         * @return the weight of this element.
         */
        public int weight() {
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
         * Gets a {@link String} representation of this {@link WeightedElement}, which is a formatted
         * drop change of the element, multiplied by {@code 100} with {@code three} decimal points.
         *
         * @return a string representation of this object.
         */
        @Override
        public String toString() {
            return "%s: %.3f%%".formatted(element.toString(), getDropChance() * 100);
        }
    }
    
}
