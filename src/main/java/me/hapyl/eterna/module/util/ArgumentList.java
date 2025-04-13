package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.chat.Chat;

import javax.annotation.Nonnull;

public class ArgumentList {
    
    public final String[] array;
    public final int length;
    
    public ArgumentList(@Nonnull String[] array) {
        this.array = array;
        this.length = array.length;
    }
    
    /**
     * Gets a {@link TypeConverter} at the given index, or an empty type converted if index is out of bounds.
     *
     * @param index - The index to get the object at.
     * @return a {@link TypeConverter} at the given index, or an empty type converted if index is out of bounds.
     */
    @Nonnull
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
    @Nonnull
    public String getString(int index) {
        return index < 0 || index >= array.length ? "" : String.valueOf(array[index]);
    }
    
    /**
     * Converts the array into a single string starting from the specified index,
     * with elements separated by spaces.
     * <p>If the index is out of bounds, returns an empty string.</p>
     *
     * @param startIndex - The starting index from which to begin joining the array elements.
     * @return A space-separated string of array elements from the given index, or an empty string if the index is invalid.
     */
    @Nonnull
    public String makeStringArray(int startIndex) {
        if (startIndex < 0 || startIndex > array.length) {
            return "";
        }
        
        return Chat.arrayToString(array, startIndex);
    }
}

