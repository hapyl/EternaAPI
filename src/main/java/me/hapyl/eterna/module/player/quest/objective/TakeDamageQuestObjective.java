package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link QuestObjective} for the completion of which the player must take damage.
 */
public class TakeDamageQuestObjective extends QuestObjective {

    @Nullable public final EntityType entityType;
    @Nullable public final EntityDamageEvent.DamageCause damageCause;

    /**
     * Creates a new objective for the completion of which the player must take damage.
     *
     * @param entityType  - The entity type to take the damage from.
     *                    If omitted, any entity type is allowed.
     * @param damageCause - The damage type.
     *                    If omitted, any damage is allowed.
     * @param goal        - The total amount of damage to take.
     */
    public TakeDamageQuestObjective(@Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause damageCause, double goal) {
        super("Ouch!", makeDescription(entityType, damageCause, goal), goal);

        this.entityType = entityType;
        this.damageCause = damageCause;
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        final EntityType entityType = object.getAs(0, EntityType.class);
        final EntityDamageEvent.DamageCause damageCause = object.getAs(1, EntityDamageEvent.DamageCause.class);
        final Double damage = object.getAsDouble(2);

        if (this.entityType != null && this.entityType != entityType) {
            return Response.testFailed();
        }

        if (this.damageCause != null && this.damageCause != damageCause) {
            return Response.testFailed();
        }

        return damage != null ? Response.testSucceeded(damage) : Response.testFailed();
    }

    @Nonnull
    public static String makeDescription(@Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause damageCause, double goal) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Take %s damage".formatted(goal));

        if (damageCause != null) {
            builder.append(" from %s".formatted(Chat.capitalize(damageCause)));
        }

        if (entityType != null) {
            builder.append(" from %s".formatted(Chat.capitalize(entityType.getKey().getKey())));
        }

        return builder.toString();
    }
}
