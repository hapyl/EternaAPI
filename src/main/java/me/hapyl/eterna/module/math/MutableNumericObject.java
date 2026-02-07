package me.hapyl.eterna.module.math;

/**
 * Represents a mutable implementation of {@link NumericObject}.
 */
public interface MutableNumericObject extends NumericObject {
    
    /**
     * Sets the {@link Integer} value of this object.
     *
     * @param i - The integer to set.
     */
    void setInt(final int i);
    
    /**
     * Sets the {@link Long} value of this object.
     *
     * @param l - The long to set.
     */
    default void setLong(final long l) {
        setInt((int) l);
    }
    
    /**
     * Sets the {@link Double} value of this object.
     *
     * @param d - The double to set.
     */
    default void setDouble(final double d) {
        setInt((int) d);
    }
    
    /**
     * Sets the {@link Float} value of this object.
     *
     * @param f - The float to set.
     */
    default void setFloat(final float f) {
        setInt((int) f);
    }
    
    /**
     * Sets the {@link Integer} value of this objet to {@code 0}.
     */
    default void zero() {
        setInt(0);
    }
    
    /**
     * Sets the {@link Integer} value of this object to {@link Integer#MIN_VALUE}.
     */
    default void min() {
        setInt(Integer.MIN_VALUE);
    }
    
    /**
     * Sets the {@link Integer} value of this object to {@link Integer#MAX_VALUE}.
     */
    default void max() {
        setInt(Integer.MAX_VALUE);
    }
    
}
