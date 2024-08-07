package me.hapyl.eterna.module.util;

public interface Migrator<E, N> {

    N migrate(E old);

}
