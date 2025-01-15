package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class is a wrapper for the given NMS class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface NmsWrapper {

    /**
     * Defines the wrapping class.
     *
     * @return the wrapping class.
     */
    @Nonnull Class<?> value();

}
