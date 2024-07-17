package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

/**
 * Adds a goal to randomly look around.
 */
public class RandomlyLookAroundGoal extends Goal {
    /**
     * Adds a goal to randomly look around.
     *
     * @param ai - AI reference.
     */
    public RandomlyLookAroundGoal(AI ai) {
        super(new RandomLookAroundGoal(ai.getMob()));
    }
}
