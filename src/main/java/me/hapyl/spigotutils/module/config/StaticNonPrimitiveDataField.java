package me.hapyl.spigotutils.module.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * This is a little wonky implementation but could not find a better way of doing this.
 */
public class StaticNonPrimitiveDataField {

    private StaticNonPrimitiveDataField() {
    }

    // Convert string to value.
    public static Object loadFrom(Class<?> clazz, @Nonnull Object object) {
        final String objectToString = object.toString();

        if (clazz.equals(UUID.class)) {
            return UUID.fromString(objectToString);
        }

        return object;
    }

    // Convert value to string.
    public static Object saveTo(@Nullable Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof UUID actual) {
            return actual.toString();
        }

        return object;
    }

}
