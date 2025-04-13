package me.hapyl.eterna.module.ai;

import me.hapyl.eterna.module.ai.goal.Goal;
import me.hapyl.eterna.module.ai.goal.GoalType;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.bukkit.entity.LivingEntity;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;


@TestedOn(version = Version.V1_21_5)
public class MobAI implements AI {

    private int lastPriority;

    private final LivingEntity entity;
    private final GoalSelector goalSelector;

    public MobAI(LivingEntity entity, GoalSelector goalSelector) {
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
        goalSelector.addGoal(priority, goal.getGoal());
    }

    @Override
    public void addGoal(Goal goal) {
        addGoal(lastPriority++, goal);
    }

    @Override
    public void addGoal(int priority, net.minecraft.world.entity.ai.goal.Goal goal) {
        goalSelector.addGoal(priority, goal);
    }

    @Override
    public <T extends net.minecraft.world.entity.ai.goal.Goal> void removeGoals(GoalType<T> goals) {
        removeAllGoals(predicate -> goals.t().isInstance(predicate));
    }

    @Override
    public void removeGoal(Goal goal) {
        goalSelector.removeGoal(goal.getGoal());
    }

    @Override
    public void removeGoal(net.minecraft.world.entity.ai.goal.Goal goal) {
        goalSelector.removeGoal(goal);
    }

    @Override
    public void removeAllGoals() {
        goalSelector.removeAllGoals(predicate -> true);
    }

    @Override
    public void removeAllGoals(Predicate<net.minecraft.world.entity.ai.goal.Goal> predicate) {
        goalSelector.removeAllGoals(predicate);
    }

    @Override
    public Set<WrappedGoal> getGoals() {
        return goalSelector.getAvailableGoals();
    }

    @Override
    public Stream<WrappedGoal> getRunningGoals() {
        return goalSelector.getAvailableGoals().stream(); // FIXME (hapyl): Mon, May 6:
    }

    @Override
    public Mob getMob() {
        return StaticAIAccessor.getMob(entity);
    }

}
