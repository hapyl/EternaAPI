package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated methods uses raw-types, and should be avoided.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RawUsage {
    
    /**
     * Gets the name of the method that should be used instead.
     *
     * @return the name of the method that should be used instead.
     */
    @Nonnull
    String useInstead();
    
}
