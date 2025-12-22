package me.hapyl.eterna.module.event.protocol;

import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called whenever the Server is sending a {@link Packet} to a {@link Player}.
 * <p>This event handles {@link PacketFlow#CLIENTBOUND} packets.</p>
 */
public class PacketSendEvent extends PacketEvent {
    
    private static final HandlerList HANDLER_LIST = new HandlerList();
    
    public PacketSendEvent(Player who, Channel channel, Packet<?> packet) {
        super(who, channel, packet);
    }
    
    /**
     * Gets the {@link Player} who this {@link Packet} is being sent to.
     *
     * @return the player who this packet is being sent to.
     */
    @Nonnull
    @Override
    public Player getPlayer() {
        return super.getPlayer();
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
