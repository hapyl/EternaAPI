package me.hapyl.spigotutils.module.util;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public final class Enums {

    private Enums() {
        throw new IllegalStateException();
    }

    public static <T extends Enum<T>> Enum<T> getNextValue(Class<T> enumClass, Enum<T> current) {
        final Enum<T>[] values = getValues(enumClass);
        for (int i = 0; i < values.length; i++) {
            final Enum<T> e = values[i];
            if (e.equals(current)) {
                return values.length > (i + 1) ? values[i + 1] : values[0];
            }
        }
        return current;
    }

    public static <T extends Enum<T>> Enum<T> getPrevious(Class<T> enumClass, Enum<T> current) {
        final Enum<T>[] values = getValues(enumClass);
        for (int i = 0; i < values.length; i++) {
            final Enum<T> e = values[i];
            if (e.equals(current)) {
                return i == 0 ? values[values.length - 1] : values[i - 1];
            }
        }
        return current;
    }

    public static <T extends Enum<T>> String[] getValuesNames(Class<T> enumClass) {
        final Enum<T>[] values = getValues(enumClass);
        if (values == null || values.length == 0) {
            return new String[0];
        }

        return CollectionUtils.migrate(values, new String[values.length], Enum::name);
    }

    @CheckForNull
    public static <T extends Enum<T>> T byName(Class<T> enumClass, String name, T def) {
        return Validate.getEnumValue(enumClass, name, def);
    }

    @Nullable
    public static <T extends Enum<T>> T byName(Class<T> enumClass, String name) {
        return byName(enumClass, name, null);
    }

    public static <T extends Enum<T>> Enum<T>[] getValues(Class<T> enumClass) {
        return enumClass.getEnumConstants();
    }

}
