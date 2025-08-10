package me.hapyl.eterna;

import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents the API logger, supporting the Java logger and adding custom methods for debug and tests.
 * <br>
 * Should not be used outside the API.
 */
@ApiStatus.Internal
public final class EternaLogger {
    
    public static final String PREFIX = "&b&lEternaAPI&b> &a";
    public static final String DEBUG_PREFIX = PREFIX + "&c&lDEBUG&4> &7&o";
    public static final String TEST_PREFIX = PREFIX + "&2&lTEST&2> &a";
    
    @ApiStatus.Internal
    public static void broadcastMessageOP(@Nullable Object string) {
        Chat.broadcastOp(PREFIX + string);
    }
    
    @ApiStatus.Internal
    public static void broadcastMessageConsole(@Nullable Object string) {
        Chat.sendMessage(Bukkit.getConsoleSender(), PREFIX + string);
    }
    
    @ApiStatus.Internal
    public static void sendMessage(@Nonnull CommandSender player, @Nullable Object message) {
        Chat.sendMessage(player, PREFIX + message);
    }
    
    @ApiStatus.Internal
    public static void info(@Nullable Object message) {
        EternaPlugin.getPlugin().getLogger().info(String.valueOf(message));
    }
    
    @ApiStatus.Internal
    public static void warn(@Nullable Object message) {
        EternaPlugin.getPlugin().getLogger().warning(String.valueOf(message));
    }
    
    @ApiStatus.Internal
    public static void severe(@Nullable Object message) {
        EternaPlugin.getPlugin().getLogger().severe(String.valueOf(message));
    }
    
    @Nonnull
    @ApiStatus.Internal // Please don't forget you HAVE to throw the returned exception, not just do Eternalogger#exception() !!!
    public static RuntimeException exception(@Nonnull Exception e) {
        if (EternaPlugin.getPlugin().getConfig().getBoolean("dev.print-stack-traces")) {
            severe("An exception has occurred!");
            e.printStackTrace();
        }
        
        return new RuntimeException(e);
    }
    
    @ApiStatus.Internal
    public static void debug(@Nullable Object message) {
        final String formatted = String.valueOf(message);
        
        info(DEBUG_PREFIX + formatted);
        Chat.broadcastOp(DEBUG_PREFIX + formatted);
    }
    
    @ApiStatus.Internal
    public static void test(@Nullable String s) {
        info(TEST_PREFIX + s);
        Chat.broadcastOp(TEST_PREFIX + s);
    }
}
