package me.hapyl.eterna.module.registry;

import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Capitalizable;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Represents a key to identify an object.
 *
 * @see Key#ofString(String)
 * @see Key#ofStringOrNull(String)
 * @see Keyed
 */
public class Key implements Capitalizable {
    
    /**
     * A pattern that all {@link Key} must match.
     */
    public static final Pattern PATTERN = Pattern.compile("^[a-z0-9_]+$");
    
    /**
     * A constant used for {@link #empty()} {@link Key}.
     */
    public static final String EMPTY_KEY_CONSTANT = "_";
    
    /**
     * An empty {@link Key} instance.
     *
     * @see #empty()
     */
    private final static Key EMPTY = new Key(EMPTY_KEY_CONSTANT);
    
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
     */
    @Override
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
     * Ensures that this key is not empty.
     * <p>If the key is empty, an {@link IllegalArgumentException} is thrown.</p>
     *
     * <p>This method returns {@code this} to allow for fluent method chaining.</p>
     *
     * @throws IllegalArgumentException – If the key is empty
     */
    @SelfReturn
    public Key nonEmpty() throws IllegalArgumentException {
        if (isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty!");
        }
        
        return this;
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
    public final boolean equals(Object object) {
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
    public final int hashCode() {
        return Objects.hashCode(key);
    }
    
    /**
     * Gets a {@link NamespacedKey} representation of this {@link Key}.
     * <p>The namespace always belongs to {@code Eterna}</p>
     *
     * @return a {@link NamespacedKey} representation of this {@link Key}.
     */
    @Nonnull
    public final NamespacedKey asNamespacedKey() {
        return BukkitUtils.createKey(key);
    }
    
    /**
     * Capitalized the {@link String} key of this {@link Key}.
     *
     * @return the capitalized value.
     */
    @Nonnull
    @Override
    public String capitalize() {
        return Capitalizable.capitalize(key.replace("_", " "));
    }
    
    /**
     * A factory method for creating {@link Key}s.
     * <p>This returns {@link #empty()} {@link Key} for empty strings and {@link #EMPTY_KEY_CONSTANT}.</p>
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
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Creates a new {@link Key} based on the given {@link UUID}.
     *
     * @param uuid - The uuid to convert to a {@link Key}.
     * @return a new key.
     */
    @Nonnull
    public static Key ofUuid(@Nonnull UUID uuid) {
        return ofString(uuid.toString()
                            .replace("-", "_")
                            .toLowerCase()
        );
    }
    
    /**
     * Creates a new random {@link Key}.
     *
     * @return a new random key.
     */
    @Nonnull
    public static Key ofRandom() {
        return ofUuid(UUID.randomUUID());
    }
    
    /**
     * Gets an empty {@link Key} instance.
     * <br>
     * The returned {@link Key} is guaranteed to return true when {@link #isEmpty()} is called.
     *
     * @return an empty ket.
     */
    @Nonnull
    public static Key empty() {
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
