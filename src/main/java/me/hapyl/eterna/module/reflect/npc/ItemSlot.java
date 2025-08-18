package me.hapyl.eterna.module.reflect.npc;

import net.minecraft.world.entity.EquipmentSlot;

import javax.annotation.Nonnull;

/**
 * Defines an item slot for a {@link HumanNPC}.
 */
public enum ItemSlot {
    
    MAINHAND(EquipmentSlot.MAINHAND),
    OFFHAND(EquipmentSlot.OFFHAND),
    FEET(EquipmentSlot.FEET),
    LEGS(EquipmentSlot.LEGS),
    CHEST(EquipmentSlot.CHEST),
    HEAD(EquipmentSlot.HEAD);
    
    private final EquipmentSlot slot;
    
    ItemSlot(EquipmentSlot slot) {
        this.slot = slot;
    }
    
    @Nonnull
    public EquipmentSlot getSlot() {
        return slot;
    }
}