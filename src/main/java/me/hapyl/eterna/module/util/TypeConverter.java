package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Represents a helper class that allows converts a raw {@link Object} into a type.
 */
public class TypeConverter {
    
    private final Object object;
    
    /**
     * Creates a new {@link TypeConverter}.
     *
     * @param object - The object to convert.
     * @throws NullPointerException if the given object is {@code null}.
     */
    public TypeConverter(@NotNull Object object) {
        this.object = Objects.requireNonNull(object, "Convertible object cannot be null!");
    }
    
    /**
     * Converts the object to {@link String}.
     *
     * @return the object as {@code string}.
     */
    @NotNull
    public String toString() {
        return String.valueOf(object);
    }
    
    /**
     * Converts the object to {@link Byte}.
     *
     * @return the object as a {@code byte}, or {@code 0} if object is not a {@code byte}.
     */
    public byte toByte() {
        return toByte((byte) 0);
    }
    
    /**
     * Converts the object to {@link Byte}.
     *
     * @param defaultValue - The default value.
     * @return the object as a {@code byte}, or {@code 0} if object is not a {@code byte}.
     */
    public byte toByte(byte defaultValue) {
        return Numbers.toByte(object, defaultValue);
    }
    
    /**
     * Converts the object to {@link Short}.
     *
     * @return the object as a {@code short}, or {@code 0} if object is not a {@code short}.
     */
    public short toShort() {
        return toShort((short) 0);
    }
    
    /**
     * Converts the object to {@link Short}.
     *
     * @param defaultValue - The default value.
     * @return the object as a {@code short}, or {@code defaultValue} if object is not a {@code short}.
     */
    public short toShort(short defaultValue) {
        return Numbers.toShort(object, defaultValue);
    }
    
    /**
     * Converts the object to {@link Integer}.
     *
     * @return the object as an {@code int}, or {@code 0} if object is not an {@code int}.
     */
    public int toInt() {
        return toInt(0);
    }
    
    /**
     * Converts the object to {@link Integer}.
     *
     * @param defaultValue - The default value.
     * @return the object as an {@code int}, or {@code defaultValue} if object is not a {@code int}.
     */
    public int toInt(int defaultValue) {
        return Numbers.toInt(object, defaultValue);
    }
    
    /**
     * Converts the object to {@link Long}.
     *
     * @return the object as a {@code long}, or {@code 0} if object is not a {@code long}.
     */
    public long toLong() {
        return toLong(0);
    }
    
    /**
     * Converts the object to {@link Long}.
     *
     * @param defaultValue - The default value.
     * @return the object as a {@code long}, or {@code defaultValue} if object is not a {@code long}.
     */
    public long toLong(long defaultValue) {
        return Numbers.toLong(object, defaultValue);
    }
    
    /**
     * Converts the object to {@link Float}.
     *
     * @return the object as a {@code float}, or {@code 0} if object is not a {@code float}.
     */
    public float toFloat() {
        return toFloat(0);
    }
    
    /**
     * Converts the object to {@link Float}.
     *
     * @param defaultValue - The default value.
     * @return the object as a {@code float}, or {@code defaultValue} if object is not a {@code float}.
     */
    public float toFloat(float defaultValue) {
        return Numbers.toFloat(object, defaultValue);
    }
    
    /**
     * Converts the object to {@link Double}.
     *
     * @return the object as a {@code double}, or {@code 0} if object is not a {@code double}.
     */
    public double toDouble() {
        return toDouble(0);
    }
    
    /**
     * Converts the object to {@link Double}.
     *
     * @param defaultValue - The default value.
     * @return the object as a {@code double}, or {@code defaultValue} if object is not a {@code double}.
     */
    public double toDouble(double defaultValue) {
        return Numbers.toDouble(object, defaultValue);
    }
    
    /**
     * Converts the object to {@link Boolean}.
     *
     * @return the object as a {@code boolean}; {@code false} otherwise.
     */
    public boolean toBoolean() {
        return "true".equalsIgnoreCase(object.toString());
    }
    
    /**
     * Converts the object to the given {@link Enum} constant.
     * <p>This method assumes the enum constants are following the Java's namings conversions, and are in {@code SCREAMING_CASE}.</p>
     *
     * @param clazz - The enum class.
     * @param <T>   - The enum type.
     * @return the object as the {@code enum} constant, or {@code null} if there is no constant by that exact name.
     */
    @Nullable
    public <T extends Enum<T>> T toEnum(@NotNull Class<T> clazz) {
        return Enums.byName(clazz, object.toString());
    }
    
    /**
     * Converts the object to the given {@link Enum} constant.
     *
     * <p>
     * This method assumes the enum constants are following the Java's namings conversions, and are in {@code SCREAMING_CASE}.
     * </p>
     *
     * @param clazz        - The enum class.
     * @param defaultValue - The default value if there is no constant.
     * @param <T>          - The enum type.
     * @return the object as the {@code enum} constant, or {@code null} if there is no constant by that exact name.
     */
    @NotNull
    public <T extends Enum<T>> T toEnum(@NotNull Class<T> clazz, @NotNull T defaultValue) {
        final T enumValue = toEnum(clazz);
        
        return enumValue != null ? enumValue : defaultValue;
    }
    
