package me.hapyl.spigotutils.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this parameter, be that an array or a number, must be within the given range.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Range {

    /**
     * The minimum range the value should have. (Inclusive)
     */
    int min() default 0;

    /**
     * The maximum range the value should have. (Inclusive)
     */
    int max() default Integer.MAX_VALUE;

    /**
     * Whenever method will throw an error if the value is outside the given range.
     */
    boolean throwsError() default false;

}
