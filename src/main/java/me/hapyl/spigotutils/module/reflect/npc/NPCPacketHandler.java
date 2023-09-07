package me.hapyl.spigotutils.module.reflect.npc;

import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

public class NPCPacketHandler {

    private final HumanNPC npc;

    public NPCPacketHandler(HumanNPC npc) {
        this.npc = npc;
    }

    private void sendPacket(Packet<?> packet) {
        for (Player player : npc.getPlayers()) {
            Reflect.sendPacket(packet, player);
        }
    }

}
