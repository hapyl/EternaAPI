package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.Keyed;

import javax.annotation.Nonnull;

/**
 * A utility class for converting {@link Keyed} objects to strings with various formatting options.
 * <p>
 * This class allows easy conversion of Bukkit {@code Keyed} objects to strings, with options
 * to strip the "minecraft:" namespace, change the case of the string, or capitalize it.
 */
public final class KeyedToString {

    private final Keyed keyed;

    private boolean stripMinecraft;
    private byte stringCase;

    KeyedToString(Keyed keyed) {
        this.keyed = keyed;
        this.stripMinecraft = false;
        this.stringCase = 0;
    }

    /**
     * Strips the "minecraft:" namespace from the key if present.
     */
    public KeyedToString stripMinecraft() {
        this.stripMinecraft = true;
        return this;
    }

    /**
     * Converts the key to lowercase.
     */
    public KeyedToString lowercase() {
        this.stringCase = 0;
        return this;
    }

    /**
     * Converts the key to uppercase.
     */
    public KeyedToString uppercase() {
        this.stringCase = 1;
        return this;
    }

    /**
     * Capitalizes the key, typically the first letter of each word.
     */
    public KeyedToString capitalize() {
        this.stringCase = 2;
        return this;
    }

    /**
     * Converts the {@link Keyed} object to a string with the specified formatting options.
     *
     * @return The formatted key as a string.
     */
    @Override
    public String toString() {
        String key = keyed.key().key().toString();

        if (stripMinecraft) {
            key = key.replace("minecraft:", "");
        }

        key = switch (stringCase) {
            case 0 -> key.toLowerCase();
            case 1 -> key.toUpperCase();
            case 2 -> Chat.capitalize(key);
            default -> key;
        };

        return key;
    }

    /**
     * Creates a new {@link KeyedToString} instance for the given {@link Keyed} object.
     *
     * @param keyed – The {@link Keyed} object to be converted to a string.
     * @return A new {@link KeyedToString} instance.
     */
    @Nonnull
    public static KeyedToString of(@Nonnull Keyed keyed) {
        return new KeyedToString(keyed);
    }

}