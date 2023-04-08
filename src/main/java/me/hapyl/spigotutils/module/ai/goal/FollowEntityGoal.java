package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.ai.StaticAIAccessor;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowEntity;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Adds a goal to follow an entity.
 * <b>Players are not insistent!</b>
 */
public class FollowEntityGoal extends Goal {

    /**
     * Adds a goal to follow an entity.
     * <b>Players are not insistent!</b>
     *
     * @param ai            - AI reference.
     * @param entity        - Entity to follow.
     * @param speedModifier - Speed modifier.
     * @param stopDistance  - Minimum distance.
     * @param areaSize      - Maximum distance.
     * @throws IllegalArgumentException if entity is a player.
     */
    public FollowEntityGoal(AI ai, LivingEntity entity, double speedModifier, float stopDistance, float areaSize) {
        super(new PathfinderGoalFollowEntity(ai.getMob(), speedModifier, stopDistance, areaSize));

        if (entity instanceof Player) {
            throw new IllegalArgumentException("Players are not insistent!");
        }

        final PathfinderGoal goal = getGoal();

        try {
            FieldUtils.writeDeclaredField(goal, "c", StaticAIAccessor.getMob(entity), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
