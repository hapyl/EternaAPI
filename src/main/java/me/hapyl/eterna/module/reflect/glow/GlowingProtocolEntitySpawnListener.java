package me.hapyl.eterna.module.reflect.glow;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedBundlePacket;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedPacketPlayOutSpawnEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GlowingProtocolEntitySpawnListener implements Listener {

    private final GlowingRegistry registry = Eterna.getRegistry().glowingRegistry;

    @EventHandler()
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final Player player = ev.getPlayer();
        final WrappedBundlePacket bundlePacket = ev.getBundlePacket();

        if (bundlePacket == null) {
            return;
        }

        final WrappedPacketPlayOutSpawnEntity packet = bundlePacket.getFirstPacketWrapped(PacketWrappers.PACKET_PLAY_OUT_SPAWN_ENTITY);

        if (packet == null) {
            return;
        }

        final int entityId = packet.getEntityId();
        final Entity entity = registry.getById(entityId);

        if (entity == null) {
            return;
        }

        final Glowing glowing = registry.getGlowing(player, entity);

        if (glowing == null || !glowing.isGlowing()) {
            return;
        }

        // Since I have no idea how to properly fix this, doing it by
        // stopping the glowing and restart it with previous duration.

        // This also calls the previous glowing tick and stop methods,
        // which might not be the best thing, but whatever IT WORKS!
        glowing.restart();
    }
}
