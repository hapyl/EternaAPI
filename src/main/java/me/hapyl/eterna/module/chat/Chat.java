package me.hapyl.eterna.module.chat;

import me.hapyl.eterna.EternaLogger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * Provides easier {@link String} and minecraft chat manipulations.
 *
 * @author hapyl
 */
public final class Chat {
    
    public static final char ALTERNATE_COLOR_CHAR;
    
    private static final Method methodFromString;
    
    static {
        ALTERNATE_COLOR_CHAR = '&';
        
        try {
            final Class<?> clazz = Class.forName("org.bukkit.craftbukkit.util.CraftChatMessage");
            methodFromString = clazz.getDeclaredMethod("fromStringOrEmpty", String.class);
            
        }
        catch (Exception e) {
            throw EternaLogger.exception(e);
        }
    }
    
    private Chat() {
    }
    
    /**
     * Safely returns ItemStack name if present, prettified material name otherwise.
     *
     * @param stack - ItemStack.
     * @return Safely returns ItemStack name if present, prettified material name otherwise.
     */
    @Nonnull
    public static String getItemName(@Nonnull ItemStack stack) {
        final ItemMeta meta = stack.getItemMeta();
        
        return meta != null ? meta.getDisplayName() : stringifyItemName(stack.getType());
    }
    
    /**
     * Prettifies ItemStack name and amount.
     *
     * @param stack - ItemStack.
     * @return Prettified ItemStack name and amount.
     */
    @Nonnull
    public static String stringifyItemStack(@Nonnull ItemStack stack) {
        return String.format("x%s %s", stack.getAmount(), getItemName(stack));
    }
    
    /**
     * Formats the string by coloring it with {@link Chat#ALTERNATE_COLOR_CHAR} and {@link ColorBlockParser}.
     *
     * @param input - String to format.
     * @return a formatted string.
     */
    @Nonnull
    public static String format(@Nonnull Object input) {
        String string = color(input);
        
        // Parse color text if possible
        if (ColorBlockParser.canParse(string)) {
            string = ColorBlockParser.parse(string);
        }
        
        return string;
    }
    
    /**
     * Colors the given string with an {@link Chat#ALTERNATE_COLOR_CHAR}.
     *
     * @param string - String to color.
     * @return the colored string.
     */
    @Nonnull
    public static String color(@Nonnull Object string) {
        return ChatColor.translateAlternateColorCodes(ALTERNATE_COLOR_CHAR, String.valueOf(string));
    }
    
    /**
     * Returns a {@link Text} component from a give input.
     *
     * @param input - Object to format.
     * @return a Text component with formatted string.
     */
    @Nonnull
    public static Text text(@Nonnull Object input) {
        return new Text(Chat.format(input));
    }
    
    /**
     * Sends a formatted message to a sender.
     *
     * @param sender  - Receiver actually.
     * @param message - Message.
     */
    public static void sendMessage(@Nonnull CommandSender sender, @Nonnull Object message) {
        sender.sendMessage(format(message));
    }
    
    /**
     * Sends a formatted message to a player.
     *
     * @param player  - Player.
     * @param message - Message to send.
     */
    public static void sendMessage(@Nonnull Player player, @Nonnull Object message) {
        sendMessage((CommandSender) player, message);
    }
    
    /**
     * Converts seconds into an array of time formatted as such: [hours, minutes, seconds]
     *
     * @param sec - Time in seconds to format.
     * @return formatted array in format [hours, minutes, seconds]
     */
    public static long[] formatTime(long sec) {
        try {
            long hours = Math.max(0, sec / 3600);
            long minutes = Math.max(0, (sec % 3600) / 60);
            long seconds = Math.max(0, sec % 60);
            
            return new long[] { hours, minutes, seconds };
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            return new long[3];
        }
    }
    
    /**
     * Formats seconds into string formatted as such: 0h 0m 0s
     *
     * @param millis - Time in milliseconds to format.
     * @return formatted string.
     */
    @Nonnull
    public static String formatTimeString(long millis) {
        if (millis >= 3600000) {
            return new SimpleDateFormat("hh:mm:ss").format(millis);
        }
        else if (millis >= 60000) {
            return new SimpleDateFormat("mm:ss").format(millis);
        }
        return new SimpleDateFormat("ss").format(millis);
    }
    
