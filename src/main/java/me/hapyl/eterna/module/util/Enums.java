package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for enums.
 */
@UtilityClass
public final class Enums {
    
    private Enums() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets the next {@link Enum} constant after the given one, wrapping to the first if at the end.
     *
     * @param enumClass – The enum class.
     * @param current   – The current enum constant.
     * @return the next enum constant, or the first if the current is the last
     */
    @Nonnull
    public static <T extends Enum<T>> T getNextValue(@Nonnull Class<T> enumClass, @Nonnull T current) {
        final T[] constants = enumClass.getEnumConstants();
        
        return constants[(current.ordinal() + 1) % constants.length];
    }
    
    /**
     * Gets the previous {@link Enum} constant before the given one, wrapping to the last if at the beginning.
     *
     * @param enumClass – The enum class.
     * @param current   – The current enum constant.
     * @return the previous enum constant, or the last if the current is the first
     */
    @Nonnull
    public static <T extends Enum<T>> T getPreviousValue(@Nonnull Class<T> enumClass, @Nonnull T current) {
        final T[] constants = enumClass.getEnumConstants();
        
        return constants[(current.ordinal() - 1 + constants.length) % constants.length];
    }
    
    /**
     * Gets an array of {@link String} containing the {@link Enum#name()} of each {@link Enum} constant.
     *
     * @param enumClass - The enum class.
     * @return an array of {@link String} containing the {@link Enum#name()} of each {@link Enum} constant.
     */
    @Nonnull
    public static <T extends Enum<T>> String[] getValuesNames(@Nonnull Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                     .map(Enum::name)
                     .toArray(String[]::new);
    }
    
    /**
     * Gets a {@link List} containing the {@link Enum#name()} of each {@link Enum} constants.
     *
     * @param enumClass - The enum class.
     * @param <T>       - The enum type.
     * @return a {@link List} containing the {@link Enum#name()} of each {@link Enum} constants.
     */
    @Nonnull
    public static <T extends Enum<T>> List<String> getValuesNameAsList(@Nonnull Class<T> enumClass) {
        return Lists.newArrayList(getValuesNames(enumClass));
    }
    
    /**
     * Gets the {@link Enum} constant by the given name, or {@code def} if there is no constant by the given name.
     * <p><i>The name is forcefully uppercased to respect Java's constant name convention.</i></p>
     *
     * @param enumClass - The enum class.
     * @param name      - The name of the content.
     * @param def       - The default value to return of there is no constant by the given name.
     * @return an enum constant by name, or {@code def} if no constant by the given name.
     */
    @Nonnull
    public static <T extends Enum<T>> T byName(@Nonnull Class<T> enumClass, @Nonnull String name, @Nonnull T def) {
        final T anEnum = byName(enumClass, name);
        
        return anEnum != null ? anEnum : def;
    }
    
    /**
     * Gets the {@link Enum} constant by the given name, or {@code null} if there is no constant by the given name.
     * <p><i>The name is forcefully uppercased to respect Java's constant name convention.</i></p>
     *
     * @param enumClass - The enum class.
     * @param name      - The name of the content.
     * @return an enum constant by name, or {@code null} if no constant by the given name.
     */
    @Nullable
    public static <T extends Enum<T>> T byName(@Nonnull Class<T> enumClass, @Nonnull String name) {
        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        }
        catch (IllegalArgumentException ignored0) {
            return null;
        }
    }
    
    /**
     * Gets {@link Enum} constants for the given class.
     * <p>This method removes the {@code null}ability from {@code enumClass.getEnumConstants()}.</p>
     *
     * @param enumClass - The enum class.
     * @return {@link Enum} constants for the given class.
     */
    @Nonnull
    public static <T extends Enum<T>> T[] getValues(@Nonnull Class<T> enumClass) {
        return Objects.requireNonNull(enumClass.getEnumConstants(), "The provided class isn't an enum!");
    }
    
    /**
     * Gets a random {@link Enum} value for the given {@link Enum}, or {@code null} if the {@link Enum} is empty.
     *
     * @param enumClass - The enum class.
     * @param <T>       - The enum type.
     * @return a random {@link Enum} value for the given {@link Enum}, or {@code null} if the {@link Enum} is empty.
     */
    @Nullable
    public static <T extends Enum<T>> T getRandomValue(@Nonnull Class<T> enumClass) {
        return getRandomValue(enumClass, null);
    }
    
    /**
     * Gets a random {@link Enum} value for the given {@link Enum}, or {@code def} if the {@link Enum} is empty.
     *
     * @param enumClass - The enum class.
     * @param def       - The default value to return if the enum is empty.
     * @param <T>       - The enum type.
     * @return a random {@link Enum} value for the given {@link Enum}, or {@code def} if the {@link Enum} is empty.
     */
    public static <T extends Enum<T>> T getRandomValue(@Nonnull Class<T> enumClass, T def) {
        return CollectionUtils.randomElement(getValues(enumClass), def);
    }
    
}
