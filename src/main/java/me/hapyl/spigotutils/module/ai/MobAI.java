package me.hapyl.spigotutils.module.ai;

import me.hapyl.spigotutils.module.ai.goal.Goal;
import me.hapyl.spigotutils.module.ai.goal.GoalType;
import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;
import org.bukkit.entity.LivingEntity;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@TestedOn(version = Version.V1_20_4)
public class MobAI implements AI {

    private int lastPriority;

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
    public void addGoal(int priority, Goal goal) {
        goalSelector.a(priority, goal.getGoal());
    }

    @Override
    public void addGoal(Goal goal) {
        addGoal(lastPriority++, goal);
    }

    @Override
    public void addGoal(int priority, PathfinderGoal goal) {
        goalSelector.a(priority, goal);
    }

    @Override
    public <T extends PathfinderGoal> void removeGoals(GoalType<T> goals) {
        removeAllGoals(predicate -> goals.t().isInstance(predicate));
    }

    @Override
    public void removeGoal(Goal goal) {
        goalSelector.a(goal.getGoal());
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
        return goalSelector.b().stream(); // FIXME (hapyl): Mon, May 6: 
    }

    @Override
    public EntityInsentient getMob() {
        return StaticAIAccessor.getMob(entity);
    }

}
