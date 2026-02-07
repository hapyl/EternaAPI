package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Sets;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Represents an internal {@link ClientboundSetEntityDataPacket} handler.
 */
@ApiStatus.Internal
public final class GlowingProtocolMetadataHandler extends EternaKeyed implements Listener {
    
    private final Set<Packet<?>> ignoredPackets = Sets.newConcurrentHashSet();
    
    public GlowingProtocolMetadataHandler(@Nullable EternaKey key) {
        super(key);
    }
    
    @EventHandler()
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final Player player = ev.getPlayer();
        
        ev.getWrappedPacket(PacketWrappers.CLIENTBOUND_SET_ENTITY_DATA).ifPresent(packet -> {
            final int entityId = packet.getEntityId();
            
            if (ignoredPackets.contains(packet.getPacket())) {
                return;
            }
            
            GlowingHandler.handler.getByEntityId(entityId).forEach(glowingInstance -> {
                
                // If the player who's receiving the packet isn't the player instance owner, cancel the packet and return
                if (!player.equals(glowingInstance.player())) {
                    ev.setCancelled(true);
                    return;
                }
                
                // Otherwise do some changes and resend the packet
                final WrappedClientboundSetEntityDataPacket.WrappedEntityDataList packedItems = packet.getWrappedEntityDataList();
                
                for (WrappedClientboundSetEntityDataPacket.WrappedEntityDataValue item : packedItems) {
                    final int id = item.getId();
                    final Byte value = item.getValueAs(Byte.class);
                    
                    // We need to override byte value with id 0
                    if (id == 0 && value != null) {
                        item.setValue((byte) (value | GlowingInstance.BITMASK));
                    }
                }
                
                final ClientboundSetEntityDataPacket newPacket = new ClientboundSetEntityDataPacket(entityId, packedItems.asVanilla());
                
                // Only send the packet to the instance player
                ignoredPackets.add(newPacket);
                Reflect.sendPacket(player, newPacket);
            });
        });
    }
    
}
