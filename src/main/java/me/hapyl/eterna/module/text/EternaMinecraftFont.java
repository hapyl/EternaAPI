package me.hapyl.eterna.module.text;

import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an extension to {@link MinecraftFont} with a support of {@link SmallCaps}.
 */
public final class EternaMinecraftFont extends MinecraftFont {
    
    /**
     * Defines the sole {@link EternaMinecraftFont} instance.
     */
    @NotNull
    public static final EternaMinecraftFont INSTANCE = new EternaMinecraftFont();
    
    /**
     * Defines the length of the whitespace character.
     */
    public static final int WHITESPACE_LENGTH = 3;
    
    private EternaMinecraftFont() {
        super();
        
        // Instantiate SmallCaps
        SmallCaps.CHARS.forEach((c, ch) -> setChar(ch.ch(), new CharacterSprite(ch.length(), 1, new boolean[ch.length()])));
        
        super.malleable = false;
    }
    
    /**
     * Gets the length of the given {@link Character}.
     *
     * <p>
     * If the character is whitespace, this method always returns {@link #WHITESPACE_LENGTH}.
     * </p>
     *
     * @param ch - The character the get the length of.
     * @return the length of the given character.
     */
    public int charLength(final char ch) {
        if (ch == ' ') {
            return WHITESPACE_LENGTH;
        }
        
        final CharacterSprite characterSprite = getChar(ch);
        
        return characterSprite != null ? characterSprite.getWidth() + 1 : 1;
    }
    
}
