package me.hapyl.spigotutils;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Represents API logger.
 */
public class EternaLogger {

    public static final String PREFIX = "&b&lEternaAPI&b> &a";
    public static final String DEBUG_PREFIX = PREFIX + "&c&lDEBUG&4> &7&o";
    public static final String TEST_PREFIX = PREFIX + "&2&lTEST&2> &a";

    public static void broadcastMessageOP(String string, Object... format) {
        Chat.broadcastOp(PREFIX + string, format);
    }

    public static void broadcastMessageConsole(String string, Object... format) {
        Chat.sendMessage(Bukkit.getConsoleSender(), PREFIX + string, format);
    }

    public static void sendMessage(CommandSender player, String message, Object... format) {
        Chat.sendMessage(player, PREFIX + message, format);
    }

    public static void info(String message, Object... format) {
        EternaPlugin.getPlugin().getLogger().info(message.formatted(format));
    }

    public static void warn(String message, Object... format) {
        EternaPlugin.getPlugin().getLogger().warning(message.formatted(format));
    }

    public static void severe(String message, Object... format) {
        EternaPlugin.getPlugin().getLogger().severe(message.formatted(format));
    }

    public static void exception(Exception e) {
        if (!EternaPlugin.getPlugin().getConfig().getBoolean("dev.print-stack-traces")) {
            return;
        }

        severe("An exception has occurred!");
        e.printStackTrace();
    }

    public static void debug(Object message, Object... format) {
        final String formatted = message.toString().formatted(format);

        info(DEBUG_PREFIX + formatted);
        Chat.broadcastOp(DEBUG_PREFIX + formatted);
    }

    public static void test(String s) {
        info(TEST_PREFIX + s);
        Chat.broadcastOp(TEST_PREFIX + s);
    }
}
