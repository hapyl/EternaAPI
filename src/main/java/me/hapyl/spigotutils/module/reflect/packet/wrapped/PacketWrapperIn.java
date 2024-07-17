package me.hapyl.spigotutils.module.reflect.packet.wrapped;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

import java.util.function.Function;

public final class PacketWrapperIn<P extends Packet<ServerGamePacketListener>, W extends WrappedPacket<P>> extends PacketWrapper<ServerGamePacketListener, P, W> {
    PacketWrapperIn(Class<P> clazz, Function<P, W> function) {
        super(clazz, function);
    }
}
