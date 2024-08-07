package me.hapyl.eterna.module.entity;

public interface Spawnable<E> {

    E spawn();

    boolean isSpawned();

}
