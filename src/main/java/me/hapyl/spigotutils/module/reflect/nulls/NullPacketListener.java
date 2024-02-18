package me.hapyl.spigotutils.module.reflect.nulls;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.PlayerConnection;

public class NullPacketListener extends PlayerConnection {

    public NullPacketListener(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer, CommonListenerCookie commonlistenercookie) {
        super(minecraftserver, networkmanager, entityplayer, commonlistenercookie);
    }

    @Override
    public void h() { // resumeFlushing
    }

    @Override
    public void b(Packet<?> packet) { // sendPacket
    }
}
