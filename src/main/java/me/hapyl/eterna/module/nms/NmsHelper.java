package me.hapyl.eterna.module.nms;

import me.hapyl.eterna.module.annotate.UtilityClass;

/**
 * A very helpful nms helper, mostly used to convert between {@code bukkit >< nms}.
 */
@UtilityClass
public final class NmsHelper {
    
    /**
     * A {@link org.bukkit.inventory.EquipmentSlot} to {@link net.minecraft.world.entity.EquipmentSlot} converter.
     */
    public static final NmsConverter<org.bukkit.inventory.EquipmentSlot, net.minecraft.world.entity.EquipmentSlot> EQUIPMENT_SLOT;
    
    static {
        EQUIPMENT_SLOT = NmsConverterEnum.build(
                org.bukkit.inventory.EquipmentSlot.class,
                net.minecraft.world.entity.EquipmentSlot.class,
                equipmentSlot -> switch (equipmentSlot) {
                    case HAND -> net.minecraft.world.entity.EquipmentSlot.MAINHAND;
                    case OFF_HAND -> net.minecraft.world.entity.EquipmentSlot.OFFHAND;
                    case FEET -> net.minecraft.world.entity.EquipmentSlot.FEET;
                    case LEGS -> net.minecraft.world.entity.EquipmentSlot.LEGS;
                    case CHEST -> net.minecraft.world.entity.EquipmentSlot.CHEST;
                    case HEAD -> net.minecraft.world.entity.EquipmentSlot.HEAD;
                    case BODY -> net.minecraft.world.entity.EquipmentSlot.BODY;
                    case SADDLE -> net.minecraft.world.entity.EquipmentSlot.SADDLE;
                }
        );
    }
    
    private NmsHelper() {
        UtilityClass.Validator.throwIt();
    }
    
}
