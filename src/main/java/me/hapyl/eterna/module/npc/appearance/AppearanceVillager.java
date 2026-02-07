package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link EntityType#VILLAGER} appearance.
 */
public class AppearanceVillager extends Appearance {
    
    private VillagerVariant variant;
    private VillagerProfession profession;
    private VillagerLevel level;
    
    public AppearanceVillager(@NotNull Npc npc, @NotNull VillagerVariant variant, @NotNull VillagerProfession profession, @NotNull VillagerLevel level) {
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
    @NotNull
    public VillagerVariant getVariant() {
        return variant;
    }
    
    /**
     * Sets the {@link VillagerVariant}.
     *
     * @param variant - The new villager variant.
     */
    public void setVariant(@NotNull VillagerVariant variant) {
        this.variant = variant;
        this.updateEntityData();
    }
    
    /**
     * Gets the {@link VillagerProfession}.
     *
     * @return the {@link VillagerProfession}.
     */
    @NotNull
    public VillagerProfession getProfession() {
        return profession;
    }
    
    /**
     * Sets the {@link VillagerProfession}.
     *
     * @param profession - The new profession.
     */
    public void setProfession(@NotNull VillagerProfession profession) {
        this.profession = profession;
        this.updateEntityData();
    }
    
    /**
     * Gets the {@link VillagerLevel}.
     *
     * @return the {@link VillagerLevel}.
     */
    @NotNull
    public VillagerLevel getLevel() {
        return level;
    }
    
    /**
     * Sets the {@link VillagerLevel}.
     *
     * @param level - The new villager level.
     */
    public void setLevel(@NotNull VillagerLevel level) {
        this.level = level;
        this.updateEntityData();
    }
    
    @NotNull
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
