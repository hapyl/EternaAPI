package me.hapyl.spigotutils.module.reflect.wrapper;

import me.hapyl.spigotutils.module.reflect.DataWatcherType;
import net.minecraft.network.syncher.DataWatcher;

import javax.annotation.Nullable;

public class WrappedDataWatcher extends Wrapper<DataWatcher> {

    WrappedDataWatcher(DataWatcher dataWatcher) {
        super(dataWatcher);
    }

    @Nullable
    public Byte getByte(int index) {
        return obj.a(DataWatcherType.BYTE.get().a(index));
    }

}
