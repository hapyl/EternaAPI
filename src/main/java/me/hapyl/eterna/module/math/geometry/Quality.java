package me.hapyl.eterna.module.math.geometry;

import me.hapyl.eterna.module.math.Geometry;

/**
 * Represents a quality preset for {@link Geometry}.
 */
public class Quality {
    
    private static final double PI = Math.PI;
    
    public static final Quality ROTTEN_POTATO = custom(PI);
    public static final Quality POTATO = custom(PI / 2);
    
    public static final Quality VERY_LOW = custom(PI / 4);
    public static final Quality LOW = custom(PI / 8);
    public static final Quality NORMAL = custom(PI / 12);
    public static final Quality HIGH = custom(PI / 16);
    public static final Quality VERY_HIGH = custom(PI / 22);
    
    public static final Quality SUPER_HIGH = custom(PI / 32);
    public static final Quality ULTRA_HIGH = custom(PI / 64);
    public static final Quality EXTREME_HIGH = custom(PI / 96);
    public static final Quality PROBABLY_TOO_MUCH_QUALITY = custom(PI / 256);
    
    private final double step;
    
    private Quality(double step) {
        this.step = step;
    }
    
    public double getStep() {
        return step;
    }
    
    public static Quality custom(double quality) {
        return new Quality(Math.clamp(quality, 0.0001d, Math.PI));
    }
    
}
