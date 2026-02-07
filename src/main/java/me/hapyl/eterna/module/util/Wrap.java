package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Represents a {@link Wrap} for {@link CollectionUtils#wrapToString(Collection, Wrap)}.
 */
public interface Wrap {
    
    /**
     * Defines the default {@link Wrap} used.
     */
    @NotNull
    Wrap DEFAULT = of("[", ", ", "]");
    
    /**
     * Gets the {@link String} used at the start of the {@link Wrap}.
     *
     * @return the string used at the start of the wrap.
     */
    @NotNull
    String start();
    
    /**
     * Gets the {@link String} used as a delimiter of each element of the {@link Wrap}.
     *
     * @return the string used as a delimiter of each element of the wrap.
     */
    @NotNull
    String delimiter();
    
    /**
     * Gets the {@link String} used at the end of the {@link Wrap}.
     *
     * @return the string used at the end of the wrap.
     */
    @NotNull
    String end();
    
    /**
     * Converts this {@link Wrap} into a {@link Collector}.
     *
     * @return the collector.
     */
    @NotNull
    default Collector<CharSequence, ?, String> asCollector() {
        return Collectors.joining(delimiter(), start(), end());
    }
    
    /**
     * A static factory method for creating {@link Wrap}.
     *
     * @param start     - The start string.
     * @param delimiter - The delimiter string.
     * @param end       - The end string.
     * @return a new wrap.
     */
    @NotNull
    static Wrap of(@Nullable String start, @Nullable String delimiter, @Nullable String end) {
        return new Wrap() {
            @Override
            @NotNull
            public String start() {
                return start == null ? "" : start;
            }
            
            @Override
            @NotNull
            public String delimiter() {
                return delimiter == null ? "" : delimiter;
            }
            
            @Override
            @NotNull
            public String end() {
                return end == null ? "" : end;
            }
        };
    }
    
    /**
     * A static factory method for creating default {@link MapWrap}.
     *
     * @return a new default wrap.
     */
    @NotNull
    static <K, V> Wrap.MapWrap<K, V> ofMap() {
        return ofMap("{", ", ", "}", (k, v) -> k + "=" + v);
    }
    
    /**
     * A static factory method for creating {@link MapWrap}.
     *
     * @param start     - The start string.
     * @param delimiter - The delimiter string.
     * @param end       - The end string.
     * @param function  - The function that defines how {@link K} and {@link V} should be formatted.
     * @return a new wrap.
     */
    @NotNull
    static <K, V> Wrap.MapWrap<K, V> ofMap(
            @NotNull String start,
            @NotNull String delimiter,
            @NotNull String end,
            @NotNull BiFunction<K, V, String> function
    ) {
        return new MapWrap<>() {
            
            @Override
            @NotNull
            public String keyToValue(@NotNull K key, @NotNull V value) {
                return function.apply(key, value);
            }
            
            @Override
            @NotNull
            public String start() {
                return start;
            }
            
            @Override
            @NotNull
            public String delimiter() {
                return delimiter;
            }
            
            @Override
            @NotNull
            public String end() {
                return end;
            }
        };
    }
    
    /**
     * Represents a {@link MapWrap} used for {@link CollectionUtils#wrapToString(Map, MapWrap)}.
     *
     * @param <K> - The key type.
     * @param <V> - The value type.
     */
    interface MapWrap<K, V> extends Wrap {
        
        /**
         * Defines how {@link K} and {@link V} should be displayed.
         *
         * @param key   - The key.
         * @param value - The value.
         * @return a string how to format key and value.
         */
        @NotNull
        String keyToValue(@NotNull K key, @NotNull V value);
    }
}