package me.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.world.entity.Pose;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum NPCPose {

    /**
     * Standing.
     */
    STANDING(Pose.STANDING),

    /**
     * Flying in creative.
     */
    FALL_FLYING(Pose.FALL_FLYING),

    /**
     * Sleeping in the bed.
     */
    SLEEPING(Pose.SLEEPING),

    /**
     * Swimming in a liquid.
     */
    SWIMMING(Pose.SWIMMING),

    /**
     * Idk
     */
    SPIN_ATTACK(Pose.SPIN_ATTACK),

    /**
     * Crunching (Sneaking)
     */
    CROUCHING(Pose.CROUCHING),

    /**
     * Long Jumping.
     * <br>
     * This is only used as a 'fix' for {@link #STANDING} pose not working.
     */
    @Deprecated
    LONG_JUMPING(Pose.LONG_JUMPING),

    /**
     * Entity Dying.
     * <br>
     * This makes the NPC to have very small hitbox.
     */
    @Deprecated
    DYING(Pose.DYING);

    private final Pose nms;

    NPCPose(Pose nms) {
        this.nms = nms;
    }

    @Nonnull
    public Pose getNMSValue() {
        return this.nms;
    }

    @Nullable
    public static NPCPose fromNMS(@Nonnull Pose nms) {
        for (NPCPose value : values()) {
            if (value.nms == nms) {
                return value;
            }
        }

        return null;
    }

    @Nonnull
    public static NPCPose fakeStandingPoseForNPCBecauseActualStandingPoseDoesNotWorkForSomeReason() {
        return LONG_JUMPING;
    }

}
