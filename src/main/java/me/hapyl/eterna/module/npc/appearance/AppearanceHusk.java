package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Husk;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link EntityType#HUSK} appearance.
 */
public class AppearanceHusk extends AppearanceHumanoid {
    public AppearanceHusk(@NotNull Npc npc) {
        super(npc, new Husk(EntityType.HUSK, dummyWorld()));
    }
}
