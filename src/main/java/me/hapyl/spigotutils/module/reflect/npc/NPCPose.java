package me.hapyl.spigotutils.module.reflect.npc;

import com.sun.jna.platform.win32.WinDef;
import net.minecraft.world.entity.EntityPose;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.LinkOption;

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
     * Long Jumping.
     * <br>
     * This is only used as a 'fix' for {@link #STANDING} pose not working.
     */
    @Deprecated
    LONG_JUMPING(EntityPose.g),

    /**
     * Entity Dying.
     * <br>
     * This makes the NPC to have very small hitbox.
     */
    @Deprecated
    DYING(EntityPose.h);

    private final EntityPose nms;

    NPCPose(EntityPose nms) {
        this.nms = nms;
    }

    @Nonnull
    public EntityPose getNMSValue() {
        return this.nms;
    }

    @Nullable
    public static NPCPose fromNMS(@Nonnull EntityPose an) {
        for (NPCPose value : values()) {
            if (value.nms == an) {
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
