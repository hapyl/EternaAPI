package me.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.world.entity.EntityPose;

import javax.annotation.Nullable;

public enum NPCPose {

    /**
     * Standing.
     */
    STANDING(EntityPose.a),
    /**
     * Flying in creative.
     */
    FALL_FLYING(EntityPose.b),
    /**
     * Sleeping in the bed.
     */
    SLEEPING(EntityPose.c),
    /**
     * Swimming in a liquid.
     */
    SWIMMING(EntityPose.d),
    /**
     * Idk
     */
    SPIN_ATTACK(EntityPose.e),
    /**
     * Crunching (Sneaking)
     */
    CROUCHING(EntityPose.f),
    /**
     * Long Jumping
     */
    @Deprecated
    LONG_JUMPING(EntityPose.g),
    /**
     * Dying
     */
    @Deprecated
    DYING(EntityPose.h);

    private final EntityPose nms;

    NPCPose(EntityPose nms) {
        this.nms = nms;
    }

    @Nullable
    public static NPCPose fromNMS(EntityPose an) {
        for (NPCPose value : values()) {
            if (value.nms == an) {
                return value;
            }
        }

        return null;
    }

    public EntityPose getNMSValue() {
        return this.nms;
    }

}
