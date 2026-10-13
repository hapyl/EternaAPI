package me.hapyl.eterna.module.util;

import net.minecraft.world.InteractionHand;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a hand of an entity.
 */
public enum Hand {
    
    /**
     * Defines the main hand of the entity.
     */
    MAINHAND(EquipmentSlot.HAND, InteractionHand.MAIN_HAND),
    
    /**
     * Defines the offhand of the entity.
     */
    OFFHAND(EquipmentSlot.OFF_HAND, InteractionHand.OFF_HAND);
    
    private final EquipmentSlot equipmentSlot;
    private final InteractionHand interactionHand;
    
    Hand(@NotNull EquipmentSlot equipmentSlot, @NotNull InteractionHand interactionHand) {
        this.equipmentSlot = equipmentSlot;
        this.interactionHand = interactionHand;
    }
    
    /**
     * Gets the bukkit {@link EquipmentSlot} for this {@link Hand}.
     *
     * @return the bukkit equipment slot for this hand.
     */
    public @NotNull EquipmentSlot asEquipmentSlot() {
        return equipmentSlot;
    }
    
    /**
     * Gets the vanilla {@link InteractionHand} for this {@link Hand}.
     *
     * @return the vanilla interaction hand for this hand.
     */
    public @NotNull InteractionHand asInteractionHand() {
        return interactionHand;
    }
    
}