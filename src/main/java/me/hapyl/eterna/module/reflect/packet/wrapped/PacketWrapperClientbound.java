package me.hapyl.eterna.module.reflect.packet.wrapped;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

import java.util.function.Function;

public final class PacketWrapperClientbound<P extends Packet<ClientGamePacketListener>, W extends WrappedPacket<P>> extends PacketWrapper<ClientGamePacketListener, P, W> {
    PacketWrapperClientbound(Class<P> clazz, Function<P, W> function) {
        super(clazz, function);
    }
}
