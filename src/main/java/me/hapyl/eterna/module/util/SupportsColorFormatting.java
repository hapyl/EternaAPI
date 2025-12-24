package me.hapyl.eterna.module.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that this string parameter supports chat colors.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Deprecated(forRemoval = true)
public @interface SupportsColorFormatting {

    /**
     * Annotates what character should be used as a color char.
     * <p>
     * Defaults to '&'.
     *
     * @return a character that is used as color char.
     */
    char translateChar() default '&';

    /**
     * Gets if this string supports bukkit hex colors.
     *
     * @return true if this string supports bukkit hex colors.
     */
    boolean hex() default true;
}
