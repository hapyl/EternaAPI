package me.hapyl.spigotutils.module.util;

public interface Migrator<E, N> {

    N migrate(E old);

}
