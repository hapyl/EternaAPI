package me.hapyl.eterna.module.reflect.nulls;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class NullPacketListener extends ServerGamePacketListenerImpl {

    public NullPacketListener(MinecraftServer minecraftserver, Connection networkmanager, ServerPlayer entityplayer, CommonListenerCookie commonlistenercookie) {
        super(minecraftserver, networkmanager, entityplayer, commonlistenercookie);
    }


    @Override
    public final void resumeFlushing() {
    }

    @Override
    public final void sendPacket(Packet<?> packet) {
    }

}
