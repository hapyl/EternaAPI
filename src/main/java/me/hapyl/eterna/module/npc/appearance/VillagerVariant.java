package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.annotate.DefaultEnumValue;
import me.hapyl.eterna.module.nms.NmsEnum;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerType;

import javax.annotation.Nonnull;

/**
 * Represents a {@link AppearanceVillager} variant.
 */
public enum VillagerVariant implements NmsEnum<ResourceKey<VillagerType>> {
    
    /**
     * A desert variant.
     */
    DESERT(VillagerType.DESERT),
    
    /**
     * A jungle variant.
     */
    JUNGLE(VillagerType.JUNGLE),
    
    /**
     * A plains variant.
     */
    @DefaultEnumValue
    PLAINS(VillagerType.PLAINS),
    
    /**
     * A savannah variant.
     */
    SAVANNA(VillagerType.SAVANNA),
    
    /**
     * A snowy variant.
     */
    SNOW(VillagerType.SNOW),
    
    /**
     * A swamp variant.
     */
    SWAMP(VillagerType.SWAMP),
    
    /**
     * A taiga variant.
     */
    TAIGA(VillagerType.TAIGA);
    
    private final ResourceKey<VillagerType> nms;
    
    VillagerVariant(@Nonnull ResourceKey<VillagerType> nms) {
        this.nms = nms;
    }
    
    @Nonnull
    @Override
    public ResourceKey<VillagerType> toNms() {
        return this.nms;
    }
}
