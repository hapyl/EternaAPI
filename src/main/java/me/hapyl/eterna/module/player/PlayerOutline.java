package me.hapyl.eterna.module.player;

import me.hapyl.eterna.module.util.Disposable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Allows creating client-side, per-player {@link WorldBorder} for outline purposes.
 */
public class PlayerOutline implements Disposable {
    
    /**
     * Defines the default world border size.
     */
    public static final double DEFAULT_WORLD_BORDER_SIZE = 100_000;
    
    /**
     * Defines the maximum world border size.
     */
    public static final double MAX_WORLD_BORDER_SIZE = 5.9999968E7;
    
    private final Player player;
    private final WorldBorder border;
    
    /**
     * Creates a new {@link PlayerOutline} for the given {@link Player}.
     *
     * @param player - The player for whom to create the world border.
     */
    public PlayerOutline(@NotNull Player player) {
        this.player = player;
        
        this.border = Bukkit.createWorldBorder();
        this.border.setWarningTimeTicks(0);
    }
    
    /**
     * Gets the {@link Player} for whom this {@link PlayerOutline} belongs to.
     *
     * @return the player for whom this outline belongs to.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Sets the {@link Color} of this {@link PlayerOutline}.
     *
     * <p>
     * This method sets the actual border to {@link #DEFAULT_WORLD_BORDER_SIZE}; if that's not enough, use {@link #setColor(Color, double)}
     * for custom world-border size.
     * </p>
     *
     * @param color - The new color to set.
     */
    public void setColor(@NotNull Color color) {
        setColor(color, DEFAULT_WORLD_BORDER_SIZE);
    }
    
    /**
     * Sets the {@link Color} of this {@link PlayerOutline}.
     *
     * @param color - The color to set.
     * @param size  - The new size to of the border.
     */
    public void setColor(@NotNull Color color, @Range(from = 1, to = (long) MAX_WORLD_BORDER_SIZE) double size) {
        final Player player = getPlayer();
        final Location location = player.getLocation();
        
        size = Math.clamp(size, 1.0, MAX_WORLD_BORDER_SIZE);
        border.setCenter(location.getX(), location.getZ());
        
        border.setSize(size);
        border.setWarningDistance(Integer.MAX_VALUE);
        
        switch (color) {
            case RED -> border.changeSize(size - 1, Integer.MAX_VALUE);
            case GREEN -> border.changeSize(size + 1, Integer.MAX_VALUE);
        }
        
        player.setWorldBorder(border);
    }
    
    /**
     * Completely removes this outline, returning the world border from the current {@link World}.
     */
    @Override
    public void dispose() {
        // Set to the world's actual world-border
        player.setWorldBorder(player.getWorld().getWorldBorder());
    }
    
    /**
     * Represents the {@link PlayerOutline} color.
     */
    public enum Color {
        /**
         * Defines the green outline color.
         */
        GREEN,
        
        /**
         * Defines the red outline color.
         */
        RED
    }
    
}
