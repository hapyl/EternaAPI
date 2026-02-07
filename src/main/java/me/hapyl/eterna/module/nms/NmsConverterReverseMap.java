package me.hapyl.eterna.module.nms;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * Represents an internal {@link NmsConverter} that handles conversion via a {@link Map}.
 *
 * @param <B> - The {@code bukkit} type.
 * @param <N> - The {@code nms} type.
 */
@ApiStatus.Internal
public final class NmsConverterReverseMap<B, N> implements NmsConverter<B, N> {
    
    private final Map<B, N> lookup;
    
    NmsConverterReverseMap(@NotNull Map<B, N> lookup) {
        this.lookup = lookup;
    }
    
    // Performs a reverse search of the `bukkit` object by `nms` object via iterating the map
    
    /**
     * Performs a reverse search of the {@code bukkit} object by {@code nms} object via iterating over the {@link Map}.
     *
     * @param n - The {@code nms} object.
     * @return the {@code bukkit} object mapped to the given {@code nms} object.
     * @throws IllegalArgumentException if no {@code bukkit} object is mapped to the given {@code nms} object.
     */
    @NotNull
    @Override
    public  B toBukkit(@NotNull N n) {
        for (Map.Entry<B, N> entry : this.lookup.entrySet()) {
            if (entry.getValue().equals(n)) {
                return entry.getKey();
            }
        }
        
        throw new IllegalArgumentException("Cannot find mapping for `%s`!".formatted(String.valueOf(n)));
    }
    
    /**
     * Gets the {@code nms} object mapped to the given {@code bukkit} object.
     *
     * @param b - The {@code bukkit} object.
     * @return the {@code nms} object mapped to the given {@code bukkit} object.
     * @throws NullPointerException if no {@code nms} object is mapped to the given {@code bukkit} object.
     */
    @NotNull
    @Override
    public N toNms(@NotNull B b) {
        return Objects.requireNonNull(this.lookup.get(b));
    }
    
}
