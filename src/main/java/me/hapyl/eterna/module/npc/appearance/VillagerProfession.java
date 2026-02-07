package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.nms.NmsWrapper;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link AppearanceVillager} profession.
 * <p>The profession may look different based on {@link VillagerVariant}.</p>
 */
public enum VillagerProfession implements NmsWrapper<ResourceKey<net.minecraft.world.entity.npc.villager.VillagerProfession>> {
    
    /**
     * No profession has no profession.
     */
    NONE(net.minecraft.world.entity.npc.villager.VillagerProfession.NONE),
    
    /**
     * The villager is an armorer.
     */
    ARMORER(net.minecraft.world.entity.npc.villager.VillagerProfession.ARMORER),
    
    /**
     * The villager is a butcher.
     */
    BUTCHER(net.minecraft.world.entity.npc.villager.VillagerProfession.BUTCHER),
    
    /**
     * The villager is a cartographer.
     */
    CARTOGRAPHER(net.minecraft.world.entity.npc.villager.VillagerProfession.CARTOGRAPHER),
    
    /**
     * The villager is a cleric.
     */
    CLERIC(net.minecraft.world.entity.npc.villager.VillagerProfession.CLERIC),
    
    /**
     * The villager is a farmer.
     */
    FARMER(net.minecraft.world.entity.npc.villager.VillagerProfession.FARMER),
    
    /**
     * The villager is a fisherman.
     */
    FISHERMAN(net.minecraft.world.entity.npc.villager.VillagerProfession.FISHERMAN),
    
    /**
     * The villager is a fletcher.
     */
    FLETCHER(net.minecraft.world.entity.npc.villager.VillagerProfession.FLETCHER),
    
    /**
     * The villager is a leatherworker.
     */
    LEATHERWORKER(net.minecraft.world.entity.npc.villager.VillagerProfession.LEATHERWORKER),
    
    /**
     * The villager is a librarian.
     */
    LIBRARIAN(net.minecraft.world.entity.npc.villager.VillagerProfession.LIBRARIAN),
    
    /**
     * The villager is a mason.
     */
    MASON(net.minecraft.world.entity.npc.villager.VillagerProfession.MASON),
    
    /**
     * The villager is a nitwit. (Unemployed)
     */
    NITWIT(net.minecraft.world.entity.npc.villager.VillagerProfession.NITWIT),
    
    /**
     * The villager is a shepherd.
     */
    SHEPHERD(net.minecraft.world.entity.npc.villager.VillagerProfession.SHEPHERD),
    
    /**
     * The villager is a toolsmith.
     */
    TOOLSMITH(net.minecraft.world.entity.npc.villager.VillagerProfession.TOOLSMITH),
    
    /**
     * The villager is a weaponsmith.
     */
    WEAPONSMITH(net.minecraft.world.entity.npc.villager.VillagerProfession.WEAPONSMITH);
    
    private final ResourceKey<net.minecraft.world.entity.npc.villager.VillagerProfession> nms;
    
    VillagerProfession(@NotNull ResourceKey<net.minecraft.world.entity.npc.villager.VillagerProfession> nms) {
        this.nms = nms;
    }
    
    @NotNull
    @Override
    public ResourceKey<net.minecraft.world.entity.npc.villager.VillagerProfession> toNms() {
        return nms;
    }
}
