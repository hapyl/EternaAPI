package me.hapyl.eterna.module.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a utility helper class that allows reading a value from {@link JsonObject} as an {@link Optional}.
 *
 * <p>
 * The utility designed to <b>never</b> throw errors and fallback to {@link Optional#empty()} if either an element does not exist,
 * or its type isn't what is requested.
 * </p>
 */
@UtilityClass
public class JsonOptionals {
    
    private JsonOptionals() {
    }
    
    /**
     * Gets a {@link Boolean} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the boolean.
     * @param key  - The key of the element.
     * @return a {@code boolean} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<Boolean> getBoolean(@NotNull JsonObject json, @NotNull String key) {
        return getPrimitive0(json, key, JsonPrimitive::isBoolean, JsonPrimitive::getAsBoolean);
    }
    
    /**
     * Gets a {@link String} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the string.
     * @param key  - The key of the element.
     * @return a {@code string} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<String> getString(@NotNull JsonObject json, @NotNull String key) {
        return getPrimitive0(json, key, JsonPrimitive::isString, JsonPrimitive::getAsString);
    }
    
    /**
     * Gets a {@link Number} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the number.
     * @param key  - The key of the element.
     * @return a {@code number} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<Number> getNumber(@NotNull JsonObject json, @NotNull String key) {
        return getNumber0(json, key, JsonPrimitive::getAsNumber);
    }
    
    /**
     * Gets a {@link Double} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the double.
     * @param key  - The key of the element.
     * @return a {@code double} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<Double> getDouble(@NotNull JsonObject json, @NotNull String key) {
        return getNumber0(json, key, JsonPrimitive::getAsDouble);
    }
    
    /**
     * Gets a {@link Float} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the float.
     * @param key  - The key of the element.
     * @return a {@code float} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<Float> getFloat(@NotNull JsonObject json, @NotNull String key) {
        return getNumber0(json, key, JsonPrimitive::getAsFloat);
    }
    
    /**
     * Gets a {@link Long} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the long.
     * @param key  - The key of the element.
     * @return a {@code long} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<Long> getLong(@NotNull JsonObject json, @NotNull String key) {
        return getNumber0(json, key, JsonPrimitive::getAsLong);
    }
    
    /**
     * Gets a {@link Short} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the short.
     * @param key  - The key of the element.
     * @return a {@code short} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<Short> getShort(@NotNull JsonObject json, @NotNull String key) {
        return getNumber0(json, key, JsonPrimitive::getAsShort);
    }
    
    /**
     * Gets a {@link Integer} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the integer.
     * @param key  - The key of the element.
     * @return a {@code integer} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<Integer> getInteger(@NotNull JsonObject json, @NotNull String key) {
        return getNumber0(json, key, JsonPrimitive::getAsInt);
    }
    
    /**
     * Gets a {@link Byte} from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json - The json object from which to retrieve the byte.
     * @param key  - The key of the element.
     * @return a {@code byte} wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static Optional<Byte> getByte(@NotNull JsonObject json, @NotNull String key) {
        return getNumber0(json, key, JsonPrimitive::getAsByte);
    }
    
    /**
     * Gets a value from the given {@link JsonObject} at the given {@code key} wrapped in an {@link Optional}.
     *
     * @param json    - The json object from which to retrieve the boolean.
     * @param key     - The key of the element.
     * @param checker - The checker for type validation.
     * @param getter  - The getter function.
     * @return a value wrapped in an optional, or an empty optional if it doesn't exist.
     */
    @NotNull
    public static <E> Optional<E> get(@NotNull JsonObject json, @NotNull String key, @NotNull Predicate<JsonElement> checker, @NotNull Function<JsonElement, E> getter) {
        final JsonElement jsonElement = json.get(key);
        
        if (jsonElement == null || !checker.test(jsonElement)) {
            return Optional.empty();
        }
        
        return Optional.ofNullable(getter.apply(jsonElement));
    }
    
    @NotNull
    public static <E extends Enum<E>> Optional<E> getEnum(@NotNull JsonObject json, @NotNull String key, @NotNull Class<E> enumClass) {
        // Not calling `getPrimitive0` or `getString` to reduce unnecessary Optional wrapping
        final JsonElement jsonElement = json.get(key);
        
        if (!(jsonElement instanceof JsonPrimitive jsonPrimitive) || !jsonPrimitive.isString()) {
            return Optional.empty();
        }
        
        return Optional.ofNullable(Enums.byName(enumClass, jsonPrimitive.getAsString()));
    }
    
    @NotNull
    private static <E> Optional<E> getPrimitive0(@NotNull JsonObject json, @NotNull String key, @NotNull Predicate<JsonPrimitive> checker, @NotNull Function<JsonPrimitive, E> getter) {
        final JsonElement jsonElement = json.get(key);
        
        // instanceof is simpler than calling `isPrimitive` since it does the exact same thing
        if (!(jsonElement instanceof JsonPrimitive jsonPrimitive) || !checker.test(jsonPrimitive)) {
            return Optional.empty();
        }
        
        return Optional.of(getter.apply(jsonPrimitive));
    }
    
    @NotNull
    private static <E extends Number> Optional<E> getNumber0(@NotNull JsonObject json, @NotNull String key, @NotNull Function<JsonPrimitive, E> getter) {
        return getPrimitive0(json, key, JsonPrimitive::isNumber, getter);
    }
    
}
