package me.hapyl.eterna.module.math;

/**
 * Represents a numeric object that may be represented as a {@link Number}.
 */
public interface NumericObject {
    
    /**
     * Gets an {@link Integer} representation of this object.
     *
     * @return an integer representation of this object.
     */
    int toInt();
    
    /**
     * Gets a {@link Long} representation of this object.
     *
     * @return a long representation of this object.
     */
    default long toLong() {
        return toInt();
    }
    
    /**
     * Gets a {@link Double} representation of this object.
     *
     * @return a double representation of this object.
     */
    default double toDouble() {
        return toInt();
    }
    
    /**
     * Gets a {@link Float} representation of this object.
     *
     * @return a float representation of this object.
     */
    default float toFloat() {
        return toInt();
    }
    
}
