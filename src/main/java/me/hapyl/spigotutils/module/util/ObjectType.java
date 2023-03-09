package me.hapyl.spigotutils.module.util;

import org.bukkit.inventory.ItemStack;

/**
 * Used to check object type in {@link Validate#getType(Object)}
 */
public enum ObjectType {

    /**
     * Provided object is null.
     */
    NULL(),
    /**
     * Provided object is boolean.
     */
    BYTE(byte.class, Byte.class),
    /**
     * Provided object is boolean.
     */
    SHORT(short.class, Short.class),
    /**
     * Provided object is integer.
     */
    INT(int.class, Integer.class),
    /**
     * Provided object is long.
     */
    LONG(long.class, Long.class),
    /**
     * Provided object is double.
     */
    DOUBLE(double.class, Double.class),
    /**
     * Provided object is float.
     */
    FLOAT(float.class, Float.class),
    /**
     * Provided object is String.
     */
    STRING(String.class),
    /**
     * Provided object is character.
     */
    CHAR(char.class, Character.class),
    /**
     * Provided object is ItemStack.
     */
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
