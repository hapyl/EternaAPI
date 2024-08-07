package me.hapyl.eterna.module.ai;

import me.hapyl.eterna.module.ai.goal.Goal;
import me.hapyl.eterna.module.ai.goal.GoalType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Interface for adding goals to mobs.
 *
 * @apiNote Deprecated methods indicate that they are using NMS classes.
 */
public interface AI {

    /**
     * Adds a new goal to the mob.
     *
     * @param priority - Priority of the goal, lower is higher priority.
     * @param goal     - Goal to add.
     */
    void addGoal(int priority, Goal goal);

    /**
     * Adds goal with next priority.
     *
     * @param goal - Goal to add.
     */
    void addGoal(Goal goal);

    /**
     * Adds a new goal to the mob.
     *
     * @param priority - Priority of the goal, lower is higher priority.
     * @param goal     - Goal to add.
     * @apiNote Deprecated methods indicate that they are using NMS classes.
     */
    @Deprecated
    void addGoal(int priority, net.minecraft.world.entity.ai.goal.Goal goal);

    /**
     * Removes all goals of the specified type.
     *
     * @param goals - Type of goals to remove.
     */
    <T extends net.minecraft.world.entity.ai.goal.Goal> void removeGoals(GoalType<T> goals);

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
     * @apiNote Deprecated methods indicate that they are using NMS classes.
     */
    @Deprecated
    void removeGoal(net.minecraft.world.entity.ai.goal.Goal goal);

    /**
     * Removes all goals from the mob.
     */
    void removeAllGoals();

    /**
     * Removes all goals that match the predicate.
     *
     * @param predicate - Predicate to match.
     */
    void removeAllGoals(Predicate<net.minecraft.world.entity.ai.goal.Goal> predicate);

    /**
     * Returns all goals of the mob.
     *
     * @return - Set of goals.
     * @apiNote Deprecated methods indicate that they are using NMS classes.
     */
    @Deprecated
    Set<WrappedGoal> getGoals();

    /**
     * Returns all running goals of the mob.
     *
     * @return - Stream of running goals.
     * @apiNote Deprecated methods indicate that they are using NMS classes.
     */
    @Deprecated
    Stream<WrappedGoal> getRunningGoals();

    /**
     * Returns the mob that this AI is attached to.
     *
     * @return - Mob.
     */
    Mob getMob();

    default <T> T getMob(Class<T> clazz) {
        if (!clazz.isInstance(getMob())) {
            throw new IllegalArgumentException("Mob is not of type " + clazz.getSimpleName() + "!");
        }

        return clazz.cast(getMob());
    }

}
