package me.hapyl.eterna.module.math;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents an <b>immutable</b> {@link Vector3} that holds three {@link Double} coordinates with support for basic vector math.
 */
public class Vector3 {
    
    private final double x;
    private final double y;
    private final double z;
    
    /**
     * Creates a new {@link Vector3}.
     *
     * @param x - The {@code x} coordinate.
     * @param y - The {@code y} coordinate.
     * @param z - The {@code z} coordinate.
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Adds the given {@link Vector3} values.
     *
     * @param other - The vector to add.
     * @return a new vector containing the sum of the vector values.
     */
    @NotNull
    public Vector3 add(@NotNull Vector3 other) {
        return add(other.x, other.y, other.z);
    }
    
    /**
     * Adds the given components to this {@link Vector3}.
     *
     * @param x - The {@code x} component to add.
     * @param y - The {@code y} component to add.
     * @param z - The {@code z} component to add.
     * @return a new vector containing the sum.
     */
    @NotNull
    public Vector3 add(double x, double y, double z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }
    
    /**
     * Subtracts another {@link Vector3} from this {@link Vector3}.
     *
     * @param other - The vector to subtract.
     * @return a new vector containing the sum of the vector values.
     */
    @NotNull
    public Vector3 subtract(@NotNull Vector3 other) {
        return subtract(other.x, other.y, other.z);
    }
    
    /**
     * Subtracts the given components from this vector.
     *
     * @param x - The {@code x} component to subtract.
     * @param y - The {@code y} component to subtract.
     * @param z - The {@code z} component to subtract.
     * @return a new vector containing the sum.
     */
    @NotNull
    public Vector3 subtract(double x, double y, double z) {
        return new Vector3(this.x - x, this.y - y, this.z - z);
    }
    
    /**
     * Multiplies this {@link Vector3} by another {@link Vector3}.
     *
     * @param other - The vector to multiply.
     * @return a new vector containing the product.
     */
    @NotNull
    public Vector3 multiply(@NotNull Vector3 other) {
        return multiply(other.x, other.y, other.z);
    }
    
    /**
     * Multiplies this {@link Vector3} by the given components.
     *
     * @param x - The {@code x} factor.
     * @param y - The {@code y} factor.
     * @param z - The {@code z} factor.
     * @return a new vector containing the product.
     */
    @NotNull
    public Vector3 multiply(double x, double y, double z) {
        return new Vector3(this.x * x, this.y * y, this.z * z);
    }
    
    /**
     * Multiplies this {@link Vector3} by the given factor.
     *
     * @param factor - The factor by which to multiply.
     * @return a new vector containing the product.
     */
    @NotNull
    public Vector3 multiply(double factor) {
        return new Vector3(this.x * factor, this.y * factor, this.z * factor);
    }
    
    /**
     * Divides this {@link Vector3} by another {@link Vector3}.
     *
     * <p>
     * The division uses a safe-division, meaning dividing by {@code 0} will simply return {@code 0}, instead of throwing {@link ArithmeticException}.
     * </p>
     *
     * @param other - The vector to divide by.
     * @return a new vector containing the quotient.
     */
    @NotNull
    public Vector3 divide(@NotNull Vector3 other) {
        return divide(other.x, other.y, other.z);
    }
    
    /**
     * Divides this {@link Vector3} by the given components.
     *
     * <p>
     * The division uses a safe-division, meaning dividing by {@code 0} will simply return {@code 0}, instead of throwing {@link ArithmeticException}.
     * </p>
     *
     * @param x - The {@code x} divisor.
     * @param y - The {@code y} divisor.
     * @param z - The {@code z} divisor.
     * @return a new vector containing the quotient.
     */
    @NotNull
    public Vector3 divide(double x, double y, double z) {
        return new Vector3(sdiv(this.x, x), sdiv(this.y, y), sdiv(this.z, z));
    }
    
    /**
     * Divides this {@link Vector3} by the given divisor.
     *
     * <p>
     * The division uses a safe-division, meaning dividing by {@code 0} will simply return {@code 0}, instead of throwing {@link ArithmeticException}.
     * </p>
     *
     * @param divisor - The divisor by which to divide.
     * @return a new vector containing the quotient.
     */
    @NotNull
    public Vector3 divide(double divisor) {
        return new Vector3(sdiv(this.x, divisor), sdiv(this.y, divisor), sdiv(this.z, divisor));
    }
    
    /**
     * Gets the length of this {@link Vector3}.
     *
     * @return length of this vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }
    
    /**
     * Gets the squared length of this {@link Vector3}.
     *
     * @return squared length of this vector.
     */
    public double lengthSquared() {
        return Numbers.square(this.x) + Numbers.square(this.y) + Numbers.square(this.z);
    }
    
