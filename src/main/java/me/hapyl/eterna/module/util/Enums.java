package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Utility class for enums.
 */
public final class Enums {

    private Enums() {
        throw new IllegalStateException();
    }

    /**
     * Returns next value in <i>values()</i> of the enum class.
     *
     * @param enumClass - Enum class.
     * @param current   - Current value.
     * @return next value of the enum class.
     */
    @Nonnull
    public static <T extends Enum<T>> T getNextValue(Class<T> enumClass, T current) {
        final T[] values = getValues(enumClass);
        for (int i = 0; i < values.length; i++) {
            final Enum<T> e = values[i];
            if (e.equals(current)) {
                return values.length > (i + 1) ? values[i + 1] : values[0];
            }
        }
        return current;
    }

    /**
     * Returns previous value in <i>values()</i> of the enum class.
     *
     * @param enumClass - Enum class.
     * @param current   - Current value.
     * @return previous value in <i>values()</i> of the enum class.
     */
    @Nonnull
    public static <T extends Enum<T>> T getPreviousValue(Class<T> enumClass, T current) {
        final T[] values = getValues(enumClass);
        for (int i = 0; i < values.length; i++) {
            final Enum<T> e = values[i];
            if (e.equals(current)) {
                return i == 0 ? values[values.length - 1] : values[i - 1];
            }
        }
        return current;
    }

    /**
     * Returns array of strings filled with enum constants names.
     *
     * @param enumClass - Enum class.
     * @return array of strings filled with enum constants names.
     */
    @Nonnull
    public static <T extends Enum<T>> String[] getValuesNames(Class<T> enumClass) {
        final Enum<T>[] values = getValues(enumClass);

        if (values == null || values.length == 0) {
            return new String[0];
        }

        final String[] strings = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            strings[i] = values[i].name();
        }

        return strings;
    }

    @Nonnull
    public static <T extends Enum<T>> List<String> getValuesNameAsList(@Nonnull Class<T> enumClass) {
        return Lists.newArrayList(getValuesNames(enumClass));
    }

    /**
     * Returns an enum constant by name.
     *
     * @param enumClass - Enum class.
     * @param name      - Name to search for.
     * @param def       - Default value.
     * @return an enum constant by name.
     */
    @Nonnull
    public static <T extends Enum<T>> T byName(Class<T> enumClass, String name, @Nonnull T def) {
        return Validate.getEnumValue(enumClass, name, def);
    }

    /**
     * Returns an enum constant by name or null if invalid.
     *
     * @param enumClass - Enum class.
     * @param name      - Name to search for.
     * @return an enum constant by name or null if invalid.
     */
    @Nullable
    public static <T extends Enum<T>> T byName(Class<T> enumClass, String name) {
        return Validate.getEnumValue(enumClass, name, null);
    }

    /**
     * Return array of enum values. Same as Enum#values().
     *
     * @param enumClass - Enum class.
     * @return array of enum values. Same as Enum#values().
     */
    @Nullable
    public static <T extends Enum<T>> T[] getValues(Class<T> enumClass) {
        return enumClass.getEnumConstants();
    }

    /**
     * Gets a random enum value from an enum, or null if enum doesn't have any values.
     *
     * @param enumClass - Enum class.
     * @return a random enum value; or null.
     */
    @Nullable
    public static <T extends Enum<T>> T getRandomValue(Class<T> enumClass) {
        return getRandomValue(enumClass, null);
    }

    /**
     * Gets a random enum value from an enum, or default if enum doesn't have any values.
     *
     * @param enumClass - Enum class,
     * @param def       - Default value.
     * @return a random enum value from an enum, or default.
     */
    public static <T extends Enum<T>> T getRandomValue(Class<T> enumClass, T def) {
        final T[] values = getValues(enumClass);

        if (values == null) {
            return def;
        }

        return values[ThreadRandom.nextInt(values.length)];
    }

}
