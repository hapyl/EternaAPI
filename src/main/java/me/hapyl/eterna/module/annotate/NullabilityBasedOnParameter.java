package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the return value of the annotated method is based on the parameter.
 * <br>
 * If the given parameter is <code>null</code>, so can the return value be, and vise versa.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NullabilityBasedOnParameter {

    /**
     * Parameter name.
     *
     * @return parameter name.
     */
    @Nonnull
    String value();

}
