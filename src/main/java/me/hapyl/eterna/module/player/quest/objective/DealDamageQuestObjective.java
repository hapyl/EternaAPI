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
 * A {@link QuestObjective} for the completion of which the player must deal damage.
 */
public class DealDamageQuestObjective extends QuestObjective {

    @Nullable public final EntityType entityType;
    @Nullable public final EntityDamageEvent.DamageCause damageCause;

    /**
     * Creates a new objective for completion of which the player must deal damage.
     *
     * @param entityType  - The entity to deal the damage to.
     *                    If omitted, any entity types are allowed.
     * @param damageCause - The type of damage to deal.
     *                    If omitted, any damage type is allowed.
     * @param goal        - The total amount of damage to deal.
     */
    public DealDamageQuestObjective(@Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause damageCause, double goal) {
        super("Damager", makeDescription(entityType, damageCause, goal), goal);

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
    public static String makeDescription(@Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause cause, double goal) {
        final StringBuilder builder = new StringBuilder("Deal %s".formatted(goal));

        if (cause != null) {
            builder.append(" ").append(Chat.capitalize(cause));
        }

        builder.append(" damage");

        if (entityType != null) {
            builder.append(" to ").append(Chat.capitalize(entityType.getKey().getKey()));
        }

        return builder.append(".").toString();
    }
}
