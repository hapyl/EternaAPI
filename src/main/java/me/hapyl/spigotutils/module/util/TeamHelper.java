package me.hapyl.spigotutils.module.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;

/**
 * This helper works with team to apply collision or name tag visibility.
 */
public enum TeamHelper {

    FAKE_ENTITY("fakeEntity", team -> {
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }),
    GLOWING("glowing", null),
    NPC("npc", team -> team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER));

    private final String name;
    private final Action<Team> action;

    public static final String PARENT = "eternaApi.";

    TeamHelper(String name, Action<Team> action) {
        this.name = PARENT + name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public boolean compareName(String name) {
        return name.equalsIgnoreCase(this.name);
    }

    /**
     * Returns team if exists or create if not.
     *
     * @param score - Scoreboard to create to.
     */
    @Nonnull
    public Team getTeam(Scoreboard score) {
        Team team = score.getTeam(name);
        if (team == null) {
            team = score.registerNewTeam(name);
            if (action != null) {
                action.use(team);
            }
        }
        return team;
    }

    public void addToTeam(Player player, Entity entity) {
        addToTeam(player.getScoreboard(), entity);
    }

    public void addToTeam(Scoreboard score, Entity entity) {
        final Team team = getTeam(score);
        team.addEntry(getEntityName(entity));
    }

    public boolean isInTeam(Player player, Entity entity) {
        return isInTeam(player.getScoreboard(), entity);
    }

    public boolean isInTeam(Scoreboard score, Entity entity) {
        final Team team = getTeam(score);
        return team.hasEntry(getEntityName(entity));
    }

    public void removeFromTeam(Player player, Entity entity) {
        removeFromTeam(player.getScoreboard(), entity);
    }

    public void removeFromTeam(Scoreboard score, Entity entity) {
        final Team team = getTeam(score);
        team.removeEntry(getEntityName(entity));
    }

    private String getEntityName(Entity entity) {
        return entity instanceof Player player ? player.getName() : entity.getUniqueId().toString();
    }

}
