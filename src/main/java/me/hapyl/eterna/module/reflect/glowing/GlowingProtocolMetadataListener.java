package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Sets;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import me.hapyl.eterna.builtin.manager.GlowingManager;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedPacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.Set;

@ApiStatus.Internal
public final class GlowingProtocolMetadataListener extends EternaLock implements Listener {
    
    private final GlowingManager manager = Eterna.getManagers().glowing;
    private final Set<Packet<?>> ignoredPackets = Sets.newConcurrentHashSet();
    
    public GlowingProtocolMetadataListener(@Nullable EternaKey key) {
        super(key);
    }
    
    @EventHandler()
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final Player player = ev.getPlayer();
        final WrappedPacketPlayOutEntityMetadata packet = ev.getWrappedPacket(PacketWrappers.PACKET_PLAY_OUT_ENTITY_METADATA);
        
        if (packet == null) {
            return;
        }
        
        // Fixme -> Might need to add a bundle check but hasn't seen glowing fail yet
        
        final int entityId = packet.getEntityId();
        final GlowingInstance instance = manager.getGlowing(entityId);
        
        if (ignoredPackets.contains(packet.getPacket()) || instance == null) {
            return;
        }
        
        // If the player who's receiving the packet isn't the player instance owner, cancel the packet and return
        if (!player.equals(instance.player())) {
            ev.setCancelled(true);
            return;
        }
        
        // Otherwise do some changes and resend the packet
        
        final WrappedPacketPlayOutEntityMetadata.WrappedDataWatcherValueList packedItems = packet.getWrappedDataWatcherValueList();
        
        for (WrappedPacketPlayOutEntityMetadata.WrappedDataWatcherValue item : packedItems) {
            final int id = item.getId();
            final Byte value = item.getValueAs(Byte.class);
            
            // We need to override byte value with id 0
            if (id == 0 && value != null) {
                item.setValue((byte) (value | Glowing.BITMASK));
            }
        }
        
        final ClientboundSetEntityDataPacket newPacket = new ClientboundSetEntityDataPacket(entityId, packedItems.getAsDataWatcherObjectList());
        
        // Only send the packet to the instance player
        ignoredPackets.add(newPacket);
        Reflect.sendPacket(player, newPacket);
    }
    
}
