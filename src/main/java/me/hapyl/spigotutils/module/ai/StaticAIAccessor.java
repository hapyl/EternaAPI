package me.hapyl.spigotutils.module.ai;

import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import org.bukkit.entity.LivingEntity;

public final class StaticAIAccessor {

    public static AI getAI(LivingEntity entity) {
        final EntityInsentient minecraftEntity = getMob(entity);

        if (minecraftEntity == null) {
            throw new IllegalArgumentException("Invalid entity.");
        }

        final PathfinderGoalSelector goalSelector = minecraftEntity.bO;
        return new MobAI(entity, goalSelector);
    }

    public static EntityInsentient getMob(LivingEntity entity) {
        return (EntityInsentient) Reflect.getMinecraftEntity(entity);
    }
}
