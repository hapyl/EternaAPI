package kz.hapyl.spigotutils.module.util;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class Helper {

	public static Team getEntityTeam(Scoreboard scoreboard) {
		return getTeam(scoreboard, "ETERNA_API_ET", team -> {
			team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
			team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		});
	}

	public static Team getNpcTeam(Scoreboard scoreboard) {
		return getTeam(scoreboard, "ETERNA_API", team -> team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER));
	}

	public static Team getGlowingTeam(Scoreboard scoreboard) {
		return getTeam(scoreboard, "ETERNA_API_GC", null);
	}

	private static Team getTeam(Scoreboard score, String name, Action<Team> action) {
		Team team = score.getTeam(name);
		if (team == null) {
			team = score.registerNewTeam(name);
			if (action != null) {
				action.use(team);
			}
		}
		return team;
	}

}
