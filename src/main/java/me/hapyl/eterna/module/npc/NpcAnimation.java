package me.hapyl.eterna.module.npc;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an {@link Npc} animation.
 */
public enum NpcAnimation {
    
    /**
     * Swings the main hand.
     * FIXME @Jan 09, 2026 (xanyjl) -> Does not work for mannequins, will be fixed in 26.1
     */
    SWING_MAIN_HAND(ClientboundAnimatePacket.SWING_MAIN_HAND),
    
    /**
     * Swings the offhand.
     * FIXME @Jan 09, 2026 (xanyjl) -> Does not work for mannequins, will be fixed in 26.1
     */
    SWING_OFF_HAND(ClientboundAnimatePacket.SWING_OFF_HAND),
    
    /**
     * Takes damage. (Glows red)
     */
    TAKE_DAMAGE(-1) {
        @NotNull
        @Override
        public Packet<?> makePacket(@NotNull Entity entity) {
            return new ClientboundHurtAnimationPacket(entity.getId(), 0.0f);
        }
    },
    
    /**
     * Plays critical particle effect.
     */
    CRITICAL_EFFECT(ClientboundAnimatePacket.CRITICAL_HIT),
    
    /**
     * Plays critical enchanted weapon effect.
     */
    MAGIC_CRITICAL_EFFECT(ClientboundAnimatePacket.MAGIC_CRITICAL_HIT);
    
    private final int action;
    
    NpcAnimation(int action) {
        this.action = action;
    }
    
    /**
     * Creates the animation {@link Packet}.
     *
     * @param entity - The entity for whom to create the packet.
     * @return the animation packet.
     */
    @NotNull
    public Packet<?> makePacket(@NotNull Entity entity) {
        return new ClientboundAnimatePacket(entity, action);
    }
    
    /**
     * Gets the magic value of the animation.
     *
     * @return the magic value of the animation.
     */
    public int getAction() {
        return action;
    }
}
