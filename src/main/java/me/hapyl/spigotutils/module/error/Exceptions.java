package me.hapyl.spigotutils.module.error;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * This util class allows to handle exceptions.
 *
 * @author hapyl
 */
public final class Exceptions {

    /**
     * Throws an abstract error with provided message.
     *
     * @param message - Message.
     */
    public static void throwRuntimeError(String message) {
        throw new RuntimeException(message);
    }

    /**
     * Runs runnable with and ignored all exceptions.
     *
     * @param runnable - Runnable.
     */
    public static void runSafe(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ignored) {
        }
    }

    /**
     * Runs runnable with and reports any exceptions.
     *
     * @param runnable - Runnable.
     */
    public static void handleAny(Runnable runnable) {
        handleAny(null, runnable);
    }

    /**
     * /** Runs runnable with and reports any exceptions to executor and console.
     *
     * @param runnable - Runnable.
     * @param executor - Executor.
     */
    public static void handleAny(CommandSender executor, Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (executor != null) {
                executor
                        .spigot()
                        .sendMessage(new ComponentBuilder(Chat.format("&cAn error occurred, please report this! &e(\"%s\")", e.getMessage()))
                                             .event(LazyClickEvent.COPY_TO_CLIPBOARD.of(Arrays.toString(e.getStackTrace())))
                                             .create());
            }
            e.printStackTrace();
        }
    }


}
