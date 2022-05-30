package me.hapyl.spigotutils.module.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This helper works with team to apply collision or name tag visibility.
 */
public final class Helper {

    private static final String PARENT = "eternaApi.";

    // team helper
    public enum Teams {
        FAKE_ENTITY(PARENT + "fakeEntity"),
        GLOWING(PARENT + "glowing"),
        NPC(PARENT + "npc");

        private final String name;

        Teams(String name) {
            this.name = name;
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
         * @param score  - Scoreboard to create to.
         * @param action - Action to apply to the team.
         */
        @Nonnull
        private Team getTeam(Scoreboard score, @Nullable Action<Team> action) {
            Team team = score.getTeam(name);
            if (team == null) {
                team = score.registerNewTeam(name);
                if (action != null) {
                    action.use(team);
                }
            }
            return team;
        }

        private Team getTeam(Scoreboard scoreboard) {
            return getTeam(scoreboard, null);
        }

        public void addToTeam(Scoreboard score, Entity entity) {
            final Team team = getTeam(score);
            team.addEntry(getEntityName(entity));
        }

        public boolean isInTeam(Scoreboard score, Entity entity) {
            final Team team = getTeam(score);
            return team.hasEntry(getEntityName(entity));
        }

        public void removeFromTeam(Scoreboard score, Entity entity) {
            final Team team = getTeam(score);
            team.removeEntry(getEntityName(entity));
        }

        private String getEntityName(Entity entity) {
            return entity instanceof Player player ? player.getName() : entity.getUniqueId().toString();
        }

    }

    public static Team getEntityTeam(Scoreboard scoreboard) {
        return Teams.FAKE_ENTITY.getTeam(scoreboard, team -> {
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        });
    }

    public static Team getNpcTeam(Scoreboard scoreboard) {
        return Teams.NPC.getTeam(scoreboard, team -> team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER));
    }

    public static Team getGlowingTeam(Scoreboard scoreboard) {
        return Teams.GLOWING.getTeam(scoreboard, null);
    }


}
