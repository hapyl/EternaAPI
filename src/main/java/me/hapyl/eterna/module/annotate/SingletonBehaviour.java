package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated type (class, interface, etc.) <b>must</b> be treated as a singleton upon creating, meaning storing it as a constant or in a registry.
 *
 * <p>
 * Example usage:
 *
 * <pre>{@code
 * class MyClass {
 *   // Good, a static singleton constant!
 *   public static final SingletonClass MY_SINGLETON = new SingletonClass();
 *
 *   // Good, but only if `MyClass` is a singleton itself!
 *   public final SingletonClass mySingleton = new SingletonClass();
 *
 *   // Bad!
 *   public void myMethod() {
 *       final SingletonClass badSingleton = new SingletonClass();
 *
 *       badSingleton.domeSomething();
 *   }
 * }
 * }</pre>
 *
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface SingletonBehaviour {

}
