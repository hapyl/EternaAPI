package me.hapyl.spigotutils.module.event.protocol;

import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called whenever the server receives a packet from a client.
 * <br>
 * This event handles <b>PacketPlayIn</b> and <b>Clientbound</b> packets!
 */
public class PacketReceiveEvent extends PacketEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PacketReceiveEvent(Player who, Channel channel, Packet<?> packet) {
        super(who, channel, packet);
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
