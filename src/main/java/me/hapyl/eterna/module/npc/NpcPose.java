package me.hapyl.eterna.module.npc;

import me.hapyl.eterna.module.nms.NmsWrapper;
import me.hapyl.eterna.module.npc.appearance.Appearance;
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Represents a pose of an {@link Npc}.
 *
 * <p>Note that some poses are only applicable to certain {@link Appearance} and are ignored for those that don't support it.</p>
 */
public enum NpcPose implements NmsWrapper<Pose> {
    
    STANDING(Pose.STANDING),
    FALL_FLYING(Pose.FALL_FLYING),
    SLEEPING(Pose.SLEEPING),
    SWIMMING(Pose.SWIMMING),
    SPIN_ATTACK(Pose.SPIN_ATTACK),
    CROUCHING(Pose.CROUCHING),
    LONG_JUMPING(Pose.LONG_JUMPING),
    DYING(Pose.DYING),
    CROAKING(Pose.CROAKING),         // TODO @Dec 15, 2025 (xanyjl) -> Frog
    USING_TONGUE(Pose.USING_TONGUE), // TODO @Dec 15, 2025 (xanyjl) -> Frog
    SITTING(Pose.SITTING),           // TODO @Dec 15, 2025 (xanyjl) -> Camel
    ROARING(Pose.ROARING),           // TODO @Dec 15, 2025 (xanyjl) -> Warden
    SNIFFING(Pose.SNIFFING),         // TODO @Dec 15, 2025 (xanyjl) -> Warden
    EMERGING(Pose.EMERGING),         // TODO @Dec 15, 2025 (xanyjl) -> Warden
    DIGGING(Pose.DIGGING),           // TODO @Dec 15, 2025 (xanyjl) -> Warden
    SLIDING(Pose.SLIDING),           // TODO @Dec 15, 2025 (xanyjl) -> Breeze
    SHOOTING(Pose.SHOOTING),         // TODO @Dec 15, 2025 (xanyjl) -> Breeze
    INHALING(Pose.INHALING);         // TODO @Dec 15, 2025 (xanyjl) -> Breeze
    
    private final Pose pose;
    @Nullable private final Set<Class<? super Appearance>> applicableTo;
    
    @SafeVarargs
    NpcPose(@NotNull Pose pose, @Nullable Class<? super Appearance>... applicableTo) {
        this.pose = pose;
        this.applicableTo = applicableTo != null ? Set.of(applicableTo) : null;
    }
    
    /**
     * Gets the {@code nms} pose.
     *
     * @return the {@code nms} pose.
     */
    @NotNull
    @Override
    public Pose toNms() {
        return this.pose;
    }
    
    /**
     * Gets whether this {@link NpcPose} is applicable to the given {@link Appearance}.
     * <p>Poses that aren't applicable to the appearance ignored is most cases and cause no difference in the actual pose.</p>
     *
     * @param appearance - The appearance to check.
     * @return {@code true} if this pose is applicable to the given appearance, {@code false} otherwise.
     */
    public boolean isApplicableTo(@NotNull Class<? super Appearance> appearance) {
        return this.applicableTo == null || this.applicableTo.contains(appearance);
    }
    
    /**
     * For whatever fucking reason, setting the pose to {@link NpcPose#STANDING} doesn't actually change the hitbox for humans,
     * so we have to fake the hitbox by setting it to a random, full-size hitbox and updating the metadata.
     *
     * @return A fake {@link NpcPose#STANDING} pose.
     */
    @NotNull
    public static Pose fuckassFakeStandingPoseBecauseIFuckingHateEverythingThatExistsEspeciallyMojang() {
        return Pose.LONG_JUMPING;
    }
    
}
