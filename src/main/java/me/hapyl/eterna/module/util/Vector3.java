package me.hapyl.eterna.module.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * An immutable vector that holds three {@link Double} coordinates with support for basic vector math.
 */
public class Vector3 {
    
    private final double x;
    private final double y;
    private final double z;
    
    /**
     * Creates a new immutable {@link Vector3}.
     *
     * @param x - The {@code X} coordinate.
     * @param y - The {@code Y} coordinate.
     * @param z - The {@code Z} coordinate.
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Adds another vector to this vector.
     *
     * @param other - The vector to add.
     * @return a new {@link Vector3} containing the sum.
     */
    @Nonnull
    public Vector3 add(@Nonnull Vector3 other) {
        return add(other.x, other.y, other.z);
    }
    
    /**
     * Adds the given components to this vector.
     *
     * @param x - The {@code X} component to add.
     * @param y - The {@code Y} component to add.
     * @param z - The {@code Z} component to add.
     * @return a new {@link Vector3} containing the sum.
     */
    @Nonnull
    public Vector3 add(double x, double y, double z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }
    
    /**
     * Subtracts another vector from this vector.
     *
     * @param other - The vector to subtract.
     * @return a new {@link Vector3} containing the difference.
     */
    @Nonnull
    public Vector3 subtract(@Nonnull Vector3 other) {
        return subtract(other.x, other.y, other.z);
    }
    
    /**
     * Subtracts the given components from this vector.
     *
     * @param x - The {@code X} component to subtract.
     * @param y - The {@code Y} component to subtract.
     * @param z - The {@code Z} component to subtract.
     * @return a new {@link Vector3} containing the difference.
     */
    @Nonnull
    public Vector3 subtract(double x, double y, double z) {
        return new Vector3(this.x - x, this.y - y, this.z - z);
    }
    
    /**
     * Multiplies this vector by another vector.
     *
     * @param other - The vector to multiply.
     * @return a new {@link Vector3} containing the product.
     */
    @Nonnull
    public Vector3 multiply(@Nonnull Vector3 other) {
        return multiply(other.x, other.y, other.z);
    }
    
    /**
     * Multiplies this vector by the given components.
     *
     * @param x - The {@code X} factor.
     * @param y - The {@code Y} factor.
     * @param z - The {@code Z} factor.
     * @return a new {@link Vector3} containing the product.
     */
    @Nonnull
    public Vector3 multiply(double x, double y, double z) {
        return new Vector3(this.x * x, this.y * y, this.z * z);
    }
    
    /**
     * Divides this vector by another vector.
     * <p>The division uses a safe-division, meaning dividing by {@code 0} will simply return {@code 0}, instead of throwing {@link ArithmeticException}.</p>
     *
     * @param other - The vector to divide by.
     * @return a new {@link Vector3} containing the quotient.
     */
    @Nonnull
    public Vector3 divide(@Nonnull Vector3 other) {
        return divide(other.x, other.y, other.z);
    }
    
    /**
     * Divides this vector by the given components.
     * <p>The division uses a safe-division, meaning dividing by {@code 0} will simply return {@code 0}, instead of throwing {@link ArithmeticException}.</p>
     *
     * @param x - The {@code X} divisor.
     * @param y - The {@code Y} divisor.
     * @param z - The {@code Z} divisor.
     * @return a new {@link Vector3} containing the quotient.
     */
    @Nonnull
    public Vector3 divide(double x, double y, double z) {
        return new Vector3(divide(this.x, x), divide(this.y, y), divide(this.z, z));
    }
    
    /**
     * Gets the length of this vector.
     *
     * @return length of this vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }
    
    /**
     * Gets the squared length of this vector.
     *
     * @return squared length of this vector.
     */
    public double lengthSquared() {
        return square(this.x) + square(this.y) + square(this.z);
    }
    
    /**
     * Gets the distance between this vector and another.
     *
     * @param other - The vector to measure distance to.
     * @return distance to {@code other}.
     */
    public double distance(@Nonnull Vector3 other) {
        return Math.sqrt(distanceSquared(other));
    }
    
