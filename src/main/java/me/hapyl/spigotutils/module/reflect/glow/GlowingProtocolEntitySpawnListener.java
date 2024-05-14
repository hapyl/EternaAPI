package me.hapyl.spigotutils.module.reflect.glow;

import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.module.event.protocol.PacketSendEvent;
import me.hapyl.spigotutils.module.reflect.wrapper.WrappedBundlePacket;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
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

        final PacketPlayOutSpawnEntity packet = bundlePacket.getFirstPacket(PacketPlayOutSpawnEntity.class);

        if (packet == null) {
            return;
        }

        final int entityId = packet.b();
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
