package me.hapyl.eterna.module.ai.goal;

import me.hapyl.eterna.module.ai.AI;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

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
        super(new RandomStrollGoal(ai.getMob(PathfinderMob.class), speedModifier, interval));
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
