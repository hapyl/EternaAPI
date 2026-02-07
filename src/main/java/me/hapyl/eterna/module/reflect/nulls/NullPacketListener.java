package me.hapyl.eterna.module.reflect.nulls;

import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

/**
 * Represents a null {@link ServerGamePacketListenerImpl}.
 */
@ApiStatus.Internal
public class NullPacketListener extends ServerGamePacketListenerImpl {
    
    public NullPacketListener(MinecraftServer minecraftserver, Connection networkmanager, ServerPlayer entityplayer, CommonListenerCookie commonlistenercookie) {
        super(minecraftserver, networkmanager, entityplayer, commonlistenercookie);
    }
    
    @Override
    public final void resumeFlushing() {
    }
    
    @Override
    public void send(@NotNull Packet<?> packet) {
    }
    
    @Override
    public void send(@NotNull Packet<?> packet, @Nullable ChannelFutureListener sendListener) {
    }
}
