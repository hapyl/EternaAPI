package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a named {@link JsonProperty} reader for {@link JsonObject}.
 *
 * @param <J> - The element type.
 */
public interface JsonProperty<J> {
    
    /**
     * Gets the name of the property.
     *
     * @return - The name of the property.
     */
    @NotNull String property();
    
    /**
     * Reads the property from the given {@link JsonObject}.
     *
     * <p>
     * Special Cases:
     *     <ul>
     *         <li>If the property does not exist in the json object, {@code null} is returned.
     *         <li>If the property type and json element type mismatch, {@code null} is returned.
     *         <li>If the json element returns {@code null}, {@code null} is returned.
     *     </ul>
     * </p>
     *
     * @param jsonObject - The JSON object to read from.
     * @return the property value, or {@code null}.
     */
    @Nullable J read(@NotNull JsonObject jsonObject);
    
    /**
     * Reads the property from the given {@link JsonObject}.
     *
     * <p>
     * Special Cases:
     *     <ul>
     *         <li>If the property does not exist in the json object, {@code defaultValue} is returned.
     *         <li>If the property type and json element type mismatch, {@code defaultValue} is returned.
     *         <li>If the json element returns {@code null}, {@code defaultValue} is returned.
     *     </ul>
     * </p>
     *
     * @param jsonObject   - The JSON object to read from.
     * @param defaultValue - The default value to return.
     * @return the property value, or {@code defaultValue}.
     */
    @NotNull J read(@NotNull JsonObject jsonObject, @NotNull J defaultValue);
    
    /**
     * Reads the property from the given {@link JsonObject}.
     *
     * <p>
     * Special Cases:
     *     <ul>
     *         <li>If the property does not exist in the json object, an {@link IllegalArgumentException} is thrown.
     *         <li>If the property type and json element type mismatch, an {@link IllegalArgumentException} is thrown.
     *         <li>If the json element returns {@code null}, an {@link IllegalArgumentException} is thrown.
     *     </ul>
     * </p>
     *
     * @param jsonObject - The JSON object to read from.
     * @return the property value.
     */
    @NotNull J readOrThrow(@NotNull JsonObject jsonObject) throws IllegalArgumentException;
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link JsonObject}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<JsonObject> ofJsonObject(@NotNull String property) {
        return new JsonPropertyJsonObject(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link JsonArray}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<JsonArray> ofJsonArray(@NotNull String property) {
        return new JsonPropertyJsonArray(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link String}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<String> ofString(@NotNull String property) {
        return new JsonPropertyString(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link Integer}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<Integer> ofInteger(@NotNull String property) {
        return new JsonPropertyInteger(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link Long}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<Long> ofLong(@NotNull String property) {
        return new JsonPropertyLong(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link Float}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<Float> ofFloat(@NotNull String property) {
        return new JsonPropertyFloat(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link Double}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<Double> ofDouble(@NotNull String property) {
        return new JsonPropertyDouble(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link Boolean}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<Boolean> ofBoolean(@NotNull String property) {
        return new JsonPropertyBoolean(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link Byte}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<Byte> ofByte(@NotNull String property) {
        return new JsonPropertyByte(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link Byte}.
     *
     * @param property - The property name.
     * @return a new JSON property.
     */
    static @NotNull JsonProperty<Short> ofShort(@NotNull String property) {
        return new JsonPropertyShort(property);
    }
    
    /**
     * A static factory method for creating a {@link JsonProperty} that reads {@link Enum}.
     *
     * <p>
     * Internally, this reads a {@link String} from the {@link JsonObject} and converts to the given {@link Enum}; if the enum constant
     * does not exist, it is considered as if {@link JsonElement} returned {@code null}.
     * </p>
     *
     * @param property  - The property name.
     * @param enumClass - The enum class.
     * @return a new JSON property.
     */
    static <E extends Enum<E>> @NotNull JsonProperty<E> ofEnum(@NotNull String property, @NotNull Class<E> enumClass) {
        return new JsonPropertyEnum<>(property, enumClass);
    }
    
}
