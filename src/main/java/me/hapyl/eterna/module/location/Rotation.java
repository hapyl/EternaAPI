package me.hapyl.eterna.module.location;

/**
 * Represents an object with a {@code yaw} and {@code pitch} rotation.
 */
public interface Rotation {
    
    /**
     * Gets the {@code yaw} rotation.
     *
     * @return the {@code yaw} rotation.
     */
    float yaw();
    
    /**
     * Gets the {@code pitch} rotation.
     *
     * @return the {@code pitch} rotation.
     */
    float pitch();
    
}
