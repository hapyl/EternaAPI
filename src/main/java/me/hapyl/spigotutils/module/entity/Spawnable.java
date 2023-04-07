package me.hapyl.spigotutils.module.entity;

public interface Spawnable<E> {

    E spawn();

    boolean isSpawned();

}
