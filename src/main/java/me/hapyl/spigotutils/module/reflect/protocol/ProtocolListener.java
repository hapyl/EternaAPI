package me.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.hapyl.spigotutils.EternaPlugin;

public abstract class ProtocolListener {

    public ProtocolListener(PacketType type) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(EternaPlugin.getPlugin(), type) {
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
