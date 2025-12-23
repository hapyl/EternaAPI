package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;

import javax.annotation.Nonnull;

/**
 * Represents a {@link EntityType#VILLAGER} appearance.
 */
public class AppearanceVillager extends Appearance {
    
    private VillagerVariant variant;
    private VillagerProfession profession;
    private VillagerLevel level;
    
    public AppearanceVillager(@Nonnull Npc npc, @Nonnull VillagerVariant variant, @Nonnull VillagerProfession profession, @Nonnull VillagerLevel level) {
        super(npc, new Villager(EntityType.VILLAGER, dummyWorld()));
        
        this.variant = variant;
        this.profession = profession;
        this.level = level;
        
        this.updateEntityData();
    }
    
    /**
     * Gets the {@link VillagerVariant}.
     *
     * @return the villager variant.
     */
    @Nonnull
    public VillagerVariant getVariant() {
        return variant;
    }
    
    /**
     * Sets the {@link VillagerVariant}.
     *
     * @param variant - The new villager variant.
     */
    public void setVariant(@Nonnull VillagerVariant variant) {
        this.variant = variant;
        this.updateEntityData();
    }
    
    /**
     * Gets the {@link VillagerProfession}.
     *
     * @return the {@link VillagerProfession}.
     */
    @Nonnull
    public VillagerProfession getProfession() {
        return profession;
    }
    
    /**
     * Sets the {@link VillagerProfession}.
     *
     * @param profession - The new profession.
     */
    public void setProfession(@Nonnull VillagerProfession profession) {
        this.profession = profession;
        this.updateEntityData();
    }
    
    /**
     * Gets the {@link VillagerLevel}.
     *
     * @return the {@link VillagerLevel}.
     */
    @Nonnull
    public VillagerLevel getLevel() {
        return level;
    }
    
    /**
     * Sets the {@link VillagerLevel}.
     *
     * @param level - The new villager level.
     */
    public void setLevel(@Nonnull VillagerLevel level) {
        this.level = level;
        this.updateEntityData();
    }
    
    @Nonnull
    @Override
    public Villager getHandle() {
        return (Villager) super.getHandle();
    }
    
    @Override
    public void updateEntityData() {
        class Holder {
            private static final EntityDataAccessor<VillagerData> accessor = EntityDataSerializers.VILLAGER_DATA.createAccessor(18);
        }
        
        // Write villager data
        super.getEntityData().set(
                Holder.accessor,
                new VillagerData(
                        BuiltInRegistries.VILLAGER_TYPE.getOrThrow(variant.toNms()),
                        BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(profession.toNms()),
                        level.ordinal() + 1
                )
        );
        
        super.updateEntityData();
    }
}
