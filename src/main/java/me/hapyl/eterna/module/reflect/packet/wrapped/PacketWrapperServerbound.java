package me.hapyl.eterna.module.reflect.packet.wrapped;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

import java.util.function.Function;

public final class PacketWrapperServerbound<P extends Packet<ServerGamePacketListener>, W extends WrappedPacket<P>> extends PacketWrapper<ServerGamePacketListener, P, W> {
    PacketWrapperServerbound(Class<P> clazz, Function<P, W> function) {
        super(clazz, function);
    }
}
