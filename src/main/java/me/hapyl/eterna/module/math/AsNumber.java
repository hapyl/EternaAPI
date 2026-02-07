package me.hapyl.eterna.module.math;

/**
 * Represents an object that can be converted as a {@link Number}.
 */
public interface AsNumber {
    
    /**
     * Converts this object into an {@link Integer}.
     *
     * @return this object as a {@code integer}.
     */
    int asInt();
    
    /**
     * Converts this object into a {@link Long}.
     *
     * @return this object as a {@code long}.
     */
    long asLong();
    
    /**
     * Converts this object into a {@link Float}.
     *
     * @return this object as a {@code float}.
     */
    float asFloat();
    
    /**
     * Converts this object into a {@link Double}.
     *
     * @return this object as a {@code double}.
     */
    double asDouble();
    
}
