package me.hapyl.eterna.module.event.protocol;

import io.netty.channel.Channel;
import net.minecraft.network.ClientboundPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link PacketSendEvent} which is fired whenever the {@code server} sends a {@link Packet} to the {@code client}.
 *
 * <p>This event handles {@link PacketFlow#CLIENTBOUND} packets.</p>
 */
public class PacketSendEvent extends PacketEvent<ClientboundPacketListener> {
    
    private static final HandlerList HANDLER_LIST = new HandlerList();
    
    @ApiStatus.Internal
    public PacketSendEvent(@NotNull Player who, Channel channel, Packet<?> packet) {
        super(who, channel, packet);
    }
    
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
