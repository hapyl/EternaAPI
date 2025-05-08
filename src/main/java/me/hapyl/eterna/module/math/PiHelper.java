package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.annotate.UtilityClass;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

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
    public static final double PI_2 = Math.PI * 2;
    
    /**
     * The {@code π / 2} constant.
     */
    public static final double HALF_PI = Math.PI / 2;
    
    private PiHelper() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Rotates a value around the given degrees, starting from {@code startDegree} and rotating up to {@code degreesToRotate}.
     * The method calls the provided {@code consumer} for each step in radians from the start angle to the target angle.
     *
     * @param startDegree     - The starting angle in degrees.
     * @param degreesToRotate - The number of degrees to rotate.
     * @param step            - The step increment in radians for each rotation iteration.
     * @param consumer        - A {@link Consumer} that accepts the current angle in radians at each step.
     */
    public static void rotate(double startDegree, double degreesToRotate, double step, @Nonnull Consumer<Double> consumer) {
        final double rx = Math.toRadians(startDegree);
        final double rz = Math.toRadians(startDegree + degreesToRotate);
        double d;
        
        for (d = rx; d < rz; d += step) {
            consumer.accept(d);
        }
    }
    
    /**
     * Gets the radians for the given degree.
     *
     * @param degree - The degree.
     * @return the radians.
     */
    public static double rad(double degree) {
        return Math.toRadians(degree);
    }
    
    /**
     * Gets the degrees for the given radians.
     *
     * @param rad - The radians.
     * @return the degrees.
     */
    public static double deg(double rad) {
        return Math.toDegrees(rad);
    }
    
    /**
     * Gets the sine for the given angle (in radians).
     *
     * @param angle - The angle.
     * @return the sine for the given angle (in radians).
     */
    public static double sin(double angle) {
        return Math.sin(angle);
    }
    
    /**
     * Gets the cosine for the given angle (in radians).
     *
     * @param angle - The angle.
     * @return the cosine for the given angle (in radians).
     */
    public static double cos(double angle) {
        return Math.cos(angle);
    }
    
    /**
     * Returns the result of dividing {@code 2 * Math.PI} by the given value.
     *
     * @param v – The divisor.
     * @return The result of {@code 2π / v}.
     */
    public static double ofTwoPi(double v) {
        return Math.PI * 2 / v;
    }
    
}