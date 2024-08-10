package me.hapyl.eterna.module.util;

/**
 * A mutable implementation of {@link NumericObject}.
 */
public interface MutableNumericObject extends NumericObject {

    /**
     * Sets the {@link Integer} value.
     *
     * @param i - New integer.
     */
    void setInt(final int i);

    /**
     * Sets the {@link Long} value.
     *
     * @param l - New long.
     */
    default void setLong(final long l) {
        setInt((int) l);
    }

    /**
     * Sets the {@link Double} value.
     *
     * @param d - New double.
     */
    default void setDouble(final double d) {
        setInt((int) d);
    }

    /**
     * Sets the {@link Float} value.
     *
     * @param f - New float.
     */
    default void setFloat(final float f) {
        setInt((int) f);
    }

    /**
     * Sets the {@link Integer} value to zero.
     */
    default void zero() {
        setInt(0);
    }

    /**
     * Sets the {@link Integer} value to {@link Integer#MIN_VALUE}.
     */
    default void min() {
        setInt(Integer.MIN_VALUE);
    }

    /**
     * Sets the {@link Integer} value to {@link Integer#MAX_VALUE}.
     */
    default void max() {
        setInt(Integer.MAX_VALUE);
    }

}
