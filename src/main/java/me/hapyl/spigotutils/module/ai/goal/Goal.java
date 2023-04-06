package me.hapyl.spigotutils.module.ai.goal;

import net.minecraft.world.entity.ai.goal.PathfinderGoal;

public class Goal {

    protected PathfinderGoal goal;

    private Goal() {
    }

    protected static Goal empty() {
        return new Goal();
    }

    public Goal(PathfinderGoal goal) {
        this.goal = goal;
    }

    public PathfinderGoal getGoal() {
        return goal;
    }
}
