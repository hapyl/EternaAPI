package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedBundlePacket;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class GlowingHandler extends EternaHandler<Player, GlowingImpl> implements Listener {
    
    static GlowingHandler handler;
    
    private final Set<Packet<?>> ignoredPackets = Sets.newConcurrentHashSet();
    
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
    public Stream<GlowingInstance> getByEntityId(int entityId) {
        return registry.values()
                       .stream()
                       .flatMap(glowing -> glowing.byEntityId(entityId));
    }
    
    @NotNull
    @Override
    protected Map<Player, GlowingImpl> makeNewMap() {
        return Maps.newConcurrentMap(); // Dealing with packets, safer to use concurrent
    }
    
    @EventHandler
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final Player player = ev.getPlayer();
        final WrappedBundlePacket bundlePacket = ev.getBundlePacket();
        
        // When entity respawns, we simply re-send the glowing packet to the player
        // for whom the entity is glowing
        if (bundlePacket != null) {
            bundlePacket.getBundledPackets()
                        .stream()
                        .filter(ClientboundAddEntityPacket.class::isInstance)
                        .map(ClientboundAddEntityPacket.class::cast)
                        // Realistically there should never be two of the same packets bundled up, right?
                        .findFirst()
                        .ifPresent(packet -> {
                            this.getByEntityId(packet.getId()).forEach(instance -> {
                                instance.sendGlowingPacket(true);
                            });
                        });
        }
        
        // Otherwise we intercept the SetEntityDataPacket and modify the glowing bit, which is needed for when an entity updates their metadata
        // which is as simple as sneaking for a player
        final WrappedClientboundSetEntityDataPacket packetSetEntityData = ev.getWrappedPacket(PacketWrappers.CLIENTBOUND_SET_ENTITY_DATA).orElse(null);
        
        if (packetSetEntityData != null) {
            final int entityId = packetSetEntityData.getEntityId();
            
            // If it's our own packet, ignore it
            if (ignoredPackets.contains(packetSetEntityData.getPacket())) {
                return;
            }
            
            this.getByEntityId(entityId).forEach(glowingInstance -> {
                // If the player who's receiving the packet isn't the player instance owner, cancel the packet and return
                if (!player.equals(glowingInstance.player())) {
                    ev.setCancelled(true);
                    return;
                }
                
                // Otherwise do some changes and resend the packet
                final WrappedClientboundSetEntityDataPacket.WrappedEntityDataList packedItems = packetSetEntityData.getWrappedEntityDataList();
                
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
        }
    }
    
    @EventHandler
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        registry.remove(ev.getPlayer());
    }
    
}
