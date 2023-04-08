package me.hapyl.spigotutils.module.ai.goal;

import net.minecraft.world.entity.ai.goal.PathfinderGoal;

public class Goal {

    protected PathfinderGoal goal;

    protected Goal() {
    }

    protected Goal(PathfinderGoal goal) {
        this.goal = goal;
    }

    public PathfinderGoal getGoal() {
        if (goal == null) {
            throw new NullPointerException("if using empty constructor then set the goal you lummox");
        }
        return goal;
    }
}
