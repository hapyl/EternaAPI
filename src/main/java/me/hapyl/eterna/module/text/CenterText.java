package me.hapyl.eterna.module.text;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class to perfectly center a {@link String} with a support of {@link SmallCaps}.
 *
 * <p>
 * The original post can be found <a href="https://spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872/">here</a>.
 * </p>
 *
 * @author SirSpoodles
 */
@UtilityClass
public final class CenterText {
    
    /**
     * Defines the center char pixel.
     */
    public final static int CENTER_PX = 154;
    
    /**
     * Defines the center motD pixel.
     */
    public final static int CENTER_PX_MOTD = 126;
    
    private CenterText() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Centers the given {@link String} with the given {@code centerPx}.
     *
     * @param string   - The string to center.
     * @param centerPx - The center pixel.
     * @return the centered string.
     */
    @NotNull
    public static String center(@NotNull String string, final int centerPx) {
        if (string.isEmpty()) {
            return "";
        }
        
        final EternaMinecraftFont minecraftFont = EternaMinecraftFont.INSTANCE;
        
        int messagePxSize = 0;
        
        for (final char ch : string.toCharArray()) {
            messagePxSize += minecraftFont.charLength(ch);
            messagePxSize++;
        }
        
        final int halvedMessageSize = messagePxSize / 2;
        final int toCompensate = centerPx - halvedMessageSize;
        final int spaceLength = EternaMinecraftFont.WHITESPACE_LENGTH + 1;
        
        int compensated = 0;
        
        final StringBuilder sb = new StringBuilder();
        
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        
        return sb + string;
    }
    
    /**
     * Centers the given {@link String} with the default {@link #CENTER_PX}.
     *
     * @param message - The message to center.
     * @return the centered string.
     */
    @NotNull
    public static String center(@NotNull String message) {
        return center(message, CENTER_PX);
    }
    
    /**
     * Centers the given {@link String} with the default {@link #CENTER_PX_MOTD}.
     *
     * @param motD - The motD to center.
     * @return the centered string.
     */
    @NotNull
    public static String centerMotD(@NotNull String motD) {
        return center(motD, CENTER_PX_MOTD);
    }
    
}