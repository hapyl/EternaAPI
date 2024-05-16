package me.hapyl.spigotutils.module.reflect.field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FieldConverters {

    private FieldConverters() {
    }

    /**
     * Returns a self-converter. (default)
     *
     * @return a self-converter. (default)
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> FieldConverter<T> self() {
        return new FieldConverter<>() {
            @Nonnull
            @Override
            public T fromObject(@Nonnull Object object) {
                return (T) object;
            }

            @Nonnull
            @Override
            public Object toObject(@Nonnull T t) {
                return t;
            }
        };
    }

    @Nonnull
    public static <T> FieldConverter<T> thisOrDefault(@Nullable FieldConverter<T> converter) {
        return converter != null ? converter : self();
    }
}
