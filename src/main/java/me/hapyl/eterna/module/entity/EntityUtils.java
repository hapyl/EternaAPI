package me.hapyl.eterna.module.entity;

import me.hapyl.eterna.module.annotate.PacketOperation;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.team.PacketTeam;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * A small utility class allowing entity manipulations via packets.
 */
@UtilityClass
public final class EntityUtils {
    
    private EntityUtils() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Hides the given {@link Entity} for the given {@link Player}.
     * <p>This doesn't actually kill the entity, only removes it for the client.</p>
     *
     * @param player - The player for whom to hide the entity.
     * @param entity - The entity to hide.
     */
    @PacketOperation
    public static void hide(@NotNull Player player, @NotNull Entity entity) {
        Reflect.destroyEntity(Reflect.getHandle(entity), player);
    }
    
    /**
     * Shows the given {@link Entity} for the given {@link Player}.
     *
     * @param player - The player for whom to show the entity.
     * @param entity - The entity to show.
     */
    @PacketOperation
    public static void show(@NotNull Player player, @NotNull Entity entity) {
        Reflect.createEntity(Reflect.getHandle(entity), player);
    }
    
    /**
     * Sets the {@link Entity} collision for the given {@link Player}.
     * <p>Minecraft collision are handles by the client via {@link Team}, and this method uses {@link PacketTeam} to handle collisions, meaning relogging will break the collision.</p>
     *
     * @param player    - The player for whom the collision changes.
     * @param entity    - The entity for whom to change the collision.
     * @param collision - The new collision.
     */
    @PacketOperation
    public static void collision(@NotNull Player player, @NotNull Entity entity, @NotNull Collision collision) {
        final UUID uuid = entity.getUniqueId();
        final String scoreboardName = entity.getScoreboardEntryName();
        final Team existingTeam = player.getScoreboard().getEntryTeam(scoreboardName);
        
        final PacketTeam packetTeam = new PacketTeam("collision_" + uuid, existingTeam);
        packetTeam.create(player);
        packetTeam.entry(player, scoreboardName);
        packetTeam.option(player, Team.Option.COLLISION_RULE, collision == Collision.ALLOW ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
    }
    
    /**
     * Entity collision status.
     */
    public enum Collision {
        /**
         * Allows entity collisions.
         */
        ALLOW,
        
        /**
         * Deny entity collisions.
         */
        DENY
    }
    
}
