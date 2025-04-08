package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.manager.GlowingManager;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedBundlePacket;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedPacketPlayOutSpawnEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GlowingProtocolEntitySpawnListener implements Listener {
    
    private final GlowingManager manager = Eterna.getManagers().glowing;
    
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
