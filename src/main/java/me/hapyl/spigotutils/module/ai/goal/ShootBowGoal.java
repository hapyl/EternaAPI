package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.ai.goal.PathfinderGoalBowShoot;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.entity.monster.IRangedEntity;

public class ShootBowGoal extends Goal {

    public ShootBowGoal(AI ai, double speedModifier, int attackInterval, float maxAttackDistance) throws ClassCastException {
        super(new PathfinderGoalBowShoot<>((EntityMonster & IRangedEntity) ai.getMob(), speedModifier, attackInterval, maxAttackDistance));
    }
}
