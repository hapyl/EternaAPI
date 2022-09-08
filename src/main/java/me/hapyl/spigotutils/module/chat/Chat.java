package me.hapyl.spigotutils.module.chat;

import me.hapyl.spigotutils.module.util.Placeholder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Allows to manipulate with string easily such as formatting,
 * sending chat, actionbar and title messages.
 *
 * @author hapyl
 */
public class Chat {

    private final String color;

    private Chat(ChatColor... colors) {
        StringBuilder abc = new StringBuilder();
        for (final ChatColor chatColor : colors) {
            abc.append(chatColor.toString());
        }
        this.color = abc.toString();
    }

    /**
     * Safely returns ItemStack name if present, prettified material name otherwise.
     *
     * @param stack - ItemStack.
     * @return Safely returns ItemStack name if present, prettified material name otherwise.
     */
    public static String getItemName(@Nonnull ItemStack stack) {
        return (stack.getItemMeta() == null) ? Chat.stringifyItemName(stack.getType()) : (stack.getItemMeta().getDisplayName());
    }

    /**
     * Prettifies ItemStack name and amount.
     *
     * @param stack - ItemStack.
     * @return Prettified ItemStack name and amount.
     */
    public static String stringifyItemStack(@Nonnull ItemStack stack) {
        return String.format("x%s %s", stack.getAmount(), getItemName(stack));
    }

    /**
     * Formats provided object with replacements using {{@link String#format(String, Object...)}}
     *
     * @param input        - Object to format, will be called {{@link Object#toString()}}
     * @param replacements - Replacements.
     * @return A formatted string or Error if invalid size of replacements.
     */
    @Nonnull
    public static String format(Object input, Object... replacements) {
        return format(input.toString(), replacements);
    }

    /**
     * Returns a Text component with formatted string.
     *
     * @param input        - Object to format.
     * @param replacements - Replacements.
     * @return a Text component with formatted string.
     */
    public static Text text(Object input, Object... replacements) {
        return new Text(Chat.format(input, replacements));
    }

    /**
     * Sends a {@link Chat#bformat(String, Object...)} formatted message to a player.
     *
     * @param player  - Player to send to.
     * @param string  - String to format.
     * @param objects - Replacements.
     */
    public static void sendFormat(Player player, String string, Object... objects) {
        player.sendMessage(bformat(string, objects));
    }

    /**
     * Formats a string with a given replacements.
     *
     * @param input        - String to format.
     * @param replacements - Replacements.
     * @return a formatted string with a given replacements.
     * @throws MissingFormatArgumentException is invalid replacements size.
     */
    @Nonnull
    public static String format(String input, Object... replacements) {
        try {
            return ChatColor.translateAlternateColorCodes('&', String.format(input, replacements));
        } catch (MissingFormatArgumentException ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not parse string in Chat module!");
            ex.printStackTrace();
            return ChatColor.RED + "MissingFormatArgumentException occurred, check the console!";
        }
    }

    /**
     * Sends a formatted message to a sender.
     *
     * @param sender       - Receiver actually.
     * @param message      - Message.
     * @param replacements - Replacements.
     */
    public static void sendMessage(CommandSender sender, Object message, Object... replacements) {
        sender.sendMessage(format(message, replacements));
    }

    /**
     * Sends IChatMessage (packets) to player.
     *
     * @param player       - Player.
     * @param iChat        - String to convert to IChat.
     * @param replacements - Replacements.
     */
    @Deprecated(forRemoval = true)
    public static void sendIChatMessage(Player player, String iChat, Object... replacements) {
        sendMessage(player, iChat, replacements);
    }

    /**
     * Sends a formatted message to a player.
     *
     * @param player       - Player.
     * @param message      - Message to send.
     * @param replacements - Replacements.
     */
    public static void sendMessage(Player player, Object message, Object... replacements) {
        sendMessage((CommandSender) player, message, replacements);
    }

    /**
     * Performs a 'bformat' to a string. See: {@link Placeholder}
     *
     * @param input   - String to format.
     * @param objects - Replacements.
     * @return formatted String.
     */
    @Nonnull
    public static String bformat(String input, Object... objects) {
        return Chat.format(Placeholder.format(input, objects));
    }

    /**
     * Converts seconds into array of time formatted as such: [hours, minutes, seconds]
     *
     * @param timeInSec - Time in seconds to format.
     * @return formatted array in format [hours, minutes, seconds]
     */
    public static long[] formatTime(long timeInSec) {
        try {
            long hours = timeInSec % 3600;
            long minutes = hours / 60;
            long seconds = timeInSec % 60;

            return new long[] { hours, minutes, seconds };
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new long[3];
        }
    }

