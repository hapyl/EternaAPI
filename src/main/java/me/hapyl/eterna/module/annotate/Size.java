package me.hapyl.eterna.module.annotate;

import org.jetbrains.annotations.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Indicates that the annotated {@link Collection} or {@code array} size must be within the range.
 *
 * <p>
 * If annotated on a method, it may, but not required to throw an {@link IllegalArgumentException} if the size isn't within range.
 * </p>
 *
 * @see Range
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Size {
    
    /**
     * Gets the minimum size (inclusive).
     *
     * @return the minimum size (inclusive).
     */
    int from();
    
    /**
     * Gets the maximum size (inclusive).
     *
     * @return the maximum size (inclusive).
     */
    int to();
    
}
