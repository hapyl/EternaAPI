package me.hapyl.spigotutils.module.reflect.wrapper;

import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class WrappedPacket<T extends PacketListener> extends Wrapper<Packet<T>> {

    WrappedPacket(Packet<T> tPacket) {
        super(tPacket);
    }

}
