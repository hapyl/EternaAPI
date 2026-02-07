package me.hapyl.eterna.module.annotate;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method returns itself as per builder style:
 * <pre>{@code
 * @SelfReturn
 * public MyClass helloWorld() {
 *     this.helloWorld = true;
 *     return this;
 * }
 * }</pre>
 * {@link SelfReturn} is always assumed to be {@link NotNull}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SelfReturn {
}
