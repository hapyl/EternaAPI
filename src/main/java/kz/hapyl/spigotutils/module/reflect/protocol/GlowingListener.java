package kz.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.reflect.glow.GlowingManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class GlowingListener extends ProtocolListener {

    private final GlowingManager manager = EternaPlugin.getPlugin().getGlowingManager();

    public GlowingListener() {
        super(PacketType.Play.Server.ENTITY_METADATA);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final int entityId = packet.getIntegers().read(0);
        final Player player = event.getPlayer();

        final Entity entity = manager.getById(entityId);
        if (entity == null || !manager.isGlowing(entity, player)) {
            return;
        }

        final List<WrappedWatchableObject> data = packet.getWatchableCollectionModifier().read(0);
        for (WrappedWatchableObject object : data) {
            if (object.getIndex() == 0) {
                final byte initByte = (byte) object.getValue();
                final byte bitMask = (byte) 0x40;
                object.setValue((byte) (initByte | bitMask));
            }
        }
    }
}
