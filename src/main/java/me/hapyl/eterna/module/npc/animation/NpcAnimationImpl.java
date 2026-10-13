package me.hapyl.eterna.module.npc.animation;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class NpcAnimationImpl implements NpcAnimation {
    
    private final int magicValue;
    private final String toString;
    
    NpcAnimationImpl(int magicValue, @NotNull String toString) {
        this.magicValue = magicValue;
        this.toString = toString;
    }
    
    @Override
    public @NotNull Packet<? extends PacketListener> createPacket(@NotNull Npc npc) {
        return new ClientboundAnimatePacket(npc.getAppearance().getHandle(), magicValue);
    }
    
    @Override
    public String toString() {
        return toString;
    }
    
}