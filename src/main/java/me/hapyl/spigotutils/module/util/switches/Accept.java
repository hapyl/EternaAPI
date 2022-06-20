package me.hapyl.spigotutils.module.util.switches;

public interface Accept {
    void accept();

    interface Value<T> {
        void accept(T t);
    }

    interface ValueSwitch<T> {
        void accept(T t, Switch<T> self);
    }
}
