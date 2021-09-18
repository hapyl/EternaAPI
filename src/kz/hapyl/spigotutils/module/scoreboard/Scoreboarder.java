package kz.hapyl.spigotutils.module.scoreboard;

import kz.hapyl.spigotutils.module.chat.Chat;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class Scoreboarder {

	private final Scoreboard scoreboard;
	private final Objective objective;
	private final List<String> lines;
	private final Set<Player> players;

	public Scoreboarder(String title) {
		this.lines = new ArrayList<>();
		this.players = new HashSet<>();

		this.scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
		this.objective = this.scoreboard.registerNewObjective(title, "dummy", "title");
		this.setTitle(title);
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		// 17 is the limit so create 17 teams
		for (int i = 0; i < 17; i++) {
			this.scoreboard.registerNewTeam("%" + i).addEntry(colorCharAt(i));
		}

	}

	public void setTitle(String title) {
		this.objective.setDisplayName(title);
	}

	public void setLine(int line, String text) {
		if (line < 0 || line > 17) {
			throw new IllegalArgumentException("line cannot be negative nor higher than 17");
		}
		Objects.requireNonNull(this.scoreboard.getTeam("%" + line)).setSuffix(Chat.format(text));
	}

	public void setLines(String... lines) {
		this.lines.clear();
		this.lines.addAll(Arrays.asList(lines));

		int index = this.lines.size();
		for (final String line : this.lines) {
			setLine(index, line);
			--index;
		}

		this.updateLines();
	}

	// this forces teams to update and set their values
	public void updateLines() {
		for (int size = this.lines.size(); size >= 0; size--) {
			this.objective.getScore(colorCharAt(size)).setScore(size);
		}
	}

	private String colorCharAt(int i) {
		return ChatColor.COLOR_CHAR + "" + ChatColor.ALL_CODES.charAt(i);
	}

	public void addPlayer(Player player) {
		updateLines();
		this.players.add(player);
		player.setScoreboard(this.scoreboard);
	}

	public void removePlayer(Player player) {
		updateLines();
		this.players.remove(player);
		this.scoreboard.getEntries().remove(player.getName());
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public Objective getObjective() {
		return objective;
	}

	public List<String> getLines() {
		return lines;
	}
}
