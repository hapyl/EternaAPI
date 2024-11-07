package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;

/**
 * A utility annotation to annotate utility classes, with validation options to put in the constructor.
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
public @interface UtilityClass {

    /**
     * A validator methods to put in the constructor.
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
        @Nonnull
        public static IllegalStateException getIt() {
            return new IllegalStateException("Do not instantiate utility classes!");
        }
    }
}
