package me.hapyl.spigotutils.module.entity;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.util.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

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
        Reflect.hideEntity(entity, viewers);
    }

    /**
     * Hides entity using packets.
     *
     * @param entity - Entity to hide.
     * @param player - Viewers.
     */
    public static void hideEntity(@Nonnull Entity entity, @Nonnull Player player) {
        Reflect.hideEntity(entity, player);
    }

    /**
     * Shows hidden entity using packets.
     *
     * @param entity - Entity to show.
     * @param player - Viewers.
     */
    public static void showEntity(@Nonnull Entity entity, @Nonnull Player player) {
        Reflect.showEntity(entity, player);
    }

    /**
     * Shows hidden entity using packets.
     *
     * @param entity  - Entity to show.
     * @param viewers - Viewers.
     */
    public static void showEntity(@Nonnull Entity entity, @Nonnull Collection<Player> viewers) {
        Reflect.showEntity(entity, viewers);
    }

    /**
     * Changes collision of entity. This method uses scoreboards and will not work if scoreboard is changed.
     *
     * @param entity  - Entity to change collision for.
     * @param flag    - if true, collision will be removed, added otherwise.
     * @param viewers - Who will be affected by collision change.
     */
    public static void setCollision(@Nonnull Entity entity, @Nonnull Collision flag, @Nonnull Collection<Player> viewers) {
        for (final Player viewer : viewers) {
            final Team team = TeamHelper.FAKE_ENTITY.getTeam(viewer.getScoreboard());

            team.setOption(Team.Option.COLLISION_RULE, flag == Collision.ALLOW ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
            team.addEntry(entity.getUniqueId().toString());
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
