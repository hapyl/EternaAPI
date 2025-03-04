package me.hapyl.eterna;

import me.hapyl.eterna.module.annotate.ReturnValueOfThisMethodIsAnExceptionAndItMustBeThrown;
import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * Represents API logger.
 */
public final class EternaLogger {

    public static final String PREFIX = "&b&lEternaAPI&b> &a";
    public static final String DEBUG_PREFIX = PREFIX + "&c&lDEBUG&4> &7&o";
    public static final String TEST_PREFIX = PREFIX + "&2&lTEST&2> &a";

    public static final boolean BREAKPOINT = true;

    public static void broadcastMessageOP(String string) {
        Chat.broadcastOp(PREFIX + string);
    }

    public static void broadcastMessageConsole(String string) {
        Chat.sendMessage(Bukkit.getConsoleSender(), PREFIX + string);
    }

    public static void sendMessage(CommandSender player, String message) {
        Chat.sendMessage(player, PREFIX + message);
    }

    public static void info(String message) {
        EternaPlugin.getPlugin().getLogger().info(message);
    }

    public static void warn(String message) {
        EternaPlugin.getPlugin().getLogger().warning(message);
    }

    public static void severe(String message) {
        EternaPlugin.getPlugin().getLogger().severe(message);
    }

    @Nonnull
    @ReturnValueOfThisMethodIsAnExceptionAndItMustBeThrown
    public static RuntimeException exception(Exception e) {
        if (EternaPlugin.getPlugin().getConfig().getBoolean("dev.print-stack-traces")) {
            severe("An exception has occurred!");
            e.printStackTrace();
        }

        return new RuntimeException(e);
    }

    public static void debug(Object message) {
        final String formatted = String.valueOf(message);

        info(DEBUG_PREFIX + formatted);
        Chat.broadcastOp(DEBUG_PREFIX + formatted);
    }

    public static void test(String s) {
        info(TEST_PREFIX + s);
        Chat.broadcastOp(TEST_PREFIX + s);
    }
}
