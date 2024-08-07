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

    // In
    public static final PacketWrapperIn<ServerboundInteractPacket, WrappedPacketPlayInUseEntity> PACKET_PLAY_IN_USE_ENTITY;

    // Out
    public static final PacketWrapperOut<ClientboundSetEntityDataPacket, WrappedPacketPlayOutEntityMetadata> PACKET_PLAY_OUT_ENTITY_METADATA;
    public static final PacketWrapperOut<ClientboundAddEntityPacket, WrappedPacketPlayOutSpawnEntity> PACKET_PLAY_OUT_SPAWN_ENTITY;

    static {
        // In
        PACKET_PLAY_IN_USE_ENTITY = ofIn(ServerboundInteractPacket.class, WrappedPacketPlayInUseEntity::new);

        // Out
        PACKET_PLAY_OUT_ENTITY_METADATA = ofOut(ClientboundSetEntityDataPacket.class, WrappedPacketPlayOutEntityMetadata::new);
        PACKET_PLAY_OUT_SPAWN_ENTITY = ofOut(ClientboundAddEntityPacket.class, WrappedPacketPlayOutSpawnEntity::new);
    }

    private static <P extends Packet<ServerGamePacketListener>, W extends WrappedPacket<P>> PacketWrapperIn<P, W> ofIn(Class<P> clazz, Function<P, W> function) {
        return new PacketWrapperIn<>(clazz, function);
    }

    private static <P extends Packet<ClientGamePacketListener>, W extends WrappedPacket<P>> PacketWrapperOut<P, W> ofOut(Class<P> clazz, Function<P, W> function) {
        return new PacketWrapperOut<>(clazz, function);
    }


}
