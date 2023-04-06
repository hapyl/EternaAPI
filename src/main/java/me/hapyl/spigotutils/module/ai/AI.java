package me.hapyl.spigotutils.module.ai;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface AI {

    void addGoal(int priority, PathfinderGoal goal);

    void removeGoal(PathfinderGoal goal);

    void removeAllGoals();

    void removeAllGoals(Predicate<PathfinderGoal> predicate);

    Set<PathfinderGoalWrapped> getGoals();

    Stream<PathfinderGoalWrapped> getRunningGoals();

    EntityInsentient getMob();

}
