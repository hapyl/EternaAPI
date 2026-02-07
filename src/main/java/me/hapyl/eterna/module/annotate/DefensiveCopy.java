package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated parameter is defensively copied, meaning no modifications to the actual object are made.
 *
 * <p>If this annotation is applied to method, its return value is defensively copied.</p>
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.PARAMETER, ElementType.METHOD })
public @interface DefensiveCopy {
}
