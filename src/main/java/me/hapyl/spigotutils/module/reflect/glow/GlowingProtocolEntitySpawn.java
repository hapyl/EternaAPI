package me.hapyl.spigotutils.module.reflect.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.hapyl.spigotutils.module.reflect.protocol.ProtocolListener;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class GlowingProtocolEntitySpawn extends ProtocolListener {

    private final GlowingRegistry registry = EternaRegistry.getGlowingManager();

    public GlowingProtocolEntitySpawn() {
        super(PacketType.Play.Server.SPAWN_ENTITY);
    }

    @Override
    public void onPacketReceiving(@Nonnull PacketEvent event) {
    }

    @Override
    public void onPacketSending(@Nonnull PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final Player player = event.getPlayer();
        final int entityId = packet.getIntegers().read(0);
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
