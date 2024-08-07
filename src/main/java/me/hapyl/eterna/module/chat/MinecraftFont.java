package me.hapyl.eterna.module.chat;

import me.hapyl.eterna.module.util.SmallCaps;

public class MinecraftFont extends org.bukkit.map.MinecraftFont {

    /**
     * The default Minecraft font with an addition of {@link SmallCaps}.
     */
    public static final MinecraftFont defaultFont = new MinecraftFont();

    /**
     * The length of a space.
     */
    public static final int spaceLength = 3;

    private MinecraftFont() {
        super();

        // Init small caps
        SmallCaps.letters.forEach((c, ch) -> {
            setChar(ch.c(), new CharacterSprite(ch.length(), 1, new boolean[ch.length()]));
        });
    }

    /**
     * Gets the length of a character.
     *
     * @param c - Character.
     * @return the length of a character.
     */
    public int getCharLength(char c) {
        return getCharLength(c, false);
    }

    /**
     * Gets the length of a character.
     *
     * @param c      - Character.
     * @param isBold - Should get length for a bold character?
     * @return the length of a character.
     */
    public int getCharLength(char c, boolean isBold) {
        if (' ' == c) {
            return spaceLength;
        }

        final CharacterSprite ch = getChar(c);

        return ch != null ? ch.getWidth() + (isBold ? 1 : 0) : 1;
    }

}
