package me.hapyl.spigotutils.module.reflect.glow;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.event.protocol.PacketSendEvent;
import me.hapyl.spigotutils.module.reflect.DataWatcherType;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

public class GlowingProtocolMetadataListener implements Listener {

    private final GlowingRegistry registry = Eterna.getRegistry().glowingRegistry;
    private final Set<Packet<?>> ignoredPackets = Sets.newConcurrentHashSet();

    @EventHandler()
    public void handlePacketSendEvent(PacketSendEvent ev) {
        final Player player = ev.getPlayer();
        final PacketPlayOutEntityMetadata packet = ev.getPacket(PacketPlayOutEntityMetadata.class);

        if (packet == null) {
            return;
        }

        final int entityId = packet.b();
        final Entity entity = registry.getById(entityId);

        if (ignoredPackets.contains(packet) || entity == null) {
            return;
        }

        final Glowing glowing = registry.getGlowing(player, entity);

        if (glowing == null || !glowing.isGlowing()) {
            return;
        }

        final List<DataWatcher.c<?>> watchingObjects = packet.e();
        final List<DataWatcher.c<?>> watchingObjectsCopy = Lists.newArrayList();

        for (DataWatcher.c<?> object : watchingObjects) {
            final Object value = object.c();

            if (object.a() == 0 && value instanceof Byte byteValue) {
                watchingObjectsCopy.add(new DataWatcher.c<>(0, DataWatcherType.BYTE.get(), (byte) (byteValue | Glowing.GLOWING_BIT_MASK)));
            }
            else {
                // Create copy via reflection
                try {
                    final Constructor<?> constructor = DataWatcher.c.class.getConstructors()[0];
                    final Object newValue = constructor.newInstance(object.a(), object.b(), object.c());

                    watchingObjectsCopy.add((DataWatcher.c<?>) newValue);
                } catch (Exception e) {
                    EternaLogger.exception(e);
                }
            }
        }

        final PacketPlayOutEntityMetadata newPacket = new PacketPlayOutEntityMetadata(entityId, watchingObjectsCopy);

        ev.setCancelled(true);

        ignoredPackets.add(newPacket);
        Reflect.sendPacket(player, newPacket);
    }

}
