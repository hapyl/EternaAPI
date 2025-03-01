package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this method or class has been tested using {@link TestedOn#version()} Minecraft version.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE, ElementType.METHOD })
public @interface TestedOn {
    /**
     * Gets the tested version for this build.
     */
    @Nonnull Version version();
}
