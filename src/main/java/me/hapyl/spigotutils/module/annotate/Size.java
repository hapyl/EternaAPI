package me.hapyl.spigotutils.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the size or length of {@link #of()} must match the given one.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Size {

    @Nonnull
    String of();

    @Nonnull
    String mustMatch() default "";

    @Nonnull
    String mustBeLessThan() default "";

    @Nonnull
    String mustBeGreaterThan() default "";

    int mustEqualTo() default 0;

    int mustBeLess() default 0;

    int mustBeGreater() default 0;

    int is() default 0;

    int isNot() default 0;
}
