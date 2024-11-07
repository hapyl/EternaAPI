package me.hapyl.eterna.module.reflect.packet;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class IPacketImpl<P extends Packet<?>> implements IPacket<P> {

    private final P packet;

    IPacketImpl(P packet) {
        this.packet = packet;
    }

    @Override
    public void send(@Nonnull Player player) {
        Reflect.sendPacket(player, this.packet);
    }

    @Nonnull
    @Override
    public P packet() {
        return this.packet;
    }

}
