package me.hapyl.spigotutils.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that annotated variable <b>must not</b> be used as {@link AutoCloseable}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.LOCAL_VARIABLE)
public @interface DoNotUseAutoCloseable {
}
