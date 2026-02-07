package me.hapyl.eterna.module.command;

import me.hapyl.eterna.module.util.TypeConverter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents a list of arguments, with access to each element via {@link TypeConverter}.
 */
public class ArgumentList {
    
    /**
     * Defines the array length.
     */
    public final int length;
    
    private final String[] array;
    
    /**
     * Creates a new {@link ArgumentList} from the input {@code array}.
     *
     * @param array - The input array.
     */
    public ArgumentList(@NotNull String[] array) {
        this.array = array;
        this.length = array.length;
    }
    
    /**
     * Gets a {@link TypeConverter} at the given index, or an empty type converted if index is out of bounds.
     *
     * @param index - The index to get the object at.
     * @return a {@link TypeConverter} at the given index, or an empty type converted if index is out of bounds.
     */
    @NotNull
    public TypeConverter get(int index) {
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
     * Returns the {@link Float} at the given index, or {@code 0f} if the index is out of bounds or does not contain a float.
     *
     * @param index - The index to retrieve the value from.
     * @return The float at the given index, or {@code 0f} if unavailable.
     */
    public float getFloat(int index) {
        return get(index).toFloat();
    }
    
    /**
     * Returns the {@link Double} at the given index, or {@code 0d} if the index is out of bounds or does not contain a double.
     *
     * @param index - The index to retrieve the value from.
     * @return The double at the given index, or {@code 0f} if unavailable.
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
    @NotNull
    public String getString(int index) {
        return get(index).toString();
    }
    
    /**
     * Converts the array into a single string starting from the specified index,
     * with elements separated by spaces.
     * <p>If the index is out of bounds, returns an empty string.</p>
     *
     * @param startIndex - The starting index from which to begin joining the array elements.
     * @return A space-separated string of array elements from the given index, or an empty string if the index is invalid.
     */
    @NotNull
    public String makeStringArray(int startIndex) {
        if (startIndex < 0 || startIndex > array.length) {
            return "";
        }
        
        return Arrays.stream(array)
                     .map(String::valueOf)
                     .collect(Collectors.joining(" "));
    }
    
    /**
     * Creates a copy of the given {@link ArgumentList} from the specified range.
     *
     * @param origin - The original argument list.
     * @param from   - The starting index (inclusive).
     * @param to     - The ending index (inclusive).
     * @return a range copy of the argument list.
     */
    @NotNull
    public static ArgumentList copyOfRange(@NotNull ArgumentList origin, int from, int to) {
        return new ArgumentList(Arrays.copyOfRange(origin.array, from, to));
    }
}

