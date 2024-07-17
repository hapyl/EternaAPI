package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.world.entity.TamableAnimal;
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
        super(new net.minecraft.world.entity.ai.goal.FollowOwnerGoal(
                ai.getMob(TamableAnimal.class),
                speedModifier,
                minDistance,
                maxDistance
        ));

        final net.minecraft.world.entity.ai.goal.Goal goal = getGoal();

        try {
            FieldUtils.writeDeclaredField(goal, "owner", Reflect.getMinecraftEntity(entity), true);
        } catch (IllegalAccessException e) {
            EternaLogger.exception(e);
        }
    }
}
