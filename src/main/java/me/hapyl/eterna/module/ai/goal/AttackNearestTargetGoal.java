package me.hapyl.eterna.module.ai.goal;

import me.hapyl.eterna.module.ai.AI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class AttackNearestTargetGoal<T extends LivingEntity> extends Goal {

    /**
     * Adds a goal to attack nearest target.
     *
     * @param ai        - AI.
     * @param entity    - Entity to attack.
     * @param interval  - Attack interval.
     * @param mustSee   - Is entity must be visible.
     * @param mustReach - Is must reach entity.
     * @param predicate - Predicate.
     */
    public AttackNearestTargetGoal(AI ai, EntityType entity, int interval, boolean mustSee, boolean mustReach, @Nullable Predicate<T> predicate) {
        super(new NearestAttackableTargetGoal<>(
                ai.getMob(),
                AvoidTargetGoal.entityLivingClassFromType(entity),
                interval,
                mustSee,
                mustReach,
                new TargetingConditions.Selector() {
                    @Override
                    public boolean test(net.minecraft.world.entity.LivingEntity livingEntity, ServerLevel serverLevel) {
                        return predicate != null && predicate.test((T)livingEntity.getBukkitEntity());
                    }
                }
        ));
    }

    public AttackNearestTargetGoal(AI ai, EntityType entity, boolean mustSee) {
        this(ai, entity, 10, mustSee);
    }

    public AttackNearestTargetGoal(AI ai, EntityType entity, int interval, boolean mustSee) {
        this(ai, entity, interval, mustSee, false);
    }

    public AttackNearestTargetGoal(AI ai, EntityType entity, int interval, boolean mustSee, boolean mustReach) {
        this(ai, entity, interval, mustSee, mustReach, null);
    }
}
