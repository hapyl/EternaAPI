package kz.hapyl.spigotutils.module.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;

public class Validate {
    public static final int DEFAULT_INT = 0;
    public static final long DEFAULT_LONG = 0L;
    public static final double DEFAULT_DOUBLE = 0.0d;
    public static final float DEFAULT_FLOAT = 0.0f;
    public static final short DEFAULT_SHORT = (short) 0;
    public static final byte DEFAULT_BYTE = (byte) 0;
    public static final String DEFAULT_NAME = "unnamed";

    public static int getInt(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        else {
            try {
                return Integer.parseInt(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_INT;
            }
        }
    }

    public static boolean isInt(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return obj instanceof Integer;
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

    public static long getLong(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        else {
            try {
                return Long.parseLong(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_LONG;
            }
        }
    }

    public static boolean isLong(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return obj instanceof Long;
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

    public static double getDouble(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        else {
            try {
                return Double.parseDouble(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_DOUBLE;
            }
        }
    }

    public static boolean isDouble(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return obj instanceof Double;
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

    public static double getShort(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        else {
            try {
                return Short.parseShort(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_SHORT;
            }
        }
    }

    public static boolean isShort(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return obj instanceof Short;
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

    public static double getByte(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        else {
            try {
                return Byte.parseByte(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_BYTE;
            }
        }
    }

    public static boolean isByte(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return obj instanceof Byte;
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

    public static float getFloat(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        else {
            try {
                return Float.parseFloat(getString(obj));
            } catch (NumberFormatException ignored0) {
                return DEFAULT_FLOAT;
            }
        }
    }

    public static boolean isFloat(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof Number) {
            return obj instanceof Float;
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

    public static String getString(Object obj) {
        Validate.notNull(obj);
        if (obj instanceof String) {
            return obj.toString();
        }
        else {
            return String.valueOf(obj);
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static void notNullItemStack(ItemStack stack) {
        notNull(stack, "Null ItemStack will cause TRAP server crashes, use Material.AIR!");
        notNull(stack.getType(), "Null ItemStack will cause TRAP server crashes, use Material.AIR!");
    }

    public static ItemStack requireNotNull(ItemStack stack) {
        if (stack == null) {
            return new ItemStack(Material.AIR);
        }
        return stack;
    }

    public static void notNull(Object obj, String abc) {
        if (obj == null) {
            throw new NullPointerException(abc);
        }
    }

    public static void notNull(Object obj) {
        Validate.notNull(obj, "object must not be null");
    }

    public static void notNull(Object obj, CommandSender sender, String msg) {
        if (obj == null) {
            sender.sendMessage(ChatColor.RED + msg);
        }
    }

    public static <T> T ifNull(T t, T ifNull) {
        return t == null ? ifNull : t;
    }

    public static <T> T requireNotNull(T t) {
        if (t == null) {
            throw new IllegalArgumentException("Argument must not be null!");
        }
        return t;
    }

    public static <T> T nonNull(T t, T def) {
        if (t == null) {
            return def;
        }
        return t;
    }

    public static void isTrue(boolean mustBeTrue, String ifFalse) {
        if (!mustBeTrue) {
            throw new IllegalArgumentException(ifFalse);
        }
    }

    public static void isTrue(boolean mustBeTrue) {
        isTrue(mustBeTrue, mustBeTrue + " must be true");
    }

    public static boolean eitherOf(boolean... booleans) {
        for (final boolean aBoolean : booleans) {
            if (aBoolean) {
                return true;
            }
        }
        return false;
    }

    public static boolean neitherTrue(boolean... booleans) {
        for (final boolean aBoolean : booleans) {
            if (!aBoolean) {
                return false;
            }
        }
        return true;
    }

    public static void isFalse(boolean mustBeFalse, String ifTrue) {
        if (mustBeFalse) {
            throw new IllegalArgumentException(ifTrue);
        }
    }

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

    public static void isFalse(boolean mustBeFalse) {
        isFalse(mustBeFalse, mustBeFalse + " must be false");
    }

    public static <T> T ifTrue(boolean b, T ifTrue, T ifFalse) {
        return b ? ifTrue : ifFalse;
    }

    public static <T> T ifFalse(boolean b, T ifFalse, T ifTrue) {
        return !b ? ifFalse : ifTrue;
    }

    @Nullable
    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, Object obj) {
        return getEnumValue(enumClass, obj, null);
    }

    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, Object obj, @Nullable T def) {
        Validate.notNull(enumClass);
        Validate.notNull(obj);
        try {
            return Enum.valueOf(enumClass, String.valueOf(obj).toUpperCase());
        } catch (IllegalArgumentException ignored0) {
            return def;
        }
    }

    public static ObjectType getType(Object obj) {
        return ObjectType.testSample(obj);
    }

    public static String getItemName(org.bukkit.inventory.ItemStack stack) {
        if (stack.getItemMeta() == null) {
            return DEFAULT_NAME;
        }
        final ItemMeta meta = stack.getItemMeta();
        if (!meta.hasDisplayName()) {
            return DEFAULT_NAME;
        }
        return meta.getDisplayName();
    }

    public static boolean hasItemName(org.bukkit.inventory.ItemStack stack) {
        return !getItemName(stack).equals(DEFAULT_NAME);
    }


}