    /**
     * Converts the object to {@link Player}.
     *
     * @return the object as a {@link Player}, or {@code null} if no player exists by that exact name.
     */
    @Nullable
    public Player toPlayer() {
        return Bukkit.getPlayerExact(toString());
    }
    
    /**
     * Converts this object to {@link UUID}.
     *
     * @return the object as {@code uuid}, or {@code null} if the object cannot be converted to {@code uuid}.
     */
    @Nullable
    public UUID toUuid() {
        return BukkitUtils.getUuidFromString(toString());
    }
    
    /**
     * Converts this object to {@link UUID}.
     *
     * @param defaultValue - The default value.
     * @return the object as {@code uuid}, or {@code null} if the object cannot be converted to {@code uuid}.
     */
    @NotNull
    public UUID toUuid(@NotNull UUID defaultValue) {
        final UUID uuid = toUuid();
        
        return uuid != null ? uuid : defaultValue;
    }
    
    /**
     * Converts the object to a {@link Registry} value.
     *
     * @param registry - The registry to retrieve the value.
     * @param <K>      - The registry type.
     * @return the object as registry item, or {@code null} if invalid key or not registered.
     */
    @NotNull
    public <K extends Keyed> Optional<K> toRegistryItem(@NotNull Registry<K> registry) {
        final Key key = Key.ofStringOrNull(toString());
        
        return key != null ? registry.get(key) : Optional.empty();
    }
    
    /**
     * Converts the object to a {@link Key}.
     *
     * @return the object as a {@code key}, or {@code null} if invalid key.
     */
    @Nullable
    public Key toKey() {
        return Key.ofStringOrNull(toString());
    }
    
    /**
     * Converts the object to a user-defined {@link Convertible}.
     *
     * @param convertible - The convertible.
     * @param <T>         - The convertible type.
     * @return the object as a user-defined convertible object, or {@code null} if failed to convert.
     */
    @Nullable
    public <T> T toConvertible(@NotNull Convertible<T> convertible) {
        return convertible.convert(object);
    }
    
    /**
     * Converts the object into a {@code static} constant field from the given {@code targetClass}.
     * <p>This is to be used as a helper method for "static-registries".</p>
     *
     * @param targetClass - The target class.
     * @param fieldType   - The field type.
     * @param <C>         - The target class.
     * @param <F>         - The field type.
     * @return a static constant value by the same name as this {@code object} string value wrapped in an optional.
     */
    @NotNull
    public <C, F> Optional<F> toStaticConstant(@NotNull Class<C> targetClass, @NotNull Class<F> fieldType) {
        try {
            final Field field = targetClass.getDeclaredField(toString());
            field.setAccessible(true);
            
            final Object value = field.get(null);
            
            return fieldType.isInstance(value) ? Optional.of(fieldType.cast(value)) : Optional.empty();
        }
        catch (NoSuchFieldException | IllegalAccessException ex) {
            return Optional.empty();
        }
    }
    
    /**
     * Converts the object with the given {@code converterFn} and wraps it in an {@link Optional}.
     * <p>Meant to be used as follows:</p>
     * <pre>{@code
     * TypeConverter.from("69")
     *              .asOptional(TypeConverter::toInt)
     *              .get();
     * }</pre>
     *
     * @param converterFn - The converter function to apply.
     * @param <T>         - The object type.
     * @return an {@code optional} containing the converted object, or an empty {@code optional} if failed to convert.
     */
    @NotNull
    public <T> Optional<T> asOptional(@NotNull Function<TypeConverter, T> converterFn) {
        return Optional.ofNullable(converterFn.apply(this));
    }
    
    /**
     * Creates a new {@link TypeConverter} for the given {@link Object}.
     *
     * @param object - The object for conversion.
     * @return a new converter.
     */
    @NotNull
    public static TypeConverter from(@NotNull Object object) {
        return new TypeConverter(object);
    }
    
    /**
     * Creates a new {@link TypeConverter} for the given {@link Object}.
     *
     * @param object - The object for conversion.
     * @return a new converter, or an empty converter if the object is {@code null}.
     */
    @NotNull
    public static TypeConverter fromNullable(@Nullable Object object) {
        return object != null ? new TypeConverter(object) : empty();
    }
    
    /**
     * Gets an empty {@link TypeConverter}.
     * <p>Entirely useless method at its own, only used for {@link #fromNullable(Object)}.</p>
     *
     * @return the empty converter.
     */
    @NotNull
    public static TypeConverter empty() {
        class Holder {
            private static final TypeConverter value = new TypeConverter("");
        }
        
        return Holder.value;
    }
    
    /**
     * Represents a custom converter for {@link TypeConverter}.
     *
     * @param <T> - The converting type.
     * @see TypeConverter#toConvertible(Convertible)
     */
    public interface Convertible<T> {
        
        /**
         * Converts the given {@link Object} into a {@link T}.
         *
         * @param object - The object to convert.
         * @return the object as {@link T}.
         */
        @Nullable
        T convert(@NotNull Object object);
        
    }
}
