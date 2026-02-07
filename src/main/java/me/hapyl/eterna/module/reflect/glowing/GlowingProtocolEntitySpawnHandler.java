package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedBundlePacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an internal {@link ClientboundAddEntityPacket} handler.
 */
@ApiStatus.Internal
public final class GlowingProtocolEntitySpawnHandler extends EternaKeyed implements Listener {
    
    public GlowingProtocolEntitySpawnHandler(@Nullable EternaKey key) {
        super(key);
    }
    
    @EventHandler()
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final WrappedBundlePacket bundlePacket = ev.getBundlePacket();
        
        if (bundlePacket == null) {
            return;
        }
        
        bundlePacket.getBundledPackets()
                    .stream()
                    .filter(ClientboundAddEntityPacket.class::isInstance)
                    .map(ClientboundAddEntityPacket.class::cast)
                    .findFirst()
                    .ifPresent(packet -> {
                        GlowingHandler.handler.getByEntityId(packet.getId()).forEach(instance -> {
                            instance.sendGlowingPacket(true);
                        });
                    });
    }
    
}
