package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A utility class for managing and manipulating a list of strings for {@link PlayerHologram}.
 */
public class StringArray {

    private final List<String> list;

    StringArray(@Nonnull String... values) {
        this.list = Lists.newArrayList(values);
    }

    /**
     * Appends a string to the list based on a condition.
     * <p>
     * If the condition is {@code true}, {@code ifTrue} is appended; otherwise, {@code ifFalse} is appended.
     *
     * @param condition – The condition to evaluate.
     * @param ifTrue    – The string to append if the condition is {@code true}.
     * @param ifFalse   – The string to append if the condition is {@code false}.
     */
    public StringArray appendIf(boolean condition, @Nonnull String ifTrue, @Nonnull String ifFalse) {
        return append(condition ? ifTrue : ifFalse);
    }

    /**
     * Appends the given strings to the list.
     *
     * @param values – The strings to append.
     */
    public StringArray append(@Nonnull String... values) {
        this.list.addAll(List.of(values));
        return this;
    }

    /**
     * Appends all strings from another {@link StringArray} to this instance.
     *
     * @param other – The {@link StringArray} containing strings to append.
     */
    public StringArray append(@Nonnull StringArray other) {
        this.list.addAll(other.list);
        return this;
    }

    /**
     * Converts the list of strings to an array.
     *
     * @return An array containing all strings in the list.
     */
    @Nonnull
    public final String[] toArray() {
        return list.toArray(new String[] {});
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
}
