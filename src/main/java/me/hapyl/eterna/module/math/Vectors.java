package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.annotate.Mutates;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * A helpful utility class for working with {@link Vector}.
 */
@UtilityClass
public final class Vectors {
    
    private Vectors() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets an empty {@link Vector}.
     *
     * @return an empty vector.
     */
    @NotNull
    public static Vector zero() {
        return new Vector();
    }
    
    /**
     * Gets a {@link Vector} that points down.
     *
     * @return a vector that points down.
     */
    @NotNull
    public static Vector down() {
        return new Vector(0, -1, 0);
    }
    
    /**
     * Geta a {@link Vector} that points up.
     *
     * @return a vector to points up.
     */
    @NotNull
    public static Vector up() {
        return new Vector(0, 1, 0);
    }
    
    /**
     * Gets a {@link Vector} with a negative minecraft gravity constant.
     *
     * @return a vector with a negative minecraft gravity constant.
     * @deprecated Gravity is now a customizable attribute.
     */
    @NotNull
    @Deprecated
    public static Vector gravity() {
        return new Vector(0, -BukkitUtils.GRAVITY, 0);
    }
    
    /**
     * Rotates the given {@link Vector} around the {@code x} axis.
     *
     * <p>
     * The rotation is performed in-place using the provided cosine and sine
     * values of the rotation angle.
     * </p>
     *
     * @param vector - The vector to rotate.
     * @param cos    - The cosine of the rotation angle.
     * @param sin    - The sine of the rotation angle.
     * @return The rotated vector.
     */
    @NotNull
    public static Vector rotateX(@NotNull Vector vector, double cos, double sin) {
        final double y = vector.getY() * cos - vector.getZ() * sin;
        final double z = vector.getY() * sin + vector.getZ() * cos;
        
        return vector.setY(y).setZ(z);
    }
    
    /**
     * Rotates the given {@link Vector} around the {@code y} axis.
     *
     * <p>
     * The rotation is performed in-place using the provided cosine and sine
     * values of the rotation angle.
     * </p>
     *
     * @param vector - The vector to rotate.
     * @param cos    - The cosine of the rotation angle.
     * @param sin    - The sine of the rotation angle.
     * @return The rotated vector.
     */
    @NotNull
    public static Vector rotateY(@NotNull Vector vector, double cos, double sin) {
        final double x = vector.getX() * cos + vector.getZ() * sin;
        final double z = vector.getX() * -sin + vector.getZ() * cos;
        
        return vector.setX(x).setZ(z);
    }
    
    /**
     * Rotates the given {@link Vector} around the {@code z} axis.
     *
     * <p>
     * The rotation is performed in-place using the provided cosine and sine
     * values of the rotation angle.
     * </p>
     *
     * @param vector - The vector to rotate.
     * @param cos    - The cosine of the rotation angle.
     * @param sin    - The sine of the rotation angle.
     * @return The rotated vector.
     */
    @NotNull
    public static Vector rotateZ(@NotNull Vector vector, double cos, double sin) {
        final double x = vector.getX() * cos - vector.getY() * sin;
        final double y = vector.getX() * sin + vector.getY() * cos;
        
        return vector.setX(x).setY(y);
    }
    
    /**
     * Creates a random {@link Vector} with all components in the range {@code [0, 1]}.
     *
     * @return A random vector.
     */
    @NotNull
    public static Vector random() {
        return random(1, 1, 1);
    }
    
    /**
     * Creates a random {@link Vector} with {@code x} and {@code z} components sharing the same range.
     *
     * <p>
     * The {@code x} and {@code z} components are generated in the range {@code [0, xz]}, while
     * the {@code y} component is generated in the range {@code [0, y]}.
     * </p>
     *
     * @param xz - The maximum value for the {@code x} and {@code z} components.
     * @param y  - The maximum value for the {@code y} component.
     * @return A random vector.
     */
    @NotNull
    public static Vector random(double xz, double y) {
        return random(xz, y, xz);
    }
    
    /**
     * Creates a random {@link Vector}.
     *
     * @param x - The maximum value for the {@code x} component.
     * @param y - The maximum value for the {@code y} component.
     * @param z - The maximum value for the {@code z} component.
     * @return A random vector.
     */
    @NotNull
    public static Vector random(double x, double y, double z) {
        final Random random = new Random();
        
        return new Vector(random.nextDouble() * x, random.nextDouble() * y, random.nextDouble() * z);
    }
    
    /**
     * Computes a {@link Vector} pointing to the left of the given {@code origin} direction.
     *
     * <p>
     * The origin vector is normalized, flattened onto the {@code x/z} plane, rotated
     * 90 degrees around the Y axis, and scaled to the given magnitude.
     * </p>
     *
     * @param origin    - The origin direction vector.
     * @param magnitude - The resulting vector magnitude.
     * @return The left-direction vector.
     */
    @NotNull
    public static Vector left(@NotNull @Mutates Vector origin, double magnitude) {
        return origin.normalize().setY(0).rotateAroundY(PiHelper.HALF_PI).multiply(magnitude);
    }
    
    /**
     * Computes a {@link Vector} pointing to the left of the given {@code origin} direction.
     *
     * <p>
     * The origin vector is normalized, flattened onto the {@code x/z} plane, rotated
     * -90 degrees around the Y axis, and scaled to the given magnitude.
     * </p>
     *
     * @param origin    - The origin direction vector.
     * @param magnitude - The resulting vector magnitude.
     * @return The right-direction vector.
     */
    @NotNull
    public static Vector right(@NotNull @Mutates Vector origin, double magnitude) {
        return origin.normalize().setY(0).rotateAroundY(-PiHelper.HALF_PI).multiply(magnitude);
    }
    
    /**
     * Computes a normalized direction {@link Vector} from one {@link Location} to another.
     *
     * <p>If both locations resolve to the same position, a {@link #zero()} vector is returned.</p>
     *
     * @param from - The starting location.
     * @param to   - The target location.
     * @return The normalized direction vector pointing from {@code from} to {@code to}.
     */
    @NotNull
    public static Vector directionTo(@NotNull Location from, @NotNull Location to) {
        final Vector vector = from.toVector().subtract(to.toVector());
        
        return vector.lengthSquared() > 0 ? vector.normalize() : zero();
    }
    
}
