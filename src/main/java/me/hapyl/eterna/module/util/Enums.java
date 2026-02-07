package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a helper utility class for {@link Enum}.
 */
@UtilityClass
public final class Enums {
    
    private Enums() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets the next {@link Enum} constant after the current {@link E}, wrapping to the start if the next index is out-of-bounds.
     *
     * @param enumClass – The enum class.
     * @param current   – The current enum constant.
     * @return the next enum constant after the current {@link E}, wrapping to the start if the next index is out-of-bounds.
     */
    @NotNull
    public static <E extends Enum<E>> E getNextValue(@NotNull Class<E> enumClass, @NotNull E current) {
        final E[] constants = enumClass.getEnumConstants();
        
        return constants[(current.ordinal() + 1) % constants.length];
    }
    
    /**
     * Gets the previous {@link Enum} constant before the current {@link E}, wrapping to the end if the next index is out-of-bounds.
     *
     * @param enumClass – The enum class.
     * @param current   – The current enum constant.
     * @return the previous enum constant before the current {@link E}, wrapping to the end if the next index is out-of-bounds.
     */
    @NotNull
    public static <E extends Enum<E>> E getPreviousValue(@NotNull Class<E> enumClass, @NotNull E current) {
        final E[] constants = enumClass.getEnumConstants();
        
        return constants[(current.ordinal() - 1 + constants.length) % constants.length];
    }
    
    /**
     * Gets an array of {@link String} containing the {@link Enum#name()} of each {@link Enum} constant.
     *
     * @param enumClass - The enum class.
     * @return an array of string containing the name of each enum constant.
     */
    @NotNull
    public static <T extends Enum<T>> String[] getValuesNames(@NotNull Class<T> enumClass) {
        return getValuesNameAsList(enumClass).toArray(String[]::new);
    }
    
    /**
     * Gets an <b>immutable</b> {@link List} containing the {@link Enum#name()} of each {@link Enum} constants.
     *
     * @param enumClass - The enum class.
     * @param <T>       - The enum type.
     * @return an <b>immutable</b> list containing the name of each enum constants.
     */
    @NotNull
    public static <T extends Enum<T>> List<String> getValuesNameAsList(@NotNull Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                     .map(Enum::name)
                     .toList();
    }
    
    /**
     * Gets the {@link Enum} constant by the given name, or {@code defaultValue} if there is no constant by the given name.
     *
     * <p>
     * The name is forcefully uppercased to respect Java's constant name convention, which also means that the
     * target {@link Enum} class must also follow the convention.
     * </p>
     *
     * @param enumClass    - The enum class.
     * @param name         - The name of the content.
     * @param defaultValue - The default value to return of there is no constant by the given name.
     * @return an enum constant by name, or {@code defaultValue} if no constant by the given name.
     */
    @NotNull
    public static <T extends Enum<T>> T byName(@NotNull Class<T> enumClass, @NotNull String name, @NotNull T defaultValue) {
        final T anEnum = byName(enumClass, name);
        
        return anEnum != null ? anEnum : defaultValue;
    }
    
    /**
     * Gets the {@link Enum} constant by the given name, or {@code null} if there is no constant by the given name.
     *
     * <p>
     * The name is forcefully uppercased to respect Java's constant name convention, which also means that the
     * target {@link Enum} class must also follow the convention.
     * </p>
     *
     * @param enumClass - The enum class.
     * @param name      - The name of the content.
     * @return an enum constant by name, or {@code null} if no constant by the given name.
     */
    @Nullable
    public static <T extends Enum<T>> T byName(@NotNull Class<T> enumClass, @NotNull String name) {
        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        }
        catch (IllegalArgumentException ignored0) {
            return null;
        }
    }
    
    /**
     * Gets {@link Enum} constants for the given {@link Enum} class.
     *
     * <p>
     * This method removes the {@code null}ability from {@code enumClass.getEnumConstants()}.
     * </p>
     *
     * @param enumClass - The enum class.
     * @return the enum constants for the given enum class.
     */
    @NotNull
    public static <T extends Enum<T>> T[] getValues(@NotNull Class<T> enumClass) {
        return Objects.requireNonNull(enumClass.getEnumConstants(), "The provided class isn't an enum!");
    }
    
    /**
     * Gets a random {@link Enum} value for the given {@link Enum}, or {@code null} if the {@link Enum} is empty.
     *
     * @param enumClass - The enum class.
     * @param <T>       - The enum type.
     * @return a random enum value for the given enum, or {@code null} if the enum is empty.
     */
    @Nullable
    public static <T extends Enum<T>> T getRandomValue(@NotNull Class<T> enumClass) {
        return CollectionUtils.randomElement(getValues(enumClass));
    }
    
    /**
     * Gets a random {@link Enum} value for the given {@link Enum}, or {@code defaultValue} if the {@link Enum} is empty.
     *
     * @param enumClass    - The enum class.
     * @param defaultValue - The default value to return if the enum is empty.
     * @param <T>          - The enum type.
     * @return a random enum value for the given enum, or {@code defaultValue} if the enum is empty.
     */
    @NotNull
    public static <T extends Enum<T>> T getRandomValue(@NotNull Class<T> enumClass, @NotNull T defaultValue) {
        return CollectionUtils.randomElement(getValues(enumClass), defaultValue);
    }
    
    
    /**
     * Gets a random {@link Enum} value for the given {@link Enum}, or the first constant if the {@link Enum} is empty.
     *
     * @param enumClass - The enum class.
     * @param <T>       - The enum type.
     * @return a random enum value for the given enum, or the first constant if the enum is empty.
     */
    @NotNull
    public static <T extends Enum<T>> T getRandomValueOrFirst(@NotNull Class<T> enumClass) {
        return CollectionUtils.randomElementOrFirst(getValues(enumClass));
    }
    
}
