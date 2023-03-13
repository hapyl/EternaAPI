package me.hapyl.spigotutils.module.command.completer;

import javax.annotation.Nullable;

public interface Checker2 extends Checker {

    @Nullable
    String check(String arg, String[] args);

    default String check(String arg) {
        throw new IllegalStateException("Illegal invoke of Checker2#check(String)");
    }

}
