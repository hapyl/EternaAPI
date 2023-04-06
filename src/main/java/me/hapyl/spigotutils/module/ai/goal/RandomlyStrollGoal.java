package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class RandomlyStrollGoal extends Goal {

    public RandomlyStrollGoal(AI ai, double speedModifier) {
        this(ai, speedModifier, 120);
    }

    public RandomlyStrollGoal(AI ai, double speedModifier, int interval) {
        super(new RandomStrollGoal(ai.getMob(PathfinderMob.class), speedModifier, interval));
    }
}
