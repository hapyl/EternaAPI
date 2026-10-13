package me.hapyl.eterna.module.player;

import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A helper class that allows creating a per-player vignette.
 */
public class Vignette {
    
    private static final double WORLD_BORDER_SIZE = 5.9999968E7;
    
    private Vignette() {
    }
    
    /**
     * Clears the vignette for the given {@link Player}.
     *
     * @param player - The player for whom to clear the vignette.
     */
    public static void clear(@NotNull Player player) {
        player.setWorldBorder(player.getWorld().getWorldBorder());
    }
    
    /**
     * Sets the vignette to be red for the given {@link Player}.
     *
     * @param player - The player for whom to set the vignette.
     */
    public static void red(@NotNull Player player) {
        final WorldBorder worldBorder = Bukkit.createWorldBorder();
        worldBorder.setCenter(player.getX(), player.getZ());
        worldBorder.setSize(WORLD_BORDER_SIZE);
        worldBorder.setWarningDistance(Integer.MAX_VALUE);
        
        player.setWorldBorder(worldBorder);
    }
    
}
