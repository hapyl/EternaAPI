package me.hapyl.eterna.module.command;

import com.google.common.collect.Sets;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Represents command cooldown.
 */
public class CommandCooldown {

    private final SimpleCommand command;
    private final Map<UUID, Long> usedAt;
    private final Set<String> ignoreCooldownPermissions;

    public CommandCooldown(SimpleCommand command) {
        this.command = command;
        this.ignoreCooldownPermissions = Sets.newHashSet();
        this.usedAt = new HashMap<>();
    }

    /**
     * Adds permission to cooldown ignore list.
     *
     * @param permission - Permission to add.
     */
    public void addIgnoreCooldownPermission(String permission) {
        ignoreCooldownPermissions.add(permission);
    }

    /**
     * Removes permission from cooldown ignore list.
     *
     * @param permission - Permissions to remove.
     * @return if permission was removed.
     */
    public boolean removeIgnoreCooldownPermission(String permission) {
        return ignoreCooldownPermissions.remove(permission);
    }

    /**
     * @param player - Player to test permissions.
     * @return - True if player has permission of ignoring cooldown.
     * <b>NOTE that if player is OP AND there is NO ignored permissions, it will return false!</b>
     */
    public boolean canIgnoreCooldown(Player player) {
        if (player.isOp() && ignoreCooldownPermissions.isEmpty()) {
            return false;
        }
        for (String permission : ignoreCooldownPermissions) {
            if (player.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if player is currently on cooldown and stops the cooldown if it's over.
     *
     * @param player - Player to test.
     * @return - True if player is on cooldown, false otherwise.
     */
    public boolean hasCooldown(Player player) {
        final long passed = System.currentTimeMillis() - getCooldown(player);
        if (passed >= (this.command.getCooldownTick() * 50L)) {
            this.stopCooldown(player);
            return false;
        }
        return true;
    }

    /**
     * Returns time left in milliseconds.
     *
     * @param player - Player to test.
     * @return - Time left in milliseconds.
     */
    public long getTimeLeft(Player player) {
        return (this.command.getCooldownTick() * 50L) - (System.currentTimeMillis() - getCooldown(player));
    }

    /**
     * Returns time when player used the command at in millis.
     *
     * @param player - Player to test.
     * @return - Time when player used the command at in millis.
     */
    private long getCooldown(Player player) {
        return this.usedAt.getOrDefault(player.getUniqueId(), 0L);
    }

    /**
     * Starts cooldown for player.
     *
     * @param player - Player to start cooldown for.
     */
    public void startCooldown(Player player) {
        this.usedAt.put(player.getUniqueId(), System.currentTimeMillis());
    }

    /**
     * Stops cooldown for player.
     *
     * @param player - Player to stop cooldown for.
     */
    public void stopCooldown(Player player) {
        this.usedAt.remove(player.getUniqueId());
    }

}
