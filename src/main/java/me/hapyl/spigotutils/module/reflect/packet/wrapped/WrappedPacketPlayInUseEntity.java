package me.hapyl.spigotutils.module.reflect.packet.wrapped;

import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.world.EnumHand;
import net.minecraft.world.phys.Vec3D;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class WrappedPacketPlayInUseEntity extends WrappedPacket<PacketPlayInUseEntity> {

    private static final Class<?> INTERACT_CLASS = Reflect.getClass("net.minecraft.network.protocol.game.PacketPlayInUseEntity$d");
    private static final WrappedAction ATTACK_ACTION = makeAction(WrappedActionType.ATTACK, EnumHand.a, null);

    private final int entityId;
    private final WrappedAction action;

    public WrappedPacketPlayInUseEntity(PacketPlayInUseEntity packet) {
        super(packet);

        entityId = super.readField("b", Integer.class);
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

    private WrappedAction readAction() {
        final Object action = super.readField("c", Object.class);
        final Field enumHandField = Reflect.getDeclaredField(action, "a");

        // if hand is null we assume it's ATTACK
        if (enumHandField == null) {
            return ATTACK_ACTION;
        }

        final EnumHand enumHand = Reflect.getDeclaredFieldValue(action, "a", EnumHand.class);

        if (INTERACT_CLASS.isInstance(action)) {
            return makeAction(WrappedActionType.INTERACT, enumHand, null);
        }

        // INTERACT_AT has a Vec3D with the clicked position
        final Vec3D vec3D = Reflect.getDeclaredFieldValue(action, "b", Vec3D.class);

        return makeAction(WrappedActionType.INTERACT_AT, enumHand, vec3D);
    }

    private static WrappedAction makeAction(WrappedActionType type, EnumHand enumHand, Vec3D vec3D) {
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
        public static WrappedHand of(@Nonnull EnumHand enumHand) {
            return switch (enumHand) {
                case a -> MAIN_HAND;
                case b -> OFF_HAND;
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

        public WrappedPosition(Vec3D vec3D) {
            this.x = vec3D.a();
            this.y = vec3D.b();
            this.z = vec3D.c();
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
