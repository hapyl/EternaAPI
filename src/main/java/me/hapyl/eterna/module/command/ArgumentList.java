package me.hapyl.eterna.module.command;

import me.hapyl.eterna.module.util.TypeConverter;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Represents a {@link ArgumentList} which is a wrapped for {@code String[]} arguments with access to each element via {@link TypeConverter}.
 */
public class ArgumentList implements Iterable<String> {
    
    /**
     * Defines the array length.
     */
    public final int length;
    
    private final String[] array;
    
    /**
     * Creates a new {@link ArgumentList} from the given {@code args}.
     *
     * @param args - The string arguments.
     */
    public ArgumentList(@NotNull String[] args) {
        this.array = args;
        this.length = args.length;
    }
    
    /**
     * Gets a {@link TypeConverter} at the given index, or an empty type converted if index is out of bounds.
     *
     * @param index - The index to get the object at.
     * @return a {@link TypeConverter} at the given index, or an empty type converted if index is out of bounds.
     */
    public @NotNull TypeConverter get(int index) {
        return TypeConverter.fromNullable(index < 0 || index >= array.length ? null : array[index]);
    }
    
    /**
     * Returns the {@link Integer} at the given index, or {@code 0} if the index is out of bounds or does not contain an integer.
     *
     * @param index - The index to retrieve the value from.
     * @return The integer at the given index, or {@code 0} if unavailable.
     */
    public int getInt(int index) {
        return get(index).toInt();
    }
    
    /**
     * Returns the {@link Float} at the given index, or {@code 0} if the index is out of bounds or does not contain a float.
     *
     * @param index - The index to retrieve the value from.
     * @return The float at the given index, or {@code 0} if unavailable.
     */
    public float getFloat(int index) {
        return get(index).toFloat();
    }
    
    /**
     * Returns the {@link Double} at the given index, or {@code 0} if the index is out of bounds or does not contain a double.
     *
     * @param index - The index to retrieve the value from.
     * @return The double at the given index, or {@code 0} if unavailable.
     */
    public double getDouble(int index) {
        return get(index).toDouble();
    }
    
    /**
     * Returns the {@link String} at the given index, or {@code ""} (empty string) if the index is out of bounds.
     *
     * @param index - The index to retrieve the value from.
     * @return The float at the given index, or {@code ""} (empty string) if index is out of bounds.
     */
    public @NotNull String getString(int index) {
        return get(index).toString();
    }
    
    /**
     * Joins the arguments into a single string starting from the specified index, with each element separated by the given {@code separator}.
     *
     * @param startIndex - The starting index from which to begin joining the array elements.
     * @param separator  - The separator to be used between each element.
     * @return A single string of element string values separated by the given separator.
     */
    public @NotNull String joinString(int startIndex, @NotNull String separator) {
        if (startIndex < 0 || startIndex > array.length) {
            return "";
        }
        
        return Arrays.stream(array)
                     .skip(startIndex)
                     .map(String::valueOf)
                     .collect(Collectors.joining(separator));
    }
    
    /**
     * Joins the arguments into a single string starting from the specified index, with each element separated by a space.
     *
     * @param startIndex - The starting index from which to begin joining the array elements.
     * @return A single string of element string values separated by a space.
     */
    public @NotNull String joinString(int startIndex) {
        return joinString(startIndex, " ");
    }
    
    /**
     * Gets the string representation of this {@link ArgumentList}.
     *
     * @return the string representation of this argument list.
     */
    @Override
    public @NotNull String toString() {
        return Arrays.toString(array);
    }
    
    /**
     * Creates a copy of this {@link ArgumentList} from and to the specified ranges.
     *
     * @param from - The starting index (inclusive).
     * @param to   - The ending index (exclusive).
     * @return a copy of this argument list.
     */
    public @NotNull ArgumentList copyOfRange(int from, int to) {
        return copyOfRange(this, from, to);
    }
    
    /**
     * Creates a copy of this {@link ArgumentList} from the specified index to the end of the arguments list.
     *
     * @param from - The starting index (inclusive).
     * @return a copy of this argument list.
     */
    public @NotNull ArgumentList copyOfRange(int from) {
        return copyOfRange(this, from, this.length);
    }
    
    /**
     * Gets an {@link Iterator} for this {@link ArgumentList}, which does not support removing.
     *
     * @return a new iterator.
     */
    @Override
    public @NonNull Iterator<String> iterator() {
        return new ArgumentListIterator(this);
    }
    
    /**
     * Creates a copy of the given {@link ArgumentList} from and to the specified ranges.
     *
     * @param origin - The original argument list.
     * @param from   - The starting index (inclusive).
     * @param to     - The ending index (exclusive).
     * @return a copy of the argument list.
     */
    public static @NotNull ArgumentList copyOfRange(@NotNull ArgumentList origin, int from, int to) {
        return new ArgumentList(Arrays.copyOfRange(origin.array, from, to));
    }
    
    /**
     * Creates a copy of the given {@link ArgumentList} from the specified index to the end of the arguments list.
     *
     * @param origin - The original argument list.
     * @param from   - The starting index (inclusive).
     * @return a copy of the argument list.
     */
    public static @NotNull ArgumentList copyOfRange(@NotNull ArgumentList origin, int from) {
        return copyOfRange(origin, from, origin.length);
    }
    
    /**
     * A static factory method for creating {@link ArgumentList} from the given {@code args}.
     *
     * @param args - The arguments from which to create the argument list.
     * @return a new argument list.
     */
    public static @NotNull ArgumentList create(@NotNull String[] args) {
        return new ArgumentList(args);
    }
    
    public static class ArgumentListIterator implements Iterator<String> {
        
        private final ArgumentList argumentList;
        private int index;
        
        ArgumentListIterator(@NotNull ArgumentList argumentList) {
            this.argumentList = argumentList;
        }
        
        @Override
        public boolean hasNext() {
            return index < argumentList.length;
        }
        
        @Override
        public String next() {
            return argumentList.array[index++];
        }
        
    }
    
}