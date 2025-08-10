package me.hapyl.eterna.module.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Validator utility class.
 * <br>
 * This utility class throws error if the validation is failed (unless otherwise stated).
 */
public final class Validate {

    private Validate() {
    }

    /**
     * Gets a {@link String} representation of the given {@link Object}.
     * <br>
     * This method is different to {@link String#valueOf(Object)} since it returns an empty string, rather than {@code "null"}
     */
    @Nonnull
    public static String getString(@Nullable Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }

    /**
     * Validates that the given {@link ItemStack} is not null.
     * <br>
     * Null {@link ItemStack} causes server crashes, or at least they used to.
     *
     * @param itemStack - {@link ItemStack}.
     */
    @Deprecated
    public static void notNullItemStack(@Nullable ItemStack itemStack) {
        notNull(itemStack, "Null ItemStack will cause TRAP server crashes, use Material.AIR!");
        notNull(itemStack.getType(), "Null ItemStack will cause TRAP server crashes, use Material.AIR!");
    }

    /**
     * Returns either the given {@link ItemStack} if it's not null, an empty {@link ItemStack} otherwise.
     *
     * @param itemStack - Item stack to check.
     * @return either the give {@link ItemStack} if it's not null, an empty {@link ItemStack} otherwise.
     */
    @Nonnull
    public static ItemStack requireNotNull(@Nullable ItemStack itemStack) {
        return itemStack != null ? itemStack : new ItemStack(Material.AIR);
    }

    /**
     * Returns true if the given {@link Object} is null, false otherwise.
     *
     * @param object - Object to check.
     * @return true if the given {@link Object} is null, false otherwise.
     */
    public static boolean isNull(@Nullable Object object) {
        return object == null;
    }

