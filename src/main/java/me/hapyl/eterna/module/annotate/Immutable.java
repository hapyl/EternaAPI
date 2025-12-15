package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated parameter must not be mutated.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.PARAMETER })
public @interface Immutable {
    
}
