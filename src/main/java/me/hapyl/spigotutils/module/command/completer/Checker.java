package me.hapyl.spigotutils.module.command.completer;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface Checker {

    @Nullable
    String check(Player player, String arg);

}
