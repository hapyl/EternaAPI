package me.hapyl.eterna.module.reflect.packet.wrapped;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * Represents a wrapped {@link ServerboundInteractPacket}.
 */
public class WrappedServerboundInteractPacket extends WrappedPacket<ServerboundInteractPacket> {
    
    private static final Class<?> INTERACT_CLASS = Reflect.classForName("net.minecraft.network.protocol.game.ServerboundInteractPacket$InteractionAction");
    private static final WrappedAction ATTACK_ACTION = makeAction(WrappedActionType.ATTACK, InteractionHand.MAIN_HAND, null);
    
    private final int entityId;
    private final WrappedAction action;
    
    public WrappedServerboundInteractPacket(ServerboundInteractPacket packet) {
        super(packet);
        
        entityId = super.readField("entityId", Integer.class);
        action = readAction();
    }
    
    /**
     * Gets the Id of the clicked entity.
     *
     * @return the Id of the clicked entity.
     */
    public int getEntityId() {
        return entityId;
    }
    
    /**
     * Gets the {@link WrappedAction}.
     *
     * @return the action.
     */
    @Nonnull
    public WrappedAction getAction() {
        return action;
    }
    
    // Mojang being annoying as always and `private`d the classes we need to check, so reflection it is
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
    
    private static WrappedAction makeAction(WrappedActionType type, InteractionHand enumHand, Vec3 vec3D) {
        return new WrappedAction() {
            private final WrappedHand hand = WrappedHand.of(enumHand);
            private final WrappedPosition vector = vec3D != null ? new WrappedPosition(vec3D) : null;
            
            @Nonnull
            @Override
            public WrappedActionType getType() {
                return type;
            }
            
            @Nonnull
            @Override
            public WrappedHand getHand() {
                return hand;
            }
            
            @Nullable
            @Override
            public WrappedPosition getPosition() {
                return vector;
            }
        };
    }
    
    /**
     * Wraps an action type.
     */
    public enum WrappedActionType {
        /**
         * Right-Clicked at entity.
         */
        INTERACT,
        /**
         * Left-Clicked at entity.
         */
        ATTACK,
        /**
         * Right-Clicked at entity.
         */
        INTERACT_AT
    }
    
    /**
     * Wraps an interaction hand.
     */
    public enum WrappedHand {
        /**
         * Main hand.
         */
        MAIN_HAND,
        /**
         * Offhand
         */
        OFF_HAND;
        
        @Nonnull
        public static WrappedHand of(@Nonnull InteractionHand enumHand) {
            return switch (enumHand) {
                case MAIN_HAND -> MAIN_HAND;
                case OFF_HAND -> OFF_HAND;
            };
        }
    }
    
    /**
     * Wraps an action.
     */
    public interface WrappedAction {
        /**
         * Gets the action type of this action.
         *
         * @return the action type.
         */
        @Nonnull
        WrappedActionType getType();
        
        /**
         * Gets the hand of this action.
         *
         * @return the hand of this action.
         */
        @Nonnull
        WrappedHand getHand();
        
        /**
         * Gets the clicked position of {@link WrappedActionType} is {@link WrappedActionType#INTERACT_AT}, <code>null</code> otherwise.
         *
         * @return the clicked position of the action type is {@link WrappedActionType#INTERACT_AT}, <code>null</code> otherwise.
         */
        @Nullable
        WrappedPosition getPosition();
    }
    
    /**
     * Represents a clicked position.
     */
    public static class WrappedPosition {
        private final double x;
        private final double y;
        private final double z;
        
        public WrappedPosition(Vec3 vec3D) {
            this.x = vec3D.x();
            this.y = vec3D.y();
            this.z = vec3D.z();
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
        
        public double getZ() {
            return z;
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + "{x: %s, y: %s, z: %s}".formatted(x, y, z);
        }
    }
}
