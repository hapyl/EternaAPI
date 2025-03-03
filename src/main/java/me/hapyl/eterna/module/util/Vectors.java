package me.hapyl.eterna.module.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * A collection of helpful methods for working with {@link Vector}.
 */
public interface Vectors {

    /**
     * Gets a vector with all vectors being zero.
     */
    @Nonnull
    Vector ZERO = new Vector();

    /**
     * Gets a vector that points down.
     */
    @Nonnull
    Vector DOWN = new Vector(0, -1, 0);

    /**
     * Gets a vector that points up.
     */
    @Nonnull
    Vector UP = new Vector(0, 1, 0);

    /**
     * Gets a vector with gravity velocity.
     */
    @Nonnull
    Vector GRAVITY = new Vector(0, -BukkitUtils.GRAVITY, 0);

    /**
     * Gets a relative cuboid offset.
     */
    @Nonnull
    double[][] RELATIVE = {
            { -1.0d, 0.0d }, // -x, z
            { 1.0d, 0.0d },  // +x, z
            { 0.0d, -1.0d }, // x, -z
            { 0.0d, 1.0d },  // x, +z
            { 1.0d, 1.0d },  // +x, +z
            { -1.0d, 1.0d }, // -x, +z
            { 1.0d, -1.0d }, // +x, -z
            { -1.0d, -1.0d } // -x, -z
    };

    @Nonnull
    static Vector rotateX(@Nonnull Vector vector, double cos, double sin) {
        double y = vector.getY() * cos - vector.getZ() * sin;
        double z = vector.getY() * sin + vector.getZ() * cos;
        return vector.setY(y).setZ(z);
    }

    @Nonnull
    static Vector rotateY(@Nonnull Vector vector, double cos, double sin) {
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;
        return vector.setX(x).setZ(z);
    }

    @Nonnull
    static Vector rotateZ(@Nonnull Vector vector, double cos, double sin) {
        double x = vector.getX() * cos - vector.getY() * sin;
        double y = vector.getX() * sin + vector.getY() * cos;
        return vector.setX(x).setY(y);
    }

    /**
     * Gets a random {@link Vector} between <code>0.0-1.0</code>.
     *
     * @return a random vector.
     */
    @Nonnull
    static Vector random() {
        return random(1, 1);
    }

    /**
     * Gets a random {@link Vector} between <code>0.0-1.0</code>.
     *
     * @param xz - X and Z factor.
     * @param y  - Y factor.
     * @return a random vector.
     */
    @Nonnull
    static Vector random(double xz, double y) {
        return random(xz, y, xz);
    }

    /**
     * Gets a random {@link Vector} between <code>0.0-1.0</code>.
     *
     * @param x - X factor.
     * @param y - Y factor.
     * @param z - Z factor.
     * @return a random vector.
     */
    @Nonnull
    static Vector random(double x, double y, double z) {
        final Random random = new Random();

        return new Vector(random.nextDouble() * x, random.nextDouble() * y, random.nextDouble() * z);
    }

    /**
     * Gets a vector towards the left of the origin.
     *
     * @param origin    - Origin.
     * @param magnitude - Magnitude.
     * @return a vector towards the left of the origin.
     */
    @Nonnull
    static Vector left(@Nonnull Location origin, double magnitude) {
        return origin.getDirection().normalize().setY(0).rotateAroundY(Math.PI / 2).multiply(magnitude);
    }

    /**
     * Gets a vector towards the right of the origin.
     *
     * @param origin    - Origin.
     * @param magnitude - Magnitude.
     * @return a vector towards the right of the origin.
     */
    @Nonnull
    static Vector right(@Nonnull Location origin, double magnitude) {
        return origin.getDirection().normalize().setY(0).rotateAroundY(-Math.PI / 2).multiply(magnitude);
    }

    /**
     * Calculates the normalized direction vector from one location to another.
     *
     * <br>
     * The method returns a vector pointing from the {@code from} location to the {@code to} location.
     * If the locations are the same (i.e., the length of the vector is zero), the method returns {@link #ZERO}.
     *
     * @param from - The starting location.
     * @param to   - The target location.
     * @return The normalized direction vector from {@code from} to {@code to}, or {@link #ZERO} if the locations are the same.
     */
    @Nonnull
    static Vector directionTo(@Nonnull Location from, @Nonnull Location to) {
        final Vector vector = from.toVector().subtract(to.toVector());

        return vector.lengthSquared() > 0 ? vector.normalize() : ZERO;
    }

}
