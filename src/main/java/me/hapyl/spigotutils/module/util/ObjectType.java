package me.hapyl.spigotutils.module.util;

import org.bukkit.inventory.ItemStack;

public enum ObjectType {

    NULL(),
    BYTE(byte.class, Byte.class),
    SHORT(short.class, Short.class),
    INT(int.class, Integer.class),
    LONG(long.class, Long.class),
    DOUBLE(double.class, Double.class),
    FLOAT(float.class, Float.class),
    STRING(String.class),
    CHAR(char.class, Character.class),
    ITEM(ItemStack.class);

    private final Class<?>[] samples;

    ObjectType(Class<?>... samples) {
        this.samples = samples;
    }

    public boolean testSample(Class<?> other) {
        if (other == null && this == NULL) {
            return true;
        }
        for (final Class<?> aClass : this.samples) {
            if (aClass == other) {
                return true;
            }
        }
        return false;
    }

    public static ObjectType testSample(Object obj) {
        if (obj == null) {
            return NULL;
        }
        for (final ObjectType value : values()) {
            if (value.testSample(obj.getClass())) {
                return value;
            }
        }
        return NULL;
    }


}
