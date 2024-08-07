package me.hapyl.eterna.module.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This util class is used to send perfectly centered messages in chat.
 * <p>
 * Original Post
 * <a href="https://spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872/">Original</a>
 *
 * @author SirSpoodles (Original Class)
 * @author hapyl (Added clickable impl)
 */
public class CenterChat {

    /**
     * The center chat pixel.
     * (By the original author.)
     */
    public final static int CENTER_PX = 154;

    /**
     * The center motD pixel.
     * (By hapyl via eyes.)
     */
    public final static int CENTER_PX_MOTD = 126;

    /**
     * Send a centered message to a player.
     *
     * @param player  - Player.
     * @param message - Message to center and send.
     */
    public static void sendCenteredMessage(@Nonnull Player player, @Nonnull String message) {
        player.sendMessage(makeString(message));
    }

    /**
     * Sends a centered clickable and hoverable message to a player.
     *
     * @param player  - Player.
     * @param message - Message.
     * @param hover   - Hover event.
     * @param click   Click event.
     */
    public static void sendCenteredClickableMessage(@Nonnull Player player, @Nonnull String message, @Nullable HoverEvent hover, @Nullable ClickEvent click) {
        final ComponentBuilder builder = new ComponentBuilder(makeString(message));
        if (hover != null) {
            builder.event(hover);
        }
        if (click != null) {
            builder.event(click);
        }
        player.spigot().sendMessage(builder.create());
    }

    /**
     * Creates a MotD centered message.
     *
     * @param message - Message to center.
     * @return a centered motD message.
     * @see #CENTER_PX_MOTD
     */
    @Nonnull
    public static String makeMotD(@Nonnull String message) {
        return makeString(message, CENTER_PX_MOTD);
    }

    /**
     * Creates a centered message with a default center px.
     *
     * @param message - Message to center.
     * @return a centered message.
     * @see #CENTER_PX
     */
    @Nonnull
    public static String makeString(@Nonnull String message) {
        return makeString(message, CENTER_PX);
    }

    /**
     * Creates a centered message with a custom center px.
     *
     * @param message  - Message.
     * @param centerPx - Center px.
     * @return a centered message.
     */
    @Nonnull
    public static String makeString(@Nonnull String message, int centerPx) {
        if (message.isEmpty()) {
            return "";
        }

        final MinecraftFont font = MinecraftFont.defaultFont;
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            }
            else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }
            else {
                messagePxSize += font.getCharLength(c, isBold);
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = centerPx - halvedMessageSize;
        int spaceLength = MinecraftFont.spaceLength + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb + message;
    }

}