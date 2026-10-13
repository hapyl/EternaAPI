package me.hapyl.eterna.module.npc.animation;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.util.Hand;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents an animation that can be played by an {@link Npc}.
 */
public interface NpcAnimation {
    
    /**
     * Creates a {@link Packet} that will be sent to players.
     *
     * @param npc - The npc who will play the animation.
     * @return a new packet for the animation.
     */
    @NotNull Packet<? extends PacketListener> createPacket(@NotNull Npc npc);
    
    /**
     * A static factory method for creating animation that displays damage taken, by glowing the {@link Npc} red.
     *
     * @return the take damage animation.
     */
    static @NotNull NpcAnimation takeDamage() {
        return Holder.TAKE_DAMAGE;
    }
    
    /**
     * A static factory method for creating animation that displays the critical hit, which spawns crit particles.
     *
     * @return the critical hit animation.
     */
    static @NotNull NpcAnimation criticalHit() {
        return Holder.CRITICAL_HIT;
    }
    
    /**
     * A static factory method for creating animation that displays the magical critical hit, which spawns magic crit particles.
     *
     * @return the magical critical hit, which spawns magic crit particles.
     */
    static @NotNull NpcAnimation criticalHitMagic() {
        return Holder.CRITICAL_HIT_MAGIC;
    }
    
    /**
     * A static factory method for creating animation that makes the {@link Npc} swing its hand.
     *
     * @param hand      - The hand to swing, either left or right.
     * @param swingType - The swing type.
     * @param duration  - The duration of the animation; the default value is {@code 6}.
     * @return the swing hand animation.
     */
    static @NotNull NpcAnimation swingHand(@NotNull Hand hand, @NotNull SwingType swingType, @Range(from = 0, to = Integer.MAX_VALUE) int duration) {
        return new NpcAnimationSwingHandImpl(hand, swingType, duration);
    }
    
    /**
     * A static factory method for creating animation that makes the {@link Npc} swing its hand.
     *
     * @param hand      - The hand to swing, either left or right.
     * @param swingType - The swing type.
     * @return the swing hand animation.
     */
    static @NotNull NpcAnimation swingHand(@NotNull Hand hand, @NotNull SwingType swingType) {
        return new NpcAnimationSwingHandImpl(hand, swingType, Holder.DEFAULT_SWING_ANIMATION_DURATION);
    }
    
    class Holder {
        
        private static final NpcAnimation TAKE_DAMAGE;
        private static final NpcAnimation CRITICAL_HIT;
        private static final NpcAnimation CRITICAL_HIT_MAGIC;
        
        public static final int DEFAULT_SWING_ANIMATION_DURATION;
        
        static {
            TAKE_DAMAGE = new NpcAnimation() {
                @Override
                public @NotNull Packet<? extends PacketListener> createPacket(@NotNull Npc npc) {
                    return new ClientboundHurtAnimationPacket(npc.getEntityId(), 0.0f);
                }
                
                @Override
                public String toString() {
                    return "take_damage";
                }
            };
            
            CRITICAL_HIT = new NpcAnimationImpl(ClientboundAnimatePacket.CRITICAL_HIT, "critical_hit");
            CRITICAL_HIT_MAGIC = new NpcAnimationImpl(ClientboundAnimatePacket.MAGIC_CRITICAL_HIT, "critical_effect_magic");
            
            DEFAULT_SWING_ANIMATION_DURATION = 6;
        }
        
        private Holder() {
        }
        
    }
    
}