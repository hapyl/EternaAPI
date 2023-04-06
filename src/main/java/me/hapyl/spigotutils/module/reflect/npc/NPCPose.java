package me.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.world.entity.Pose;

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
     * Long Jumping
     */
    @Deprecated
    LONG_JUMPING(Pose.LONG_JUMPING),
    /**
     * Dying
     */
    @Deprecated
    DYING(Pose.DYING);

    private final Pose nms;

    NPCPose(Pose nms) {
        this.nms = nms;
    }

    @Nullable
    public static NPCPose fromNMS(Pose an) {
        for (NPCPose value : values()) {
            if (value.nms == an) {
                return value;
            }
        }

        return null;
    }

    public Pose getNMSValue() {
        return this.nms;
    }

}
