package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;

public class FloatGoal extends Goal {

    /**
     * Adds a goal to float in water.
     *
     * @param ai - AI.
     */
    public FloatGoal(AI ai) {
        super(new net.minecraft.world.entity.ai.goal.FloatGoal(ai.getMob()));
    }
}
