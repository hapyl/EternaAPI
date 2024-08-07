package me.hapyl.eterna.module.command.completer;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * Provides a way to check if a string is valid for a specific argument with addition of arguments provided in the command.
 */
public interface Checker2 extends Checker {

    @Nullable
    String check(Player player, String arg, String[] args);

    default String check(Player player, String arg) {
        throw new IllegalStateException("Illegal invoke of Checker2#check(String)");
    }

}
