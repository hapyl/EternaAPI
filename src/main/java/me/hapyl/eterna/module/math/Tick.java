package me.hapyl.eterna.module.math;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Represents a wrapper of a Minecraft tick unit.
 */
public interface Tick extends ComponentLike {
    
    /**
     * Defines an infinity component that is used for invalid ticks formats.
     */
    @NotNull Component INFINITY = Component.text("∞");
    
    /**
     * Gets the value of this {@link Tick} in {@code ticks}.
     *
     * @return the value of this tick in ticks.
     */
    int ticks();
    
    /**
     * Gets the value of this {@link Tick} in {@code seconds}.
     *
     * @return the value of this tick in seconds.
     */
    float seconds();
    
    /**
     * Gets the value of this {@link Tick} in {@code minutes}.
     *
     * @return the value of this tick in minutes.
     */
    float minutes();
    
    /**
     * Gets the value of this {@link Tick} in {@code hours}.
     *
     * @return the value of this tick in hours.
     */
    float hours();
    
    /**
     * Formats this {@link Tick} into seconds with {@link Precision#COMPACT} precision.
     *
     * @return formatted ticks as seconds.
     */
    @Override
    default @NotNull Component asComponent() {
        return format(this.ticks(), Precision.COMPACT);
    }
    
    /**
     * Formats this {@link Tick} into seconds with the given {@link Precision}.
     *
     * @param precision - The precision to use.
     * @return formatted ticks as seconds.
     */
    default @NotNull Component asComponent(@NotNull Precision precision) {
        return format(this.ticks(), precision);
    }
    
    /**
     * A static factory method for creating {@link Tick} from the given {@code ticks}.
     *
     * @param ticks - The duration in ticks.
     * @return a new {@link Tick} instance.
     */
    static @NotNull Tick ofTicks(int ticks) {
        return new TickImpl(ticks);
    }
    
    /**
     * A static factory method for creating {@link Tick} from the given {@code seconds}.
     *
     * @param seconds - The duration in seconds.
     * @return a new {@link Tick} instance.
     */
    static @NotNull Tick ofSeconds(float seconds) {
        return new TickImpl((int) Math.round(seconds * TickImpl.RATIO_TICKS_TO_SECONDS));
    }
    
    /**
     * A static factory method for creating {@link Tick} from the given {@code minutes}.
     *
     * @param minutes - The duration in minutes.
     * @return a new {@link Tick} instance.
     */
    static @NotNull Tick ofMinutes(float minutes) {
        return new TickImpl((int) Math.round(minutes * TickImpl.RATIO_TICKS_TO_MINUTES));
    }
    
    /**
     * A static factory method for creating {@link Tick} from the given {@code hours}.
     *
     * @param hours - The duration in hours.
     * @return a new {@link Tick} instance.
     */
    static @NotNull Tick ofHours(float hours) {
        return new TickImpl((int) Math.round(hours * TickImpl.RATIO_TICKS_TO_HOURS));
    }
    
    /**
     * Formats the given {@code ticks} into seconds with the given {@link Precision}.
     *
     * @param ticks     - The ticks to format.
     * @param precision - The precision to use.
     * @return the formatted ticks.
     */
    static @NotNull Component format(int ticks, @NotNull Precision precision) {
        if (ticks < 0) {
            return INFINITY;
        }
        
        // Format compact ticks by directly dividing by 20
        if (precision == Precision.COMPACT && ticks % 20 == 0) {
            return Component.text((ticks / 20) + "s");
        }
        
        // We format using `String.format` because `formatted` does not have overloads that allow using a different Locale,
        // which we need to preserve the `.` formats for decimals across all locales.
        return Component.text(String.format(Locale.ROOT, precision.format, (double) ticks / 20));
    }
    
    /**
     * Formats the given {@code ticks} into seconds with the {@link Precision#COMPACT} precision.
     *
     * @param ticks - The ticks to format.
     * @return the formatted ticks.
     */
    static @NotNull Component format(int ticks) {
        return format(ticks, Precision.COMPACT);
    }
    
}