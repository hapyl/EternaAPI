package me.hapyl.spigotutils.module.ai.goal;

public class Goal {

    protected net.minecraft.world.entity.ai.goal.Goal goal;

    private Goal() {
    }

    protected static Goal empty() {
        return new Goal();
    }

    public Goal(net.minecraft.world.entity.ai.goal.Goal goal) {
        this.goal = goal;
    }

    public net.minecraft.world.entity.ai.goal.Goal getGoal() {
        return goal;
    }
}
