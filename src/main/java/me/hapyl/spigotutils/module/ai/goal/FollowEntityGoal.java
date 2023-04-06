package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.ai.StaticAIAccessor;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowEntity;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class FollowEntityGoal extends Goal {

    /**
     * Follows an insistent entity.
     * <b>Players are not insistent!</b>
     *
     * @param ai            - AI of the entity
     * @param entity        - Entity to follow
     * @param speedModifier - Speed modifier
     * @param minDistance   - Minimum distance
     * @param maxDistance   - Maximum distance
     * @throws IllegalArgumentException if entity is a player
     */
    public FollowEntityGoal(AI ai, LivingEntity entity, double speedModifier, float minDistance, float maxDistance) {
        super(new PathfinderGoalFollowEntity(ai.getMob(), speedModifier, minDistance, maxDistance));

        if (entity instanceof Player) {
            throw new IllegalArgumentException("Use FollowOwnerGoal for players!");
        }

        final PathfinderGoal goal = getGoal();

        try {
            FieldUtils.writeDeclaredField(goal, "c", StaticAIAccessor.getMob(entity), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
