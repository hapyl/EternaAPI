package kz.hapyl.spigotutils.module.entity;

import kz.hapyl.spigotutils.module.reflect.Reflect;
import kz.hapyl.spigotutils.module.util.Helper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

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

    public static void setCollision(Entity entity, boolean flag, Collection<Player> viewers) {
        for (final Player viewer : viewers) {
            final Team team = Helper.getEntityTeam(viewer.getScoreboard());
            team.setOption(Team.Option.COLLISION_RULE, flag ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
            team.addEntry(entity.getUniqueId().toString());
        }
    }

}
