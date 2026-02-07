package me.hapyl.eterna.module.text;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents a utility class that allows converting English {@link String} into a {@code ꜱᴍᴀʟʟ ᴄᴀᴘꜱ}.
 */
@UtilityClass
public final class SmallCaps {
    
    /**
     * An <b>immutable</b> {@link Map} containing 26 {@link SmallCapsChar} from {@code a} to {@code z}.
     */
    public static final Map<Character, SmallCapsChar> CHARS = Map.ofEntries(
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
    
    private SmallCaps() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Formats the given {@link String} into small caps.
     *
     * <p>
     * If the string contains non-English characters, they're appended as-is.
     * </p>
     *
     * @param string - The string to format.
     * @return the string as small caps.
     */
    @NotNull
    public static String format(@NotNull String string) {
        final StringBuilder builder = new StringBuilder();
        
        for (char c : string.toLowerCase().toCharArray()) {
            final SmallCapsChar smallCaps = CHARS.getOrDefault(c, null);
            
            builder.append(smallCaps != null ? smallCaps.ch : c);
        }
        
        return builder.toString();
    }
    
    /**
     * Gets the {@link SmallCapsChar} from an English character.
     *
     * @param ch - The character for which to retrieve a small caps char.
     * @return a small caps character, or {@code null} if the given char doesn't have a small caps equivalent.
     */
    @Nullable
    public static SmallCapsChar getSmallCapsChar(char ch) {
        return CHARS.get(Character.toLowerCase(ch));
    }
    
    /**
     * Gets the {@link SmallCapsChar} object by the given small caps char.
     *
     * <p>
     * This is a helper method to retrieve a {@link SmallCapsChar} in case it's {@link SmallCapsChar#length()} is needed.
     * </p>
     *
     * @param ch - The small caps character.
     * @return a small caps character, or {@code null} if the given char is not a small caps character.
     */
    @Nullable
    public static SmallCapsChar getSmallCapsCharBySmallCapsChar(char ch) {
        for (SmallCapsChar smallCapsChar : CHARS.values()) {
            if (smallCapsChar.ch == ch) {
                return smallCapsChar;
            }
        }
        
        return null;
    }
    
    /**
     * Represents a {@link SmallCapsChar} that contains a {@link Character} and {@code length}.
     *
     * @param ch     - The small caps character.
     * @param length - The length of the character.
     */
    public record SmallCapsChar(char ch, int length) {
    }
    
}
