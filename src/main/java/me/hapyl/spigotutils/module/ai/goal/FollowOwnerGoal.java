package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.world.entity.TamableAnimal;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.entity.LivingEntity;

public class FollowOwnerGoal extends Goal {

    public FollowOwnerGoal(AI ai, LivingEntity entity, double speedModifier, float minDistance, float maxDistance, boolean canFly) {
        super(new net.minecraft.world.entity.ai.goal.FollowOwnerGoal(
                ai.getMob(TamableAnimal.class),
                speedModifier,
                minDistance,
                maxDistance,
                canFly
        ));

        final net.minecraft.world.entity.ai.goal.FollowOwnerGoal goal = (net.minecraft.world.entity.ai.goal.FollowOwnerGoal) getGoal();

        try {
            FieldUtils.writeDeclaredField(goal, "f", Reflect.getMinecraftEntity(entity), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