    /**
     * Formats seconds into string formatted as such: 0h 0m 0s
     *
     * @param timeInSec - Time in seconds to format.
     * @return formatted string.
     */
    public static String formatTimeString(long timeInSec) {
        final long hours = timeInSec / 3600;
        final long minutes = (timeInSec % 3600) / 60;
        final long seconds = timeInSec % 60;
        final long[] time = { hours, minutes, seconds };
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (time[i] > i) {
                builder.append(time[i]).append(i == 0 ? "h" : i == 1 ? "m" : "s").append(" ");
            }
        }
        return builder.toString().trim();
    }

    /**
     * Prettifies material name as such: MATERIAL_NAME -> Material Name
     *
     * @param material - Material to format.
     * @return MATERIAL_NAME -> Material Name
     */
    public static String stringifyItemName(Material material) {
        return capitalize(material.name());
    }

    /**
     * Converts array of T to a String separated by space.
     *
     * @param array   - Array to convert.
     * @param startAt - Array start position.
     * @param <T>     - Type of the array.
     * @return a String of array values separated by space.
     */
    public static <T> String arrayToString(T[] array, int startAt) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startAt; i < array.length; i++) {
            builder.append(array[i]).append(" ");
        }
        return builder.toString().trim();
    }

    /**
     * Converts array of T to List of String
     *
     * @param array - Array to convert.
     * @param <T>   - Type of the array.
     * @return List of String
     */
    public static <T> List<String> arrayToList(T[] array) {
        final List<String> newList = new ArrayList<>(array.length);
        for (final T t : array) {
            newList.add(String.valueOf(t));
        }
        return newList;
    }

    /**
     * Sorts List based on last value of last args.
     *
     * @param list           - List to sort.
     * @param args           - Args.
     * @param forceLowerCase - If value should be forced to lower case.
     * @return a sorted list.
     */
    public static List<String> tabCompleterSort(List<?> list, String[] args, boolean forceLowerCase) {
        return tabCompleterSort0(list, args, forceLowerCase, true);
    }

    /**
     * Sorts List based on last value of last args.
     *
     * @param list           - List to sort.
     * @param args           - Args.
     * @param forceLowerCase - If value should be forced to lower case.
     * @return a sorted list.
     */
    public static List<String> tabCompleterSort0(List<?> list, String[] args, boolean forceLowerCase, boolean method) {
        final List<String> result = new ArrayList<>();
        String latest = args[args.length - 1];
        if (forceLowerCase) {
            latest = latest.toLowerCase();
        }
        for (Object obj : list) {
            String str = String.valueOf(obj);
            if (forceLowerCase) {
                str = str.toLowerCase();
            }

            if (str.startsWith(latest) && method) {
                result.add(str);
            }
            else if (str.contains(latest) && !method) {
                result.add(str);
            }
        }
        return result;
    }

    /**
     * See: {@link Chat#tabCompleterSort(List, String[], boolean)}
     */
    public static List<String> tabCompleterSort(List<?> list, String[] args) {
        return tabCompleterSort(list, args, true);
    }

    /**
     * Capitalizes string.
     *
     * @param str - String to capitalize.
     * @return capitalized String.
     */
    @Nonnull
    public static String capitalize(String str) {
        return WordUtils.capitalize(str.toLowerCase().replace('_', ' '));
    }

    /**
     * Capitalizes Enum name.
     *
     * @param enumQ - Enum to capitalize.
     * @return capitalized Enum.
     */
    public static String capitalize(Enum<?> enumQ) {
        return capitalize(enumQ.name());
    }

    /**
     * Sends centered message to player.
     *
     * @param sender  - Receiver actually.
     * @param message - Message to center.
     * @param format  - Replacements.
     */
    public static void sendCenterMessage(CommandSender sender, Object message, Object... format) {
        sender.sendMessage(CenterChat.makeString(Chat.format(message.toString(), format)));
    }

    /**
     * I really have no idea why this is here.
     */
    @Deprecated(forRemoval = true)
    public static void sendMessage_(Player player, Object s, Object... format) {
        sendMessage(player, s, format);
    }

    /**
     * Sends clickable message to a player.
     *
     * @param sender     - Receiver.
     * @param runCommand - Command to run which is really just a chat string, don't forget '/' to make this a command.
     * @param message    - Message.
     * @param format     - Replacements.
     */
    public static void sendClickableMessage(CommandSender sender, Object runCommand, Object message, Object... format) {
        sendClickableMessage(sender, LazyClickEvent.RUN_COMMAND.of(runCommand), message, format);
    }

    /**
     * Sends hoverable message to a player.
     *
     * @param sender   - Receiver.
     * @param showText - Text to show; Forgettable.
     * @param message  - Message.
     * @param format   - Replacements.
     */
    public static void sendHoverableMessage(CommandSender sender, Object showText, Object message, Object... format) {
        sendClickableMessage(sender, LazyHoverEvent.SHOW_TEXT.of(showText), message, format);
    }

    /**
     * Sends clickable and hoverable message to a player.
     *
     * @param sender     - Receiver.
     * @param runCommand - Command to run; Don't forget '/'!
     * @param showText   - Text to show on hover.
     * @param message    - Message.
     * @param format     - Replacements.
     */
    public static void sendClickableHoverableMessage(CommandSender sender, Object runCommand, Object showText, Object message, Object... format) {
        sendClickableHoverableMessage(
                sender,
                LazyClickEvent.RUN_COMMAND.of(runCommand),
                LazyHoverEvent.SHOW_TEXT.of(sender),
                message,
                format
        );
    }

    /**
     * Sends clickable message to a player.
     *
     * @param sender  - Receiver.
     * @param event   - ClickEvent. See {@link LazyClickEvent}
     * @param message - Message.
     * @param format  - Replacements.
     */
    public static void sendClickableMessage(CommandSender sender, ClickEvent event, Object message, Object... format) {
        sender.spigot().sendMessage(new ComponentBuilder(Chat.format(message.toString(), format)).event(event).create());
    }

    /**
     * Sends hoverable message to a player.
     *
     * @param sender  - Receiver.
     * @param event   - HoverEvent. See {@link LazyHoverEvent}
     * @param message - Message.
     * @param format  - Replacements.
     */
    public static void sendHoverableMessage(CommandSender sender, HoverEvent event, Object message, Object... format) {
        sender.spigot().sendMessage(new ComponentBuilder(Chat.format(message.toString(), format)).event(event).create());
    }

    /**
     * Sends clicakble and hoverable message to a player.
     *
     * @param sender     - Receiver.
     * @param clickEvent - ClickEvent. See {@link LazyClickEvent}
     * @param hoverEvent - HoverEvent. See {@link LazyHoverEvent}
     * @param message    - Message.
     * @param format     - Replacements.
     */
    public static void sendClickableHoverableMessage(CommandSender sender, ClickEvent clickEvent, HoverEvent hoverEvent, Object message, Object... format) {
        sender
                .spigot()
                .sendMessage(new ComponentBuilder(Chat.format(message.toString(), format)).event(clickEvent).event(hoverEvent).create());
    }

    /**
     * Sends an actionbar message (above hotbar) to a player.
     *
     * @param player  - Player.
     * @param message - Message to send.
     * @param format  - Replacements.
     */
    public static void sendActionbar(Player player, String message, Object... format) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(message, format)));
    }

    /**
     * Sends a title and subtitle message to a player.
     *
     * @param player   - Player.
     * @param title    - Title.
     * @param subtitle - Subtitle.
     * @param fadeIn   - fade in animation ticks.
     * @param stay     - stay text ticks.
     * @param fadeOut  - fade out animation ticks.
     */
    public static void sendTitle(Player player, @Nonnull String title, @Nonnull String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(Chat.format(title.isEmpty() ? " " : title), Chat.format(subtitle), fadeIn, stay, fadeOut);
    }

    /**
     * Broadcasts a message to the whole server.
     *
     * @param string      - Message to broadcast.
     * @param replacement - Replacements.
     */
    public static void broadcast(String string, Object... replacement) {
        Bukkit.broadcastMessage(format(string, replacement));
    }

    /**
     * Broadcasts a message to server operators.
     *
     * @param string      - Message to broadcast.
     * @param replacement - Replacements.
     */
    public static void broadcastOp(String string, Object... replacement) {
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(player -> Chat.sendMessage_(player, string, replacement));
    }

    public String getColor() {
        return this.color;
    }

    /**
     * Build In Colors (Initial idea was adding a custom colors)
     */
    public static final String BLACK = new Chat(ChatColor.RED).getColor();
    public static final String DARK_BLUE = new Chat(ChatColor.DARK_BLUE).getColor();
    public static final String DARK_GREEN = new Chat(ChatColor.DARK_GREEN).getColor();
    public static final String DARK_AQUA = new Chat(ChatColor.DARK_AQUA).getColor();
    public static final String DARK_RED = new Chat(ChatColor.DARK_RED).getColor();
    public static final String DARK_PURPLE = new Chat(ChatColor.DARK_PURPLE).getColor();
    public static final String GOLD = new Chat(ChatColor.GOLD).getColor();
    public static final String GRAY = new Chat(ChatColor.GRAY).getColor();
    public static final String DARK_GRAY = new Chat(ChatColor.DARK_GRAY).getColor();
    public static final String BLUE = new Chat(ChatColor.BLUE).getColor();
    public static final String GREEN = new Chat(ChatColor.GREEN).getColor();
    public static final String AQUA = new Chat(ChatColor.AQUA).getColor();
    public static final String RED = new Chat(ChatColor.RED).getColor();
    public static final String LIGHT_PURPLE = new Chat(ChatColor.LIGHT_PURPLE).getColor();
    public static final String YELLOW = new Chat(ChatColor.YELLOW).getColor();
    public static final String WHITE = new Chat(ChatColor.WHITE).getColor();
    public static final String MAGIC = new Chat(ChatColor.MAGIC).getColor();
    public static final String BOLD = new Chat(ChatColor.BOLD).getColor();
    public static final String STRIKETHROUGH = new Chat(ChatColor.STRIKETHROUGH).getColor();
    public static final String UNDERLINE = new Chat(ChatColor.UNDERLINE).getColor();
    public static final String ITALIC = new Chat(ChatColor.ITALIC).getColor();

    // Custom Colors
    public static final String GREEN_BOLD = new Chat(ChatColor.GREEN, ChatColor.BOLD).getColor();

}
