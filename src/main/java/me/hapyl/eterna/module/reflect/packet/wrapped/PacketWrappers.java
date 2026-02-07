package me.hapyl.eterna.module.reflect.packet.wrapped;

import me.hapyl.eterna.module.event.protocol.PacketEvent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a collection of {@link PacketWrapper}.
 *
 * @see PacketEvent#getWrappedPacket(PacketWrapper)
 */
public final class PacketWrappers {
    
    // *-* Serverbound *-* //
    
    /**
     * Defines a wrapper for {@link WrappedServerboundInteractPacket}.
     */
    @NotNull
    public static final PacketWrapper.Server<ServerboundInteractPacket, WrappedServerboundInteractPacket> SERVERBOUND_INTERACT;
    
    /**
     * Defines a wrapper for {@link WrappedServerboundInputPacket}.
     */
    @NotNull
    public static final PacketWrapper.Server<ServerboundPlayerInputPacket, WrappedServerboundInputPacket> SERVERBOUND_INPUT;
    
    // *-* Clientbound *-* //
    
    /**
     * Defines a wrapper for {@link WrappedClientboundSetEntityDataPacket}.
     */
    @NotNull
    public static final PacketWrapper.Client<ClientboundSetEntityDataPacket, WrappedClientboundSetEntityDataPacket> CLIENTBOUND_SET_ENTITY_DATA;
    
    /**
     * Defines a wrapper for {@link WrappedClientboundAddEntityPacket}.
     */
    @NotNull
    public static final PacketWrapper.Client<ClientboundAddEntityPacket, WrappedClientboundAddEntityPacket> CLIENTBOUND_ADD_ENTITY;
    
    static {
        SERVERBOUND_INTERACT = server(ServerboundInteractPacket.class, WrappedServerboundInteractPacket::new);
        SERVERBOUND_INPUT = server(ServerboundPlayerInputPacket.class, WrappedServerboundInputPacket::new);
        
        CLIENTBOUND_SET_ENTITY_DATA = client(ClientboundSetEntityDataPacket.class, WrappedClientboundSetEntityDataPacket::new);
        CLIENTBOUND_ADD_ENTITY = client(ClientboundAddEntityPacket.class, WrappedClientboundAddEntityPacket::new);
    }
    
    private PacketWrappers() {
    }
    
    @NotNull
    private static <P extends Packet<ServerGamePacketListener>, W extends WrappedPacket<P>> PacketWrapper.Server<P, W> server(Class<P> clazz, Function<P, W> function) {
        return new PacketWrapper.Server<>(clazz, function);
    }
    
    @NotNull
    private static <P extends Packet<ClientGamePacketListener>, W extends WrappedPacket<P>> PacketWrapper.Client<P, W> client(Class<P> clazz, Function<P, W> function) {
        return new PacketWrapper.Client<>(clazz, function);
    }
    
    
}
