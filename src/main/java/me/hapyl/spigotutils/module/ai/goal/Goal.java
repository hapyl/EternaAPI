package me.hapyl.spigotutils.module.ai.goal;

public class Goal {

    protected net.minecraft.world.entity.ai.goal.Goal goal;

    public Goal(net.minecraft.world.entity.ai.goal.Goal goal) {
        this.goal = goal;
    }

    public net.minecraft.world.entity.ai.goal.Goal getGoal() {
        if (goal == null) {
            throw new NullPointerException("if using empty constructor, then set the goal you lummox");
        }
        return goal;
    }
}
