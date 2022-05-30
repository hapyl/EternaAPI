package me.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.world.entity.EntityPose;

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
     * Idk
     */
    LONG_JUMPING(EntityPose.g),
    /**
     * Idk
     */
    DYING(EntityPose.h);

    private final EntityPose nms;

    NPCPose(EntityPose nms) {
        this.nms = nms;
    }

    public EntityPose getNMSValue() {
        return this.nms;
    }

}
