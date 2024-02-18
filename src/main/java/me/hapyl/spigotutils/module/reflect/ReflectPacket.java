package me.hapyl.spigotutils.module.reflect;

import me.hapyl.spigotutils.module.chat.Chat;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collection;

public class ReflectPacket {
    private final Packet<?>[] packets;

    public ReflectPacket(Packet<?>... packet) {
        this.packets = packet;
    }

    // use this for single packet sending
    public static void send(Packet<?> packet, Player... players) {
        Reflect.sendPacketToAll(packet, players);
    }

    public static void wrapAndSend(Packet<?> packet, Player... players) {
        send(packet, players);
    }

    public void sendPackets(Player player) {
        if (this.packets.length == 1) {
            Reflect.sendPacketToAll(this.packets[0], player);
            return;
        }
        for (final Packet<?> packet1 : this.packets) {
            Reflect.sendPacketToAll(packet1, player);
        }
    }

    public void sendPacket(int packet, Player player) {
        if (packet >= this.packets.length) {
            Chat.sendMessage(player, "&cCould not sent packet to your since it doesn't exist!");
            return;
        }
        Reflect.sendPacketToAll(this.packets[packet], player);
    }

    @Nullable
    public Packet<?> getPacket(int packet) {
        return packet >= this.packets.length ? null : this.packets[packet];
    }

    public Packet<?>[] getPackets() {
        return packets;
    }

    public boolean hasPacket(int packet) {
        return packets.length >= packet;
    }

    public void sendPacket(int packet, Player... players) {
        for (final Player player : players) {
            sendPacket(packet, player);
        }
    }

    public void sendPackets(Player... players) {
        for (final Player player : players) {
            sendPackets(player);
        }
    }

    public void sendPackets(Collection<Player> players) {
        for (final Player player : players) {
            sendPackets(player);
        }
    }

    public void sendPacketsToEveryone() {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendPackets(onlinePlayer);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final Packet<?> packet : this.packets) {
            builder.append(packet.toString());
        }
        return builder.toString();
    }
}
