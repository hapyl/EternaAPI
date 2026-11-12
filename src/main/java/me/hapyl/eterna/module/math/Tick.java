package me.hapyl.eterna.module.math;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Represents an immutable wrapper of a Minecraft tick unit, with helpful conversion and formatting methods.
 */
public interface Tick extends ComponentLike, Comparable<Tick> {
    
    /**
     * Defines an indefinite component that is used for indefinite ticks.
     */
    @NotNull Component COMPONENT_INDEFINITE = Component.text("∞");
    
    /**
     * Defines the constant for the {@link Tick} to be considered indefinite.
     *
     * <p>
     * This constant <b>must not</b> be used for indefinability check, as it is not reliable, since <b>any negative tick</b>
     * is considered as "indefinite", prefer {@link Tick#isIndefinite()} or {@link #isIndefinite(int)} instead.
     * </p>
     */
    int INDEFINITE = -1;
    
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
     * Gets whether this {@link Tick} has a value of {@code 0}.
     *
     * @return true if this tick has value of {@code 0}; false otherwise.
     */
    boolean isZero();
    
    /**
     * Gets whether this {@link Tick} has a value of {@code 1}.
     *
     * @return {@code true} if this tick has value of {@code 1}; {@code false} otherwise.
     */
    boolean isOne();
    
    /**
     * Gets whether this {@link Tick} is finite, meaning its value is {@code >= 0}.
     *
     * @return {@code true} if this tick is finite; {@code false} otherwise.
     */
    boolean isFinite();
    
    /**
     * Gets whether this {@link Tick} is indefinite, meaning its value is {@code < 0}.
     *
     * @return {@code true} if this is indefinite, {@code false} otherwise.
     */
    boolean isIndefinite();
    
    /**
     * Gets whether the tick value of the two objects equal.
     *
     * @param object the reference object with which to compare.
     * @return {@code true} if the value if the tick values are equal; {@code false} otherwise.
     */
    @Override
    boolean equals(@Nullable Object object);
    
    /**
     * Adds the given {@link Tick} to this one.
     *
     * @param that - The tick to add.
     * @return a new tick repressing the sum.
     */
    @NotNull Tick plus(@NotNull Tick that);
    
    /**
     * Subtracts the given {@link Tick} from this one.
     *
     * @param that - The tick to subtract.
     * @return a new tick representing the difference.
     */
    @NotNull Tick minus(@NotNull Tick that);
    
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
     * A static factory method for getting a {@link Tick} instance for value of {@code 0}.
     *
     * <p>
     * The returned tick instance is guarantee to return {@code true} when {@link #isZero()} is called.
     * </p>
     *
     * @return a singleton tick instance for value of {@code 0}.
     */
    static @NotNull Tick zero() {
        return TickImpl.ZERO;
    }
    
    /**
     * A static factory method for getting a {@link Tick} instance for value of {@code 1}.
     *
     * <p>
     * The returned tick instance is guarantee to return {@code true} when {@link #isOne()} is called.
     * </p>
     *
     * @return a singleton tick instance for value of {@code 1}.
     */
    static @NotNull Tick one() {
        return TickImpl.ONE;
    }
    
    /**
     * Adds the two {@link Tick} together using {@link #plus(Tick)}.
     *
     * @param a - The first tick.
     * @param b - The second tick.
     * @return a new tick representing the sum of both ticks.
     */
    static @NotNull Tick sum(@NotNull Tick a, @NotNull Tick b) {
        return a.plus(b);
    }
    
    /**
     * Formats the given {@code ticks} into seconds with the given {@link Precision}.
     *
     * @param ticks     - The ticks to format.
     * @param precision - The precision to use.
     * @return the formatted ticks.
     */
    static @NotNull Component format(int ticks, @NotNull Precision precision) {
        if (isIndefinite(ticks)) {
            return COMPONENT_INDEFINITE;
        }
        
        // Format compact ticks by directly dividing by 20
        if (precision == Precision.COMPACT && ticks % 20 == 0) {
            return Component.text((ticks / 20) + "s");
        }
        
        // We format using `String.format` because `formatted` does not have overloads that allow using a different Locale,
        // which we need to preserve the `.` formats for decimals across all locales.
        return Component.text(String.format(Locale.ROOT, precision.format, (double) ticks / 20) + "s");
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
    
    /**
     * Gets whether the given {@code tick} is indefinite, meaning its value is {@code < 0}.
     *
     * @param tick - The tick to check.
     * @return {@code true} if the tick is indefinite; {@code false} otherwise.
     */
    static boolean isIndefinite(int tick) {
        return tick < 0;
    }
    
}