package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;

/**
 * Adds a goal to shoot a bow.
 */
public class ShootBowGoal extends Goal {

    /**
     * Adds a goal to shoot a bow.
     *
     * @param ai                  - AI reference.
     * @param speedModifier       - Speed modifier.
     * @param attackInterval      - Interval. In ticks?
     * @param attackRadiusSquared - Attack radius squared.
     * @throws ClassCastException - If the mob cannot shot bow.
     */
    public ShootBowGoal(AI ai, double speedModifier, int attackInterval, float attackRadiusSquared) throws ClassCastException {
        super(new RangedBowAttackGoal<>((Monster & RangedAttackMob) ai.getMob(), speedModifier, attackInterval, attackRadiusSquared));
    }
}
