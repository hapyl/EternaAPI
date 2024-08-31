package me.hapyl.eterna.module.registry;

import me.hapyl.eterna.module.util.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a key to identify an object.
 *
 * @see Key#ofString(String)
 * @see Key#ofStringOrNull(String)
 * @see Keyed
 */
public class Key {

    /**
     * A pattern that all {@link Key} must match.
     */
    public static final Pattern PATTERN = Pattern.compile("^[a-z0-9_]+$");

    /**
     * An empty {@link Key} instance.
     *
     * @see #empty()
     */
    private static Key EMPTY;

    private final String key;

    public Key(@Nonnull String key) {
        Validate.isTrue(PATTERN.matcher(key).matches(), "Key '%s' doesn't match the pattern '%s'!".formatted(key, PATTERN.pattern()));
        this.key = key;
    }

    /**
     * Gets the actual {@link String} of this {@link Key}.
     *
     * @return the string of this key.
     */
    @Nonnull
    public String getKey() {
        return key;
    }

    /**
     * Gets the actual {@link String} of this {@link Key}.
     *
     * @return the string of this key.
     * @deprecated This is considered as a 'lazy' way of getting the key, prefer {@link #getKey()}.
     */
    @Override
    @Deprecated
    public String toString() {
        return key;
    }

    /**
     * Returns true if this key is empty.
     *
     * @return true if this key is empty.
     */
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    /**
     * Returns true if the given {@link String} matches the {@link Key} exactly.
     *
     * @param string - String to check.
     * @return true if the given string matches the key exactly.
     */
    public boolean isKeyMatches(@Nonnull String string) {
        return key.equals(string);
    }

    /**
     * Returns true if the given {@link String} matches the {@link Key} ignoring the case.
     *
     * @param string - String to check.
     * @return true if the given string matches the key ignoring the case.
     */
    public boolean isKeyMatchesIgnoreCase(@Nonnull String string) {
        return key.equalsIgnoreCase(string);
    }

    /**
     * Returns true if the given object is a {@link Key} and it matches the string of this key.
     *
     * @param object - Object to compare.
     * @return true if the given object is equals to this one.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final Key that = (Key) object;
        return Objects.equals(key, that.key);
    }

    /**
     * Gets the hash code of this key.
     *
     * @return the hash code of this key.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    /**
     * A factory method for creating {@link Key}s.
     *
     * @param string - Id.
     * @return a new string.
     * @throws IllegalArgumentException if the given string does not match the {@link #PATTERN}.
     */
    @Nonnull
    public static Key ofString(@Nonnull String string) {
        return new Key(string);
    }

    /**
     * A factory method for creating {@link Key}s.
     *
     * @param string - Id.
     * @return a new string or null, if the string is invalid.
     */
    @Nullable
    public static Key ofStringOrNull(@Nonnull String string) {
        try {
            return ofString(string);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets an empty {@link Key} instance.
     * <br>
     * The returned {@link Key} is guaranteed to return true when {@link #isEmpty()} is called.
     *
     * @return an empty ket.
     * @deprecated Keys exist for a reason, use {@link #empty()} only for testing or development!
     */
    @Nonnull
    @Deprecated
    public static Key empty() {
        if (EMPTY == null) {
            EMPTY = new Key("_");
        }

        return EMPTY;
    }

    /**
     * Returns true if the given string matches the {@link #PATTERN}.
     *
     * @param string - String to check.
     * @return true if the given string matches the {@link #PATTERN}, false otherwise.
     */
    public static boolean isStringValid(@Nonnull String string) {
        return PATTERN.matcher(string).matches();
    }
}
