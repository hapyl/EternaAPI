package me.hapyl.spigotutils.module.util;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Validator or checker class.
 */
public class Validate {

    public static final int DEFAULT_INT = 0;
    public static final long DEFAULT_LONG = 0L;
    public static final double DEFAULT_DOUBLE = 0.0d;
    public static final float DEFAULT_FLOAT = 0.0f;
    public static final short DEFAULT_SHORT = (short) 0;
    public static final byte DEFAULT_BYTE = (byte) 0;
    public static final String DEFAULT_STRING = "";

    /**
     * Returns integer value of the object or {@link Validate#DEFAULT_INT}.
     *
     * @param obj - Object to convert.
     * @return integer value of the object or {@link Validate#DEFAULT_INT}.
     */
    public static int getInt(@Nullable Object obj) {
        if (obj == null) {
            return DEFAULT_INT;
        }
        else if (obj instanceof Number number) {
            return number.intValue();
        }
        else {
            try {
                return Integer.parseInt(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_INT;
            }
        }
    }

    /**
     * Returns true if object is an integer, false otherwise.
     *
     * @param obj - Object to check.
     * @return true if object is an integer, false otherwise.
     */
    public static boolean isInt(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj instanceof Integer) {
            return true;
        }
        else {
            try {
                Integer.parseInt(getString(obj));
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns long value of the object or {@link Validate#DEFAULT_LONG}.
     *
     * @param obj - Object to convert.
     * @return long value of the object or {@link Validate#DEFAULT_LONG}.
     */
    public static long getLong(@Nullable Object obj) {
        if (obj == null) {
            return DEFAULT_LONG;
        }
        else if (obj instanceof Number number) {
            return number.longValue();
        }
        else {
            try {
                return Long.parseLong(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_LONG;
            }
        }
    }

    /**
     * Returns true if object is long, false otherwise.
     *
     * @param obj - Object to check.
     * @return true if object is long, false otherwise.
     */
    public static boolean isLong(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj instanceof Long) {
            return true;
        }
        else {
            try {
                Long.parseLong(getString(obj));
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns double value of the object or {@link Validate#DEFAULT_DOUBLE}.
     *
     * @param obj - Object to convert.
     * @return double value of the object or {@link Validate#DEFAULT_DOUBLE}.
     */
    public static double getDouble(@Nullable Object obj) {
        if (obj == null) {
            return DEFAULT_DOUBLE;
        }
        else if (obj instanceof Number number) {
            return number.doubleValue();
        }
        else {
            try {
                return Double.parseDouble(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_DOUBLE;
            }
        }
    }

    /**
     * Returns true if object is double, false otherwise.
     *
     * @param obj - Object to check.
     * @return true if object is double, false otherwise.
     */
    public static boolean isDouble(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj instanceof Double) {
            return true;
        }
        else {
            try {
                Double.parseDouble(getString(obj));
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns short value of the object or {@link Validate#DEFAULT_SHORT}.
     *
     * @param obj - Object to convert.
     * @return short value of the object or {@link Validate#DEFAULT_SHORT}.
     */
    public static short getShort(@Nullable Object obj) {
        if (obj == null) {
            return DEFAULT_SHORT;
        }
        if (obj instanceof Number number) {
            return number.shortValue();
        }
        else {
            try {
                return Short.parseShort(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_SHORT;
            }
        }
    }

    /**
     * Returns true if object is short, false otherwise.
     *
     * @param obj - Object to check.
     * @return true if object is short, false otherwise.
     */
    public static boolean isShort(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj instanceof Short) {
            return true;
        }
        else {
            try {
                Short.parseShort(getString(obj));
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns byte value of the object or {@link Validate#DEFAULT_BYTE}.
     *
     * @param obj - Object to convert.
     * @return byte of the object or {@link Validate#DEFAULT_BYTE}.
     */
    public static byte getByte(@Nullable Object obj) {
        if (obj == null) {
            return DEFAULT_BYTE;
        }
        else if (obj instanceof Number number) {
            return number.byteValue();
        }
        else {
            try {
                return Byte.parseByte(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_BYTE;
            }
        }
    }

    /**
     * Returns true if object is byte, false otherwise.
     *
     * @param obj - Object to check.
     * @return true if object is byte, false otherwise.
     */
    public static boolean isByte(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj instanceof Byte) {
            return true;
        }
        else {
            try {
                Byte.parseByte(getString(obj));
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns float value of the object or {@link Validate#DEFAULT_FLOAT}.
     *
     * @param obj - Object to convert.
     * @return float value of the object or {@link Validate#DEFAULT_FLOAT}.
     */
    public static float getFloat(@Nullable Object obj) {
        if (obj == null) {
            return DEFAULT_FLOAT;
        }
        else if (obj instanceof Number number) {
            return number.floatValue();
        }
        else {
            try {
                return Float.parseFloat(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_FLOAT;
            }
        }
    }

    /**
     * Returns true if object is float, false otherwise.
     *
     * @param obj - Object to check.
     * @return true if object is float, false otherwise.
     */
    public static boolean isFloat(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj instanceof Float) {
            return true;
        }
        else {
            try {
                Float.parseFloat(getString(obj));
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns string value of the object or {@link Validate#DEFAULT_STRING} if object is null.
     *
     * @param obj - Object to check.
     * @return string value of the object or {@link Validate#DEFAULT_STRING} if object is null.
     */
    @Nonnull
    public static String getString(@Nullable Object obj) {
        if (obj == null) {
            return DEFAULT_STRING;
        }
        else if (obj instanceof String) {
            return obj.toString();
        }
        else {
            return String.valueOf(obj);
        }
    }

    /**
     * Throws NullPointerException if ItemStack or Material is null.
     *
     * @param stack - ItemStack.
     * @throws NullPointerException if ItemStack or Material is null.
     */
    public static void notNullItemStack(@Nullable ItemStack stack) {
        notNull(stack, "Null ItemStack will cause TRAP server crashes, use Material.AIR!");
        notNull(stack.getType(), "Null ItemStack will cause TRAP server crashes, use Material.AIR!");
    }

    /**
     * Returns ItemStack(AIR) if stack is null, stack otherwise.
     *
     * @param stack - ItemStack.
     * @return ItemStack(AIR) if stack is null, stack otherwise.
     */
    public static ItemStack requireNotNull(@Nullable ItemStack stack) {
        if (stack == null) {
            return new ItemStack(Material.AIR);
        }
        return stack;
    }

    /**
     * Returns true if object is null, false otherwise.
     *
     * @param obj - Object to check.
     * @return true if object is null, false otherwise.
     */
    public static boolean isNull(@Nullable Object obj) {
        return obj == null;
    }

    /**
     * Validates obj to not be null.
     *
     * @param obj - Object to check.
     * @param abc - Message of the exception.
     * @throws NullPointerException if obj is null.
     */
    public static void notNull(@Nullable Object obj, @Nonnull String abc) {
        if (obj == null) {
            throw new NullPointerException(abc);
        }
    }

    /**
     * Validates obj to not be null.
     *
     * @param obj - Object to check.
     * @throws NullPointerException if obj is null.
     */
    public static void notNull(@Nullable Object obj) {
        notNull(obj, "object must not be null");
    }

    /**
     * Validates obj to not be null.
     *
     * @param obj    - Object to check.
     * @param sender - Receiver of the message.
     * @param msg    - Message to send to receiver.
     */
    public static void notNull(Object obj, CommandSender sender, String msg) {
        if (obj == null) {
            Chat.sendMessage(sender, "&4ERROR. &c" + msg);
        }
    }

    /**
     * Returns default parameter if value is null.
     *
     * @param value  - Object to check.
     * @param ifNull - Default value.
     * @return default parameter if value is null.
     */
    public static <T> T ifNull(T value, T ifNull) {
        return value == null ? ifNull : value;
    }

    /**
     * Throws IllegalArgumentException if value is null, returns value otherwise.
     *
     * @param value - Value.
     * @return value is it is not null.
     * @throws IllegalArgumentException if value is null, returns value otherwise.
     */
    public static <T> T requireNotNull(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Argument must not be null!");
        }
        return value;
    }

    /**
     * Returns default parameter if value is null.
     *
     * @param value  - Object to check.
     * @param ifNull - Default value.
     * @return default parameter if value is null.
     */
    public static <T> T nonNull(T value, T ifNull) {
        if (value == null) {
            return ifNull;
        }
        return value;
    }

    /**
     * Validate that expression if true.
     *
     * @param expression - Expression to check.
     * @param message    - Message of the exception if expression is false.
     * @throws IllegalArgumentException if expression is false.
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validate that expression if true.
     *
     * @param expression - Expression to check.
     * @throws IllegalArgumentException if expression is false.
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, expression + " must be true");
    }

    /**
     * Validates that is at least one of the expressions is true.
     *
     * @param booleans - Array of expressions.
     * @return true if at least one of the expressions is true.
     */
    public static boolean eitherOf(boolean... booleans) {
        for (final boolean aBoolean : booleans) {
            if (aBoolean) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate that all expressions are false.
     *
     * @param booleans - Array of expressions.
     * @return true if all expressions are false.
     */
    public static boolean neitherTrue(boolean... booleans) {
        for (final boolean aBoolean : booleans) {
            if (aBoolean) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validate that expression is false.
     *
     * @param expression - Expression to check.
     * @param message    - Message of the exception if expression is true.
     * @throws IllegalArgumentException if expression is true.
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates that string contains at least one of the objects.
     *
     * @param str     - String.
     * @param objects - Objects.
     * @return true is string contains at least one of the objects, false otherwise.
     */
    public static boolean stringContainsOneOf(String str, Object... objects) {
        if (objects.length == 0) {
            return false;
        }
        for (final Object object : objects) {
            if (str.contains(String.valueOf(object))) {
                return true;
            }
        }
        return true;
    }

    /**
     * Validate that string contains all the objects.
     *
     * @param str     - String.
     * @param objects - Objects.
     * @return true if string contains all the objects, false otherwise.
     */
    public static boolean stringContainsAll(String str, Object... objects) {
        if (objects.length == 0) {
            return false;
        }
        for (final Object object : objects) {
            if (!str.contains(String.valueOf(object))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates that expression is false.
     *
     * @param expression - Expression to check.
     */
    public static void isFalse(boolean expression) {
        isFalse(expression, expression + " must be false");
    }

    /**
     * Returns either of values based on expression.
     *
     * @param expression - Expressions to check.
     * @param ifTrue     - Value to return if expression is true.
     * @param ifFalse    - Value to return if expression is false.
     * @return either of values based on expression.
     */
    public static <T> T ifTrue(boolean expression, T ifTrue, T ifFalse) {
        return expression ? ifTrue : ifFalse;
    }

    /**
     * Returns either of values based on expression.
     *
     * @param expression - Expressions to check.
     * @param ifFalse    - Value to return if expression is false.
     * @param ifTrue     - Value to return if expression is true.
     * @return either of values based on expression.
     */
    public static <T> T ifFalse(boolean expression, T ifFalse, T ifTrue) {
        return !expression ? ifFalse : ifTrue;
    }

    /**
     * Returns enum if exists, null otherwise.
     *
     * @param enumClass - Class of the enum.
     * @param name      - Name of the enum.
     * @return enum if exists, null otherwise.
     */
    @Nullable
    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, Object name) {
        return getEnumValue(enumClass, name, null);
    }

    /**
     * Returns enum if exists, def otherwise.
     *
     * @param enumClass - Class of the enum.
     * @param name      - Name of the enum.
     * @param def       - Default value, if enum does not exist.
     * @return enum if exists, def otherwise.
     */
    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, Object name, @Nullable T def) {
        Validate.notNull(enumClass);
        Validate.notNull(name);
        try {
            return Enum.valueOf(enumClass, String.valueOf(name).toUpperCase());
        } catch (IllegalArgumentException ignored0) {
            return def;
        }
    }

    /**
     * Returns {@link ObjectType} of the object.
     *
     * @param obj - Object.
     * @return {@link ObjectType} of the object.
     */
    public static ObjectType getType(Object obj) {
        return ObjectType.testSample(obj);
    }

    /**
     * Returns ItemStack name if exists, {@link Validate#DEFAULT_STRING} otherwise.
     *
     * @param stack - ItemStack.
     * @return ItemStack name if exists, {@link Validate#DEFAULT_STRING} otherwise.
     */
    @Nonnull
    public static String getItemName(ItemStack stack) {
        if (stack.getItemMeta() == null) {
            return DEFAULT_STRING;
        }
        final ItemMeta meta = stack.getItemMeta();
        if (!meta.hasDisplayName()) {
            return DEFAULT_STRING;
        }
        return meta.getDisplayName();
    }

    /**
     * Returns true if ItemStack has custom name, false otherwise.
     *
     * @param stack - ItemStack.
     * @return true if ItemStack has custom name, false otherwise.
     */
    public static boolean hasItemName(org.bukkit.inventory.ItemStack stack) {
        return !getItemName(stack).equals(DEFAULT_STRING);
    }

    /**
     * Validates that array has minimum length.
     *
     * @param array  - Array.
     * @param length - Minimum length of the array.
     * @throws NullPointerException           if array is null.
     * @throws ArrayIndexOutOfBoundsException if array length < length.
     */
    public static void checkLength(Object[] array, int length) {
        checkLength(array, length, array == null ? 0 : array.length);
    }

    /**
     * Validates that array length is between provided arguments.
     *
     * @param array - Array.
     * @param min   - Minimum length of the array.
     * @param max   - Maximum length of the array.
     * @throws NullPointerException           if array is null.
     * @throws ArrayIndexOutOfBoundsException if array length < min.
     * @throws ArrayIndexOutOfBoundsException if array length > max.
     */
    public static void checkLength(Object[] array, int min, int max) {
        notNull(array, "array cannot be null");
        isTrue(array.length < min, "array.length < %s!".formatted(min));
        isTrue(array.length > max, "array.length > %s!".formatted(max));
    }

    /**
     * Validates that array has item on provided index.
     *
     * @param array - Array.
     * @param index - Index.
     * @param item  - Item to check.
     * @return true if array has item on provided slot.
     */
    public static <T> boolean checkArray(T[] array, int index, T item) {
        return array.length != 0 && index <= array.length && array[index] != null && array[index].equals(item);
    }

    public static boolean checkArrayString(String[] array, int index, String value) {
        return array.length != 0 && index <= array.length && array[index] != null && array[index].equalsIgnoreCase(value);
    }

}
