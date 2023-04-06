package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;

public class RandomlyLookAroundGoal extends Goal {
    public RandomlyLookAroundGoal(AI ai) {
        super(new PathfinderGoalRandomLookaround(ai.getMob()));
    }
}
