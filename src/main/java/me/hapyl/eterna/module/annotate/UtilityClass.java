package me.hapyl.eterna.module.annotate;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class is a utility class.
 * <p>Supports a built-in validation utility:</p>
 * <pre>{@code
 *
 * @UtilityClass
 * class MyUtilityClass {
 *     private MyUtilityClass() {
 *         UtilityClass.Validator.throwIt();
 *     }
 * }
 *
 * // Or
 * @UtilityClass
 * class MyUtilityClass {
 *     private MyUtilityClass() {
 *         throw UtilityClass.Validator.getIt();
 *     }
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE })
public @interface UtilityClass {
    
    /**
     * A validator utility class.
     */
    final class Validator {
        
        /**
         * Throws {@link IllegalStateException}.
         */
        public static void throwIt() {
            throw getIt();
        }
        
        /**
         * Gets {@link IllegalStateException}.
         *
         * @return IllegalStateException.
         */
        @NotNull
        public static IllegalStateException getIt() {
            return new IllegalStateException("Do not instantiate utility classes!");
        }
    }
}
