package me.hapyl.eterna.module.math.geometry;

import me.hapyl.eterna.module.math.PiHelper;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the angular step size used when iterating over a full circle ({@code 0} to {@code 2π}) for drawing {@link Particle}.
 *
 * <p>Smaller values produce smoother, denser particle shapes by increasing  the number of steps.</p>
 */
public class Quality {
    
    /**
     * Defines the quality of {@code 8} particles per draw.
     */
    public static final Quality VERY_LOW = ofPiDiv(4);
    
    /**
     * Defines the quality of {@code 16} particles per draw.
     */
    public static final Quality LOW = ofPiDiv(8);
    
    /**
     * Defines the quality of {@code 24} particles per draw.
     */
    public static final Quality NORMAL = ofPiDiv(12);
    
    /**
     * Defines the quality of {@code 32} particles per draw.
     */
    public static final Quality HIGH = ofPiDiv(16);
    
    /**
     * Defines the quality of {@code 44} particles per draw.
     */
    public static final Quality VERY_HIGH = ofPiDiv(22);
    
    /**
     * Defines the quality of {@code 64} particles per draw.
     */
    public static final Quality SUPER_HIGH = ofPiDiv(32);
    
    /**
     * Defines the quality of {@code 128} particles per draw.
     */
    public static final Quality ULTRA_HIGH = ofPiDiv(64);
    
    /**
     * Defines the quality of {@code 192} particles per draw.
     */
    public static final Quality EXTREMELY_HIGH = ofPiDiv(96);
    
    private final double quality;
    
    Quality(double quality) {
        this.quality = quality;
    }
    
    /**
     * Gets the angular step, in radians, used when advancing around a full circle.
     *
     * @return The step size in radians.
     */
    public double getQuality() {
        return quality;
    }
    
    /**
     * A static factory method for creating {@link Quality}.
     *
     * @param quality - The angular step, in radians.
     *                <p>Will be clamped between {@link PiHelper#PI_MIN_VALUE} and {@code π}.</p>
     * @return a new {@link Quality}.
     */
    @NotNull
    public static Quality of(final double quality) {
        return new Quality(Math.clamp(quality, PiHelper.PI_MIN_VALUE, PiHelper.PI));
    }
    
    /**
     * A static factory method for creating {@link Quality}.
     *
     * @param divisor - The divisor by which to divide {@code π}.
     * @return a new {@link Quality}.
     */
    @NotNull
    public static Quality ofPiDiv(final double divisor) {
        return of(PiHelper.PI / divisor);
    }
    
}
