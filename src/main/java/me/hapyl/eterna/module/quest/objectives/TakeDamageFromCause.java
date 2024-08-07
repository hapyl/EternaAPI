package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nullable;

public class TakeDamageFromCause extends QuestObjective {

    private final EntityDamageEvent.DamageCause cause;

    public TakeDamageFromCause(double goalTotal, EntityDamageEvent.DamageCause cause) {
        super(QuestObjectiveType.TAKE_DAMAGE_FROM_CAUSE, goalTotal, "Ouch", "Take %s from %s.", goalTotal, Chat.capitalize(cause));
        this.cause = cause;
    }

    @Override
    public double testQuestCompletion(@Nullable Object... objects) {
        if (objects == null) {
            return -1.0d;
        }

        if (objects.length == 2) {
            return (objects[1] instanceof EntityDamageEvent.DamageCause cause &&
                    this.cause == cause) ? Validate.getDouble(objects[0]) : -1.0d;
        }
        return -1.0d;
    }
}
