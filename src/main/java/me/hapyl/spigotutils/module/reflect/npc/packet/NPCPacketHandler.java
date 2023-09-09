package me.hapyl.spigotutils.module.reflect.npc.packet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.packet.NPCPacketType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.level.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.Map;

public class NPCPacketHandler {

    private final HumanNPC npc;
    private final Map<NPCPacketType, Packet<?>> packets;

    public NPCPacketHandler(@Nonnull HumanNPC npc) {
        this.npc = npc;
        this.packets = Maps.newHashMap();

        initPackets();
    }

    @Nonnull
    public Packet<?> getPacket(@Nonnull NPCPacketType type) {
        final Packet<?> packet = packets.get(type);

        if (packet == null) {
            throw new IllegalArgumentException("Packet %s does not exist for %s!".formatted(type, npc.toString()));
        }

        return packet;
    }

    public void sendPackets(@Nonnull NPCPacketType... types) {
        for (NPCPacketType type : types) {
            sendPacket(type);
        }
    }

    public void sendPacket(@Nonnull NPCPacketType type) {
        sendPacket(getPacket(type));
    }

    protected void initPackets() {
        final EntityPlayer human = npc.getHuman();

        packets.put(NPCPacketType.ADD_PLAYER, new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.a, human));
        packets.put(NPCPacketType.REMOVE_PLAYER, new ClientboundPlayerInfoRemovePacket(Lists.newArrayList(npc.getUuid())));
        packets.put(NPCPacketType.SPAWN, new PacketPlayOutNamedEntitySpawn(human));
        packets.put(NPCPacketType.DESTROY, new PacketPlayOutEntityDestroy(npc.getId()));
    }

    private void sendPacket(Packet<?> packet) {
        Reflect.sendPacket(packet, npc.getPlayers());
    }

}
