package me.hapyl.eterna.module.reflect.packet.wrapped;

import me.hapyl.eterna.module.event.protocol.PacketEvent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;

import java.util.function.Function;

/**
 * A collection of {@link PacketWrapper}, used to wrap raw {@link Packet}s into their {@link WrappedPacket} version.
 *
 * @apiNote Each packet must be wrapped manually, therefore, this will take a lot of work, and I don't feel like doing it in a single update, considering I need the wrapper soon. -h
 * @see PacketEvent#getWrappedPacket(PacketWrapper)
 */
public final class PacketWrappers {

    // Serverbound
    public static final PacketWrapperServerbound<ServerboundInteractPacket, WrappedServerboundInteractPacket> PACKET_PLAY_IN_USE_ENTITY;
    public static final PacketWrapperServerbound<ServerboundPlayerInputPacket, WrappedPacketServerboundInput> PACKET_PLAY_IN_STEER_VEHICLE;

    // Clientbound
    public static final PacketWrapperClientbound<ClientboundSetEntityDataPacket, WrappedPacketClientboundEntityData> PACKET_PLAY_OUT_ENTITY_METADATA;
    public static final PacketWrapperClientbound<ClientboundAddEntityPacket, WrappedPacketPlayOutSpawnEntity> PACKET_PLAY_OUT_SPAWN_ENTITY;

    static {
        // In
        PACKET_PLAY_IN_USE_ENTITY = server(ServerboundInteractPacket.class, WrappedServerboundInteractPacket::new);
        PACKET_PLAY_IN_STEER_VEHICLE = server(ServerboundPlayerInputPacket.class, WrappedPacketServerboundInput::new);

        // Out
        PACKET_PLAY_OUT_ENTITY_METADATA = client(ClientboundSetEntityDataPacket.class, WrappedPacketClientboundEntityData::new);
        PACKET_PLAY_OUT_SPAWN_ENTITY = client(ClientboundAddEntityPacket.class, WrappedPacketPlayOutSpawnEntity::new);
    }

    private static <P extends Packet<ServerGamePacketListener>, W extends WrappedPacket<P>> PacketWrapperServerbound<P, W> server(Class<P> clazz, Function<P, W> function) {
        return new PacketWrapperServerbound<>(clazz, function);
    }

    private static <P extends Packet<ClientGamePacketListener>, W extends WrappedPacket<P>> PacketWrapperClientbound<P, W> client(Class<P> clazz, Function<P, W> function) {
        return new PacketWrapperClientbound<>(clazz, function);
    }


}
