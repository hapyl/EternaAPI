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
    public static final PacketWrapper.Server<ServerboundInteractPacket, WrappedServerboundInteractPacket> SERVERBOUND_INTERACT;
    public static final PacketWrapper.Server<ServerboundPlayerInputPacket, WrappedServerboundInputPacket> SERVERBOUND_INPUT;

    // Clientbound
    public static final PacketWrapper.Client<ClientboundSetEntityDataPacket, WrappedSetEntityDataPacket> CLIENTBOUND_SET_ENTITY_DATA;
    public static final PacketWrapper.Client<ClientboundAddEntityPacket, WrappedClientboundAddEntityPacket> CLIENTBOUND_ADD_ENTITY;

    static {
        // Serverbound
        SERVERBOUND_INTERACT = server(ServerboundInteractPacket.class, WrappedServerboundInteractPacket::new);
        SERVERBOUND_INPUT = server(ServerboundPlayerInputPacket.class, WrappedServerboundInputPacket::new);

        // Clientbound
        CLIENTBOUND_SET_ENTITY_DATA = client(ClientboundSetEntityDataPacket.class, WrappedSetEntityDataPacket::new);
        CLIENTBOUND_ADD_ENTITY = client(ClientboundAddEntityPacket.class, WrappedClientboundAddEntityPacket::new);
    }

    private static <P extends Packet<ServerGamePacketListener>, W extends WrappedPacket<P>> PacketWrapper.Server<P, W> server(Class<P> clazz, Function<P, W> function) {
        return new PacketWrapper.Server<>(clazz, function);
    }

    private static <P extends Packet<ClientGamePacketListener>, W extends WrappedPacket<P>> PacketWrapper.Client<P, W> client(Class<P> clazz, Function<P, W> function) {
        return new PacketWrapper.Client<>(clazz, function);
    }


}
