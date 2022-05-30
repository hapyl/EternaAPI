package me.hapyl.spigotutils.module.entity;

import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.util.Helper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.Collection;

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
    public static void hideEntity(Entity entity, Collection<Player> viewers) {
        Reflect.hideEntity(entity, viewers);
    }

    /**
     * Hides entity using packets.
     *
     * @param entity  - Entity to hide.
     * @param viewers - Viewers.
     */
    public static void hideEntity(Entity entity, Player... viewers) {
        Reflect.hideEntity(entity, viewers);
    }

    /**
     * Shows hidden entity using packets.
     *
     * @param entity  - Entity to show.
     * @param viewers - Viewers.
     */
    public static void showEntity(Entity entity, Player... viewers) {
        Reflect.showEntity(entity, viewers);
    }

    /**
     * Shows hidden entity using packets.
     *
     * @param entity  - Entity to show.
     * @param viewers - Viewers.
     */
    public static void showEntity(Entity entity, Collection<Player> viewers) {
        Reflect.showEntity(entity, viewers);
    }

    /**
     * Changes collision of entity. This method uses scoreboards and will not work if scoreboard is changed.
     *
     * @param entity  - Entity to change collision for.
     * @param flag    - if true, collision will be removed, added otherwise.
     * @param viewers - Who will be affected by collision change.
     */
    public static void setCollision(Entity entity, Collision flag, Collection<Player> viewers) {
        for (final Player viewer : viewers) {
            final Team team = Helper.getEntityTeam(viewer.getScoreboard());
            team.setOption(Team.Option.COLLISION_RULE, flag == Collision.ALLOW ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
            team.addEntry(entity.getUniqueId().toString());
        }
    }

    public static void setCollision(Entity entity, Collision flag, Player... players) {
        setCollision(entity, flag, Arrays.stream(players).toList());
    }

    public enum Collision {
        ALLOW,
        DENY
    }

}
