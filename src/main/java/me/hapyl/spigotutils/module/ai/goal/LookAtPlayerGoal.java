package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.player.Player;

public class LookAtPlayerGoal extends Goal {

    public LookAtPlayerGoal(AI ai, float distance) {
        this(ai, distance, 0.02f);
    }

    public LookAtPlayerGoal(AI ai, float distance, float chance) {
        super(new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(ai.getMob(), Player.class, distance, chance));
    }

}