    /**
     * Validates that the given {@link Object} is not null, throws {@link NullPointerException} otherwise.
     *
     * @param object  - Object to check.
     * @param message - {@link NullPointerException} message.
     */
    public static void notNull(@Nullable Object object, @Nonnull String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    /**
     * Validates that the given {@link Object} is not null, throws {@link NullPointerException} otherwise.
     *
     * @param object - Object to check.
     */
    public static void notNull(@Nullable Object object) {
        notNull(object, "Object must not be null!");
    }

    /**
     * Returns either the given {@link Object} if it's not null, {@code def} otherwise.
     *
     * @param value - Value to check.
     * @param def   - Default value.
     * @return either the given {@link Object} is it's not null, {@code def} otherwise.
     */
    @Nonnull
    public static <T> T nonNull(@Nullable T value, @Nonnull T def) {
        return value != null ? value : def;
    }

    /**
     * Validates that the given boolean expression is {@code true},
     * throws {@link IllegalArgumentException} with the given message otherwise.
     *
     * @param expression - Expression to check.
     * @param message    - {@link IllegalArgumentException} message.
     */
    public static void isTrue(boolean expression, @Nonnull String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates that the given condition is true for the provided object.
     * If the condition is not met, an {@link IllegalArgumentException} is thrown with the specified message.
     *
     * @param e       - The object to validate.
     * @param fn      - The function to test the object against.
     * @param message - The exception message if the validation fails.
     * @return the validated object if the condition is true.
     * @throws IllegalArgumentException if the condition is false.
     */
    @Nonnull
    public static <E> E isTrue(@Nonnull E e, @Nonnull Function<E, Boolean> fn, @Nonnull String message) {
        if (!fn.apply(e)) {
            throw new IllegalArgumentException(message);
        }

        return e;
    }

    /**
     * Validates that the given boolean expression is {@code true},
     * throws {@link IllegalArgumentException} with the given message otherwise.
     *
     * @param expression - Expression to check.
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, "The expression must be true!");
    }

    /**
     * Validates that the given boolean expression is {@code false},
     * throws  {@link IllegalArgumentException} with the given message otherwise.
     *
     * @param expression - Expression to check.
     * @param message    - {@link IllegalArgumentException} message.
     */
    public static void isFalse(boolean expression, @Nonnull String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates that the given boolean expression is {@code false},
     * throws  {@link IllegalArgumentException} with the given message otherwise.
     *
     * @param expression - Expression to check.
     */
    public static void isFalse(boolean expression) {
        isFalse(expression, "The expression must be false!");
    }

    /**
     * Returns one of two values based on the evaluation of the given boolean expression.
     *
     * @param expression - The boolean expression to evaluate.
     * @param ifTrue     - The value to return if the expression is {@code true}.
     * @param ifFalse    - The value to return if the expression is {@code false}.
     * @return {@code ifTrue} if the expression is {@code true}, otherwise {@code ifFalse}.
     */
    public static <T> T ifTrue(boolean expression, @Nonnull T ifTrue, @Nonnull T ifFalse) {
        return expression ? ifTrue : ifFalse;
    }


    /**
     * Returns one of two values based on the negation of the given boolean expression.
     *
     * @param expression - The boolean expression to evaluate.
     * @param ifFalse    - The value to return if the expression is {@code false}.
     * @param ifTrue     - The value to return if the expression is {@code true}.
     * @return {@code ifFalse} if the expression is {@code false}, otherwise {@code ifTrue}.
     */
    public static <T> T ifFalse(boolean expression, @Nonnull T ifFalse, @Nonnull T ifTrue) {
        return !expression ? ifFalse : ifTrue;
    }

    /**
     * Returns true if at least one of the given booleans if {@code true}, {@code false} otherwise.
     *
     * @param booleans - Booleans to check.
     * @return true if at least one of the given booleans if {@code true}, {@code false} otherwise.
     */
    public static boolean eitherOf(@Nonnull boolean... booleans) {
        for (final boolean aBoolean : booleans) {
            if (aBoolean) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if all the given booleans are {@code false}, {@code false} otherwise.
     *
     * @param booleans - Booleans to check.
     * @return {@code true} if all the given booleans are {@code false}, {@code false} otherwise.
     */
    public static boolean neitherTrue(@Nonnull boolean... booleans) {
        for (final boolean aBoolean : booleans) {
            if (aBoolean) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the given {@link String} contains one of the given {@link Object}.
     * <br>
     * The objects are converted to a string using {@link String#valueOf(Object)} before comparing it.
     *
     * @param string  - String to check.
     * @param objects - Objects to check.
     * @return true if the given {@link String} contains one of the given {@link Object}.
     */
    public static boolean stringContainsOneOf(@Nonnull String string, @Nonnull Object... objects) {
        if (objects.length == 0) {
            return false;
        }
        for (final Object object : objects) {
            if (string.contains(String.valueOf(object))) {
                return true;
            }
        }
        return true;
    }

    /**
     * Returns true if the given {@link String} contains all the given {@link Object}.
     * <br>
     * The objects are converted to a string using {@link String#valueOf(Object)} before comparing it.
     *
     * @param string  - String to check.
     * @param objects - Objects to check.
     * @return true if the given {@link String} contains all the given {@link Object}.
     */
    public static boolean stringContainsAll(@Nonnull String string, @Nonnull Object... objects) {
        if (objects.length == 0) {
            return false;
        }
        for (final Object object : objects) {
            if (!string.contains(String.valueOf(object))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the given {@link ItemStack} display name, or an empty string is it doesn't have a name.
     *
     * @param itemStack - {@link ItemStack} to get the display name for.
     * @return the given {@link ItemStack} display name, or an empty string is it doesn't have a name.
     */
    @Nonnull
    public static String getItemName(@Nonnull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return "";
        }

        final ItemMeta meta = itemStack.getItemMeta();

        if (!meta.hasDisplayName()) {
            return "";
        }

        return meta.getDisplayName();
    }

    /**
     * Returns {@code true} if the given {@link ItemStack} has a display name, {@code false} otherwise.
     *
     * @param itemStack - {@link ItemStack} to check.
     * @return {@code true} if the given {@link ItemStack} has a display name, {@code false} otherwise.
     */
    public static boolean hasItemName(@Nonnull ItemStack itemStack) {
        return !getItemName(itemStack).isEmpty();
    }

    /**
     * Checks that the given array length matches the given length.
     *
     * @param array  - Array to check.
     * @param length - Length.
     */
    public static void checkLength(@Nonnull Object[] array, int length) {
        isTrue(array.length == length, "Array length must be %s, not %s".formatted(length, array.length));
    }

    /**
     * Checks that the given array length is between min (exclusive) and max (exclusive).
     *
     * @param array - Array to check.
     * @param min   - Minimum length. (exclusive)
     * @param max   - Maximum length. (exclusive)
     */
    public static void checkLength(@Nonnull Object[] array, int min, int max) {
        notNull(array, "Array cannot be null!");
        isTrue(array.length < min, "array.length < %s!".formatted(min));
        isTrue(array.length > max, "array.length > %s!".formatted(max));
    }

    /**
     * Checks if the given array contains an item at the given item.
     * <br>
     * This method returns {@code false} if the index is out of bounds.
     *
     * @param array - Array to check.
     * @param index - Index to check the item on.
     * @param item  - Item to check.
     * @return true if the given array contains the item on the given index.
     */
    public static <T> boolean checkArray(@Nonnull T[] array, int index, @Nonnull T item) {
        return array.length != 0 && index < array.length && array[index] != null && array[index].equals(item);
    }

}
