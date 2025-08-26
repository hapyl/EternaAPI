package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.SelfReturn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

/**
 * A modifiable wrapper of {@link List}, used in {@link Hologram}.
 * <p>Each individual string may be {@code null} or empty, resulting in an empty line displayed in the {@link Hologram}.</p>
 */
public class StringArray implements Iterable<String> {
    
    private final List<String> list;
    
    StringArray(@Nonnull String... values) {
        this.list = Lists.newArrayList(values);
    }
    
    /**
     * Appends the given strings to the list.
     *
     * @param values – The strings to append.
     */
    @SelfReturn
    public StringArray append(@Nonnull String... values) {
        this.list.addAll(List.of(values));
        return this;
    }
    
    /**
     * Appends all strings from another {@link StringArray} to this instance.
     *
     * @param other – The {@link StringArray} containing strings to append.
     */
    @SelfReturn
    public StringArray append(@Nonnull StringArray other) {
        this.list.addAll(other.list);
        return this;
    }
    
    /**
     * Gets the {@link String} at the given index, or {@code null} if index is out of bounds.
     *
     * @param index - The index to retrieve the element at.
     * @return the {@link String} at the given index, or {@code null} if index is out of bounds.
     */
    @Nullable
    public String get(int index) {
        return index < 0 || index >= list.size() ? null : list.get(index);
    }
    
    /**
     * Gets the size of the underlying {@link List}.
     *
     * @return the size of the underlying {@link List}.
     */
    public int size() {
        return list.size();
    }
    
    /**
     * Converts the list of strings to an array.
     *
     * @return An array containing all strings in the list.
     */
    @Nonnull
    public final String[] toArray() {
        return list.toArray(String[]::new);
    }
    
    /**
     * Gets an immutable copy of the underlying array.
     *
     * @return an immutable copy of the underlying array.
     */
    @Nonnull
    public final List<String> toList() {
        return List.copyOf(list);
    }
    
    @Nonnull
    @Override
    public Iterator<String> iterator() {
        return list.iterator();
    }
    
    /**
     * Creates a new {@link StringArray} with the given initial values.
     *
     * @param values – The initial values to be included in the array.
     * @return A new {@link StringArray} instance.
     */
    @Nonnull
    public static StringArray of(@Nonnull String... values) {
        return new StringArray(values);
    }
    
    /**
     * Creates a new empty {@link StringArray}.
     *
     * @return A new empty {@link StringArray} instance.
     */
    @Nonnull
    public static StringArray empty() {
        return new StringArray();
    }
    
    @Override
    public String toString() {
        return list.toString();
    }
}
