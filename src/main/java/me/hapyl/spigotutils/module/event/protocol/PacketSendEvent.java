package me.hapyl.spigotutils.module.event.protocol;

import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called whenever the server is about to send a packet to a player.
 * <br>
 * This event handles <b>PacketPlayOut</b> and <b>Serverbound</b> packets!
 */
public class PacketSendEvent extends PacketEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PacketSendEvent(Player who, Channel channel, Packet<?> packet) {
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
