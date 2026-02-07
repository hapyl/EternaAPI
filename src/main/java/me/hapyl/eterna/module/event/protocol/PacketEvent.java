package me.hapyl.eterna.module.event.protocol;

import io.netty.channel.Channel;
import me.hapyl.eterna.module.annotate.Asynchronous;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrapper;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedBundlePacket;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedPacket;
import me.hapyl.eterna.Runnables;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
     * Wraps the {@link Packet} into an {@link WrappedPacket} with the given {@link PacketWrapper}.
     *
     * @param wrapper - The wrapper.
     * @return an {@link Optional} containing the wrapped packet, or {@link Optional#empty()} optional if the given wrapper isn't compatible with the packet.
     * @see PacketWrappers
     */
    @NotNull
    public <T extends PacketListener, P extends Packet<@NotNull T>, W extends WrappedPacket<P>> Optional<W> getWrappedPacket(@NotNull PacketWrapper<T, P, W> wrapper) {
        return wrapper.wrap(packet);
    }
    
    /**
     * Gets a {@link WrappedBundlePacket} that contains a {@link List} of {@link Packet}.
     *
     * @return a wrapped bundle packet, or {@code null} if the packet isn't a bundle packet.
     */
    @Nullable
    public WrappedBundlePacket getBundlePacket() {
        if (packet instanceof ClientboundBundlePacket bundlePacket) {
            return new WrappedBundlePacket(bundlePacket);
        }
        
        return null;
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
}
