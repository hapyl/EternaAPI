package me.hapyl.spigotutils.module.reflect.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.reflect.protocol.ProtocolListener;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GlowingProtocolMetadata extends ProtocolListener {

    private final GlowingRegistry registry = EternaPlugin.getPlugin().getGlowingManager();

    public GlowingProtocolMetadata() {
        super(PacketType.Play.Server.ENTITY_METADATA);
    }

    @Override
    public void onPacketReceiving(@Nonnull PacketEvent event) {
    }

    @Override
    public void onPacketSending(@Nonnull PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final int entityId = packet.getIntegers().read(0);
        final Player player = event.getPlayer();
        final Entity entity = registry.getById(entityId);

        if (packet.getMeta("ignore").isPresent() || entity == null) {
            return;
        }

        final Glowing glowing = registry.getGlowing(player, entity);

        if (glowing == null || !glowing.isGlowing()) {
            return;
        }

        final PacketContainer deepClone = packet.deepClone();
        final StructureModifier<List<WrappedDataValue>> collectionModifier = deepClone.getDataValueCollectionModifier();
        final WrappedDataValue dataValue = collectionModifier.read(0).get(0);

        if (dataValue != null && dataValue.getIndex() == 0) {
            final byte byteValue = (byte) dataValue.getValue();
            dataValue.setValue((byte) (byteValue | Glowing.GLOWING_BIT_MASK));
        }

        // Cancel the packet and re-send a fake packet with the glowing changed.
        deepClone.setMeta("ignore", true);
        event.setCancelled(true);

        manager.sendServerPacket(player, deepClone);
    }


}
