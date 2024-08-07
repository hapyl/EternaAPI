package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;

/**
 * Annotates that the annotated object must be not empty.
 * <br>
 * The following are considered as <code>empty</code>:
 * <ul>
 *    <li> {@link String#isEmpty()}
 *    <li> {@link String#isBlank()}
 *    <li> {@link Collection#isEmpty()}
 *    <li> {@link Map#isEmpty()}
 *    <li> <code>array.length == 0</code>
 * </ul>
 * <p>
 * etc.
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
}
