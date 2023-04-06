package me.hapyl.spigotutils.module.ai.goal;

import net.minecraft.world.entity.ai.goal.PathfinderGoal;

public class Goal {

    private final PathfinderGoal minecraftGoal;

    public Goal(PathfinderGoal minecraftGoal) {
        this.minecraftGoal = minecraftGoal;
    }

    public PathfinderGoal getMinecraftGoal() {
        return minecraftGoal;
    }
}
