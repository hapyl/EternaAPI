package me.hapyl.spigotutils.module.addons;

/**
 * Represents a key-value variable.
 */
public class AddonVar {

    private final String key;
    private final String value;

    public AddonVar(String key, String value) {
        this.key = key.trim();
        this.value = value.trim();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean key(String key) {
        return this.key.equals(key);
    }

    public boolean value(String value) {
        return this.value.equals(value);
    }
}
