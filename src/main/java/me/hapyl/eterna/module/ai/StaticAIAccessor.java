package me.hapyl.eterna.module.ai;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.bukkit.entity.LivingEntity;

public final class StaticAIAccessor {

    public static AI getAI(LivingEntity entity) {
        final Mob minecraftEntity = getMob(entity);

        if (minecraftEntity == null) {
            throw new IllegalArgumentException("Invalid entity.");
        }

        final GoalSelector goalSelector = minecraftEntity.goalSelector;
        return new MobAI(entity, goalSelector);
    }

    public static Mob getMob(LivingEntity entity) {
        return (Mob) Reflect.getMinecraftEntity(entity);
    }
}