    /**
     * Prettifies material name as such: MATERIAL_NAME -> Material Name
     *
     * @param material - Material to format.
     * @return MATERIAL_NAME -> Material Name
     */
    @Nonnull
    public static String stringifyItemName(@Nonnull Material material) {
        return capitalize(material.name());
    }
    
    /**
     * Converts an array of T to a String separated by space.
     *
     * @param array   - Array to convert.
     * @param startAt - Array start position.
     * @param <T>     - Type of the array.
     * @return a String of array values separated by space.
     */
    @Nonnull
    public static <T> String arrayToString(@Nonnull T[] array, int startAt) {
        final StringBuilder builder = new StringBuilder();
        
        for (int i = startAt; i < array.length; i++) {
            if (i != startAt) {
                builder.append(" ");
            }
            
            builder.append(array[i]);
        }
        
        return builder.toString().trim();
    }
    
    /**
     * Converts an array of T to List of String
     *
     * @param array - Array to convert.
     * @param <T>   - Type of the array.
     * @return List of String
     */
    @Nonnull
    public static <T> List<String> arrayToList(@Nonnull T[] array) {
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
     * @param forceLowerCase - If value should be forced to lowercase.
     * @return a sorted list.
     */
    @Nonnull
    public static List<String> tabCompleterSort(@Nonnull List<?> list, @Nonnull String[] args, boolean forceLowerCase) {
        return tabCompleterSort0(list, args, forceLowerCase, true);
    }
    
    /**
     * Sorts List based on last value of last args.
     *
     * @param list           - List to sort.
     * @param args           - Args.
     * @param forceLowerCase - If value should be forced to lowercase.
     * @param method         - Whether to use {@link String#startsWith(String)} or {@link String#contains(CharSequence)}.
     * @return a sorted list.
     */
    @Nonnull
    public static List<String> tabCompleterSort0(@Nonnull List<?> list, @Nonnull String[] args, boolean forceLowerCase, boolean method) {
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
     * @see Chat#tabCompleterSort(List, String[], boolean)
     */
    @Nonnull
    public static List<String> tabCompleterSort(@Nonnull List<?> list, @Nonnull String[] args) {
        return tabCompleterSort(list, args, true);
    }
    
    /**
     * Capitalizes string.
     *
     * @param str - String to capitalize.
     * @return capitalized String.
     */
    @Nonnull
    public static String capitalize(@Nonnull String str) {
        return WordUtils.capitalize(str.toLowerCase().replace('_', ' '));
    }
    
    /**
     * Capitalizes Enum name.
     *
     * @param enumQ - Enum to capitalize.
     * @return capitalized Enum.
     */
    @Nonnull
    public static String capitalize(@Nonnull Enum<?> enumQ) {
        return capitalize(enumQ.name());
    }
    
    /**
     * Sends centered message to player.
     *
     * @param sender  - Receiver actually.
     * @param message - Message to center.
     */
    public static void sendCenterMessage(@Nonnull CommandSender sender, @Nonnull Object message) {
        sender.sendMessage(CenterChat.makeString(Chat.format(message)));
    }
    
    /**
     * Broadcasts a centered message to all online players.
     *
     * @param message - Message.
     */
    public static void broadcastCenterMessage(@Nonnull Object message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendCenterMessage(player, message));
    }
    
    /**
     * Sends a clickable message to a player.
     *
     * @param sender     - Receiver.
     * @param runCommand - Command to run, which is really just a chat string, remember '/' to make this a command.
     * @param message    - Message.
     */
    public static void sendClickableMessage(@Nonnull CommandSender sender, @Nonnull String runCommand, @Nonnull String message) {
        sendClickableMessage(sender, LazyClickEvent.RUN_COMMAND.of(runCommand), message);
    }
    
    /**
     * Sends hoverable message to a player.
     *
     * @param sender   - Receiver.
     * @param showText - Text to show; Forgettable.
     * @param message  - Message.
     */
    public static void sendHoverableMessage(@Nonnull CommandSender sender, @Nonnull String showText, @Nonnull String message) {
        sendHoverableMessage(sender, LazyHoverEvent.SHOW_TEXT.of(showText), message);
    }
    
    /**
     * Sends a clickable and hoverable message to a player.
     *
     * @param sender     - Receiver.
     * @param runCommand - Command to run; Remember '/'!
     * @param showText   - Text to show on hover.
     * @param message    - Message.
     */
    public static void sendClickableHoverableMessage(@Nonnull CommandSender sender, @Nonnull String runCommand, @Nonnull String showText, @Nonnull String message) {
        sendClickableHoverableMessage(
                sender,
                LazyClickEvent.RUN_COMMAND.of(runCommand),
                LazyHoverEvent.SHOW_TEXT.of(showText),
                message
        );
    }
    
    /**
     * Sends a clickable message to a player.
     *
     * @param sender  - Receiver.
     * @param event   - ClickEvent.
     * @param message - Message.
     * @see LazyEvent
     * @see LazyClickEvent
     */
    public static void sendClickableMessage(@Nonnull CommandSender sender, @Nonnull ClickEvent event, @Nonnull Object message) {
        sender.spigot().sendMessage(
                new ComponentBuilder(Chat.format(message))
                        .event(event)
                        .create()
        );
    }
    
    /**
     * Sends hoverable message to a player.
     *
     * @param sender  - Receiver.
     * @param event   - HoverEvent.
     * @param message - Message.
     * @see LazyEvent
     * @see LazyHoverEvent
     */
    public static void sendHoverableMessage(@Nonnull CommandSender sender, @Nonnull HoverEvent event, @Nonnull Object message) {
        sender.spigot().sendMessage(
                new ComponentBuilder(Chat.format(message))
                        .event(event)
                        .create()
        );
    }
    
    /**
     * Sends clicakble and hoverable message to a player.
     *
     * @param sender     - Receiver.
     * @param clickEvent - ClickEvent.
     * @param hoverEvent - HoverEvent.
     * @param message    - Message.
     * @see LazyEvent
     * @see LazyClickEvent
     * @see LazyHoverEvent
     */
    public static void sendClickableHoverableMessage(@Nonnull CommandSender sender, @Nonnull ClickEvent clickEvent, @Nonnull HoverEvent hoverEvent, @Nonnull Object message) {
        sender.spigot().sendMessage(
                new ComponentBuilder(Chat.format(message))
                        .event(clickEvent)
                        .event(hoverEvent)
                        .create()
        );
    }
    
    /**
     * Sends an actionbar message (above hotbar) to a player.
     *
     * @param player  - Player.
     * @param message - Message to send.
     */
    public static void sendActionbar(@Nonnull Player player, @Nonnull Object message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(message)));
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
    public static void sendTitle(@Nonnull Player player, @Nonnull String title, @Nonnull String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(Chat.format(title.isEmpty() ? " " : title), Chat.format(subtitle), fadeIn, stay, fadeOut);
    }
    
