package me.hapyl.eterna.module.npc;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public class NpcPlayerData {
    
    protected final Player player;
    
    protected Visibility visibility;
    protected long lastInteraction;
    
    NpcPlayerData(@Nonnull Player player) {
        this.player = player;
        this.visibility = Visibility.VISIBLE;
        this.lastInteraction = 0L;
    }
    
}
