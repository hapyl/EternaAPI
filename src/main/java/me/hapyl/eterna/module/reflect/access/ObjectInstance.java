package me.hapyl.eterna.module.reflect.access;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a wrapper for an object to refer to it on reflection access.
 */
public interface ObjectInstance {
    
    /**
     * Refers to a static object, or {@code null} to access a {@code static} field/method.
     */
    ObjectInstance STATIC = () -> null;
    
    /**
     * Refers to an object.
     *
     * @return the object reference.
     */
    @Nullable
    Object refer();
    
}
