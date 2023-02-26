package me.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.hapyl.spigotutils.module.reflect.visibility.Visibility;
import org.bukkit.entity.Player;

public class VisibilityListener extends ProtocolListener {
    public VisibilityListener() {
        super(PacketType.Play.Server.SPAWN_ENTITY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        final Player player = event.getPlayer();
        final PacketContainer packet = event.getPacket();

        final int entityId = packet.getIntegers().read(0);

        final Visibility visibility = Visibility.of(entityId);
        if (visibility == null || visibility.canSee(player)) {
            return;
        }

        event.setCancelled(true);
    }
}
