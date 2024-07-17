package me.hapyl.spigotutils.module.reflect.nulls;

import me.hapyl.spigotutils.EternaLogger;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.apache.commons.lang.reflect.FieldUtils;

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
    public final void send(Packet<?> packet, @org.jetbrains.annotations.Nullable PacketSendListener packetsendlistener) {
    }

    @Override
    public final void send(Packet<?> packet, @org.jetbrains.annotations.Nullable PacketSendListener packetsendlistener, boolean flag) {
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
            EternaLogger.exception(e);
        }
    }
}
