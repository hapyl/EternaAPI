package me.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.reflect.glow.GlowingRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GlowingListener extends ProtocolListener {

    private final GlowingRegistry manager = EternaPlugin.getPlugin().getGlowingManager();
    private final ProtocolManager protocol = ProtocolLibrary.getProtocolManager();

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

        if (packet.getMeta("ignore").isPresent() || entity == null || !manager.isGlowing(player, entity)) {
            return;
        }

        final PacketContainer clonePacket = packet.deepClone();
        final WrappedDataValue dataValue = clonePacket.getDataValueCollectionModifier().read(0).get(0);

        if (dataValue == null) {
            return;
        }

        if (dataValue.getIndex() == 0) {
            dataValue.setValue((byte) ((byte) dataValue.getValue() | 0x40));
            clonePacket.setMeta("ignore", true);
            protocol.sendServerPacket(player, clonePacket);
            event.setCancelled(true);
        }
    }


}
