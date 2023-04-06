package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStrollLand;

public class RandomlyStrollGoal extends Goal {
    public RandomlyStrollGoal(AI ai, double speedModifier) {
        super(new PathfinderGoalRandomStrollLand(ai.getMob(EntityCreature.class), speedModifier));
    }
}
