package kz.hapyl.spigotutils.module.chat;

import kz.hapyl.spigotutils.module.annotate.NOTNULL;
import kz.hapyl.spigotutils.module.reflect.ReflectPacket;
import kz.hapyl.spigotutils.module.util.Placeholder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.UUID;

/**
 * Allows to manipulate with string easily such as formatting, sending chat, actionbar and title messages.
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

    public static String getItemName(ItemStack stack) {
        return (stack.getItemMeta() == null) ? Chat.stringifyItemName(stack.getType()) : (stack.getItemMeta()
                                                                                               .getDisplayName());
    }

    public static String stringifyItemStack(ItemStack stack) {
        return String.format("x%s %s", stack.getAmount(), getItemName(stack));
    }

    public String getColor() {
        return this.color;
    }

    @NOTNULL
    public static String format(Object input, Object... replacements) {
        return format(input.toString(), replacements);
    }

    public static Text text(Object input, Object... replacements) {
        return new Text(Chat.format(input, replacements));
    }

    public static void sendFormat(Player player, String string, Object... objects) {
        player.sendMessage(bformat(string, objects));
    }

    @NOTNULL
    public static String format(String input, Object... replacements) {
        try {
            return ChatColor.translateAlternateColorCodes('&', String.format(input, replacements));
        } catch (MissingFormatArgumentException ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not parse string in Chat module!");
            ex.printStackTrace();
            return ChatColor.RED + "MissingFormatArgumentException occurred, check the console!";
        }
    }

    // kz.hapyl.spigotutils.module.chat.Chat.sendMessage(Lorg/bukkit/entity/Player;Ljava/lang/Object;[Ljava/lang/Object;)V
    public static void sendMessage(CommandSender sender, Object message, Object... replacements) {
        sender.sendMessage(format(message, replacements));
    }

    public static void sendIChatMessage(Player player, String iChat, Object... replacements) {
        iChat = String.format(iChat, replacements);
        new ReflectPacket(new PacketPlayOutChat(IChatBaseComponent.a(iChat),
                                                net.minecraft.network.chat.ChatMessageType.b,
                                                UUID.randomUUID())).sendPackets(player);
    }

    public static void sendMessage(Player player, Object message, Object... replacements) {
        sendMessage((CommandSender) player, message, replacements);
    }

    @NOTNULL
    public static String bformat(String input, Object... objects) {
        return Chat.format(Placeholder.format(input, objects));
    }

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

    public static String stringifyItemName(Material material) {
        return capitalize(material.name());
    }

    public static List<String> tabCompleterSort(List<?> list, String[] args, boolean forceLowerCase) {
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
            if (str.startsWith(latest)) {
                result.add(str);
            }
        }
        return result;
    }

    public static <T> String arrayToString(T[] array, int startAt) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startAt; i < array.length; i++) {
            builder.append(array[i]).append(" ");
        }
        return builder.toString().trim();
    }

    public static List<String> tabCompleterSort(List<?> list, String[] args) {
        return tabCompleterSort(list, args, true);
    }

    public static <T> List<String> arrayToList(T[] array) {
        final List<String> newList = new ArrayList<>(array.length);
        for (final T t : array) {
            newList.add(String.valueOf(t));
        }
        return newList;
    }

    @NOTNULL
    public static String capitalize(String str) {
        return WordUtils.capitalize(str.toLowerCase().replace('_', ' '));
    }

    public static String capitalize(Enum<?> enumQ) {
        return capitalize(enumQ.name().toLowerCase().replace('_', ' '));
    }

    public static void sendCenterMessage(CommandSender sender, Object message, Object... format) {
        sender.sendMessage(CenterChat.makeString(Chat.format(message.toString(), format)));
    }

    public static void sendClickableMessage(Player player, Object s, Object... format) {
        player.sendMessage(Chat.format(s.toString(), format));
    }

    public static void sendClickableMessage(CommandSender sender, ClickEvent event, Object message, Object... format) {
        sender.spigot()
              .sendMessage(new ComponentBuilder(Chat.format(message.toString(), format)).event(event).create());
    }

    public static void sendHoverableMessage(CommandSender sender, HoverEvent event, Object message, Object... format) {
        sender.spigot()
              .sendMessage(new ComponentBuilder(Chat.format(message.toString(), format)).event(event).create());
    }

    public static void sendClickableHoverableMessage(CommandSender sender, ClickEvent clickEvent,
                                                     HoverEvent hoverEvent, Object message, Object... format) {
        sender.spigot().sendMessage(new ComponentBuilder(Chat.format(message.toString(), format))
                                            .event(clickEvent)
                                            .event(hoverEvent).create());
    }

    public static void sendActionbar(Player player, String message, Object... g) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(message, g)));
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(Chat.format(title.isEmpty() ? " " : title), Chat.format(subtitle), fadeIn, stay, fadeOut);
    }

    public static void broadcast(String string, Object... replacement) {
        Bukkit.broadcastMessage(format(string, replacement));
    }

    public static void broadcastOp(String string, Object... replacement) {
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp)
              .forEach(player -> Chat.sendClickableMessage(player, string, replacement));
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
