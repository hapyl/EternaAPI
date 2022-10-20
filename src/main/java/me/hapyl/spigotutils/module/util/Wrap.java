package me.hapyl.spigotutils.module.util;

public interface Wrap {

    String start();

    String between();

    String end();

    Wrap DEFAULT = of("[", ", ", "]");

    static Wrap of(String start, String between, String end) {
        return new Wrap() {
            @Override
            public String start() {
                return start == null ? "" : start;
            }

            @Override
            public String between() {
                return between == null ? "" : between;
            }

            @Override
            public String end() {
                return end == null ? "" : end;
            }
        };
    }

}