package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * A small utility helper that allows lazy team initiation and handling.
 */
@UtilityClass
public final class TeamHelper {
    
    private TeamHelper() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets or registers a new team for the given {@link Scoreboard} with the given name.
     *
     * @param scoreboard - The scoreboard to get or create the team for.
     * @param teamName   - The team name.
     * @return the team with the given name from the given scoreboard.
     */
    @Nonnull
    public static Team fetch(@Nonnull Scoreboard scoreboard, @Nonnull String teamName) {
        return fetch(scoreboard, teamName, t -> {});
    }
    
    /**
     * Gets or registers a new team for the given {@link Scoreboard} with the given name.
     *
     * @param scoreboard - The scoreboard to get or create the team for.
     * @param teamName   - The team name.
     * @param consumer   - The consumer to apply after getting or creating the team.
     * @return the team with the given name from the given scoreboard.
     */
    @Nonnull
    public static Team fetch(@Nonnull Scoreboard scoreboard, @Nonnull String teamName, @Nonnull Consumer<Team> consumer) {
        Team team = scoreboard.getTeam(teamName);
        
        // Register if team doesn't exist
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }
        
        consumer.accept(team);
        return team;
    }
    
}