    /**
     * Gets the distance between this {@link Vector3} and another.
     *
     * @param other - The vector to measure distance to.
     * @return distance to {@code other}.
     */
    public double distance(@NotNull Vector3 other) {
        return Math.sqrt(distanceSquared(other));
    }
    
    /**
     * Gets the squared distance between this {@link Vector3} and another.
     *
     * @param other - The vector to measure distance to.
     * @return squared distance to {@code other}.
     */
    public double distanceSquared(@NotNull Vector3 other) {
        return Numbers.square(this.x - other.x) + Numbers.square(this.y - other.y) + Numbers.square(this.z - other.z);
    }
    
    /**
     * Gets the midpoint between this {@link Vector3} and another.
     *
     * @param other - The vector to find midpoint with.
     * @return a new vector representing the midpoint.
     */
    @NotNull
    public Vector3 midpoint(@NotNull Vector3 other) {
        return new Vector3((this.x + other.x) * 0.5, (this.y + other.y) * 0.5, (this.z + other.z) * 0.5);
    }
    
    /**
     * Calculates the dot product between the two {@link Vector3}.
     *
     * @param other - The other vector.
     * @return the dot product.
     */
    public double dot(@NotNull Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }
    
    /**
     * Calculates the cross product between the two {@link Vector3}.
     *
     * @param other - The other vector.
     * @return a new vector containing the cross product.
     */
    @NotNull
    public Vector3 crossProduct(@NotNull Vector3 other) {
        final double x = this.y * other.z - other.y * this.z;
        final double y = this.z * other.x - other.z * this.x;
        final double z = this.x * other.y - other.x * this.y;
        
        return new Vector3(x, y, z);
    }
    
    /**
     * Normalizes this {@link Vector3} by the unit length.
     *
     * <p>
     * If the length is {@code 0}, a {@link #zero()} vector is returned.
     * </p>
     *
     * @return a new normalized vector.
     */
    @NotNull
    public Vector3 normalize() {
        final double length = length();
        
        return length != 0.0 ? new Vector3(this.x / length, this.y / length, this.z / length) : zero();
    }
    
    /**
     * Converts this vector to a {@link Location}.
     *
     * @param world - The world of location.
     * @return a new location.
     */
    @NotNull
    public Location toLocation(@NotNull World world) {
        return new Location(world, this.x, this.y, this.z, 0.0f, 0.0f);
    }
    
    /**
     * Gets the {@code x} coordinate.
     *
     * @return the {@code x} coordinate.
     */
    public double x() {
        return x;
    }
    
    /**
     * Gets the {@code y} coordinate.
     *
     * @return the {@code y} coordinate.
     */
    public double y() {
        return y;
    }
    
    /**
     * Gets the {@code z} coordinate.
     *
     * @return the {@code z} coordinate.
     */
    public double z() {
        return z;
    }
    
    /**
     * Gets the hash code of this {@link Vector3}.
     *
     * @return the hash code of this vector.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }
    
    /**
     * Gets whether the given {@link Object} is {@link Vector3} and their values match.
     *
     * @param o - The object to check.
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
     * Gets a string representation of this {@link Vector3} with two decimal precision.
     *
     * @return a string representation of this vector.
     */
    @NotNull
    @Override
    public String toString() {
        return "Vector3[%.2f, %.2f, %.2f]".formatted(this.x, this.y, this.z);
    }
    
    /**
     * A static factory method for creating {@link Vector3}.
     *
     * @param x - The {@code x} coordinate.
     * @param y - The {@code y} coordinate
     * @param z - The {@code z} coordinate
     * @return a new vector.
     */
    @NotNull
    public static Vector3 of(double x, double y, double z) {
        return new Vector3(x, y, z);
    }
    
    /**
     * A static factory method for creating {@link Vector3} from the given {@link Location}.
     *
     * @param location - The location from which to create a vector.
     * @return a new vector.
     */
    @NotNull
    public static Vector3 ofLocation(@NotNull Location location) {
        return new Vector3(location.getX(), location.getY(), location.getZ());
    }
    
    /**
     * A static factory method for creating {@link Vector3} from the given {@link Vector}.
     *
     * @param vector - The vector from which to create a vector.
     * @return a new vector.
     */
    @NotNull
    public static Vector3 ofVector(@NotNull Vector vector) {
        return new Vector3(vector.getX(), vector.getY(), vector.getZ());
    }
    
    /**
     * Gets a zero {@link Vector3}.
     *
     * @return a new zero vector.
     */
    @NotNull
    public static Vector3 zero() {
        return new Vector3(0.0, 0.0, 0.0);
    }
    
    /**
     * Gets a {@link Vector3} that points up.
     *
     * @return a new vector that points up.
     */
    @NotNull
    public static Vector3 up() {
        return new Vector3(0.0, 1.0, 0.0);
    }
    
    private static double sdiv(double a, double b) {
        return b != 0.0 ? a / b : 0.0;
    }
    
}
