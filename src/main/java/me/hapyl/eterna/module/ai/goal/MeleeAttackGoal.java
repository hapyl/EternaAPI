package me.hapyl.eterna.module.ai.goal;

import me.hapyl.eterna.module.ai.AI;
import net.minecraft.world.entity.PathfinderMob;

public class MeleeAttackGoal extends Goal {

    /**
     * Adds a goal to melee attack.
     *
     * @param ai                           - AI.
     * @param speedModifier                - Speed modifier.
     * @param followUnseen - Should follow target even if not seen.
     */
    public MeleeAttackGoal(AI ai, double speedModifier, boolean followUnseen) {
        super(new net.minecraft.world.entity.ai.goal.MeleeAttackGoal(ai.getMob(PathfinderMob.class), speedModifier, followUnseen));
    }
}
