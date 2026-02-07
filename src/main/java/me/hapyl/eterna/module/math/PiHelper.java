package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.annotate.UtilityClass;

/**
 * Represents a helper class for working with PI-es.
 */
@UtilityClass
public final class PiHelper {
    
    /**
     * The {@code π} constant.
     */
    public static final double PI = Math.PI;
    
    /**
     * The {@code 2π} constant.
     */
    public static final double TWO_PI = Math.PI * 2;
    
    /**
     * The {@code π / 2} constant.
     */
    public static final double HALF_PI = Math.PI / 2;
    
    /**
     * The theoretical minimum {@code π} that should be used in calculations.
     */
    public static final double PI_MIN_VALUE = 0.00314159265;
    
    private PiHelper() {
        UtilityClass.Validator.throwIt();
    }
    
}