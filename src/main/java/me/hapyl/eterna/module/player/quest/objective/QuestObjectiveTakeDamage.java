package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.text.Capitalizable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must take damage.
 */
public class QuestObjectiveTakeDamage extends QuestObjectiveDealDamage {
    
    /**
     * Creates a new {@link QuestObjectiveTakeDamage}.
     *
     * @param entityType - The entity type to take the damage from, or {@code null} for any entity.
     * @param damageType - The type of damage to take, or {@code null} for any damage type.
     * @param goal       - The total amount of damage to take.
     */
    public QuestObjectiveTakeDamage(@Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause damageType, final double goal) {
        super(makeDescription(entityType, damageType, goal), entityType, damageType, goal);
    }
    
    @NotNull
    private static Component makeDescription(@Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause damageCause, double goal) {
        final TextComponent.Builder builder = Component.text();
        
        builder.append(Component.text("Take %.0f damage".formatted(goal)));
        
        if (damageCause != null) {
            builder.append(Component.text(" from %s".formatted(Capitalizable.capitalize(damageCause))));
        }
        
        if (entityType != null) {
            builder.append(Component.text(" from %s".formatted(Capitalizable.capitalize(entityType.getKey().getKey()))));
        }
        
        return builder.build();
    }
}
