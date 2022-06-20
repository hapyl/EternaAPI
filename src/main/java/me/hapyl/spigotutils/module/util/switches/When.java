package me.hapyl.spigotutils.module.util.switches;

public class When<T> {

    private final Switch<T> ref;
    private final T comparing;

    protected When(Switch<T> ref, T comparing) {
        this.ref = ref;
        this.comparing = comparing;
    }

    public Switch<T> ifTrue(Accept accept) {
        if (compare()) {
            accept.accept();
        }
        return ref;
    }

    public Switch<T> ifFalse(Accept accept) {
        if (!compare()) {
            accept.accept();
        }
        return ref;
    }

    private boolean compare() {
        return ref.t != null && ref.t.equals(comparing);
    }

}
