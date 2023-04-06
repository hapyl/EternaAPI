package me.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.world.entity.EquipmentSlot;

// ItemSlot >> EnumItemSlot
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

    public EquipmentSlot getSlot() {
        return slot;
    }
}