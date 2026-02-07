package me.hapyl.eterna.module.nms;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A helpful utility class that contains all built-in {@link NmsConverter}.
 */
@UtilityClass
public final class NmsConverters {
    
    /**
     * Defines the {@link NmsConverter} between {@code bukkit} {@link EquipmentSlot} and {@code nms} {@link net.minecraft.world.entity.EquipmentSlot}.
     */
    public static final NmsConverter<EquipmentSlot, net.minecraft.world.entity.EquipmentSlot> EQUIPMENT_SLOT;
    
    static {
        EQUIPMENT_SLOT = ofReverseMap(Map.of(
                EquipmentSlot.HAND, net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                EquipmentSlot.OFF_HAND, net.minecraft.world.entity.EquipmentSlot.OFFHAND,
                EquipmentSlot.FEET, net.minecraft.world.entity.EquipmentSlot.FEET,
                EquipmentSlot.LEGS, net.minecraft.world.entity.EquipmentSlot.LEGS,
                EquipmentSlot.CHEST, net.minecraft.world.entity.EquipmentSlot.CHEST,
                EquipmentSlot.HEAD, net.minecraft.world.entity.EquipmentSlot.HEAD,
                EquipmentSlot.BODY, net.minecraft.world.entity.EquipmentSlot.BODY,
                EquipmentSlot.SADDLE, net.minecraft.world.entity.EquipmentSlot.SADDLE
        ));
        
    }
    
    private NmsConverters() {
        UtilityClass.Validator.throwIt();
    }
    
    @NotNull
    private static <B, N> NmsConverter<B, N> ofReverseMap(@NotNull Map<B, N> lookup) {
        return new NmsConverterReverseMap<>(lookup);
    }
    
}
