package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.world.entity.EntityTameableAnimal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowOwner;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.entity.LivingEntity;

/**
 * Adds a goal to follow the owner.
 */
public class FollowOwnerGoal extends Goal {

    /**
     * Adds a goal to follow the owner.
     *
     * @param ai            - AI reference.
     * @param entity        - Entity to follow.
     * @param speedModifier - Speed modifier.
     * @param minDistance   - Minimum distance.
     * @param maxDistance   - Maximum distance.
     */
    public FollowOwnerGoal(AI ai, LivingEntity entity, double speedModifier, float minDistance, float maxDistance) {
        super(new PathfinderGoalFollowOwner(ai.getMob(EntityTameableAnimal.class), speedModifier, minDistance, maxDistance));

        final PathfinderGoal goal = getGoal();

        try {
            FieldUtils.writeDeclaredField(goal, "f", Reflect.getMinecraftEntity(entity), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
