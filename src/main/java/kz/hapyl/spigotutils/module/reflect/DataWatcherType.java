package kz.hapyl.spigotutils.module.reflect;

import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcherSerializer;

public class DataWatcherType<T> {

    public static final DataWatcherType<Byte> BYTE = new DataWatcherType<>(Byte.class, DataWatcherRegistry.a);
    public static final DataWatcherType<Integer> INT = new DataWatcherType<>(Integer.class, DataWatcherRegistry.b);
    public static final DataWatcherType<Float> FLOAT = new DataWatcherType<>(Float.class, DataWatcherRegistry.c);
    public static final DataWatcherType<String> STR = new DataWatcherType<>(String.class, DataWatcherRegistry.d);
    public static final DataWatcherType<Boolean> BOOL = new DataWatcherType<>(Boolean.class, DataWatcherRegistry.i);

    private final Class<T> clazz;
    private final DataWatcherSerializer<T> fieldName;

    private DataWatcherType(Class<T> clazz, DataWatcherSerializer<T> fieldName) {
        this.clazz = clazz;
        this.fieldName = fieldName;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public DataWatcherSerializer<T> get() {
        return fieldName;
    }

}
