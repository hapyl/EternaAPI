package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Indicates that the annotated {@link String} parameter will be lowercased.
 *
 * <p>
 * Special cases:
 * <ul>
 *     <li>If annotated on a {@link Collection} or {@code array}, each of its elements will be lowercased.
 *     <li>If the type of the parameter (or collection/array) is not a {@link String}, it will be converted to one before lowercasing it.
 * </ul>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface ForceLowercase {
}
