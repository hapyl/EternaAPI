package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated {@link String} parameter is case-sensitive.
 *
 * <p>
 * Applying this annotation to non-{@link String} types applies to their underlying {@link String} value.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface CaseSensitive {
}
