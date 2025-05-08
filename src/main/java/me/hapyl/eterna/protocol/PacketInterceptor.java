package me.hapyl.eterna.protocol;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.hapyl.eterna.module.event.protocol.PacketEvent;
import me.hapyl.eterna.module.event.protocol.PacketReceiveEvent;
import me.hapyl.eterna.module.event.protocol.PacketSendEvent;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.function.Function;

public class PacketInterceptor extends ChannelDuplexHandler {

    protected volatile Player player;

    PacketInterceptor(Player player) {
        this.player = player;
    }

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        if (workPacket(object, packet -> new PacketReceiveEvent(player, ctx.channel(), packet))) {
            return;
        }

        super.channelRead(ctx, object);
    }

    @Override
    public final void write(ChannelHandlerContext ctx, Object object, ChannelPromise promise) throws Exception {
        if (workPacket(object, packet -> new PacketSendEvent(player, ctx.channel(), packet))) {
            return;
        }

        super.write(ctx, object, promise);
    }

    private boolean workPacket(Object packetObject, Function<Packet<?>, PacketEvent> fn) {
        if (!(packetObject instanceof Packet<?> packet)) {
            return false;
        }

        final PacketEvent event = fn.apply(packet);
        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.callEvent(event);

        return event.isCancelled();
    }

}
