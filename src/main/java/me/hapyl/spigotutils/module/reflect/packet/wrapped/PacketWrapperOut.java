package me.hapyl.spigotutils.module.reflect.packet.wrapped;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;

import java.util.function.Function;

public final class PacketWrapperOut<P extends Packet<PacketListenerPlayOut>, W extends WrappedPacket<P>> extends PacketWrapper<PacketListenerPlayOut, P, W> {
    PacketWrapperOut(Class<P> clazz, Function<P, W> function) {
        super(clazz, function);
    }
}