    /**
     * Gets the squared distance between this vector and another.
     *
     * @param other - The vector to measure distance to.
     * @return squared distance to {@code other}.
     */
    public double distanceSquared(@Nonnull Vector3 other) {
        return square(this.x - other.x) + square(this.y - other.y) + square(this.z - other.z);
    }
    
    /**
     * Gets the midpoint between this vector and another.
     *
     * @param other - The vector to find midpoint with.
     * @return a new {@link Vector3} representing the midpoint.
     */
    @Nonnull
    public Vector3 midpoint(@Nonnull Vector3 other) {
        return new Vector3(
                (this.x + other.x) * 0.5,
                (this.y + other.y) * 0.5,
                (this.z + other.z) * 0.5
        );
    }
    
    /**
     * Converts this vector to a {@link Location}.
     *
     * @param world - The world of location.
     * @return a new {@link Location}.
     */
    @Nonnull
    public Location toLocation(@Nonnull World world) {
        return new Location(world, this.x, this.y, this.z, 0.0f, 0.0f);
    }
    
    /**
     * Gets this vector normalized to unit length.
     * <p>If the length is {@code 0}, a {@link #zero()} vector is returned.</p>
     *
     * @return a new normalized {@link Vector3}.
     */
    @Nonnull
    public Vector3 normalized() {
        final double length = length();
        
        return length != 0.0 ? new Vector3(this.x / length, this.y / length, this.z / length) : zero();
    }
    
    /**
     * Gets a string representation of this {@link Vector3} with two decimal precision.
     *
     * @return a string representation of this {@link Vector3}.
     */
    @Nonnull
    @Override
    public String toString() {
        return "Vector3[%.2f, %.2f, %.2f]".formatted(this.x, this.y, this.z);
    }
    
    /**
     * Gets the {@code X} coordinate.
     *
     * @return the {@code X} coordinate.
     */
    public double x() {
        return x;
    }
    
    /**
     * Gets the {@code Y} coordinate.
     *
     * @return the {@code Y} coordinate.
     */
    public double y() {
        return y;
    }
    
    /**
     * Gets the {@code Z} coordinate.
     *
     * @return the {@code Z} coordinate.
     */
    public double z() {
        return z;
    }
    
    /**
     * Gets whether this vector is equal to another.
     *
     * @param o - The other object.
     * @return {@code true} if the vectors match, {@code false} otherwise.
     */
    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final Vector3 that = (Vector3) o;
        return Double.compare(this.x, that.x) == 0 && Double.compare(this.y, that.y) == 0 && Double.compare(this.z, that.z) == 0;
    }
    
    /**
     * Gets the hash code of this vector.
     *
     * @return the hash code of this vector.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(x, y, z);
    }
    
    /**
     * Creates a new {@link Vector3} from the given coordinates.
     *
     * @param x - The {@code X} coordinate.
     * @param y - The {@code Y} coordinate
     * @param z - The {@code Z} coordinate
     * @return a new {@link Vector3}.
     */
    @Nonnull
    public static Vector3 of(double x, double y, double z) {
        return new Vector3(x, y, z);
    }
    
    /**
     * Creates a new {@link Vector3} from the given {@link Location}.
     *
     * @param location - Location.
     * @return a new vector3.
     */
    @Nonnull
    public static Vector3 of(@Nonnull Location location) {
        return new Vector3(location.getX(), location.getY(), location.getZ());
    }
    
    /**
     * Creates a new {@link Vector3} from the given {@link Vector}.
     *
     * @param vector - Vector.
     * @return a new vector3.
     */
    @Nonnull
    public static Vector3 of(@Nonnull Vector vector) {
        return new Vector3(vector.getX(), vector.getY(), vector.getZ());
    }
    
    /**
     * Gets a zero {@link Vector3}.
     *
     * @return a new zero {@link Vector3}.
     */
    @Nonnull
    public static Vector3 zero() {
        return new Vector3(0.0, 0.0, 0.0);
    }
    
    private static double square(double v) {
        return v * v;
    }
    
    private static double divide(double a, double b) {
        return b != 0.0 ? a / b : 0.0;
    }
    
}
