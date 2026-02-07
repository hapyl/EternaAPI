package me.hapyl.eterna.module.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an {@link Object} that may be identified with a {@link Key}.
 */
public interface Keyed extends KeyLike {
    
    /**
     * Gets the {@link Key} of this {@link Keyed} object.
     *
     * <p>
     * It is a good practice to {@code finalize} the method, preventing overringing it.
     * </p>
     */
    @NotNull
    Key getKey();
    
    /**
     * Gets the {@link Key} of this {@link Keyed}, as per {@link KeyLike} definition.
     *
     * @return the key of this keyed.
     */
    @NotNull
    @Override
    default Key key() {
        return this.getKey();
    }
    
    /**
     * Gets the {@link Key} of this {@link Keyed} as a {@link String}.
     *
     * <p>
     * This is a convenience method to avoid chain calling {@code getKey()}:
     * <pre>{@code keyed.getKey().getKey()}</pre>
     * </p>
     *
     * @return the string of the key if this object.
     */
    @NotNull
    default String getKeyAsString() {
        return getKey().getKey();
    }
    
    /**
     * Gets the hash code of this {@link Keyed}.
     *
     * <p>
     * Implementation should generally override and {@code finalize} this method to compare the hash code of the underlying {@link Key}.
     * </p>
     *
     * <pre>{@code
     * @Override
     * public final int hashCode() {
     *     return Objects.hashCode(this.key);
     * }
     * }</pre>
     *
     * @return the hash code of the key.
     */
    int hashCode();
    
    /**
     * Gets whether the given {@link Object} matches this {@link Keyed}.
     *
     * <p>
     * Implementation should generally override and {@code finalize} this method to compare the {@link Key} of both {@link Keyed} objects.
     * </p>
     *
     * <pre>{@code
     * @Override
     * public final boolean equals(Object object) {
     *     if (this == object) {
     *         return true;
     *     }
     *
     *     if (object == null || getClass() != object.getClass()) {
     *         return false;
     *     }
     *
     *     final Keyed that = (Keyed) object;
     *     return Objects.equals(this.key, that.key);
     * }
     * }</pre>
     *
     * @param object - The object to compare to.
     * @return {@code true} if the objects have identical keys; {@code false} otherwise.
     */
    boolean equals(@Nullable Object object);
}
