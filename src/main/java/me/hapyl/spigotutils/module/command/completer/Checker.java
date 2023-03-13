package me.hapyl.spigotutils.module.command.completer;

import javax.annotation.Nullable;

public interface Checker {

    @Nullable
    String check(String value);

}
