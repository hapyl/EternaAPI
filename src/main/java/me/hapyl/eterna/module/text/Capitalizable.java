package me.hapyl.eterna.module.text;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object whose {@link String} value can be capitalized.
 */
public interface Capitalizable {
    
    /**
     * Capitalizes the {@link String} value.
     */
    @NotNull
    String capitalize();
    
    /**
     * A static helper method for capitalizing the {@link String}.
     *
     * @param value - The string to capitalize.
     * @return the capitalized string.
     */
    @NotNull
    static String capitalize(@NotNull String value) {
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
     * A static helper method for capitalizing the {@link Enum} {@link Enum#name()}.
     *
     * @param enumValue - The enum to capitalize.
     * @return the capitalized enum name.
     */
    @NotNull
    static String capitalize(@NotNull Enum<?> enumValue) {
        return capitalize(enumValue.name().replace("_", " "));
    }
    
}
