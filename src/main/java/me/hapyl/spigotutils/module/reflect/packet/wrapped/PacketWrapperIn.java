package me.hapyl.spigotutils.module.reflect.packet.wrapped;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;

import java.util.function.Function;

public final class PacketWrapperIn<P extends Packet<PacketListenerPlayIn>, W extends WrappedPacket<P>> extends PacketWrapper<PacketListenerPlayIn, P, W> {
    PacketWrapperIn(Class<P> clazz, Function<P, W> function) {
        super(clazz, function);
    }
}
