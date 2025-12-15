package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

/**
 * Static utility class for easier bitmask manipulations.
 */
public final class Bitmask {
    
    private Bitmask() {
    }
    
    /**
     * Creates a bitmask by applying all the given bits on top of an initial base mask.
     *
     * @param baseMask - The starting bitmask value.
     * @param bits     - The bits to include in the resulting mask.
     * @return The resulting bitmask.
     */
    public static byte make(byte baseMask, @Nullable byte... bits) {
        if (bits == null || bits.length == 0) {
            return baseMask;
        }
        
        byte mask = baseMask;
        
        for (byte bit : bits) {
            mask |= bit;
        }
        
        return mask;
    }
    
    /**
     * Creates a bitmask from the specified bits.
     *
     * @param bits - The bits to include in the resulting mask.
     * @return The resulting bitmask.
     */
    public static byte make(@Nullable byte... bits) {
        return make((byte) 0, bits);
    }
    
    /**
     * Creates a bitmask from a collection using the given mapping function.
     *
     * @param collection   - The collection.
     * @param byteFunction - A function that converts a value into its corresponding bit value.
     * @return The resulting bitmask representing all provided enum values.
     */
    public static <T> byte make(@Nonnull Collection<T> collection, @Nonnull Function<T, Byte> byteFunction) {
        byte mask = 0;
        
        for (T value : collection) {
            mask |= byteFunction.apply(value);
        }
        
        return mask;
    }
    
    /**
     * Checks whether the specified bit is set within the given bitmask.
     *
     * @param mask - The bitmask to check.
     * @param bit  - The bit to test.
     * @return {@code true} if the bit is set in the mask, {@code false} otherwise.
     */
    public static boolean has(byte mask, byte bit) {
        return (mask & bit) != 0;
    }
    
}
