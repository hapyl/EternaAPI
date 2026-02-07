package me.hapyl.eterna.module.text;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a utility class that allows to convert English text upside-down.
 */
@UtilityClass
public final class UpsideDownText {
    
    private static final Map<Character, Character> CHARS = Map.ofEntries(
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
    
    private UpsideDownText() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Formats the given {@link String} to make it upside-down.
     *
     * <p>
     * Unsupported characters are appended as-is.
     * </p>
     *
     * @param string - The string to turn upside-down.
     * @return an upside down text.
     */
    @NotNull
    public static String format(@NotNull String string) {
        final StringBuilder builder = new StringBuilder();
        final char[] chars = string.toCharArray();
        
        for (char c : chars) {
            builder.append(CHARS.getOrDefault(Character.toLowerCase(c), c));
        }
        
        return builder.toString();
    }
    
}
