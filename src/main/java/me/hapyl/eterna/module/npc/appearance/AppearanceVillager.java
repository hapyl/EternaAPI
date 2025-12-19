package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;

import javax.annotation.Nonnull;

public class AppearanceVillager extends Appearance {
    
    private VillagerVariant variant;
    private VillagerProfession profession;
    private VillagerLevel level;
    
    public AppearanceVillager(@Nonnull Npc npc, @Nonnull VillagerVariant variant, @Nonnull VillagerProfession profession, @Nonnull VillagerLevel level) {
        super(npc, new Villager(EntityType.VILLAGER, dummyWorld()));
        
        this.variant = variant;
        this.profession = profession;
        this.level = level;
        
        this.updateMetadata();
    }
    
    @Nonnull
    public VillagerVariant getVariant() {
        return variant;
    }
    
    public void setVariant(@Nonnull VillagerVariant variant) {
        this.variant = variant;
        this.updateMetadata();
    }
    
    @Nonnull
    public VillagerProfession getProfession() {
        return profession;
    }
    
    public void setProfession(@Nonnull VillagerProfession profession) {
        this.profession = profession;
        this.updateMetadata();
    }
    
    @Nonnull
    public VillagerLevel getLevel() {
        return level;
    }
    
    public void setLevel(@Nonnull VillagerLevel level) {
        this.level = level;
        this.updateMetadata();
    }
    
    @Nonnull
    @Override
    public Villager getHandle() {
        return (Villager) super.getHandle();
    }
    
    @Override
    public void updateMetadata() {
        class Holder {
            private static final EntityDataAccessor<VillagerData> accessor = EntityDataSerializers.VILLAGER_DATA.createAccessor(18);
        }
        
        // Write villager data
        super.getMetadata().set(
                Holder.accessor,
                new VillagerData(
                        BuiltInRegistries.VILLAGER_TYPE.getOrThrow(variant.toNms()),
                        BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(profession.toNms()),
                        level.ordinal() + 1
                )
        );
        
        super.updateMetadata();
    }
}
