package me.hapyl.spigotutils.module.ai;

import me.hapyl.spigotutils.module.annotate.TestedNMS;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;
import org.bukkit.entity.LivingEntity;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@TestedNMS(version = "1.19.4")
public class MobAI implements AI {

    private final LivingEntity entity;
    private final PathfinderGoalSelector goalSelector;

    public MobAI(LivingEntity entity, PathfinderGoalSelector goalSelector) {
        this.entity = entity;
        this.goalSelector = goalSelector;
    }

    public static AI of(LivingEntity entity) {
        if (entity == null) {
            return null;
        }

        return StaticAIAccessor.getAI(entity);
    }

    @Override
    public void addGoal(int priority, PathfinderGoal goal) {
        goalSelector.a(priority, goal);
    }

    @Override
    public void removeGoal(PathfinderGoal goal) {
        goalSelector.a(goal);
    }

    @Override
    public void removeAllGoals() {
        goalSelector.a(predicate -> true);
    }

    @Override
    public void removeAllGoals(Predicate<PathfinderGoal> predicate) {
        goalSelector.a(predicate);
    }

    @Override
    public Set<PathfinderGoalWrapped> getGoals() {
        return goalSelector.b();
    }

    @Override
    public Stream<PathfinderGoalWrapped> getRunningGoals() {
        return goalSelector.c();
    }

    @Override
    public EntityInsentient getMob() {
        return StaticAIAccessor.getMob(entity);
    }

}
