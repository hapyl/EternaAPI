package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method returns itself as per builder style.
 * <pre>{@code
 * @SelfReturn
 * public MyClass helloWorld() {
 *     this.helloWorld = true;
 *     return this;
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SelfReturn {


}
