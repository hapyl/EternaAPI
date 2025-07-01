package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public interface MapWrap<K, V> extends Wrap {

    /**
     * How the key and value pair must be displayed.
     * <pre><code>
     *     String keyToValue(K key, V value) {
     *         return key + "=" + value;
     *     }
     *
     *     // Would be [k=v, k1=v2]
     * </code></pre>
     *
     * @param key   - Key.
     * @param value - Value.
     * @return how key and value pair must be displayed.
     */
    @Nonnull
    String keyToValue(K key, V value);

    @Nonnull
    static <K, V> MapWrap<K, V> ofDefault() {
        return of("{", ", ", "}", (k, v) -> k + "=" + v); // annoying generics have to use a method
    }

    @Nonnull
    static <K, V> MapWrap<K, V> of(
            @Nonnull String start,
            @Nonnull String between,
            @Nonnull String end,
            @Nonnull BiFunction<K, V, String> function
    ) {
        return new MapWrap<>() {

            @Override
            @Nonnull
            public String keyToValue(K key, V value) {
                return function.apply(key, value);
            }

            @Override
            public @NotNull String start() {
                return start;
            }

            @Override
            public @NotNull String between() {
                return between;
            }

            @Override
            public @NotNull String end() {
                return end;
            }
        };
    }
}
