package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.nms.NmsEnum;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nonnull;

/**
 * Represents a {@link AppearanceVillager} profession.
 * <p>The profession may look different based on {@link VillagerVariant}.</p>
 */
public enum VillagerProfession implements NmsEnum<ResourceKey<net.minecraft.world.entity.npc.VillagerProfession>> {
    
    /**
     * No profession has no profession.
     */
    NONE(net.minecraft.world.entity.npc.VillagerProfession.NONE),
    
    /**
     * The villager is an armorer.
     */
    ARMORER(net.minecraft.world.entity.npc.VillagerProfession.ARMORER),
    
    /**
     * The villager is a butcher.
     */
    BUTCHER(net.minecraft.world.entity.npc.VillagerProfession.BUTCHER),
    
    /**
     * The villager is a cartographer.
     */
    CARTOGRAPHER(net.minecraft.world.entity.npc.VillagerProfession.CARTOGRAPHER),
    
    /**
     * The villager is a cleric.
     */
    CLERIC(net.minecraft.world.entity.npc.VillagerProfession.CLERIC),
    
    /**
     * The villager is a farmer.
     */
    FARMER(net.minecraft.world.entity.npc.VillagerProfession.FARMER),
    
    /**
     * The villager is a fisherman.
     */
    FISHERMAN(net.minecraft.world.entity.npc.VillagerProfession.FISHERMAN),
    
    /**
     * The villager is a fletcher.
     */
    FLETCHER(net.minecraft.world.entity.npc.VillagerProfession.FLETCHER),
    
    /**
     * The villager is a leatherworker.
     */
    LEATHERWORKER(net.minecraft.world.entity.npc.VillagerProfession.LEATHERWORKER),
    
    /**
     * The villager is a librarian.
     */
    LIBRARIAN(net.minecraft.world.entity.npc.VillagerProfession.LIBRARIAN),
    
    /**
     * The villager is a mason.
     */
    MASON(net.minecraft.world.entity.npc.VillagerProfession.MASON),
    
    /**
     * The villager is a nitwit. (Unemployed)
     */
    NITWIT(net.minecraft.world.entity.npc.VillagerProfession.NITWIT),
    
    /**
     * The villager is a shepherd.
     */
    SHEPHERD(net.minecraft.world.entity.npc.VillagerProfession.SHEPHERD),
    
    /**
     * The villager is a toolsmith.
     */
    TOOLSMITH(net.minecraft.world.entity.npc.VillagerProfession.TOOLSMITH),
    
    /**
     * The villager is a weaponsmith.
     */
    WEAPONSMITH(net.minecraft.world.entity.npc.VillagerProfession.WEAPONSMITH);
    
    private final ResourceKey<net.minecraft.world.entity.npc.VillagerProfession> nms;
    
    VillagerProfession(@Nonnull ResourceKey<net.minecraft.world.entity.npc.VillagerProfession> nms) {
        this.nms = nms;
    }
    
    @Nonnull
    @Override
    public ResourceKey<net.minecraft.world.entity.npc.VillagerProfession> toNms() {
        return nms;
    }
}
