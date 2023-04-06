package me.hapyl.spigotutils;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

}
