package me.hapyl.spigotutils;

public enum EternaConfigValue {

    CHECK_FOR_UPDATES("check-for-updates", true),
    KEEP_TESTS("dev.keep-test-commands", true),

    ;

    public final String key;
    public final Object defaultValue;

    EternaConfigValue(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }
}
