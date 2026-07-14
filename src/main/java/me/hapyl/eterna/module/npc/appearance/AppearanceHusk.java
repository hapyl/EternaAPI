package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.zombie.Husk;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Husk} appearance.
 */
public class AppearanceHusk extends AppearanceHumanoid {
    
    public AppearanceHusk(@NotNull Npc npc) {
        super(npc, new Husk(EntityTypes.HUSK, dummyWorld()));
    }
    
    @Override
    public void onDataUpdated(@NotNull SynchedEntityData entityData) {
    }
}
