package me.hapyl.spigotutils.module.reflect.wrapper;

import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.syncher.DataWatcher;

import javax.annotation.Nonnull;

/**
 * A static wrapper class.
 */
public interface Wrappers {

    @Nonnull
    static WrappedDataWatcher wrapDataWatcher(@Nonnull DataWatcher watcher) {
        return new WrappedDataWatcher(watcher);
    }

    @Nonnull
    static WrappedBundlePacket wrapBundlePacket(@Nonnull ClientboundBundlePacket packet) {
        return new WrappedBundlePacket(packet);
    }

}
