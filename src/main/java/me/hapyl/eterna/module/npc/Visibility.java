package me.hapyl.eterna.module.npc;

import org.bukkit.entity.Player;

/**
 * Represents the current visibility of a {@link Npc}, more precisely whether a {@link Player}
 * can <i>see</i> the {@link Npc}, not whether the {@link Npc} is spawned for the player.
 */
public enum Visibility {
    /**
     * The {@link Npc} is visible for the player.
     */
    VISIBLE,
    
    /**
     * The {@link Npc} is hidden for the player.
     */
    NOT_VISIBLE
}
