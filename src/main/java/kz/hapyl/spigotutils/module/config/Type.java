package kz.hapyl.spigotutils.module.config;

public enum Type {

    GENERIC(),
    UUID();

    Type() {
    }

    public boolean isGeneric() {
        return true;
    }

    public Object convertLoad(Object obj) {
        return obj;
    }

    public Object convertSave(Object obj) {
        return obj;
    }
}
