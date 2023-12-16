package me.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.hapyl.spigotutils.EternaPlugin;

import javax.annotation.Nonnull;

public abstract class ProtocolListener {

    public final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    public ProtocolListener(@Nonnull PacketType type) {
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

    public abstract void onPacketReceiving(@Nonnull PacketEvent event);

    public abstract void onPacketSending(@Nonnull PacketEvent event);

}
