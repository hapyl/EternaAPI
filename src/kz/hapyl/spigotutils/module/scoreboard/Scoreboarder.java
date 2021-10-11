package kz.hapyl.spigotutils.module.scoreboard;

import kz.hapyl.spigotutils.module.chat.Chat;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
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
		this.objective = this.scoreboard.registerNewObjective(UUID.randomUUID().toString().substring(0, 16), "dummy", "title");
		this.setTitle(title);
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		// 17 is the limit so create 17 teams
		for (int i = 0; i < 17; i++) {
			this.scoreboard.registerNewTeam("%" + i).addEntry(colorCharAt(i));
		}
	}

	public void setTitle(String title) {
		this.objective.setDisplayName(Chat.format(title));
	}

	public void setLine(int line, String text) {
		if (line < 0 || line > 17) {
			throw new IllegalArgumentException("line cannot be negative nor higher than 17");
		}
		Objects.requireNonNull(this.scoreboard.getTeam("%" + line)).setSuffix(text.isEmpty() ? "" : Chat.format(text));
	}

	public void setLines(String... lines) {
		this.lines.clear();
		this.lines.addAll(Arrays.asList(lines));

		this.updateLines();
	}

	public void addLines(String... lines) {
		this.lines.addAll(Arrays.asList(lines));
	}

	public void addLine(String line, Object... objects) {
		this.lines.add(line.formatted(objects));
	}

	public void clearLines() {
		this.lines.clear();
	}

	public void removeLine(int line) {
		if (line < 0 || line > 17 || line >= lines.size()) {
			return;
		}
		lines.remove(line);
	}

	// this forces teams to update and set their values
	public void updateLines() {

		// remove old lines
		for (int i = 0; i < 17; i++) {
			if (i <= this.lines.size()) {
				continue;
			}
			final String colorChar = colorCharAt(i);
			final Score score = this.objective.getScore(colorChar);

			if (score.getScore() > i) {
				this.scoreboard.resetScores(colorChar);
			}
		}

		// set the value
		int index = this.lines.size() - 1;
		for (final String line : this.lines) {
			setLine(index, line);
			--index;
		}

		// update scores
		for (int i = 1; i <= this.lines.size(); i++) {
			this.objective.getScore(colorCharAt(i - 1)).setScore(i);
		}
	}

	private String colorCharAt(int i) {
		return ChatColor.COLOR_CHAR + "" + ChatColor.ALL_CODES.charAt(i);
	}

	public void addPlayer(Player player) {
		if (this.players.contains(player)) {
			return;
		}
		updateLines();
		this.players.add(player);
		player.setScoreboard(this.scoreboard);
	}

	public void removePlayer(Player player) {
		if (!this.players.contains(player)) {
			return;
		}
		this.players.remove(player);
		player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
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