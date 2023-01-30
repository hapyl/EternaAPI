package me.hapyl.spigotutils.module.util;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    public static <T extends Enum<T>> String[] getValuesNames(Class<T> enumClass) {
        final Enum<T>[] values = getValues(enumClass);
        if (values == null || values.length == 0) {
            return new String[0];
        }

        return CollectionUtils.migrate(values, new String[values.length], Enum::name);
    }

    /**
     * Returns an enum constant by name.
     *
     * @param enumClass - Enum class.
     * @param name      - Name to search for.
     * @param def       - Default value.
     * @return an enum constant by name.
     */
    @CheckForNull
    public static <T extends Enum<T>> T byName(Class<T> enumClass, String name, T def) {
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
        return byName(enumClass, name, null);
    }

    /**
     * Return array of enum values. Same as Enum#values().
     *
     * @param enumClass - Enum class.
     * @return array of enum values. Same as Enum#values().
     */
    public static <T extends Enum<T>> T[] getValues(Class<T> enumClass) {
        return enumClass.getEnumConstants();
    }

}
