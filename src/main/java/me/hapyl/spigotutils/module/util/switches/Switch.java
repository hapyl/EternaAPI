package me.hapyl.spigotutils.module.util.switches;

public class Switch<T> {

    protected final T t;

    public Switch(T t) {
        this.t = t;
    }

    public When<T> when(T t) {
        return new When<>(this, t);
    }

}
