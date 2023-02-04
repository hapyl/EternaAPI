package me.hapyl.spigotutils.module.addons;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.addons.annotate.AddonField;
import me.hapyl.spigotutils.module.addons.annotate.AddonMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public enum Type {
    /**
     * A command addon.
     * Allows to add commands and/or aliases.
     */
    COMMAND(CommandAddon.class),
    /**
     * An event listener addon.
     * Allows to listen for specific addons
     */
    LISTENER(null);

    private final Class<?> clazz;
    private final Map<String, Field> fieldMap;
    private final Map<String, Method> methodMap;

    Type(Class<?> clazz) {
        this.clazz = clazz;
        fieldMap = Maps.newLinkedHashMap();
        methodMap = Maps.newLinkedHashMap();

        write();
    }

    private void write() {
        if (clazz == null) {
            return;
        }

        try {
            // Fields
            for (Field field : clazz.getDeclaredFields()) {
                final Annotation annotation = field.getAnnotation(AddonField.class);
                if (annotation == null) {
                    continue;
                }

                fieldMap.put(field.getName(), field);
            }

            // Methods
            for (Method method : clazz.getDeclaredMethods()) {
                final AddonMethod annotation = method.getAnnotation(AddonMethod.class);
                if (annotation == null) {
                    continue;
                }

                methodMap.put(method.getName(), method);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Field fieldByName(String name) {
        return fieldMap.get(name);
    }

    public Method methodByName(String name) {
        return methodMap.get(name);
    }

}