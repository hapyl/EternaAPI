package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is an event of sorts.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {

    /**
     * An optional description about how this event is called/handled.
     *
     * @return the event description.
     */
    @Nonnull
    String description() default "";

}
