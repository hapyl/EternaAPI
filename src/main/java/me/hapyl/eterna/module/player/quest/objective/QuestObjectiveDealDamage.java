package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.text.Capitalizable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must deal damage.
 */
public class QuestObjectiveDealDamage extends QuestObjective {
    
    @Nullable private final EntityType entityType;
    @Nullable private final EntityDamageEvent.DamageCause damageType;
    
    /**
     * Creates a new {@link QuestObjectiveDealDamage}.
     *
     * @param entityType - The entity type to deal the damage to, or {@code null} for any entity.
     * @param damageType - The type of damage to deal, or {@code null} for any damage type.
     * @param goal       - The total amount of damage to deal.
     */
    public QuestObjectiveDealDamage(@Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause damageType, final double goal) {
        this(makeDescription(entityType, damageType, goal), entityType, damageType, goal);
    }
    
    @ApiStatus.Internal
    QuestObjectiveDealDamage(@NotNull Component description, @Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause damageType, final double goal) {
        super(description, goal);
        
        this.entityType = entityType;
        this.damageType = damageType;
    }
    
    /**
     * Gets the {@link EntityType} to which to deal the damage, or {@code null} if any entity type is applicable.
     *
     * @return the entity type to which to deal the damage.
     */
    @Nullable
    public EntityType getEntityType() {
        return entityType;
    }
    
    /**
     * Gets the {@link EntityDamageEvent.DamageCause} of the damage, or {@code null} if any applicable.
     *
     * @return the damage type, or {@code null} if any applicable.
     */
    @Nullable
    public EntityDamageEvent.DamageCause getDamageType() {
        return damageType;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        final EntityType entityType = array.get(0, EntityType.class);
        final EntityDamageEvent.DamageCause damageType = array.get(1, EntityDamageEvent.DamageCause.class);
        final Double damage = array.get(2, Double.class);
        
        if ((this.entityType != null && this.entityType != entityType) || (this.damageType != null && this.damageType != damageType) || (damage == null)) {
            return Response.testFailed();
        }
        
        return Response.testSucceeded(damage);
    }
    
    @NotNull
    private static Component makeDescription(@Nullable EntityType entityType, @Nullable EntityDamageEvent.DamageCause cause, double goal) {
        final TextComponent.Builder builder = Component.text();
        
        builder.append(Component.text("Deal %.0f".formatted(goal)));
        
        if (cause != null) {
            builder.append(Component.text(" ")).append(Component.text(Capitalizable.capitalize(cause)));
        }
        
        builder.append(Component.text(" damage"));
        
        if (entityType != null) {
            builder.append(Component.text(" to ")).append(Component.text(Capitalizable.capitalize(entityType.getKey().getKey())));
        }
        
        return builder.append(Component.text(".")).build();
    }
}
