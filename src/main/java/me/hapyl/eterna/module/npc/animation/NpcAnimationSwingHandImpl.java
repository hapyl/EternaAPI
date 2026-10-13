package me.hapyl.eterna.module.npc.animation;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.util.Hand;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSwingAnimationPacket;
import net.minecraft.world.item.component.SwingAnimation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class NpcAnimationSwingHandImpl implements NpcAnimation {
    
    private final Hand hand;
    private final SwingType swingType;
    private final int duration;
    private final String toString;
    
    NpcAnimationSwingHandImpl(@NotNull Hand hand, @NotNull SwingType swingType, int duration) {
        this.hand = hand;
        this.swingType = swingType;
        this.duration = duration;
        this.toString = toString(hand, swingType, duration);
    }
    
    @Override
    public @NotNull Packet<? extends PacketListener> createPacket(@NotNull Npc npc) {
        return new ClientboundSwingAnimationPacket(npc.getEntityId(), hand.asInteractionHand(), new SwingAnimation(swingType.getNms(), duration));
    }
    
    @Override
    public String toString() {
        return toString;
    }
    
    private static @NotNull String toString(@NotNull Hand hand, @NotNull SwingType swingType, int duration) {
        return "swing_hand[%s, %s, %s]".formatted(hand.name().toLowerCase(), swingType.name().toLowerCase(), duration);
    }
    
}