package me.hapyl.eterna.module.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * This helper works with team to apply collision or name tag visibility.
 * Should really be called EternaTeams or something.
 */
// TODO: 013, Mar 13, 2023 -> Should probably rework using packet teams if possible.
public enum TeamHelper {

    FAKE_ENTITY("fakeEntity", team -> {
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }),
    GLOWING("glowing", null),
    NPC("npc", team -> team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER));

    public static final String PARENT = "eterna_api";

    private final String name;
    private final Consumer<Team> action;

    TeamHelper(String name, Consumer<Team> action) {
        this.name = PARENT + "." + name;
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
                action.accept(team);
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
