package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Allows creating upside down english text.
 */
public final class UpsideDownText {

    private static final Map<Character, Character> charMap = Map.ofEntries(
            Map.entry('a', 'ɐ'),
            Map.entry('b', 'q'),
            Map.entry('c', 'ɔ'),
            Map.entry('d', 'p'),
            Map.entry('e', 'ǝ'),
            Map.entry('f', 'ⅎ'),
            Map.entry('g', 'ƃ'),
            Map.entry('h', 'ɥ'),
            Map.entry('i', 'ᴉ'),
            Map.entry('j', 'ɾ'),
            Map.entry('k', 'ʞ'),
            Map.entry('l', 'ʅ'),
            Map.entry('m', 'ɯ'),
            Map.entry('n', 'u'),
            Map.entry('o', 'o'),
            Map.entry('p', 'd'),
            Map.entry('q', 'b'),
            Map.entry('r', 'ɹ'),
            Map.entry('s', 's'),
            Map.entry('t', 'ʇ'),
            Map.entry('u', 'n'),
            Map.entry('v', 'ʌ'),
            Map.entry('w', 'ʍ'),
            Map.entry('x', 'x'),
            Map.entry('y', 'ʎ'),
            Map.entry('z', 'z')
    );

    /**
     * Creates an upside down text.
     *
     * @param string - String.
     * @return an upside down text.
     */
    @Nonnull
    public static String format(@Nonnull String string) {
        final StringBuilder builder = new StringBuilder();
        final char[] chars = string.toCharArray();

        for (char c : chars) {
            builder.append(charMap.getOrDefault(Character.toLowerCase(c), c));
        }

        return builder.toString();
    }

}
