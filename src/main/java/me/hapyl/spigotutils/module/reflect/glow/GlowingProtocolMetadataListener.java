package me.hapyl.spigotutils.module.reflect.glow;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.module.event.protocol.PacketSendEvent;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.spigotutils.module.reflect.packet.wrapped.WrappedPacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;

public class GlowingProtocolMetadataListener implements Listener {

    private final GlowingRegistry registry = Eterna.getRegistry().glowingRegistry;
    private final Set<Packet<?>> ignoredPackets = Sets.newConcurrentHashSet();

    @EventHandler()
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final Player player = ev.getPlayer();
        final WrappedPacketPlayOutEntityMetadata packet = ev.getWrappedPacket(PacketWrappers.PACKET_PLAY_OUT_ENTITY_METADATA);

        if (packet == null) {
            return;
        }

        final int entityId = packet.getEntityId();
        final Entity entity = registry.getById(entityId);

        if (ignoredPackets.contains(packet.getPacket()) || entity == null) {
            return;
        }

        final Glowing glowing = registry.getGlowing(player, entity);

        if (glowing == null || !glowing.isGlowing()) {
            return;
        }

        final WrappedPacketPlayOutEntityMetadata.WrappedDataWatcherValueList packedItems = packet.getWrappedDataWatcherValueList();

        for (WrappedPacketPlayOutEntityMetadata.WrappedDataWatcherValue item : packedItems) {
            final int id = item.getId();
            final Byte value = item.getValueAs(Byte.class);

            // We need to override byte value with id 0
            if (id == 0 && value != null) {
                item.setValue((byte) (value | Glowing.GLOWING_BIT_MASK));
            }
        }

        final PacketPlayOutEntityMetadata newPacket = new PacketPlayOutEntityMetadata(entityId, packedItems.getAsDataWatcherObjectList());

        // Minecraft is annoying with the metadata and will send a Metadata Packet to all
        // nearby players, we need to cancel the event and send our magic packet to the target.
        ev.setCancelled(true);

        ignoredPackets.add(newPacket);
        Reflect.sendPacket(player, newPacket);
    }

}
