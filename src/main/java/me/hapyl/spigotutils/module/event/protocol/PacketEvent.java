package me.hapyl.spigotutils.module.event.protocol;

import io.netty.channel.Channel;
import me.hapyl.spigotutils.module.reflect.wrapper.WrappedBundlePacket;
import me.hapyl.spigotutils.module.reflect.wrapper.Wrapper;
import me.hapyl.spigotutils.module.reflect.wrapper.Wrappers;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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
     * <br>
     * Receiver for {@link PacketSendEvent}.
     * <br>
     * Sender for {@link PacketReceiveEvent}.
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
     * Gets a {@link WrappedBundlePacket} that contains a {@link List} of {@link Packet}, or null if the packet is not a bundle packet.
     *
     * @return a wrapped bundle packet.
     */
    @Nullable
    public WrappedBundlePacket getBundlePacket() {
        if (packet instanceof ClientboundBundlePacket bundlePacket) {
            return Wrappers.wrapBundlePacket(bundlePacket);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
