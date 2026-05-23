package me.hapyl.eterna.module.event.protocol;

import io.netty.channel.Channel;
import me.hapyl.eterna.Runnables;
import me.hapyl.eterna.module.annotate.Asynchronous;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents an {@link Event} which is fired whenever a {@link Packet} is being sent/received.
 *
 * <p><b>Packet events are always asynchronous.</b></p>
 *
 * @see PacketReceiveEvent
 * @see PacketSendEvent
 */
@Asynchronous
public abstract class PacketEvent<L extends PacketListener> extends PlayerEvent implements Cancellable {
    
    private final Channel channel;
    private final Packet<?> packet;
    
    private boolean cancel;
    
    @ApiStatus.Internal
    PacketEvent(@NotNull Player player, @NotNull Channel channel, @NotNull Packet<?> packet) {
        super(player, true);
        
        this.player = player;
        this.channel = channel;
        this.packet = packet;
    }
    
    /**
     * Gets the netty {@link Channel} of this {@link Packet}.
     *
     * @return the netty channel.
     */
    @NotNull
    public Channel getChannel() {
        return channel;
    }
    
    /**
     * Gets the {@link Packet}.
     *
     * @return the packet.
     */
    @NotNull
    public Packet<?> getPacket() {
        return packet;
    }
    
    /**
     * Attempts to cast the {@link Packet} to the given {@code class} and wraps it in an {@link Optional}.
     *
     * @param packetClass - The class for casting.
     * @return an {@link Optional} containing either the cast packet, or an {@link Optional#empty()} optional if cannot be cast.
     */
    @NotNull
    public <P extends Packet<? extends @NotNull L>> Optional<P> getPacket(@NotNull Class<P> packetClass) {
        if (packetClass.isInstance(packet)) {
            return Optional.of(packetClass.cast(packet));
        }
        
        return Optional.empty();
    }
    
    /**
     * Gets a {@link ClientboundBundlePacket} wrapped in an {@link Optional}.
     *
     * <p>
     * If the packet is not a bundle packet, an empty optional is returned.
     * </p>
     *
     * @return a bundle packet wrapped in an optional, or an empty optional if the packet is not a bundle packet.
     */
    @NotNull
    public Optional<ClientboundBundlePacket> getBundlePacket() {
        return packet instanceof ClientboundBundlePacket bundlePacket ? Optional.of(bundlePacket) : Optional.empty();
    }
    
    /**
     * Gets whether this event is cancelled.
     * <p>Cancelling the event will ignore the packet, as it was never send/received.</p>
     *
     * @return {@code true} if this event is canceled; {@code false} otherwise.
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }
    
    /**
     * Sets whether this event is cancelled.
     *
     * <p>Cancelling the event will ignore the packet, as it was never send/received.</p>
     *
     * @param cancel - {@code true} to cancel the event; {@code false} otherwise.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    
    /**
     * A helper method to synchronize the given {@link Runnable} to the main thread.
     *
     * @param runnable - The runnable to synchronize.
     */
    public void synchronize(@NotNull Runnable runnable) {
        Runnables.sync(runnable);
    }
    
    /**
     * A helper method to retrieve an entity by its numeric id.
     *
     * @param world    - The target world.
     * @param entityId - The entity id.
     * @return an entity wrapped in an optional, or an empty optional if no such entity exists.
     */
    @NotNull
    public Optional<Entity> getEntityById(@NotNull World world, int entityId) {
        return Reflect.getEntityById(world, entityId);
    }
    
}
