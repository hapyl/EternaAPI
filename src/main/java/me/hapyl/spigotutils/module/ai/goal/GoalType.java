package me.hapyl.spigotutils.module.ai.goal;

import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;

import javax.annotation.Nonnull;

/**
 * Represents a GoalType with a link to the PathfinderGoal class.
 * You can use this class to cancel goal by type, rather than by class.
 */
@SuppressWarnings("rawtypes")
public record GoalType<T extends PathfinderGoal>(@Nonnull Class<T> t) {

    public static final GoalType<PathfinderGoalLookAtPlayer> LOOK_AT_PLAYER = of(PathfinderGoalLookAtPlayer.class);
    public static final GoalType<PathfinderGoalAvoidTarget> AVOID_TARGET = of(PathfinderGoalAvoidTarget.class);
    public static final GoalType<PathfinderGoalBowShoot> SHOOT_BOW = of(PathfinderGoalBowShoot.class);
    public static final GoalType<PathfinderGoalFollowEntity> FOLLOW_ENTITY = of(PathfinderGoalFollowEntity.class);
    public static final GoalType<PathfinderGoalFollowOwner> FOLLOW_OWNER = of(PathfinderGoalFollowOwner.class);
    public static final GoalType<PathfinderGoalRandomLookaround> RANDOMLY_LOOK_AROUND = of(PathfinderGoalRandomLookaround.class);
    public static final GoalType<PathfinderGoalRandomStrollLand> RANDOMLY_STROLL_AROUND = of(PathfinderGoalRandomStrollLand.class);
    public static final GoalType<PathfinderGoalTempt> TEMPT = of(PathfinderGoalTempt.class);
    public static final GoalType<PathfinderGoalGotoTarget> MOVE_TO = of(PathfinderGoalGotoTarget.class);
    public static final GoalType<PathfinderGoalNearestAttackableTarget> ATTACK_NEAREST_TARGET = of(PathfinderGoalNearestAttackableTarget.class);
    public static final GoalType<PathfinderGoalFloat> FLOAT = of(PathfinderGoalFloat.class);
    public static final GoalType<PathfinderGoalMeleeAttack> MELEE_ATTACK = of(PathfinderGoalMeleeAttack.class);

    private static <T extends PathfinderGoal> GoalType<T> of(Class<T> t) {
        return new GoalType<>(t);
    }

}
