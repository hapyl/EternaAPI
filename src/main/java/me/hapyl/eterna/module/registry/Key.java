package me.hapyl.eterna.module.registry;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.text.Capitalizable;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Represents a {@link Key} to identify a {@link Keyed} object.
 *
 * <p>
 * Keys must strictly follow the {@code ^[a-z0-9_]+$} pattern, and are immutable and case-sensitive.
 * </p>
 *
 * @see Key#ofString(String)
 * @see Key#ofStringOrNull(String)
 * @see Key#empty()
 * @see Keyed
 */
public class Key implements Capitalizable, Comparable<Key> {
    
    /**
     * Defines the pattern all {@link Key} must match.
     */
    public static final Pattern PATTERN = Pattern.compile("^[a-z0-9_]+$");
    
    /**
     * Defines the empty constant {@link String} for an {@link #empty()} {@link Key}.
     */
    public static final String EMPTY_KEY_CONSTANT = "_";
    
    /**
     * An empty {@link Key} singleton instance.
     *
     * @see #empty()
     */
    private final static Key EMPTY = new Key(EMPTY_KEY_CONSTANT);
    
    private final String key;
    
    /**
     * Creates a new {@link Key} from the given {@link String}.
     *
     * @param key - The string key.
     * @throws IllegalArgumentException if the key does not match the pattern.
     */
    public Key(@NotNull String key) {
        if (!PATTERN.matcher(key).matches()) {
            throw new IllegalArgumentException("Key `%s` doesn't match the pattern `%s`!".formatted(key, PATTERN.pattern()));
        }
        
        this.key = key;
    }
    
    /**
     * Gets the {@link String} key of this {@link Key}.
     *
     * @return the string key of this key object.
     */
    @NotNull
    public String getKey() {
        return key;
    }
    
    /**
     * Gets whether this {@link Key} is empty.
     *
     * @return {@code true} if this key is empty; {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
    
    /**
     * Gets the same {@link Key} object if it's not empty; throws an {@link IllegalArgumentException} otherwise.
     *
     * <p>
     * This is a helper method used for a strict non-empty key validations.
     * </p>
     *
     * @throws IllegalArgumentException if the key is empty.
     */
    @SelfReturn
    public Key nonEmpty() throws IllegalArgumentException {
        if (isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty!");
        }
        
        return this;
    }
    
    /**
     * Gets whether this {@link Key} matches the given {@link String}.
     *
     * @param string - The string to check.
     * @return {@code true} if the given string matches this key exactly; {@code false} otherwise.
     */
    public boolean isKeyMatches(@NotNull String string) {
        return key.equals(string);
    }
    
    /**
     * Gets the hash code of this {@link Key}.
     *
     * @return the hash code of this key.
     */
    @Override
    public final int hashCode() {
        return Objects.hashCode(key);
    }
    
    /**
     * Gets whether the given {@link Object} equals to this {@link Key}.
     *
     * @param object - The object to compare.
     * @return {@code true} if the given object is a key and their key matches; {@code false} otherwise.
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
        return Objects.equals(this.key, that.key);
    }
    
    /**
     * Gets the {@link String} representation of this {@link Key}.
     *
     * @return the string representation of this key.
     */
    @Override
    public String toString() {
        return key;
    }
    
    /**
     * Converts this {@link Key} into a {@link NamespacedKey}.
     *
     * <p>
     * The namespace of the {@link NamespacedKey} always belongs to {@link EternaPlugin}.
     * </p>
     *
     * @return a namespaced key with this key.
     */
    @NotNull
    public final NamespacedKey asNamespacedKey() {
        return BukkitUtils.createKey(key);
    }
    
    /**
     * Capitalized the {@link String} key of this {@link Key}.
     *
     * @return the capitalized value.
     */
    @NotNull
    @Override
    public String capitalize() {
        return Capitalizable.capitalize(key.replace("_", " "));
    }
    
    /**
     * Compares this {@link Key} to another.
     *
     * @param key - The key to compare to.
     * @return {@code 0} if the keys are identical, {@code < 0} if this key is shorter, {@code > 0} if this key is longer.
     */
    @Override
    public int compareTo(@NotNull Key key) {
        return this.key.compareTo(key.key);
    }
    
    /**
     * A static factory method for creating {@link Key} from the given {@link String}.
     *
     * @param string - The string to use as a key.
     * @return a new key object.
     * @throws IllegalArgumentException if the key does not match the pattern.
     */
    @NotNull
    public static Key ofString(@NotNull String string) {
        return new Key(string);
    }
    
    /**
     * A static factory method for creating {@link Key} from the given {@link String}.
     *
     * @param string - The string to use as a key.
     * @return a new key if the string matches the pattern; {@code null} otherwise.
     */
    @Nullable
    public static Key ofStringOrNull(@NotNull String string) {
        return isStringValid(string) ? new Key(string) : null;
    }
    
    /**
     * A static factory method for creating {@link Key} from the given {@link UUID}.
     *
     * <p>
     * The uuid will be lowercased and all occurrences of {@code -} will be replaced with {@code _}.
     * </p>
     *
     * @param uuid - The uuid to convert to a key.
     * @return a new key.
     */
    @NotNull
    public static Key ofUuid(@NotNull UUID uuid) {
        return ofString(uuid.toString()
                            .replace("-", "_")
                            .toLowerCase()
        );
    }
    
    /**
     * A static factory method for creating a random {@link Key}.
     *
     * <p>
     * This method uses a random {@link UUID} and converts it into a {@link Key}.
     * </p>
     *
     * <p>
     * Note that this is a method intended to be used for development only, since the sole point ot {@link Key}
     * of to identify objects, so using a random key defeats the purpose of that.
     * </p>
     *
     * @return a new random key.
     */
    @NotNull
    public static Key ofRandom() {
        return ofUuid(UUID.randomUUID());
    }
    
    /**
     * Gets the empty {@link Key} instance.
     *
     * <p>
     * The instance returned is guaranteed to return {@code true} when {@link #isEmpty()} is called.
     * </p>
     *
     * @return an empty key instance.
     */
    @NotNull
    public static Key empty() {
        return EMPTY;
    }
    
    /**
     * Gets whether the given {@link String} matches the {@link Key} pattern.
     *
     * @param string - The string to check.
     * @return {@code true} if the given string matches the key pattern; {@code false} otherwise.
     */
    public static boolean isStringValid(@NotNull String string) {
        return PATTERN.matcher(string).matches();
    }
}
