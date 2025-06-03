package me.hapyl.eterna.module.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * An immutable vector that holds three {@link Double} coordinates.
 *
 * @param x - X.
 * @param y - Y.
 * @param z - Z.
 */
public record Vector3(double x, double y, double z) {

    /**
     * Creates a new {@link Vector3} from the given doubles.
     *
     * @param x - X.
     * @param y - Y.
     * @param z - Z.
     * @return a new vector3.
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

}
