package me.hapyl.eterna.module.annotate;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method or class has been tested on {@link TestedOn#version()} and promises a complete functionality of it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE, ElementType.METHOD })
public @interface TestedOn {
    /**
     * Gets the version this method or class is tested on.
     *
     * @return the version this method or class is tested on.
     */
    @NotNull Version version();
}
