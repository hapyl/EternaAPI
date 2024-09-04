package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated parameter will be mutated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface Mutates {

    /**
     * An optional description on which operation will be used to mutate the given parameter.
     */
    @Nonnull
    String value() default "";

}
