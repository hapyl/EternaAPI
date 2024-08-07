package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;

/**
 * Indicates that the annotated method is a factory method for the given class, usually called <code>of()</code> or similar.
 */
public @interface FactoryMethod {

    /**
     * The product of this factory method.
     *
     * @return product of this factory method.
     */
    @Nonnull
    Class<?> value();

}
