package me.hapyl.spigotutils.module.ai.goal;

import net.minecraft.world.entity.ai.goal.*;

/**
 * Represents a GoalType with a link to the PathfinderGoal class.
 * You can use this class to cancel goal by type, rather than by class.
 */
@SuppressWarnings("rawtypes")
public record GoalType<T extends PathfinderGoal>(Class<T> t) {

    public static final GoalType<PathfinderGoalLookAtPlayer> LOOK_AT_PLAYER = new GoalType<>(PathfinderGoalLookAtPlayer.class);
    public static final GoalType<PathfinderGoalAvoidTarget> AVOID_TARGET = new GoalType<>(PathfinderGoalAvoidTarget.class);
    public static final GoalType<PathfinderGoalBowShoot> SHOOT_BOW = new GoalType<>(PathfinderGoalBowShoot.class);
    public static final GoalType<PathfinderGoalFollowEntity> FOLLOW_ENTITY = new GoalType<>(PathfinderGoalFollowEntity.class);
    public static final GoalType<PathfinderGoalFollowOwner> FOLLOW_OWNER = new GoalType<>(PathfinderGoalFollowOwner.class);
    public static final GoalType<PathfinderGoalRandomLookaround> RANDOMLY_LOOK_AROUND = new GoalType<>(PathfinderGoalRandomLookaround.class);
    public static final GoalType<PathfinderGoalRandomStrollLand> RANDOMLY_STROLL_AROUND = new GoalType<>(PathfinderGoalRandomStrollLand.class);
    public static final GoalType<PathfinderGoalTempt> TEMPT = new GoalType<>(PathfinderGoalTempt.class);
    public static final GoalType<PathfinderGoalGotoTarget> MOVE_TO = new GoalType<>(PathfinderGoalGotoTarget.class);

}
