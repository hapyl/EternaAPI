package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Sets;
import me.hapyl.eterna.Eterna;
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

import java.util.Set;

public class GlowingProtocolMetadataListener implements Listener {
    
    private final GlowingManager manager = Eterna.getManagers().glowing;
    private final Set<Packet<?>> ignoredPackets = Sets.newConcurrentHashSet();
    
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
        
        // Minecraft is annoying with the metadata and will send a Metadata Packet to all
        // nearby players, we need to cancel the event and send our magic packet to the target.
        ev.setCancelled(true);
        
        // Only send the packet to the instance player
        if (player.equals(instance.player())) {
            ignoredPackets.add(newPacket);
            Reflect.sendPacket(player, newPacket);
        }
    }
    
}
