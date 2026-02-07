package me.hapyl.eterna.module.reflect.packet.wrapped;

import com.google.common.collect.Lists;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a wrapped {@link BundlePacket}.
 *
 * <p>Some packets are sent and received as a bundle; this wrapped allows to iterate over them.</p>
 */
public class WrappedBundlePacket extends WrappedPacket<BundlePacket<?>> {
    
    public WrappedBundlePacket(@NotNull BundlePacket<?> bundlePacket) {
        super(bundlePacket);
    }
    
    /**
     * Gets an <b>unmodifiable</b> list of {@link Packet} in this bundle packet.
     *
     * @return an unmodifiable list of packets in this bundle packet.
     */
    @NotNull
    public List<Packet<?>> getBundledPackets() {
        return Collections.unmodifiableList(Lists.newArrayList(packet.subPackets()));
    }
    
    /**
     * Gets the first {@link Packet} that matches the given type from this {@link WrappedBundlePacket}.
     *
     * @param packetType - The packet type to match.
     * @return the first matching packet wrapped in an optional.
     */
    @Nullable
    public <T extends PacketListener, P extends Packet<@NotNull T>> Optional<P> getFirstPacket(@NotNull Class<P> packetType) {
        return getBundledPackets().stream()
                                  .filter(packetType::isInstance)
                                  .map(packetType::cast)
                                  .findFirst();
    }
    
    /**
     * Gets an <b>immutable</b> {@link List} of all {@link Packet} matching the given type, or an empty {@link List} if there are none.
     *
     * @param packetType - The packet type to match.
     * @return an <b>immutable</b> list of all packets matching the given type, an empty list if there are none.
     */
    @NotNull
    public <T extends PacketListener, P extends Packet<@NotNull T>> List<P> getPackets(@NotNull Class<P> packetType) {
        return getBundledPackets().stream()
                                  .filter(packetType::isInstance)
                                  .map(packetType::cast)
                                  .toList();
    }
    
}
