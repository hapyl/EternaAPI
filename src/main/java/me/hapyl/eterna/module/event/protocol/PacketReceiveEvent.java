package me.hapyl.eterna.module.event.protocol;

import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called whenever the Server is receiving a {@link Packet} from a {@link Player}.
 * <p>This event handles {@link PacketFlow#CLIENTBOUND} packets.</p>
 */
public class PacketReceiveEvent extends PacketEvent {
    
    private static final HandlerList HANDLER_LIST = new HandlerList();
    
    public PacketReceiveEvent(Player who, Channel channel, Packet<?> packet) {
        super(who, channel, packet);
    }
    
    /**
     * Gets the {@link Player} who sent the {@link Packet} to the server.
     *
     * @return the player who sent the packet.
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
