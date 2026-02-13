package me.hapyl.eterna.module.command;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * Represents a {@link SimpleCommand} cooldown.
 *
 * <p>Note that cooldowns are only applicable to players.</p>
 */
public class CommandCooldown implements Cooldown {
    
    private final Map<UUID, Long> usedAt;
    
    private int cooldown;
    private Permission ignoreCooldownPermission;
    
    CommandCooldown() {
        this.usedAt = Maps.newHashMap();
    }
    
    /**
     * Sets the ignore cooldown {@link Permission}.
     * <p>Players with that permission ignore the cooldown.</p>
     *
     * @param permission - The permission to set, or {@code null} to unset the permission.
     */
    public void setIgnoreCooldownPermission(@Nullable Permission permission) {
        this.ignoreCooldownPermission = permission;
    }
    
    /**
     * Gets the ignore cooldown {@link Permission}.
     * <p>Players with that permission ignore the cooldown.</p>
     *
     * @return the ignore cooldown permission.
     */
    @Nullable
    public Permission getIgnoreCooldownPermission() {
        return ignoreCooldownPermission;
    }
    
    /**
     * Gets whether the given {@link Player} can ignore the cooldown, be it because the ignore cooldown permission is unset or the player has the permission.
     *
     * @param player - The player to check.
     * @return {@code true} if the given player can ignore cooldown; {@code false} otherwise.
     */
    public boolean canIgnoreCooldown(@NotNull Player player) {
        return ignoreCooldownPermission != null && player.hasPermission(ignoreCooldownPermission);
    }
    
    /**
     * Gets whether the given {@link Player} is on cooldown.
     *
     * @param player - The player to check.
     * @return {@code true} if the given player is on cooldown; {@code false} otherwise.
     */
    public boolean isOnCooldown(@NotNull Player player) {
        return getCooldown(player) > 0;
    }
    
    /**
     * Gets the remaining cooldown time in ticks for the given {@link Player}.
     *
     * @param player - The player to check.
     * @return the number of ticks left in the cooldown, or {@code 0} if not on cooldown.
     */
    public int getCooldown(@NotNull Player player) {
        final long millis = System.currentTimeMillis();
        final long usedAt = this.usedAt.getOrDefault(player.getUniqueId(), 0L);
        
        final long differenceInTicks = (millis - usedAt) / 50;
        
        return (int) Math.max(0, (cooldown - differenceInTicks));
    }
    
    /**
     * Starts the cooldown for the given {@link Player}.
     *
     * @param player - The player for whom to start the cooldown.
     */
    public void startCooldown(@NotNull Player player) {
        this.usedAt.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    /**
     * Gets the command cooldown.
     *
     * @return the command cooldown.
     */
    @Override
    public int getCooldown() {
        return this.cooldown;
    }
    
    /**
     * Sets the command cooldown.
     *
     * @param cooldown - The cooldown to set.
     */
    @Override
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
    
}
