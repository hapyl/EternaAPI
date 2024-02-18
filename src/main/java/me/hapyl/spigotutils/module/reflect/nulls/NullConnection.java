package me.hapyl.spigotutils.module.reflect.nulls;

import me.hapyl.spigotutils.EternaLogger;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;
import org.apache.commons.lang.reflect.FieldUtils;

import javax.annotation.Nullable;
import java.io.Serial;
import java.lang.reflect.Field;
import java.net.SocketAddress;

public class NullConnection extends NetworkManager {

    public NullConnection() {
        super(EnumProtocolDirection.a);
        n = new NullChannel(null); // channel
        o = new SocketAddress() {  // address
            @Serial private static final long serialVersionUID = 8207338859896320185L;
        };
    }

    @Override
    public void a(Packet<?> packet) { // sendPacket
    }

    @Override
    public void a(Packet<?> packet, @Nullable PacketSendListener packetsendlistener) { // sendPacket
    }

    @Override
    public void a(Packet<?> packet, @Nullable PacketSendListener packetsendlistener, boolean flag) { // sendPacket
    }

    @Override
    public void c() { // flushChannel
    }

    @Override
    public boolean k() { // isConnected
        return true;
    }

    @Override
    public void a(PacketListener packetlistener) { // setListener
        try {
            final Field packetListenerField = FieldUtils.getDeclaredField(NetworkManager.class, "q", true);
            FieldUtils.writeField(packetListenerField, this, packetlistener);

            final Field disconnectListenerField = FieldUtils.getDeclaredField(NetworkManager.class, "p", true);
            FieldUtils.writeField(disconnectListenerField, this, null);
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
    }
}
