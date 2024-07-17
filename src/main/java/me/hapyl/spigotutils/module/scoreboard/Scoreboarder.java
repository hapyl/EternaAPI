package me.hapyl.spigotutils.module.scoreboard;

import me.hapyl.spigotutils.module.EternaModule;
import me.hapyl.spigotutils.module.annotate.Range;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.chat.numbers.FixedFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * This class allows creating per-player scoreboards.
 */
@EternaModule
public class Scoreboarder {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final net.minecraft.world.scores.Objective nmsObjective;
    private final List<String> lines;
    private final Set<Player> players;

    /**
     * Creates a scoreboard builder with provided title.
     *
     * @param title - Title. Supports color codes.
     */
    public Scoreboarder(@Nonnull String title) {
        this.lines = new ArrayList<>();
        this.players = new HashSet<>();

        this.scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        this.objective = scoreboard.registerNewObjective(UUID.randomUUID().toString(), Criteria.DUMMY, title);

        this.nmsObjective = Reflect.getHandle(this.objective, net.minecraft.world.scores.Objective.class);

        this.setTitle(title);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // 17 is the limit so create 17 teams
        for (int i = 0; i < 17; i++) {
            this.scoreboard.registerNewTeam("%" + i).addEntry(colorCharAt(i));
        }
    }

    /**
     * Sets if the numbers on the right are hidden.
     *
     * @param hideNumbers - Is hidden.
     */
    public void setHideNumbers(boolean hideNumbers) {
        nmsObjective.setNumberFormat(hideNumbers ? BlankFormat.INSTANCE : null);
    }

    /**
     * Changes the current title of the scoreboard.
     *
     * @param title - New title. Supports color codes.
     */
    public void setTitle(@Nonnull String title) {
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
    public void setLines(@Range(max = 17) String... lines) {
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
    public void addLines(@Nonnull String... lines) {
        this.lines.addAll(Arrays.asList(lines));
    }

    /**
     * Adds a line to the scoreboard.
     * Does NOT update scoreboard lines.
     *
     * @param line    - Line to add.
     */
    public void addLine(@Nonnull String line) {
        this.lines.add(line);
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
     * <p>
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
     * Shows this scoreboard to a given player.
     *
     * @param player - Player to show scoreboard to.
     */
    public void addPlayer(@Nonnull Player player) {
        if (this.players.contains(player)) {
            return;
        }
        updateLines();
        this.players.add(player);
        player.setScoreboard(this.scoreboard);
    }

    /**
     * Shows the main scoreboard to the player, therefore
     * hiding this scoreboard.
     *
     * @param player - PLayer to hide scoreboard from.
     */
    public void removePlayer(@Nonnull Player player) {
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
    @Nonnull
    public Set<Player> getPlayers() {
        return players;
    }

    /**
     * Returns scoreboard object of this builder.
     *
     * @return scoreboard object of this builder.
     */
    @Nonnull
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    /**
     * Returns objective of this scoreboard.
     *
     * @return objective of this scoreboard.
     */
    @Nonnull
    public Objective getObjective() {
        return objective;
    }

    /**
     * Returns lines of this scoreboard.
     */
    @Nonnull
    public List<String> getLines() {
        return lines;
    }

    private String colorCharAt(int i) {
        return ChatColor.COLOR_CHAR + "" + ChatColor.ALL_CODES.charAt(i);
    }
}
