package kz.hapyl.spigotutils.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
/**
 * Indicates min and/or max size of array supported by method.
 */
public @interface ArraySize {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

}
