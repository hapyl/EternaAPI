package me.hapyl.spigotutils.module.reflect.field;

import javax.annotation.Nonnull;

/**
 * Allows converting field value.
 * <br>
 * Used for obfuscated fields.
 */
public interface FieldConverter<T> {

    @Nonnull
    T fromObject(@Nonnull Object object);

    @Nonnull
    Object toObject(@Nonnull T t);

}
