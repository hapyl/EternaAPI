package me.hapyl.eterna.module.reflect.packet.wrapped;

import com.google.common.collect.Lists;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Represents a wrapped {@link BundlePacket}.
 * <p>Some packets are sent and received as a bundle; this wrapped allows to iterate over them.</p>
 */
public class WrappedBundlePacket extends WrappedPacket<BundlePacket<?>> {
    
    public WrappedBundlePacket(BundlePacket<?> bundlePacket) {
        super(bundlePacket);
    }
    
    /**
     * Gets an <b>unmodifiable</b> list of {@link Packet}s in this bundle packet.
     *
     * @return an unmodifiable list of packets in this bundle packet.
     */
    @Nonnull
    public List<Packet<?>> getBundledPackets() {
        return Collections.unmodifiableList(Lists.newArrayList(packet.subPackets()));
    }
    
    /**
     * Gets the first {@link Packet} that matches the given type from this bundle packet, or <code>null</code> if there are no such packets.
     *
     * @param packetType - Packet type.
     * @return the first matching packet or null.
     */
    @Nullable
    public <T extends PacketListener, P extends Packet<T>> P getFirstPacket(@Nonnull Class<P> packetType) {
        for (Packet<?> packet : getBundledPackets()) {
            final P p = matchPacket(packet, packetType);
            
            if (p != null) {
                return p;
            }
        }
        
        return null;
    }
    
    /**
     * Gets the first {@link Packet} as a {@link WrappedPacket} that matches the given type from this bundle packet, or <code>null</code> if there are no such packets.
     *
     * @param wrapper - Packet wrapper.
     * @return the first matching packet or null.
     * @see PacketWrappers
     */
    @Nullable
    public <T extends PacketListener, P extends Packet<T>, W extends WrappedPacket<P>> W getFirstPacketWrapped(@Nonnull PacketWrapper<T, P, W> wrapper) {
        final P packet = getFirstPacket(wrapper.getClazz());
        
        return packet != null ? wrapper.wrap(packet) : null;
    }
    
    /**
     * Gets a {@link List} of all {@link Packet}s matching the given type, or empty {@link List} if there are none.
     *
     * @param packetType - Packet type.
     * @return a list of all packets matching the given type, or empty list.
     */
    @Nonnull
    public <T extends PacketListener, P extends Packet<T>> List<P> getPackets(@Nonnull Class<P> packetType) {
        List<P> list = Lists.newArrayList();
        
        getBundledPackets().forEach(packet -> {
            final P p = matchPacket(packet, packetType);
            
            if (p != null) {
                list.add(p);
            }
        });
        
        return list;
    }
    
    private <T extends PacketListener, P extends Packet<T>> P matchPacket(Packet<?> packet, Class<P> packetType) {
        if (packetType.isInstance(packet)) {
            return packetType.cast(packet);
        }
        
        return null;
    }
    
}
