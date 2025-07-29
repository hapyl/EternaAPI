package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Objects;
import java.util.UUID;

public class TypeConverter {
    
    private final Object obj;
    
    public TypeConverter(@Nonnull Object obj) {
        this.obj = Objects.requireNonNull(obj, "Convertible object cannot be null!");
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
        return Numbers.getByte(obj, def);
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
        return "true".equalsIgnoreCase(obj.toString());
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
        return Enums.byName(clazz, obj.toString(), def);
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
     * Converts this object to a {@link UUID}, or null if invalid {@link UUID}.
     *
     * @return a UUID or null.
     */
    @Nullable
    public UUID toUUID() {
        try {
            return UUID.fromString(toString());
        }
        catch (IllegalArgumentException ignored0) {
            return null;
        }
    }
    
    /**
     * Converts this object to a {@link UUID}, or def if invalid {@link UUID}.
     *
     * @param def - Default value.
     * @return UUID or def.
     */
    @Nonnull
    public UUID toUUID(@Nonnull UUID def) {
        final UUID uuid = toUUID();
        
        return uuid != null ? uuid : def;
    }
    
    /**
     * Converts this object into a formatted {@link String} with the given format.
     * <pre>
     *     final TypeConverter converter = TypeConverter.from(12.34);
     *
     *     converter.toStringFormatted("%.1f"); // 12.3
     * </pre>
     *
     * @param format - Format.
     * @return the formatted string.
     */
    @Nonnull
    public String toStringFormatted(@Nonnull String format) {
        return format.formatted(obj);
    }
    
    /**
     * Converts this object into a {@link Key} and uses it to access the given {@link Registry} to retrieve an item.
     *
     * @param registry - Registry to access.
     * @return the registered item or {@code null} if not registered or malformed {@link Key}.
     */
    @Nullable
    public <K extends Keyed> K toRegistryItem(@Nonnull Registry<K> registry) {
        final Key key = Key.ofStringOrNull(toString());
        
        return key != null ? registry.get(key) : null;
    }
    
    /**
     * Converts this object into a {@link Key}.
     *
     * @return the key, or {@code null} if key is malformed.
     */
    @Nullable
    public Key toKey() {
        return Key.ofStringOrNull(toString());
    }
    
    /**
     * Converts this object into a custom {@link TypeConvertible}.
     *
     * @param convertible - The convertible.
     * @param <T>         - The convertible type.
     * @return an {@code T}, or {@code null}.
     */
    @Nullable
    public <T> T toConvertible(@Nonnull TypeConvertible<T> convertible) {
        return convertible.convert(obj);
    }
    
    /**
     * Creates a new instance of {@link TypeConverter}.
     *
     * @param any - Object to convert.
     * @param def - Default value if object is a null.
     * @param <T> - Type of the object.
     * @return a new instance of {@link TypeConverter}.
     */
    @Nonnull
    public static <T> TypeConverter from(@Nullable Object any, T def) {
        return any != null ? new TypeConverter(any) : new TypeConverter(def);
    }
    
    /**
     * Creates a new instance of {@link TypeConverter}.
     *
     * @param any - Object to convert.
     * @return a new instance of {@link TypeConverter}.
     */
    @Nonnull
    public static TypeConverter from(@Nonnull Object any) {
        return new TypeConverter(any);
    }
    
    /**
     * Creates a new instance of {@link TypeConverter} from the given nullable object.
     * If the object is {@code null}, an empty instance is created, a real instance is created otherwise.
     *
     * @param object - The object.
     * @return a new instance of {@link TypeConverter} from the given nullable object.
     */
    @Nonnull
    public static TypeConverter fromNullable(@Nullable Object object) {
        return object != null ? new TypeConverter(object) : empty();
    }
    
    /**
     * Creates a new empty instance of {@link TypeConverter}.
     * <p>This is completely useless on its own and only used in {@link #fromNullable(Object)}</p>
     *
     * @return a new empty instance of {@link TypeConverter}.
     */
    @Nonnull
    @ApiStatus.Internal
    public static TypeConverter empty() {
        return new TypeConverter("");
    }
    
}
