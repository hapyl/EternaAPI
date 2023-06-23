package me.hapyl.spigotutils.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this method or class has been tested using {@link TestedOn#version()} minecraft and spigot version.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE, ElementType.METHOD })
public @interface TestedOn {
    /**
     * Gets the tested version for this build.
     */
    Version version();

}
