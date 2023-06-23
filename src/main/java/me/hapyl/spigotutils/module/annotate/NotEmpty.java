package me.hapyl.spigotutils.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that arrays and list must not be empty.
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
}
