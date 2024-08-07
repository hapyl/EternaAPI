package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that this field <b>must</b> be accessed via the given getter, instead of directly.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AccessViaGetter {

    /**
     * Gets the getter method name.
     *
     * @return the getter method name.
     */
    @Nonnull
    String value();

}
