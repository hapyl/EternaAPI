package me.hapyl.eterna.module.reflect.packet.wrapped;

import me.hapyl.eterna.module.event.protocol.PacketReceiveEvent;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a {@link PacketWrapper} that converts raw {@link Packet} into their {@link WrappedPacket} form.
 *
 * @param <T> - The packet listener type.
 * @param <P> - The raw packet type.
 * @param <W> - The wrapped packet type.
 * @see PacketWrappers
 * @see PacketSendEvent
 * @see PacketReceiveEvent
 */
public class PacketWrapper<T extends PacketListener, P extends Packet<T>, W extends WrappedPacket<P>> {
    
    private final Class<P> clazz;
    private final Function<P, W> function;
    
    PacketWrapper(@NotNull Class<P> clazz, @NotNull Function<P, W> function) {
        this.clazz = clazz;
        this.function = function;
    }
    
    /**
     * Gets the {@link Class} of the packet.
     *
     * @return the class of the packet.
     */
    @NotNull
    public Class<P> getClazz() {
        return clazz;
    }
    
    /**
     * Wraps the given {@link Packet} if its type is identical to the {@link #getClazz()}.
     *
     * @param packet - The packet to wrap.
     * @return the wrapped packet wrapped in an optional.
     */
    @NotNull
    public Optional<W> wrap(@NotNull Packet<?> packet) {
        if (clazz.isInstance(packet)) {
            return Optional.of(function.apply(clazz.cast(packet)));
        }
        
        return Optional.empty();
    }
    
    /**
     * Represents a client-side packet.
     *
     * @param <P> - The raw packet type.
     * @param <W> - The wrapped packet type.
     */
    public static class Client<P extends Packet<@NotNull ClientGamePacketListener>, W extends WrappedPacket<P>> extends PacketWrapper<ClientGamePacketListener, P, W> {
        Client(@NotNull Class<P> clazz, @NotNull Function<P, W> function) {
            super(clazz, function);
        }
    }
    
    /**
     * Represents a server-side packet.
     *
     * @param <P> - The raw packet type.
     * @param <W> - The wrapped packet type.
     */
    public static final class Server<P extends Packet<@NotNull ServerGamePacketListener>, W extends WrappedPacket<P>> extends PacketWrapper<ServerGamePacketListener, P, W> {
        Server(@NotNull Class<P> clazz, @NotNull Function<P, W> function) {
            super(clazz, function);
        }
    }
}
