package me.hapyl.spigotutils;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Represents API logger.
 */
public class EternaLogger {

    public static final String PREFIX = "&b&lEternaAPI&b> &a";

    public static void broadcastMessageOP(String string, Object... format) {
        Chat.broadcastOp(PREFIX + string, format);
    }

    public static void broadcastMessageConsole(String string, Object... format) {
        Chat.sendMessage(Bukkit.getConsoleSender(), PREFIX + string, format);
    }

    public static void sendMessage(Player player, String message, Object... format) {
        Chat.sendMessage(player, PREFIX + message, format);
    }

    public static void info(String message, Object... format) {
        EternaPlugin.getPlugin().getLogger().info(message.formatted(format));
    }

    public static void warn(String message, Object... format) {
        EternaPlugin.getPlugin().getLogger().warning(message.formatted(format));
    }

    public static void error(String message, Object... format) {
        EternaPlugin.getPlugin().getLogger().severe(message.formatted(format));
    }

    public static void exception(Exception e) {
        error("An exception has occurred!");

        for (StackTraceElement stack : e.getStackTrace()) {
            error(stack.toString());
        }
    }
}
