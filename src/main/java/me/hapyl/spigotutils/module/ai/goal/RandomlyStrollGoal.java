package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStrollLand;

/**
 * Adds a goal to randomly stroll around.
 */
public class RandomlyStrollGoal extends Goal {

    /**
     * Adds a goal to randomly stroll around.
     *
     * @param ai            - AI reference.
     * @param speedModifier - Speed modifier.
     * @param interval      - Interval. In ticks?
     */
    public RandomlyStrollGoal(AI ai, double speedModifier, int interval) {
        super(new PathfinderGoalRandomStrollLand(ai.getMob(EntityCreature.class), speedModifier, interval));
    }

    /**
     * Adds a goal to randomly stroll around.
     *
     * @param ai            - AI reference.
     * @param speedModifier - Speed modifier.
     */
    public RandomlyStrollGoal(AI ai, double speedModifier) {
        this(ai, speedModifier, 120);
    }
}
