package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedBundlePacket;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@ApiStatus.Internal
public final class GlowingHandler extends EternaHandler<Player, GlowingImpl> implements Listener {
    
    private static final int GLOWING_ID = 0;
    
    static GlowingHandler handler;
    
    final Set<Packet<?>> preparedPackets = Sets.newConcurrentHashSet();
    
    public GlowingHandler(@NotNull EternaKey key, @NotNull EternaPlugin eterna) {
        super(key, eterna);
        
        handler = this;
    }
    
    @Override
    @NotNull
    public Optional<GlowingImpl> get(@NotNull Player player) {
        return Objects.requireNonNull(super.get(player), "Illegal get() call, use get(Player, Function)!");
    }
    
    @NotNull
    @Override
    protected Map<Player, GlowingImpl> makeNewMap() {
        return Maps.newConcurrentMap(); // Dealing with packets, safer to use concurrent
    }
    
    @EventHandler
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final Player player = ev.getPlayer();
        final Packet<?> packet = ev.getPacket();
        
        // If it's our packet, ignore everything since it's already prepared
        if (preparedPackets.contains(packet)) {
            preparedPackets.remove(packet);
            return;
        }
        
        // If the player who received the packet doesn't have glowing active, we don't care
        final GlowingImpl glowing = registry.get(player);
        
        if (glowing == null) {
            return;
        }
        
        // If a bundle packet is received, we need to check for `ClientboundAddEntityPacket` packet to restart
        // the glowing in case the entity was added again due to render distance
        if (packet instanceof ClientboundBundlePacket bundlePacket) {
            bundlePacket.subPackets().forEach(subPacket -> {
                if (!(subPacket instanceof ClientboundAddEntityPacket subPacketAddEntity)) {
                    return;
                }
                
                // If there is a packet, check whether the player has glowing for that entity, and if so, re-send the glowing packet
                final GlowingInstance glowingInstance = glowing.byEntityId(subPacketAddEntity.getId());
                
                if (glowingInstance != null) {
                    glowingInstance.sendGlowingPacket(true);
                }
            });
        }
        // Otherwise we intercept the SetEntityDataPacket and modify the glowing bit, which is needed for when an entity updates their metadata,
        // which is mostly needed for players, since the packet is sent for when a player toggles sneaking
        else if (packet instanceof ClientboundSetEntityDataPacket packetSetEntityData) {
            final int entityId = packetSetEntityData.id();
            final GlowingInstance glowingInstance = glowing.byEntityId(entityId);
            
            if (glowingInstance == null) {
                return;
            }
            
            // Unfortunately, it's not as simple as mutating the packet itself, since it can be a broadcast packet, which is identical for
            // multiple players, so modifying the glowing bit would modify it for those we don't want to see it, therefore we have to re-create
            // the packet, cancel this packet and only send our new packet to the player
            final List<? extends SynchedEntityData.DataValue<?>> originalItems = packetSetEntityData.packedItems();
            final List<SynchedEntityData.DataValue<?>> newItems = Lists.newArrayList();
            
            for (SynchedEntityData.DataValue<?> item : originalItems) {
                final int id = item.id();
                final Object value = item.value();
                
                // If the id matches glowing id and the value is byte, flip the glowing bit
                if (id == GLOWING_ID && value instanceof Byte byteValue) {
                    newItems.add(new SynchedEntityData.DataValue<>(id, EntityDataSerializers.BYTE, (byte) (byteValue | GlowingInstance.BITMASK)));
                }
                // Otherwise them original value
                else {
                    newItems.add(item);
                }
            }
            
            // Create a new packet and send it
            final ClientboundSetEntityDataPacket preparedPacket = new ClientboundSetEntityDataPacket(entityId, newItems);
            
            preparedPackets.add(preparedPacket);
            Reflect.sendPacket(player, preparedPacket);
        }
    }
    
    @EventHandler
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        registry.remove(ev.getPlayer());
    }
    
}