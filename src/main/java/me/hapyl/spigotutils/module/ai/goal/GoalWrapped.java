package me.hapyl.spigotutils.module.ai.goal;

import net.minecraft.world.entity.ai.goal.PathfinderGoal;

public class GoalWrapped extends Goal {
    public GoalWrapped(PathfinderGoal minecraftGoal) {
        super(minecraftGoal);
    }
}