    /**
     * Sends a title to all online players.
     *
     * @param title    - Title.
     * @param subtitle - Subtitle.
     * @param fadeIn   - fade in animation ticks.
     * @param stay     - stay text ticks.
     * @param fadeOut  - fade out animation ticks.
     */
    public static void sendTitles(@Nonnull String title, @Nonnull String subtitle, int fadeIn, int stay, int fadeOut) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        });
    }
    
    /**
     * Clears the title for the given player.
     *
     * @param player - Player to clear the title for.
     */
    public static void clearTitle(@Nonnull Player player) {
        player.resetTitle();
    }
    
    /**
     * Clears the title for each online player.
     */
    public static void clearTitles() {
        Bukkit.getOnlinePlayers().forEach(Player::resetTitle);
    }
    
    /**
     * Broadcasts a message to the whole server.
     *
     * @param string - Message to broadcast.
     */
    public static void broadcast(@Nonnull String string) {
        Bukkit.broadcastMessage(format(string));
    }
    
    /**
     * Broadcasts a message to server operators.
     *
     * @param string - Message to broadcast.
     */
    public static void broadcastOp(@Nonnull String string) {
        Bukkit.getOnlinePlayers()
              .stream()
              .filter(Player::isOp)
              .forEach(player -> Chat.sendMessage(player, string));
    }
    
    /**
     * Reverses the string completely.
     * <pre>
     *     Hello world -> dlrow olleH
     * </pre>
     *
     * @param input - String to reverse.
     * @return a reversed string.
     */
    @Nonnull
    public static String reverseString(@Nonnull String input) {
        final StringBuilder builder = new StringBuilder();
        final String[] strings = input.split(" ");
        
        for (int i = strings.length - 1; i >= 0; i--) {
            if (i != strings.length) {
                builder.append(" ");
            }
            
            final char[] chars = strings[i].toCharArray();
            
            for (int j = chars.length - 1; j >= 0; j--) {
                builder.append(chars[j]);
            }
        }
        
        return builder.toString().trim();
    }
    
    /**
     * Appends an integer with an appropriate <code>st</code>, <code>nd</code> or <code>th</code>.
     *
     * @param i - Integer.
     * @return an appended string.
     */
    @Nonnull
    public static String stNdTh(int i) {
        if (i >= 11 && i <= 13) {
            return i + "th";
        }
        
        final int lastDigit = i % 10;
        
        return i + switch (lastDigit) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
    
    /**
     * Returns a colored fractional string with the following format:
     * <pre>
     *    current/max
     * </pre>
     * <p>
     * The color of the <code>current</code> is base on a percent.
     *
     * @param current - Current.
     * @param max     - Max
     * @return a colored string.
     */
    @Nonnull
    public static String makeStringFractional(int current, int max) {
        final float percent = (float) current / max;
        final ChatColor color;
        
        if (percent >= 1.0f) {
            color = ChatColor.GREEN;
        }
        else if (percent >= 0.75f) {
            color = ChatColor.GOLD;
        }
        else if (percent >= 0.5f) {
            color = ChatColor.YELLOW;
        }
        else {
            color = ChatColor.RED;
        }
        
        return "%s%s&7/&a%s".formatted(color, current, max);
    }
    
    /**
     * Makes a string from the given collection, following the format:
     * <pre>
     *     First, Second, Third and Fourth
     * </pre>
     * <p>
     * Meaning appends commands between each element except the last one, where <code>and</code> is used instead of comma.
     *
     * @param collection - Collection to format.
     * @param fn         - Function on how to get the string.
     * @return a string.
     */
    @Nonnull
    public static <T> String makeStringCommaAnd(@Nonnull Collection<T> collection, @Nonnull Function<T, String> fn) {
        final StringBuilder builder = new StringBuilder();
        final int size = collection.size();
        int index = 0;
        
        for (T t : collection) {
            if (size == 1) {
                return fn.apply(t);
            }
            
            if (index == size - 1) {
                builder.append(" and ");
            }
            else if (index != 0) {
                builder.append(", ");
            }
            
            builder.append(fn.apply(t));
            ++index;
        }
        
        return builder.toString();
    }
    
    /**
     * Makes a string from the given array, following the format:
     * <pre>
     *     First, Second, Third and Fourth
     * </pre>
     * <p>
     * Meaning appends commands between each element except the last one, where <code>and</code> is used instead of comma.
     *
     * @param collection - Array to format.
     * @param fn         - Function on how to get the string.
     * @return a string.
     */
    @Nonnull
    public static <T> String makeStringCommaAnd(@Nonnull T[] collection, @Nonnull Function<T, String> fn) {
        return makeStringCommaAnd(List.of(collection), fn);
    }
    
    /**
     * Returns a formatted {@link Component}.
     *
     * @param newText - Text for format.
     * @return a formatted component.
     */
    @Nonnull
    public static Component component(@Nonnull String newText) {
        try {
            return (Component) methodFromString.invoke(null, Chat.format(newText));
        } catch (Exception e) {
            throw EternaLogger.exception(e);
        }
    }
    
}
