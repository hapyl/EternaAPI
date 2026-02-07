package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated parameter supports nested keys, as example:
 * <pre>{@code
 * // foo.bar
 * {
 *     foo: {
 *         bar: this
 *     }
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface SupportsNestedKey {
}
