package me.hapyl.spigotutils.module.inventory.gui;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

public class Arguments {

    public static final Arguments EMPTY = new Arguments((Object) null) {
        @Override
        public <T> boolean has(int index, Class<T> type) {
            return false;
        }

        @Override
        public <T> void add(T t) {
        }

        @Override
        public <T> void put(int index, T t) {
        }

        @Override
        public <T> T get(int index, T def) {
            return def;
        }
    };

    private final Map<Integer, Object> arguments;

    public Arguments(@Nullable Object... objects) {
        if (objects == null) {
            arguments = null;
            return;
        }

        arguments = Maps.newHashMap();
        for (int i = 0; i < objects.length; i++) {
            arguments.put(i, objects[i]);
        }
    }

    public <T> boolean has(int index, Class<T> type) {
        final Object obj = arguments.get(index);

        return obj != null && obj.getClass() == type;
    }

    public <T> void add(T t) {
        put(arguments.size(), t);
    }

    public <T> void put(int index, T t) {
        arguments.put(index, t);
    }

    public <T> T getAs(int index, Class<T> clazz) {
        final Object obj = get(index, null);

        if (clazz.isInstance(obj)) {
            return null;
        }

        return clazz.cast(obj);
    }

    public <T> T get(int index, T def) {
        final Object obj = arguments.getOrDefault(index, def);

        if (obj == null) {
            return def;
        }

        return (T) obj;
    }

}
