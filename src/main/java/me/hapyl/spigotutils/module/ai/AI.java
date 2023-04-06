package me.hapyl.spigotutils.module.ai;

import me.hapyl.spigotutils.module.ai.goal.Goal;
import me.hapyl.spigotutils.module.ai.goal.GoalType;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface AI {

    /**
     * Adds a new goal to the mob.
     *
     * @param priority - Priority of the goal, lower is higher priority.
     * @param goal     - Goal to add.
     */
    void addGoal(int priority, Goal goal);

    /**
     * Adds a new goal to the mob.
     *
     * @param priority - Priority of the goal, lower is higher priority.
     * @param goal     - Goal to add.
     */
    @Deprecated
    void addGoal(int priority, PathfinderGoal goal);

    /**
     * Removes all goals of the specified type.
     *
     * @param goals - Type of goals to remove.
     */
    <T extends PathfinderGoal> void removeGoals(GoalType<T> goals);

    /**
     * Removes a goal from the mob.
     *
     * @param goal - Goal to remove.
     */
    void removeGoal(Goal goal);

    /**
     * Removes a goal from the mob.
     *
     * @param goal - Goal to remove.
     */
    @Deprecated
    void removeGoal(PathfinderGoal goal);

    /**
     * Removes all goals from the mob.
     */
    void removeAllGoals();

    /**
     * Removes all goals from the mob that match the predicate.
     *
     * @param predicate - Predicate to match.
     */
    void removeAllGoals(Predicate<PathfinderGoal> predicate);

    /**
     * Returns all goals of the mob.
     *
     * @return - Set of goals.
     */
    Set<PathfinderGoalWrapped> getGoals();

    /**
     * Returns all running goals of the mob.
     *
     * @return - Stream of running goals.
     */
    Stream<PathfinderGoalWrapped> getRunningGoals();

    /**
     * Returns the mob that this AI is attached to.
     *
     * @return - Mob.
     */
    EntityInsentient getMob();

    default <T> T getMob(Class<T> clazz) {
        if (!clazz.isInstance(getMob())) {
            throw new IllegalArgumentException("Mob is not of type " + clazz.getSimpleName() + "!");
        }

        return clazz.cast(getMob());
    }

}
