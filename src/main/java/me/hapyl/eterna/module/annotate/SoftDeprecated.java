package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this class or method is soft-deprecated.
 * <br>
 * Usually means that there is a native way of doing whatever the class or method was doing.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface SoftDeprecated {

    /**
     * Reason for deprecation.
     *
     * @return reason.
     */
    @Nonnull
    String value() default "";
}
