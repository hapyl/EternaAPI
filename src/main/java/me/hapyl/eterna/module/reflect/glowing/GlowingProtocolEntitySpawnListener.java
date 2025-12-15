package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import me.hapyl.eterna.builtin.manager.GlowingManager;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedBundlePacket;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedPacketPlayOutSpawnEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;

@ApiStatus.Internal
public final class GlowingProtocolEntitySpawnListener extends EternaLock implements Listener {
    
    private final GlowingManager manager = Eterna.getManagers().glowing;
    
    public GlowingProtocolEntitySpawnListener(@Nullable EternaKey key) {
        super(key);
    }
    
    @EventHandler()
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final WrappedBundlePacket bundlePacket = ev.getBundlePacket();
        
        if (bundlePacket == null) {
            return;
        }
        
        final WrappedPacketPlayOutSpawnEntity packet = bundlePacket.getFirstPacketWrapped(PacketWrappers.PACKET_PLAY_OUT_SPAWN_ENTITY);
        
        if (packet == null) {
            return;
        }
        
        final GlowingInstance instance = manager.getGlowing(packet.getEntityId());
        
        if (instance == null) {
            return;
        }
        
        instance.sendGlowingPacket(true);
    }
    
}
