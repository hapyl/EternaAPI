package me.hapyl.eterna.module.event.protocol;

import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ServerPacketListener;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a {@link PacketReceiveEvent} which is fired whenever the {@code server} receives a {@link Packet} from the {@code client}.
 *
 * <p>This event handles {@link PacketFlow#SERVERBOUND} packets.</p>
 */
public class PacketReceiveEvent extends PacketEvent<ServerPacketListener> {
    
    private static final HandlerList HANDLER_LIST = new HandlerList();
    
    @ApiStatus.Internal
    public PacketReceiveEvent(@NotNull Player player, @NotNull Channel channel, @NotNull Packet<?> packet) {
        super(player, channel, packet);
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
