package me.hapyl.eterna.module.registry;

import org.apache.commons.lang.IllegalClassException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an {@link Enum} that can be identified with a {@link Key}.
 */
public interface KeyedEnum extends Keyed {

    /**
     * Gets the {@link Key} of this {@link Enum}.
     * <p>
     * The default implementation is as follows:
     * <pre>{@code
     * if (this instanceof Enum<?> anEnum) {
     *     return Key.ofString(anEnum.name().toLowerCase());
     * }
     *
     * throw new IllegalClassException(getClass().getSimpleName() + " must be an enum!");
     * }</pre>
     *
     * @return the {@link Key} of this {@link Enum}.
     * @throws IllegalCallerException   If the implementing class is not an {@link Enum}.
     * @throws IllegalArgumentException If the {@link Enum} name is invalid.
     */
    @Nonnull
    @Override
    default Key getKey() {
        if (this instanceof Enum<?> anEnum) {
            return Key.ofString(anEnum.name().toLowerCase());
        }

        throw new IllegalClassException(getClass().getSimpleName() + " must be an enum!");
    }

    /**
     * Gets an {@link Enum} matching the given {@link Key}, or null if it doesn't exist.
     *
     * @param enumClass - Enum class.
     * @param key       - Key to match.
     * @return an {@link Enum} matching the given {@link Key}, or null if it doesn't exist.
     */
    @Nullable
    static <E extends Enum<E> & Keyed> E of(@Nonnull Class<E> enumClass, @Nonnull Key key) {
        final E[] enumConstants = enumClass.getEnumConstants();

        for (E enumConstant : enumConstants) {
            if (enumConstant instanceof Keyed keyed && keyed.getKey().equals(key)) {
                return enumConstant;
            }
        }

        return null;
    }

}
