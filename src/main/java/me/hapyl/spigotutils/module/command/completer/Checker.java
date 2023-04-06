package me.hapyl.spigotutils.module.command.completer;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * Provides a way to check if a string is valid for a specific argument.
 */
public interface Checker {

    @Nullable
    String check(Player player, String arg);

}
