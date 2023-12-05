package me.hapyl.spigotutils.module.reflect.npc;

import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a packet handler for the NPC.
 */
public class NPCPacketHandler {

    private final HumanNPC npc;

    public NPCPacketHandler(@Nonnull HumanNPC npc) {
        this.npc = npc;
    }

    /**
     * Sends the given packets to the given player.
     *
     * @param player - Player to send the packets to.
     * @param types  - Packets to send. Will be sent in the array order.
     */
    public void sendPackets(@Nonnull Player player, @Nonnull NPCPacketType... types) {
        for (NPCPacketType type : types) {
            sendPacket(type.createPacket(npc, player), player);
        }
    }

    /**
     * Sends the given packets to all players who this NPC is shown to.
     *
     * @param types - Packets to send. Will be sent in the array order.
     */
    public void sendPackets(@Nonnull NPCPacketType... types) {
        for (Player player : npc.getPlayers()) {
            sendPackets(player, types);
        }
    }

    private void sendPacket(Packet<?> packet, Player player) {
        Reflect.sendPacket(packet, player);
    }

}
