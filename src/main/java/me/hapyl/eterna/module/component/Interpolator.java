package me.hapyl.eterna.module.component;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleUnaryOperator;

/**
 * Represents an interpolator for {@link Components#gradient(String, TextColor, TextColor, Interpolator, Style)}.
 */
public interface Interpolator {
    
    /**
     * Defines the linear interpolator, progressing at a constant rate from start to end.
     */
    @NotNull Interpolator LINEAR = create(t -> t);
    
    /**
     * Defines the quadratic slow-to-fast interpolator, starting slow and accelerating toward the end.
     */
    @NotNull Interpolator QUADRATIC_SLOW_TO_FAST = create(t -> t * t);
    
    /**
     * Defines the quadratic fast-to-slow interpolator, starting fast and decelerating toward the end.
     */
    @NotNull Interpolator QUADRATIC_FAST_TO_SLOW = create(t -> t * (2 - t));
    
    /**
     * Defines the cubic slow-to-fast interpolator, starting slow and accelerating sharply toward the end.
     */
    @NotNull Interpolator CUBIC_SLOW_TO_FAST = create(t -> t * t * t);
    
    /**
     * Defines the cubic fast-to-slow interpolator, starting fast and decelerating sharply toward the end.
     */
    @NotNull Interpolator CUBIC_FAST_TO_SLOW = create(t -> 1 - Math.pow(1 - t, 3));
    
    /**
     * Defines the ease-in-out interpolator, starting slow, accelerating through the middle, and decelerating toward the end.
     */
    @NotNull Interpolator EASE_IN_OUT = create(t -> 0.5 - Math.cos(Math.PI * t) * 0.5);
    
    /**
     * Interpolates between two {@link TextColor}, returning an array of colors of the given {@code length}.
     *
     * @param from   - The starting color.
     * @param to     - The ending color.
     * @param length - The number of colors to generate.
     * @return an array of colors.
     */
    @NotNull TextColor[] interpolate(@NotNull TextColor from, @NotNull TextColor to, int length);
    
    /**
     * A static factory method for creating {@link Interpolator}.
     *
     * @param easing - The easing operator.
     * @return a new interpolator.
     */
    static @NotNull Interpolator create(@NotNull DoubleUnaryOperator easing) {
        return new Interpolator() {
            @Override
            public @NotNull TextColor[] interpolate(@NotNull TextColor from, @NotNull TextColor to, int length) {
                final TextColor[] colors = new TextColor[length];
                
                final double fromRed = from.red();
                final double fromGreen = from.green();
                final double fromBlue = from.blue();
                
                for (int i = 0; i < length; i++) {
                    final double t = Math.clamp(easing.applyAsDouble((double) i / (length - 1)), 0, 1);
                    
                    colors[i] = TextColor.color(
                            (int) Math.round(fromRed + (to.red() - fromRed) * t),
                            (int) Math.round(fromGreen + (to.green() - fromGreen) * t),
                            (int) Math.round(fromBlue + (to.blue() - fromBlue) * t)
                    );
                }
                
                return colors;
            }
        };
    }
    
}
