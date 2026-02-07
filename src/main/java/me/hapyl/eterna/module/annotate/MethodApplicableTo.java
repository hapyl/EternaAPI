package me.hapyl.eterna.module.annotate;

import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method, specifically an {@link ItemBuilder} method only works for the specified {@link #values()}
 * and will be ignored otherwise, unless specifically stated otherwise.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface MethodApplicableTo {
    
    /**
     * Gets the {@link Material} this method is applicable to.
     *
     * @return the materials this method is applicable to.
     */
    @NotNull
    Material[] values() default { };
    
}
