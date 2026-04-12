package me.hapyl.eterna.module.reflect.packet.wrapped;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents a wrapped {@link ServerboundPlayerInputPacket}.
 *
 * <p>
 * This packet is received whenever a {@link Player} interacts with an entity.
 * </p>
 */
public class WrappedServerboundInteractPacket extends WrappedPacket<ServerboundInteractPacket> {
    
    private final int entityId;
    private final WrappedHand hand;
    private final Optional<WrappedPosition> position;
    private final boolean usingSecondaryAction;
    
    WrappedServerboundInteractPacket(@NotNull ServerboundInteractPacket packet) {
        super(packet);
        
        this.entityId = packet.entityId();
        this.hand = packet.hand() == InteractionHand.MAIN_HAND ? WrappedHand.MAIN_HAND : WrappedHand.OFF_HAND;
        this.position = WrappedPosition.ofVec3(packet.location());
        this.usingSecondaryAction = packet.usingSecondaryAction();
    }
    
    /**
     * Gets the id of the clicked {@link Entity}.
     *
     * @return the id of the clicked entity.
     */
    public int getEntityId() {
        return entityId;
    }
    
    /**
     * Gets the {@link WrappedHand} used for the interaction.
     *
     * @return the wrapped hand used for the interaction.
     */
    @NotNull
    public WrappedHand getHand() {
        return hand;
    }
    
    /**
     * Gets the {@link WrappedPosition} wrapped in an {@link Optional}
     *
     * @return the wrapped position wrapped in an optional.
     */
    @NotNull
    public Optional<WrappedPosition> getPosition() {
        return position;
    }
    
    /**
     * Gets whether the {@link Player} is sneaking.
     *
     * @return {@code true} if the player is sneaking; {@code false} otherwise.
     */
    public boolean isUsingSecondaryAction() {
        return usingSecondaryAction;
    }
    
    /**
     * Represents a {@link WrappedHand} that indicates which hand the {@link Player} has used for the interaction.
     */
    public enum WrappedHand {
        
        /**
         * Defines the {@link Player} main hand.
         */
        MAIN_HAND,
        
        /**
         * Defines the {@link Player} off-hand.
         */
        OFF_HAND
    }
    
    /**
     * Represents a clicked position.
     */
    public static class WrappedPosition {
        private final double x;
        private final double y;
        private final double z;
        
        WrappedPosition(@NotNull Vec3 vec3D) {
            this.x = vec3D.x();
            this.y = vec3D.y();
            this.z = vec3D.z();
        }
        
        /**
         * Gets the {@code x} coordinate.
         *
         * @return the {@code x} coordinate.
         */
        public double getX() {
            return x;
        }
        
        /**
         * Gets the {@code y} coordinate.
         *
         * @return the {@code y} coordinate.
         */
        public double getY() {
            return y;
        }
        
        /**
         * Gets the {@code z} coordinate.
         *
         * @return the {@code z} coordinate.
         */
        public double getZ() {
            return z;
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + "[%s, %s, %s]".formatted(x, y, z);
        }
        
        @NotNull
        public static Optional<WrappedPosition> ofVec3(@Nullable Vec3 vec3) {
            return vec3 != null ? Optional.of(new WrappedPosition(vec3)) : Optional.empty();
        }
    }
}
