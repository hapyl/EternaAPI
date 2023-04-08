package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.ai.goal.PathfinderGoalBowShoot;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.entity.monster.IRangedEntity;

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
        super(new PathfinderGoalBowShoot<>((EntityMonster & IRangedEntity) ai.getMob(), speedModifier, attackInterval, attackRadiusSquared));
    }
}
