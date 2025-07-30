package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a custom converter for {@link TypeConverter}.
 *
 * @param <T> - The converting type.
 * @see TypeConverter#toConvertible(TypeConvertible)
 */
public interface TypeConvertible<T> {
    
    @Nullable
    T convert(@Nonnull Object obj);
    
}
