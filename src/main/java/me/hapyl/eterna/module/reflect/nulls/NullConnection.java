package me.hapyl.eterna.module.reflect.nulls;

import io.netty.channel.ChannelFutureListener;
import me.hapyl.eterna.EternaLogger;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.annotation.Nullable;
import java.io.Serial;
import java.net.SocketAddress;

public class NullConnection extends Connection {

    public NullConnection() {
        super(PacketFlow.SERVERBOUND);
        channel = new NullChannel(null);
        address = new SocketAddress() {
            @Serial private static final long serialVersionUID = 8207338859896320185L;
        };
    }

    @Override
    public final void send(Packet<?> packet) {
    }
    
    @Override
    public final void send(Packet<?> packet, @Nullable ChannelFutureListener channelfuturelistener) {
    }
    
    @Override
    public final void send(Packet<?> packet, @Nullable ChannelFutureListener channelfuturelistener, boolean flag) {
    }
    
    @Override
    public final void flushChannel() {
    }

    @Override
    public final boolean isConnected() {
        return true;
    }

    @Override
    public final void setListenerForServerboundHandshake(PacketListener packetlistener) { // setListener
        try {
            FieldUtils.writeDeclaredField(this, "packetListener", packetlistener, true);
            FieldUtils.writeDeclaredField(this, "disconnectListener", null, true);
        } catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
}
