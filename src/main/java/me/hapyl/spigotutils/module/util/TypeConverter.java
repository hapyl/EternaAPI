package me.hapyl.spigotutils.module.util;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    @Nonnull
    public String toString() {
        return String.valueOf(obj);
    }

    public byte toByte() {
        return toByte((byte) 0);
    }

    public byte toByte(byte def) {
        return Validate.isByte(obj) ? Validate.getByte(obj) : def;
    }

    public short toShort() {
        return toShort((short) 0);
    }

    public short toShort(short def) {
        return Validate.isShort(obj) ? Validate.getShort(obj) : def;
    }

    public int toInt() {
        return toInt(0);
    }

    public int toInt(int def) {
        return Numbers.getInt(obj, def);
    }

    public long toLong() {
        return toLong(0);
    }

    public long toLong(long def) {
        return Numbers.getLong(obj, def);
    }

    public double toDouble() {
        return toDouble(0);
    }

    public double toDouble(double def) {
        return Numbers.getDouble(obj, def);
    }

    public float toFloat() {
        return toFloat(0);
    }

    public float toFloat(float def) {
        return Numbers.getFloat(obj, def);
    }

    @Nonnull
    public ItemStack toItemStack() {
        if (obj instanceof ItemStack itemStack) {
            return itemStack;
        }

        Material material = obj instanceof Material mat ? mat : Material.BEDROCK;
        String name = obj instanceof String str ? str : "";
        List<String> lore = Lists.newArrayList();

        if (obj instanceof List<?> list) {
            list.forEach(k -> {
                lore.add(String.valueOf(k));
            });
        }

        return new ItemBuilder(material).setName(name).setLore(lore).build();
    }

    public <T extends ConvApplicable> ConvApplicable toConvApplicable(T t) {
        return t.convert(obj);
    }

    public static <T> TypeConverter from(@Nullable Object any, T def) {
        if (any == null) {
            return new TypeConverter(def);
        }

        return new TypeConverter(any);
    }

    public static TypeConverter from(@Nonnull Object any) {
        return new TypeConverter(any);
    }

    public interface ConvApplicable {

        ConvApplicable convert(Object obj);

    }

}
