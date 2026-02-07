package me.hapyl.eterna.module.annotate;

import me.hapyl.eterna.module.util.Validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated {@code varargs} parameter must not be {@code null} and must contain at least one argument.
 *
 * <p>
 * The method on which parameter this annotation is present must implement an explicit varargs check via {@link Validate#varargs(Object[])} or {@link Validate#varargs(Object[])}.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface RequiresVarargs {
}
