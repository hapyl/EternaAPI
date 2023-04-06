package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;

public class LookAtPlayerGoal extends Goal {

    public LookAtPlayerGoal(AI ai, float chance) {
        super(new PathfinderGoalLookAtPlayer(ai.getMob(), EntityPlayer.class, chance));
    }

}
