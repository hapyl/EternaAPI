package me.hapyl.spigotutils.module.command.completer;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface Checker2 extends Checker {

    @Nullable
    String check(Player player, String arg, String[] args);

    default String check(Player player, String arg) {
        throw new IllegalStateException("Illegal invoke of Checker2#check(String)");
    }

}
