package me.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.reflect.glow.GlowingRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class GlowingListener extends ProtocolListener {

    private final GlowingRegistry manager = EternaPlugin.getPlugin().getGlowingManager();
    private final ProtocolManager protocol = ProtocolLibrary.getProtocolManager();

    public GlowingListener() {
        super(PacketType.Play.Server.ENTITY_METADATA);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
    }

    //    @Override
    //    public void onPacketSending(PacketEvent event) {
    //        final PacketContainer packet = event.getPacket();
    //        final Player player = event.getPlayer();
    //        final WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
    //
    //        final byte value = watcher.getByte(0);
    //        if (!watcher.getIndexes().contains(0) || value == 0) {
    //            watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x40);
    //            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
    //            return;
    //        }
    //
    //        if (value == 0x40) {
    //            return;
    //        }
    //
    //        // Update packet
    //        final PacketContainer newPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
    //        newPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
    //        newPacket.getIntegers().write(0, packet.getIntegers().read(0));
    //        try {
    //            protocol.sendServerPacket(player, newPacket);
    //        } catch (InvocationTargetException e) {
    //            e.printStackTrace();
    //        }
    //
    //    }

    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final int entityId = packet.getIntegers().read(0);
        final Player player = event.getPlayer();

        final Entity entity = manager.getById(entityId);
        if (entity == null || !manager.isGlowing(entity, player)) {
            return;
        }

        //
        // After many attempts of fixing the self-glowing bug
        // I gave up and returned everything as it used to be.
        // Don't know the way of doing this or checking the right values.
        //

        final List<WrappedWatchableObject> data = packet.getWatchableCollectionModifier().read(0);

        for (WrappedWatchableObject object : data) {
            if (object.getIndex() == 0) {
                final byte value = (byte) object.getValue();
                object.setValue((byte) (value | 0x40));
            }
        }

    }


}
