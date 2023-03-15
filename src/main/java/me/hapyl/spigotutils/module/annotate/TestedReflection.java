package me.hapyl.spigotutils.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this method or class has been tested using {@link TestedReflection#version()} minecraft version.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE, ElementType.METHOD })
public @interface TestedReflection {

    String version();

}
