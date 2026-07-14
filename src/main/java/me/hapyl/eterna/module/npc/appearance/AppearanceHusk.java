package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Husk;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Husk} appearance.
 */
public class AppearanceHusk extends AppearanceHumanoid implements AppearanceBaby {
    
    public AppearanceHusk(@NotNull Npc npc) {
        super(npc, new Husk(EntityTypes.HUSK, dummyWorld()));
    }
    
    @Override
    public @NotNull Husk getHandle() {
        return (Husk) super.getHandle();
    }
    
    @Override
    public void onDataUpdated(@NotNull SynchedEntityData entityData) {
    }
    
    @Override
    public void setBaby(boolean isBaby) {
        this.getHandle().setBaby(isBaby);
        this.updateEntityData();
    }
    
    @Override
    public boolean isBaby() {
        return this.getHandle().isBaby();
    }
    
}
