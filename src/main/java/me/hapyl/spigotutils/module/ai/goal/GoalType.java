package me.hapyl.spigotutils.module.ai.goal;

import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.*;

@SuppressWarnings("rawtypes")
public record GoalType<T extends Goal>(Class<T> t) {

    public static final GoalType<LookAtPlayerGoal> LOOK_AT_PLAYER = new GoalType<>(LookAtPlayerGoal.class);
    public static final GoalType<AvoidEntityGoal> AVOID_TARGET = new GoalType<>(AvoidEntityGoal.class);
    public static final GoalType<RangedBowAttackGoal> SHOOT_BOW = new GoalType<>(RangedBowAttackGoal.class);
    public static final GoalType<FollowMobGoal> FOLLOW_ENTITY = new GoalType<>(FollowMobGoal.class);
    public static final GoalType<FollowOwnerGoal> FOLLOW_OWNER = new GoalType<>(FollowOwnerGoal.class);
    public static final GoalType<RandomLookAroundGoal> RANDOMLY_LOOK_AROUND = new GoalType<>(RandomLookAroundGoal.class);
    public static final GoalType<TemptGoal> TEMPT = new GoalType<>(TemptGoal.class);
    public static final GoalType<MoveToBlockGoal> MOVE_TO = new GoalType<>(MoveToBlockGoal.class);

}
