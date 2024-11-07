package me.hapyl.eterna.module.reflect.packet;

import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Represents a simple 'wrapped' packet, allowing to send or retrieve it.
 *
 * @param <P> - The packet type.
 * @see Packets
 */
public interface IPacket<P extends Packet<?>> {

    /**
     * Sends this packet to the given player.
     *
     * @param player - The player to send the packet to.
     */
    void send(@Nonnull Player player);

    /**
     * Gets the {@link Packet} object.
     *
     * @return the packet object.
     */
    @Nonnull
    P packet();

    /**
     * Gets the raw {@link Packet} object.
     *
     * @return the raw packet object.
     */
    @Deprecated
    @Nonnull
    default Packet<?> packetRaw() {
        return packet();
    }

    /**
     * Broadcasts this packet to all online players.
     */
    default void broadcast() {
        this.broadcast(t -> true);
    }

    /**
     * Broadcasts this packet to all online players who match the given predicate.
     *
     * @param predicate - The predicate to match.
     */
    default void broadcast(@Nonnull Predicate<Player> predicate) {
        Bukkit.getOnlinePlayers().stream().filter(predicate).forEach(this::send);
    }

    /**
     * Constructs a new instance of {@link IPacket}.
     *
     * @param packet - The packet.
     * @param <P>    - The packet type.
     * @return a new instance of {@link IPacket}.
     */
    @Nonnull
    static <P extends Packet<?>> IPacketImpl<P> of(@Nonnull P packet) {
        return new IPacketImpl<>(packet);
    }


}
