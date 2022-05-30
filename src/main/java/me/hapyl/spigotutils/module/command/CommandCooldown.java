package me.hapyl.spigotutils.module.command;

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

    public boolean hasCooldown(Player player) {
        final long passed = System.currentTimeMillis() - getCooldown(player);
        if (passed >= (this.command.getCooldownTick() * 50L)) {
            this.stopCooldown(player);
            return false;
        }
        return true;
    }

    public long getTimeLeft(Player player) {
        return (this.command.getCooldownTick() * 50L) - (System.currentTimeMillis() - getCooldown(player));
    }

    private long getCooldown(Player player) {
        return this.usedAt.getOrDefault(player.getUniqueId(), 0L);
    }

    public void startCooldown(Player player) {
        this.usedAt.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void stopCooldown(Player player) {
        this.usedAt.remove(player.getUniqueId());
    }

}
