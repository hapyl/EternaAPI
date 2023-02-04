package me.hapyl.spigotutils.module.scoreboard;

import me.hapyl.spigotutils.module.EternaModule;
import me.hapyl.spigotutils.module.annotate.ArraySize;
import me.hapyl.spigotutils.module.chat.Chat;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

/**
 * This class allows to create per-player scoreboards.
 */
@EternaModule
public class Scoreboarder {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final List<String> lines;
    private final Set<Player> players;

    /**
     * Creates a scoreboard builder with provided title.
     *
     * @param title - Title. Supports color codes.
     */
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

    /**
     * Changes current title of the scoreboard.
     *
     * @param title - New title. Supports color codes.
     */
    public void setTitle(String title) {
        this.objective.setDisplayName(Chat.format(title));
    }

    /**
     * Changes specific line of the scoreboard.
     *
     * @param line - index of a line.
     * @param text - New text.
     * @throws IllegalArgumentException if line is negative or greater than 17.
     * @throws NullPointerException     if line with index doesn't exist.
     */
    public void setLine(int line, String text) {
        if (line < 0 || line > 17) {
            throw new IllegalArgumentException("line cannot be negative nor higher than 17");
        }
        Objects.requireNonNull(this.scoreboard.getTeam("%" + line)).setSuffix(text.isEmpty() ? "" : Chat.format(text));
    }

    /**
     * Removes all lines and sets new lines of the scoreboard
     * and updates lines.
     *
     * @param lines - Array of new lines.
     */
    public void setLines(@ArraySize(max = 17) String... lines) {
        this.lines.clear();
        this.lines.addAll(Arrays.asList(lines));

        this.updateLines();
    }

    /**
     * Adds lines to the scoreboard.
     * Does NOT update scoreboard lines.
     *
     * @param lines - Lines to add.
     */
    public void addLines(String... lines) {
        this.lines.addAll(Arrays.asList(lines));
    }

    /**
     * Adds a line to the scoreboard.
     * Does NOT update scoreboard lines.
     *
     * @param line    - Line to add.
     * @param objects - Format replacements.
     */
    public void addLine(String line, Object... objects) {
        this.lines.add(line.formatted(objects));
    }

    /**
     * Clears all scoreboard lines.
     * Does NOT update scoreboard lines.
     */
    public void clearLines() {
        this.lines.clear();
    }

    /**
     * Remove line at specific index.
     * Does NOT update scoreboard lines.
     *
     * @param line - index of line.
     */
    public void removeLine(int line) {
        if (line < 0 || line > 17 || line >= lines.size()) {
            return;
        }
        lines.remove(line);
    }

    /**
     * Updates scoreboard lines, setting the values
     * and changing their respective positions.
     *
     * This is only called automatically at
     * {@link Scoreboarder#setLines(String...)} Must be
     * called manually after adding lines using
     * other methods.
     */
    public void updateLines() {
        // remove old lines
        for (int i = 0; i < 17; i++) {
            if (i <= (this.lines.size() - 1)) {
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

    /**
     * Shows this scoreboard to provided player.
     *
     * @param player - Player to show scoreboard to.
     */
    public void addPlayer(Player player) {
        if (this.players.contains(player)) {
            return;
        }
        updateLines();
        this.players.add(player);
        player.setScoreboard(this.scoreboard);
    }

    /**
     * Shows main scoreboard to the player, therefore
     * hiding this scoreboard.
     *
     * @param player - PLayer to hide scoreboard from.
     */
    public void removePlayer(Player player) {
        if (!this.players.contains(player)) {
            return;
        }
        this.players.remove(player);
        player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
    }

    /**
     * Returns a set of players who can see this scoreboard.
     *
     * @return a set of players who can see this scoreboard.
     */
    public Set<Player> getPlayers() {
        return players;
    }

    /**
     * Returns scoreboard object of this builder.
     *
     * @return scoreboard object of this builder.
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    /**
     * Returns objective of this scoreboard.
     *
     * @return objective of this scoreboard.
     */
    public Objective getObjective() {
        return objective;
    }

    /**
     * Returns lines of this scoreboard.
     */
    public List<String> getLines() {
        return lines;
    }

    private String colorCharAt(int i) {
        return ChatColor.COLOR_CHAR + "" + ChatColor.ALL_CODES.charAt(i);
    }
}
