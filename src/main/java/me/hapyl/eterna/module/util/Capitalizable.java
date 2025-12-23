package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;

/**
 * Represents an object whose value can be capitalized.
 */
public interface Capitalizable {
    
    /**
     * Capitalizes the underlying value.
     */
    @Nonnull
    String capitalize();
    
    /**
     * Capitalizes the given {@link String} by lowercasing it and
     * uppercasing the first character of each word.
     *
     * @param value - The string to capitalize.
     * @return the capitalized {@link String}.
     */
    @Nonnull
    static String capitalize(@Nonnull String value) {
        if (value.isEmpty()) {
            return value;
        }
        
        final String[] words = value.toLowerCase().split(" ");
        final StringBuilder builder = new StringBuilder();
        
        for (String word : words) {
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
            
            builder.append(" ");
        }
        
        return builder.toString().trim();
    }
    
    /**
     * Capitalizes the name of the given {@link Enum} by replacing underscores with spaces
     * and applying {@link #capitalize(String)}.
     *
     * @param enumValue - The enum value to capitalize.
     * @return the capitalized {@link Enum}.
     */
    @Nonnull
    static String capitalizeEnum(@Nonnull Enum<?> enumValue) {
        return capitalize(enumValue.name().replace("_", " "));
    }
    
}
