package me.hapyl.spigotutils;

public class EternaLibrary {

    private final EternaAPI api;

    public EternaLibrary(EternaAPI api) {
        this.api = api;
    }

    public EternaAPI getAPI() {
        return api;
    }
}
