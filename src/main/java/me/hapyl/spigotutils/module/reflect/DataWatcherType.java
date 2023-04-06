package me.hapyl.spigotutils.module.reflect;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class DataWatcherType<T> {

    public static final DataWatcherType<Byte> BYTE = new DataWatcherType<>(Byte.class, EntityDataSerializers.BYTE);
    public static final DataWatcherType<Integer> INT = new DataWatcherType<>(Integer.class, EntityDataSerializers.INT);
    public static final DataWatcherType<Float> FLOAT = new DataWatcherType<>(Float.class, EntityDataSerializers.FLOAT);
    public static final DataWatcherType<String> STR = new DataWatcherType<>(String.class, EntityDataSerializers.STRING);
    public static final DataWatcherType<Boolean> BOOL = new DataWatcherType<>(Boolean.class, EntityDataSerializers.BOOLEAN);

    private final Class<T> clazz;
    private final EntityDataSerializer<T> fieldName;

    private DataWatcherType(Class<T> clazz, EntityDataSerializer<T> fieldName) {
        this.clazz = clazz;
        this.fieldName = fieldName;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public EntityDataSerializer<T> get() {
        return fieldName;
    }

}
