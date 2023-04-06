package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class ShootBowGoal extends Goal {

    public ShootBowGoal(AI ai, double speedModifier, int attackInterval, float attackRadiusSquared) throws ClassCastException {
        super(new RangedBowAttackGoal<>((Monster & RangedAttackMob) ai.getMob(), speedModifier, attackInterval, attackRadiusSquared));
    }
}
