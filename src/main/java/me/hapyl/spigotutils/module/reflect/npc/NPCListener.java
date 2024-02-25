package me.hapyl.spigotutils.module.reflect.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface NPCListener {

    /**
     * Called every time a player successfully clicks at the NPC.
     *
     * @param player - Player.
     * @param type   - Click type.
     */
    void onClick(@Nonnull Player player, @Nonnull ClickType type);

    /**
     * Called whenever this {@link HumanNPC} is shown for the player for the first time.
     * <p>
     * This is <b>not</b> called if NPC is removed due to being far away!
     *
     * @param player - Player.
     */
    default void onSpawn(@Nonnull Player player) {
    }

    /**
     * Called whenever this {@link HumanNPC} is hidden from the player.
     *
     * @param player - Player.
     */
    default void onDespawn(@Nonnull Player player) {
    }

    /**
     * Called every time this {@link HumanNPC} changes location.
     *
     * @param player - Player.
     * @param location - New location.
     */
    default void onTeleport(@Nonnull Player player, @Nonnull Location location) {
    }

}
