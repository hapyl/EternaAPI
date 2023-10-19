package me.hapyl.spigotutils.module.util;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.List;

public final class TypeConverter {

    private final Object obj;

    private TypeConverter(@Nonnull Object obj) {
        Validate.notNull(obj, "cannot convert null object");

        this.obj = obj;
    }

    /**
     * @deprecated prefer staticly typed methods
     */
    @SuppressWarnings("all")
    @Deprecated
    @Nonnull
    public <T> T to(@Nonnull Class<T> clazz, @Nonnull T def) {
        if (clazz == String.class) {
            return (T) toString();
        }
        else if (clazz == Byte.class) {
            return (T) Byte.valueOf(toByte());
        }
        else if (clazz == Short.class) {
            return (T) Short.valueOf(toShort());
        }
        else if (clazz == Integer.class) {
            return (T) Integer.valueOf(toInt());
        }
        else if (clazz == Long.class) {
            return (T) Long.valueOf(toLong());
        }

        return def;
    }

    /**
     * @deprecated prefer staticly typed methods
     */
    @SuppressWarnings("all")
    @Deprecated
    @Nullable
    public <T> T to(@Nonnull Class<T> clazz) {
        return to(clazz, null);
    }

    /**
     * Converts this object to a string.
     *
     * @return the string.
     */
    @Nonnull
    public String toString() {
        return String.valueOf(obj);
    }

    /**
     * Converts this object to a byte, or 0 if the object is not a byte.
     *
     * @return the byte.
     */
    public byte toByte() {
        return toByte((byte) 0);
    }

    /**
     * Converts this object to a byte, or def if the object is not a byte.
     *
     * @param def - Default value.
     * @return the byte.
     */
    public byte toByte(byte def) {
        return Validate.isByte(obj) ? Validate.getByte(obj) : def;
    }

    /**
     * Converts this object to a short, or 0 if the object is not a byte.
     *
     * @return the short.
     */
    public short toShort() {
        return toShort((short) 0);
    }

    /**
     * Converts this object to a short, or def if the object is not a byte.
     *
     * @param def - Default value.
     * @return the short.
     */
    public short toShort(short def) {
        return Numbers.getShort(obj, def);
    }

    /**
     * Converts this object to an integer, or 0 if the object is not an integer.
     *
     * @return the integer.
     */
    public int toInt() {
        return toInt(0);
    }

    /**
     * Converts this object to an integer, or def if the object is not an integer.
     *
     * @param def - Default value.
     * @return the integer.
     */
    public int toInt(int def) {
        return Numbers.getInt(obj, def);
    }

    /**
     * Converts this object to a long, or 0L if the object is not a long.
     *
     * @return the long.
     */
    public long toLong() {
        return toLong(0);
    }

    /**
     * Converts this object to a long, or def if the object is not a long.
     *
     * @param def - Default value.
     * @return the long.
     */
    public long toLong(long def) {
        return Numbers.getLong(obj, def);
    }

    /**
     * Converts this object to a double, or 0 if the object is not a double.
     *
     * @return the double.
     */
    public double toDouble() {
        return toDouble(0);
    }

    /**
     * Converts this object to a double, or def if the object is not a double.
     *
     * @param def - Default value.
     * @return the double.
     */
    public double toDouble(double def) {
        return Numbers.getDouble(obj, def);
    }

    /**
     * Converts this object to a float, or def if the object is not a float.
     *
     * @return the float.
     */
    public float toFloat() {
        return toFloat(0);
    }

    /**
     * Converts this object to a float, or def if the object is not a float.
     *
     * @param def - Default value.
     * @return the float.
     */
    public float toFloat(float def) {
        return Numbers.getFloat(obj, def);
    }

    /**
     * Converts this object to a boolean.
     *
     * @return the double.
     */
    public boolean toBoolean() {
        return Boolean.getBoolean(String.valueOf(obj));
    }

    /**
     * Converts this object to an enum constant, or null if the enum doesn't have a constant by the given name.
     *
     * @param clazz - Enum class.
     * @param <T>   - Enum type.
     * @return the enum constant.
     */
    @Nullable
    public <T extends Enum<T>> T toEnum(@Nonnull Class<T> clazz) {
        return toEnum(clazz, null);
    }

    /**
     * Converts this object to an enum constant, or def if the enum doesn't have a constant by the given name.
     *
     * @param clazz - Enum class.
     * @param def   - Default value.
     * @param <T>   - Enum type.
     * @return the enum constant.
     */
    @Nonnull(when = When.MAYBE)
    public <T extends Enum<T>> T toEnum(@Nonnull Class<T> clazz, T def) {
        return Validate.getEnumValue(clazz, obj, def);
    }

    /**
     * Converts this object to an online player, or null if the player is not online.
     *
     * @return the player.
     */
    @Nullable
    public Player toPlayer() {
        return Bukkit.getPlayer(toString());
    }

    /**
     * Converts this object to an online player, or def if the player is not online.
     *
     * @param def - Default value.
     * @return the player.
     */
    @Nonnull
    public Player toPlayer(@Nonnull Player def) {
        final Player player = toPlayer();

        return player == null ? def : player;
    }

    /**
     * Converts this object to an instance of {@link ConvApplicable}.
     *
     * @param conv - {@link ConvApplicable}.
     * @param <T>  - Type of the {@link ConvApplicable}.
     * @return the object.
     */
    public <T extends ConvApplicable> ConvApplicable toConvApplicable(@Nonnull T conv) {
        return conv.convert(obj);
    }

    /**
     * Creates a new instance of {@link TypeConverter}.
     *
     * @param any - Object to convert.
     * @param def - Default value if object is a null.
     * @param <T> - Type of the object.
     * @return a new instance of {@link TypeConverter}.
     */
    public static <T> TypeConverter from(@Nullable Object any, T def) {
        if (any == null) {
            return new TypeConverter(def);
        }

        return new TypeConverter(any);
    }

    /**
     * Creates a new instance of {@link TypeConverter}.
     *
     * @param any - Object to convert.
     * @return a new instance of {@link TypeConverter}.
     */
    public static TypeConverter from(@Nonnull Object any) {
        return new TypeConverter(any);
    }

    public interface ConvApplicable {
        ConvApplicable convert(Object obj);
    }

}
