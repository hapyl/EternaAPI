package me.hapyl.eterna.module.util.switches;

import javax.annotation.Nullable;
import java.util.Objects;

public class Switch<T> {

    protected final T t;

    public Switch(T t) {
        this.t = t;
    }

    public Switch<T> when(T t, Accept accept) {
        if (compare(t)) {
            accept.accept();
        }
        return this;
    }

    public Switch<T> when(T t, Accept.Value<T> accept) {
        if (compare(t)) {
            accept.accept(t);
        }
        return this;
    }

    public Switch<T> when(T t, Accept.ValueSwitch<T> accept) {
        if (compare(t)) {
            accept.accept(t, this);
        }
        return this;
    }

    public Switch<T> whenIntRange(int min, int max, Accept accept) {
        if (t instanceof Integer i && (i >= min && i < max)) {
            accept.accept();
        }
        return this;
    }

    // static members
    public static <T> Switch<T> of(T t) {
        return new Switch<>(t);
    }

    private boolean compare(@Nullable T compare) {
        return Objects.equals(t, compare);
    }

    static {
        Switch.of(13).when(5, () -> System.out.println(1)).when(2, () -> System.out.println(2));
    }

}
