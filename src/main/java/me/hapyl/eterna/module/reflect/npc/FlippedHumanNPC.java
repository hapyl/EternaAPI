package me.hapyl.eterna.module.reflect.npc;

import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Allows creating flipped "Dinnerbone" NPC.
 */
public class FlippedHumanNPC extends HumanNPC {
    public FlippedHumanNPC(Location location, @Nullable String npcName) {
        this(location, npcName, null);
    }

    public FlippedHumanNPC(@Nonnull Location location, @Nullable String npcName, @Nullable String skinOwner) {
        super(location, npcName, skinOwner, uuid -> "Dinnerbone");
    }

}
