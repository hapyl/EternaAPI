package kz.hapyl.spigotutils.registry;

import java.util.Set;

public interface Registry<T> {

    void register(T t);

    void unregister(T t);

    Set<T> registeredItems();

}
