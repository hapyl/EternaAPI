package me.hapyl.eterna.module.event.protocol;

import io.netty.channel.Channel;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrapper;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedBundlePacket;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedPacket;
import me.hapyl.eterna.module.util.Runnables;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Fires before a {@link Packet} is delivered.
 * <br>
 * <h1>Packet events are ASYNC!</h1>
 */
public abstract class PacketEvent extends Event implements Cancellable {

    private final Player player;
    private final Channel channel;
    private final Packet<?> packet;
    private boolean cancel;

    PacketEvent(Player player, Channel channel, Packet<?> packet) {
        super(true);

        this.player = player;
        this.channel = channel;
        this.packet = packet;
    }

    /**
     * Gets the player associated with this event.
     *
     * @return the player.
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the {@link Channel} this packet is on.
     *
     * @return the channel.
     */
    @Nonnull
    public Channel getChannel() {
        return channel;
    }

    /**
     * Gets the {@link Packet}.
     *
     * @return the packet.
     */
    @Nonnull
    public Packet<?> getPacket() {
        return packet;
    }

    /**
     * Gets the {@link Packet} if it matches the given class, <code>null</code> otherwise.
     *
     * @param packetClass - Packet class to match.
     * @return the packet or null.
     */
    @Nullable
    public <T extends PacketListener, P extends Packet<T>> P getPacket(@Nonnull Class<P> packetClass) {
        if (packetClass.isInstance(packet)) {
            return packetClass.cast(packet);
        }

        return null;
    }

    /**
     * Gets a {@link WrappedPacket} for the given {@link PacketWrapper}, or null, if {@link Packet} is not the correct type.
     *
     * @param wrapper - Wrapper.
     * @return a wrapped packet or null.
     * @see PacketWrappers
     */
    @Nullable
    public <T extends PacketListener, P extends Packet<T>, W extends WrappedPacket<P>> W getWrappedPacket(@Nonnull PacketWrapper<T, P, W> wrapper) {
        return wrapper.wrap(packet);
    }

    /**
     * Gets a {@link WrappedBundlePacket} that contains a {@link List} of {@link Packet}, or null if the packet is not a bundle packet.
     *
     * @return a wrapped bundle packet.
     */
    @Nullable
    public WrappedBundlePacket getBundlePacket() {
        if (packet instanceof ClientboundBundlePacket bundlePacket) {
            return new WrappedBundlePacket(bundlePacket);
        }

        return null;
    }

    /**
     * Returns true if this event was cancelled.
     * <br>
     * Cancelled event will ignore the packet, as it was never sent/received.
     *
     * @return true if this event was cancelled; false otherwise.
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets if this event is cancelled.
     * <br>
     * Cancelled event will ignore the packet, as it was never sent/received.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * A helper method to call method <b>synchronized</b>.
     *
     * @param runnable - Runnable to run synchronized.
     */
    protected void synchronize(@Nonnull Runnable runnable) {
        Runnables.runSync(runnable);
    }
}
