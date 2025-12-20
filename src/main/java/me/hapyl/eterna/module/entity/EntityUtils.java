package me.hapyl.eterna.module.entity;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * This util class allows to manipulate entities (Hide using packets, remove collision)
 *
 * @author hapyl
 */
public class EntityUtils {
    
    /**
     * Hides entity using packets.
     *
     * @param entity  - Entity to hide.
     * @param viewers - Viewers.
     */
    public static void hideEntity(@Nonnull Entity entity, @Nonnull Collection<Player> viewers) {
        final net.minecraft.world.entity.Entity handle = Reflect.getHandle(entity);
        
        viewers.forEach(player -> Reflect.destroyEntity(handle, player));
    }
    
    /**
     * Hides entity using packets.
     *
     * @param entity - Entity to hide.
     * @param player - Viewers.
     */
    public static void hideEntity(@Nonnull Entity entity, @Nonnull Player player) {
        Reflect.destroyEntity(Reflect.getHandle(entity), player);
    }
    
    /**
     * Shows hidden entity using packets.
     *
     * @param entity  - Entity to show.
     * @param viewers - Viewers.
     */
    public static void showEntity(@Nonnull Entity entity, @Nonnull Collection<Player> viewers) {
        final net.minecraft.world.entity.Entity handle = Reflect.getHandle(entity);
        
        viewers.forEach(player -> Reflect.createEntity(handle, player));
    }
    
    /**
     * Shows hidden entity using packets.
     *
     * @param entity - Entity to show.
     * @param player - Viewers.
     */
    public static void showEntity(@Nonnull Entity entity, @Nonnull Player player) {
        Reflect.createEntity(Reflect.getHandle(entity), player);
    }
    
    /**
     * Changes collision of entity. This method uses scoreboards and will not work if scoreboard is changed.
     *
     * @param entity  - Entity to change collision for.
     * @param flag    - if true, collision will be removed, added otherwise.
     * @param viewers - Who will be affected by collision change.
     */
    public static void setCollision(@Nonnull Entity entity, @Nonnull Collision flag, @Nonnull Collection<Player> viewers) {
        final UUID uuid = entity.getUniqueId();
        
        for (final Player viewer : viewers) {
            TeamHelper.fetch(
                    viewer.getScoreboard(), "fake_entity_" + uuid, team -> {
                        team.setOption(Team.Option.COLLISION_RULE, flag == Collision.ALLOW ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
                        team.addEntry(uuid.toString());
                    }
            );
        }
    }
    
    /**
     * Changes collision of entity. This method uses scoreboards and will not work if scoreboard is changed.
     *
     * @param entity - Entity to change collision for.
     * @param flag   - if true, collision will be removed, added otherwise.
     */
    public static void setCollision(@Nonnull Entity entity, @Nonnull Collision flag) {
        setCollision(entity, flag, Sets.newHashSet(Bukkit.getOnlinePlayers()));
    }
    
    public static void setCollision(@Nonnull Entity entity, @Nonnull Collision flag, @Nonnull Player player) {
        setCollision(entity, flag, List.of(player));
    }
    
    /**
     * Collision status.
     */
    public enum Collision {
        /**
         * Allows collision. (default)
         */
        ALLOW,
        /**
         * Deny collision.
         */
        DENY
    }
    
}
