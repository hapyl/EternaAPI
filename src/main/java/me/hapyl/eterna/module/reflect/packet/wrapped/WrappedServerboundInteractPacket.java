package me.hapyl.eterna.module.reflect.packet.wrapped;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Represents a wrapped {@link ServerboundPlayerInputPacket}.
 *
 * <p>
 * This packet is received whenever a {@link Player} interacts with an entity.
 * </p>
 */
public class WrappedServerboundInteractPacket extends WrappedPacket<ServerboundInteractPacket> {
    
    private static final Class<?> INTERACT_CLASS = Reflect.classForName("net.minecraft.network.protocol.game.ServerboundInteractPacket$InteractionAction");
    private static final WrappedAction ATTACK_ACTION = makeAction(WrappedActionType.ATTACK, InteractionHand.MAIN_HAND, null);
    
    private final int entityId;
    private final WrappedAction action;
    
    WrappedServerboundInteractPacket(@NotNull ServerboundInteractPacket packet) {
        super(packet);
        
        entityId = super.readField("entityId", Integer.class);
        action = readAction();
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
     * Gets the {@link WrappedAction}.
     *
     * @return the action.
     */
    @NotNull
    public WrappedAction getAction() {
        return action;
    }
    
    // Mojang being annoying as always and `private`d the classes we need to check, so reflection it is
    @NotNull
    private WrappedAction readAction() {
        final Object action = super.readField("action", Object.class);
        final Field enumHandField = Reflect.getField(action, "hand");
        
        // if hand is null we assume it's ATTACK
        if (enumHandField == null) {
            return ATTACK_ACTION;
        }
        
        final InteractionHand enumHand = Reflect.readFieldValue(action, "hand", InteractionHand.class).orElseThrow();
        
        if (INTERACT_CLASS.isInstance(action)) {
            return makeAction(WrappedActionType.INTERACT, enumHand, null);
        }
        
        // INTERACT_AT has a Vec3D with the clicked position
        final Vec3 vec3D = Reflect.readFieldValue(action, "location", Vec3.class).orElseThrow();
        
        return makeAction(WrappedActionType.INTERACT_AT, enumHand, vec3D);
    }
    
    @NotNull
    private static WrappedAction makeAction(@NotNull WrappedActionType type, @NotNull InteractionHand enumHand, @Nullable Vec3 vec3D) {
        return new WrappedAction() {
            private final WrappedHand hand = WrappedHand.ofMinecraft(enumHand);
            private final Optional<WrappedPosition> vector = Optional.ofNullable(vec3D != null ? new WrappedPosition(vec3D) : null);
            
            @NotNull
            @Override
            public WrappedActionType getType() {
                return type;
            }
            
            @NotNull
            @Override
            public WrappedHand getHand() {
                return hand;
            }
            
            @Override
            @NotNull
            public Optional<WrappedPosition> getPosition() {
                return vector;
            }
        };
    }
    
    /**
     * Represents a wrapped action type.
     */
    public enum WrappedActionType {
        
        /**
         * Defines the action for when a {@link Player} right-clicks an entity.
         */
        INTERACT,
        
        /**
         * Defines the action for when a {@link Player} left-clicks an entity.
         */
        ATTACK,
        
        /**
         * Defines the action for when a {@link Player} right-clicks an entity, with an
         * additional {@link WrappedPosition} containing the position of the click.
         */
        INTERACT_AT
    }
    
    /**
     * Represents a wrapped hand.
     */
    public enum WrappedHand {
        
        /**
         * Defines the {@link Player} main hand.
         */
        MAIN_HAND,
        
        /**
         * Defines the {@link Player} off-hand.
         */
        OFF_HAND;
        
        @NotNull
        @ApiStatus.Internal
        static WrappedHand ofMinecraft(@NotNull InteractionHand enumHand) {
            return switch (enumHand) {
                case MAIN_HAND -> MAIN_HAND;
                case OFF_HAND -> OFF_HAND;
            };
        }
    }
    
    /**
     * Represents a wrapped action.
     */
    public interface WrappedAction {
        
        /**
         * Gets the {@link WrappedActionType} of this {@link WrappedAction}.
         *
         * @return the action type of this action.
         */
        @NotNull
        WrappedActionType getType();
        
        /**
         * Gets the {@link WrappedHand} of this {@link WrappedAction}.
         *
         * @return the hand of this action.
         */
        @NotNull
        WrappedHand getHand();
        
        /**
         * Gets the {@link WrappedPosition} of this {@link WrappedAction}.
         *
         * <p>
         * The position only exists if the {@link #getType()} is {@link WrappedActionType#INTERACT_AT}.
         * </p>
         *
         * @return the click position wrapped in an optional.
         */
        @NotNull
        Optional<WrappedPosition> getPosition();
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
    }
}
