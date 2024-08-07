package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Utility class for formatting strings into small caps.
 */
public final class SmallCaps {

    /**
     * An <b>immutable</b> map containing 26 {@link SmallCapsChar} from a-z.
     */
    public static final Map<Character, SmallCapsChar> letters = Map.ofEntries(
            Map.entry('a', new SmallCapsChar('ᴀ', 5)),
            Map.entry('b', new SmallCapsChar('ʙ', 5)),
            Map.entry('c', new SmallCapsChar('ᴄ', 5)),
            Map.entry('d', new SmallCapsChar('ᴅ', 5)),
            Map.entry('e', new SmallCapsChar('ᴇ', 5)),
            Map.entry('f', new SmallCapsChar('ғ', 5)),
            Map.entry('g', new SmallCapsChar('ɢ', 5)),
            Map.entry('h', new SmallCapsChar('ʜ', 5)),
            Map.entry('i', new SmallCapsChar('ɪ', 5)),
            Map.entry('j', new SmallCapsChar('ᴊ', 5)),
            Map.entry('k', new SmallCapsChar('ᴋ', 5)),
            Map.entry('l', new SmallCapsChar('ʟ', 4)),
            Map.entry('m', new SmallCapsChar('ᴍ', 5)),
            Map.entry('n', new SmallCapsChar('ɴ', 5)),
            Map.entry('o', new SmallCapsChar('ᴏ', 5)),
            Map.entry('p', new SmallCapsChar('ᴘ', 5)),
            Map.entry('q', new SmallCapsChar('ǫ', 3)),
            Map.entry('r', new SmallCapsChar('ʀ', 1)),
            Map.entry('s', new SmallCapsChar('s', 5)),
            Map.entry('t', new SmallCapsChar('ᴛ', 5)),
            Map.entry('u', new SmallCapsChar('ᴜ', 5)),
            Map.entry('v', new SmallCapsChar('ᴠ', 4)),
            Map.entry('w', new SmallCapsChar('ᴡ', 5)),
            Map.entry('x', new SmallCapsChar('x', 1)),
            Map.entry('y', new SmallCapsChar('ʏ', 5)),
            Map.entry('z', new SmallCapsChar('ᴢ', 5))
    );

    @Nonnull
    public static String format(@Nonnull String string) {
        final StringBuilder builder = new StringBuilder();

        for (char c : string.toLowerCase().toCharArray()) {
            final SmallCapsChar smallCaps = letters.getOrDefault(c, null);

            builder.append(smallCaps != null ? smallCaps.c : c);
        }

        return builder.toString();
    }

    /**
     * Gets a {@link SmallCapsChar} by an english char.
     *
     * @param c - Char.
     * @return {@link SmallCapsChar} or null.
     */
    @Nullable
    public static SmallCapsChar getSmallCapsChar(char c) {
        return letters.get(Character.toLowerCase(c));
    }

    /**
     * Gets a {@link SmallCapsChar} by a small caps char.
     *
     * @param c - Small caps char.
     * @return {@link SmallCapsChar} or null.
     */
    @Nullable
    public static SmallCapsChar getSmallCapsCharBySmallCapsChar(char c) {
        for (SmallCapsChar ch : letters.values()) {
            if (ch.c == c) {
                return ch;
            }
        }

        return null;
    }

    /**
     * Represents a small caps character.
     *
     * @param c      - Character.
     * @param length - Length.
     */
    public record SmallCapsChar(char c, int length) {

    }

}
