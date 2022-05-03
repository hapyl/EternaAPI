package kz.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import kz.hapyl.spigotutils.SpigotUtilsPlugin;

public abstract class ProtocolListener {

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    public ProtocolListener(PacketType type) {
        manager.addPacketListener(new PacketAdapter(SpigotUtilsPlugin.getPlugin(), type) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                ProtocolListener.this.onPacketReceiving(event);
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                ProtocolListener.this.onPacketSending(event);
            }


        });
    }

    public abstract void onPacketReceiving(PacketEvent event);

    public abstract void onPacketSending(PacketEvent event);

}
