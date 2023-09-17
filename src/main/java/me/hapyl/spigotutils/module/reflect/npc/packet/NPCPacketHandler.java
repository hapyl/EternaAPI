package me.hapyl.spigotutils.module.reflect.npc.packet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Represents a packet handler for the NPC.
 */
public class NPCPacketHandler {

    private final HumanNPC npc;
    private final Map<NPCPacketType, Packet<?>> packets;

    public NPCPacketHandler(@Nonnull HumanNPC npc) {
        this.npc = npc;
        this.packets = Maps.newHashMap();

        initPackets();
    }

    /**
     * Gets the packet of the given type; or null if not initiated.
     *
     * @param type - Packet type.
     * @return the packet or null.
     */
    @Nonnull
    public Packet<?> getPacket(@Nonnull NPCPacketType type) {
        final Packet<?> packet = packets.get(type);

        if (packet == null) {
            throw new IllegalArgumentException("Packet %s does not exist for %s!".formatted(type, npc.toString()));
        }

        return packet;
    }

    /**
     * Sends the given packets to the given player.
     *
     * @param player - Player to send the packets to.
     * @param types  - Packets to send. Will be sent in the array order.
     */
    public void sendPackets(@Nonnull Player player, @Nonnull NPCPacketType... types) {
        for (NPCPacketType type : types) {
            sendPacket(getPacket(type), player);
        }
    }

    /**
     * Sends the given packets to all players who this NPC is shown to.
     *
     * @param types - Packets to send. Will be sent in the array order.
     */
    public void sendPackets(@Nonnull NPCPacketType... types) {
        for (NPCPacketType type : types) {
            sendPacket(getPacket(type));
        }
    }

    protected void initPackets() {
        final EntityPlayer human = npc.getHuman();

        packets.put(NPCPacketType.ADD_PLAYER, new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.a, human));
        packets.put(NPCPacketType.REMOVE_PLAYER, new ClientboundPlayerInfoRemovePacket(Lists.newArrayList(npc.getUuid())));
        packets.put(NPCPacketType.SPAWN, new PacketPlayOutNamedEntitySpawn(human));
        packets.put(NPCPacketType.DESTROY, new PacketPlayOutEntityDestroy(npc.getId()));
    }

    private void sendPacket(Packet<?> packet, Player player) {
        Reflect.sendPacket(packet, player);
    }

    private void sendPacket(Packet<?> packet) {
        Reflect.sendPacket(packet, npc.getPlayers());
    }

}
