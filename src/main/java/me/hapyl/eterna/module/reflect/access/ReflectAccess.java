package me.hapyl.eterna.module.reflect.access;

public class ReflectAccess {

    public static final ReflectPlayerAccess PLAYER;

    static {
        PLAYER = new ReflectPlayerAccess();
    }

    private ReflectAccess() {
    }

}
