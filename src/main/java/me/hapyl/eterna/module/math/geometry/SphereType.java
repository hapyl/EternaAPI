package me.hapyl.eterna.module.math.geometry;

import me.hapyl.eterna.module.annotate.DefaultEnumValue;
import me.hapyl.eterna.module.math.PiHelper;
import org.bukkit.Location;

/**
 * Represents a {@link SphereType} for {@link Geometry#drawSphere(Location, double, Quality, Drawable, SphereType)}.
 */
public enum SphereType {
    
    /**
     * Draws the full sphere.
     */
    @DefaultEnumValue
    FULL,
    
    /**
     * Draws only the top half of the sphere.
     */
    TOP_ONLY {
        @Override
        public double to() {
            return PiHelper.HALF_PI;
        }
    },
    
    /**
     * Draws only the bottom half of the sphere.
     */
    BOTTOM_ONLY {
        @Override
        public double from() {
            return PiHelper.HALF_PI;
        }
    };
    
    /**
     * The {@code phi} start value.
     *
     * @return {@code phi} start value.
     */
    public double from() {
        return 0;
    }
    
    /**
     * The {@code phi} end value.
     *
     * @return {@code phi} end value.
     */
    public double to() {
        return PiHelper.PI;
    }
    
}
