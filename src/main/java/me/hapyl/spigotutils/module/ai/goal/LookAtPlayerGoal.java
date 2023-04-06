package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;

/**
 * Adds a goal to look at the player.
 */
public class LookAtPlayerGoal extends Goal {

    /**
     * Adds a goal to look at the player.
     *
     * @param ai       - AI reference.
     * @param distance - Distance to look at.
     * @param chance   - Chance to look at.
     */
    public LookAtPlayerGoal(AI ai, float distance, float chance) {
        super(new PathfinderGoalLookAtPlayer(ai.getMob(), EntityPlayer.class, distance, chance));
    }

    /**
     * Adds a goal to look at the player.
     *
     * @param ai       - AI reference.
     * @param distance - Distance to look at.
     */
    public LookAtPlayerGoal(AI ai, float distance) {
        this(ai, distance, 0.02f);
    }

}
