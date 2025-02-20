package me.hapyl.eterna.module.scoreboard;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.util.EternaEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * A {@link Scoreboard} builder.
 */
public class Scoreboarder implements EternaEntity {

    public static final String DUMMY_TEAM_PREFIX = "%";
    public static final int MAX_LINES = 17;
    public static final char[] CHARS = { 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'n', 'o', '0', '1', '2', '3', '4', '5' };

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final List<String> lines;
    private final Set<Player> showingTo;

    /**
     * Creates a new {@link Scoreboard} with the given title.
     *
     * @param title - The title.
     */
    public Scoreboarder(@Nonnull String title) {
        this.lines = new ArrayList<>();
        this.showingTo = new HashSet<>();

        this.scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        this.objective = scoreboard.registerNewObjective(UUID.randomUUID().toString(), Criteria.DUMMY, title);

        this.setTitle(title);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < MAX_LINES; i++) {
            this.scoreboard.registerNewTeam(DUMMY_TEAM_PREFIX + i).addEntry(charAt(i));
        }
    }

    /**
     * Sets whether this scoreboard has it's score numbers hidden.
     *
     * @param hideNumbers - Should numbers be hidden?
     */
    public void setHideNumbers(boolean hideNumbers) {
        objective.numberFormat(hideNumbers ? NumberFormat.blank() : null);
    }

    /**
     * Sets the {@link NumberFormat} of this scoreboard.
     *
     * @param numberFormat - The number format to set.
     */
    public void setNumberFormat(@Nullable NumberFormat numberFormat) {
        objective.numberFormat(numberFormat);
    }

    /**
     * Sets the new title for this scoreboard.
     *
     * @param title - The new title.
     */
    public void setTitle(@Nonnull String title) {
        this.objective.setDisplayName(Chat.format(title));
    }

    /**
     * Sets the line at the given index to the given text.
     *
     * @param line - The index of the line; must be between 0-{@link #MAX_LINES}
     * @param text - The new text.
     * @throws IndexOutOfBoundsException - If the given line index is {@code < 0} or {@code > MAX_LINES}
     */
    public void setLine(int line, @Nonnull String text) {
        if (line < 0 || line > MAX_LINES) {
            throw new IndexOutOfBoundsException("%s < 0 || line > %s".formatted(line, MAX_LINES));
        }

        Objects.requireNonNull(this.scoreboard.getTeam(DUMMY_TEAM_PREFIX + line)).setSuffix(text.isEmpty() ? "" : Chat.format(text));
    }

    /**
     * Clears all the current lines of this scoreboard, replaces it with the given lines and updates it.
     * <br>
     * Lines beyond {@link #MAX_LINES} are ignored.
     *
     * @param lines - The lines to set.
     */
    public void setLines(@Range(from = 0, to = MAX_LINES) @Nonnull String... lines) {
        this.lines.clear();
        this.lines.addAll(Arrays.asList(lines));

        this.updateLines();
    }

    /**
     * Adds the given lines to the end of this scoreboard lines.
     * <br>
     * Lines beyond {@link #MAX_LINES} are ignored.
     * <br>
     * This method does <b>NOT</b> update the scoreboard, {@link #updateLines()} must be called to see changes.
     *
     * @param lines - The lines to add.
     */
    public void addLines(@Nonnull String... lines) {
        this.lines.addAll(Arrays.asList(lines));
    }

    /**
     * Adds the given line to the end of this scoreboard lines.
     * <br>
     * This method does <b>NOT</b> update the scoreboard, {@link #updateLines()} must be called to see changes.
     *
     * @param line - The line to add.
     */
    public void addLine(@Nonnull String line) {
        this.lines.add(line);
    }

    /**
     * Clears the lines of this scoreboard.
     * <br>
     * This method does <b>NOT</b> update the scoreboard, {@link #updateLines()} must be called to see changes.
     */
    public void clearLines() {
        this.lines.clear();
    }

    /**
     * Removes the lines at the given index.
     * <br>
     * This method does <b>NOT</b> update the scoreboard, {@link #updateLines()} must be called to see changes.
     *
     * @param index - The index to remove the line at.
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index > MAX_LINES} or {@code index > lines.size()}
     */
    public void removeLine(int index) {
        if (index < 0 || index > 17 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("%1$s < 0 || %1$s > %2$s || %1$s >= %3$s".formatted(index, MAX_LINES, lines.size()));
        }

        lines.remove(index);
    }

    /**
     * Visually updates the lines of this scoreboard.
     * <br>
     * This method is automatically called when using {@link #setLines(String...)}, but must be manually called otherwise.
     */
    public void updateLines() {
        final int length = Math.min(this.lines.size(), MAX_LINES);

        for (int i = length; i < MAX_LINES; ++i) {
            final String colorChar = charAt(i);

            scoreboard.resetScores(colorChar);
        }

        // Set lines
        for (int i = 0; i < length; i++) {
            setLine(length - i - 1, this.lines.get(i));
        }

        // Update scores
        for (int i = 0; i < length; i++) {
            this.objective.getScore(charAt(i)).setScore(i + 1);
        }
    }

    /**
     * Shows this scoreboard to a given player.
     *
     * @param player - Player to show scoreboard to.
     * @deprecated {@link #show(Player)}
     */
    @Deprecated
    public void addPlayer(@Nonnull Player player) {
        this.show(player);
    }

    /**
     * Shows the main scoreboard to the player, therefore
     * hiding this scoreboard.
     *
     * @param player - PLayer to hide scoreboard from.
     * @deprecated {@link #hide(Player)}
     */
    public void removePlayer(@Nonnull Player player) {
        this.hide(player);
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

    @Override
    public void show(@Nonnull Player player) {
        if (this.showingTo.contains(player)) {
            return;
        }

        this.showingTo.add(player);
        player.setScoreboard(this.scoreboard);
    }

    @Override
    public void hide(@Nonnull Player player) {
        if (!this.showingTo.contains(player)) {
            return;
        }

        this.showingTo.remove(player);
        player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
    }

    @Nonnull
    @Override
    public Set<Player> getShowingTo() {
        return showingTo;
    }

    private String charAt(int i) {
        return "§x§" + CHARS[i];
    }
}
