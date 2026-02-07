package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated element is immutable, and will throw {@link UnsupportedOperationException} (or similar) upon trying to mutate it.
 *
 * <p>
 * If annotated on a method, it applies to it's return value.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface Immutable {
}
