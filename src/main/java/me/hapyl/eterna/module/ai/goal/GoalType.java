package me.hapyl.eterna.module.ai.goal;

import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import javax.annotation.Nonnull;

/**
 * Represents a GoalType with a link to the PathfinderGoal class.
 * You can use this class to cancel goal by type, rather than by class.
 */
@SuppressWarnings("rawtypes")
public record GoalType<T extends Goal>(@Nonnull Class<T> t) {

    public static final GoalType<LookAtPlayerGoal> LOOK_AT_PLAYER = of(LookAtPlayerGoal.class);
    public static final GoalType<AvoidEntityGoal> AVOID_TARGET = of(AvoidEntityGoal.class);
    public static final GoalType<RangedBowAttackGoal> SHOOT_BOW = of(RangedBowAttackGoal.class);
    public static final GoalType<FollowMobGoal> FOLLOW_ENTITY = of(FollowMobGoal.class);
    public static final GoalType<FollowOwnerGoal> FOLLOW_OWNER = of(FollowOwnerGoal.class);
    public static final GoalType<RandomLookAroundGoal> RANDOMLY_LOOK_AROUND = of(RandomLookAroundGoal.class);
    public static final GoalType<RandomStrollGoal> RANDOMLY_STROLL_AROUND = of(RandomStrollGoal.class);
    public static final GoalType<TemptGoal> TEMPT = of(TemptGoal.class);
    public static final GoalType<MoveToBlockGoal> MOVE_TO = of(MoveToBlockGoal.class);
    public static final GoalType<NearestAttackableTargetGoal> ATTACK_NEAREST_TARGET = of(NearestAttackableTargetGoal.class);
    public static final GoalType<FloatGoal> FLOAT = of(FloatGoal.class);
    public static final GoalType<MeleeAttackGoal> MELEE_ATTACK = of(MeleeAttackGoal.class);

    private static <T extends Goal> GoalType<T> of(Class<T> t) {
        return new GoalType<>(t);
    }

}
