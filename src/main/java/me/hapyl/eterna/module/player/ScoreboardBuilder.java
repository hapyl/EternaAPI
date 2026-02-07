package me.hapyl.eterna.module.player;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import me.hapyl.eterna.module.component.ComponentList;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a {@link Scoreboard} builder.
 */
public class ScoreboardBuilder {
    
    /**
     * Defines a dummy team prefix used for scoreboard team magic.
     */
    @NotNull
    public static final String DUMMY_TEAM_PREFIX = "%";
    
    /**
     * Defines the maximum lines a scoreboard may have.
     */
    public static final int MAX_LINES = 15;
    
    private static final char[] MAGIC_CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    
    private final Player player;
    private final Scoreboard scoreboard;
    private final Objective objective;
    
    @NotNull private ComponentList array;
    
    /**
     * Creates a new {@link ScoreboardBuilder}.
     *
     * @param player - The owner of this scoreboard.
     * @param title  - The default title of this scoreboard.
     */
    public ScoreboardBuilder(@NotNull Player player, @NotNull Component title) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        
        this.objective = scoreboard.registerNewObjective(UUID.randomUUID().toString(), Criteria.DUMMY, title);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.numberFormat(NumberFormat.blank());
        
        this.array = ComponentList.empty();
        
        // Prepare teams
        for (int i = 0; i < MAX_LINES; i++) {
            scoreboard.registerNewTeam(DUMMY_TEAM_PREFIX + i).addEntry(charAt(i));
        }
        
        // Show the scoreboard
        player.setScoreboard(scoreboard);
    }
    
    /**
     * Sets the {@link NumberFormat} of this {@link ScoreboardBuilder}.
     *
     * @param numberFormat - The new number format, or {@code null} to reset.
     */
    public void setNumberFormat(@Nullable NumberFormat numberFormat) {
        this.objective.numberFormat(numberFormat);
    }
    
    /**
     * Sets the title of this {@link ScoreboardBuilder}.
     *
     * @param component - The new title.
     */
    public void setTitle(@NotNull Component component) {
        this.objective.displayName(component);
    }
    
    /**
     * Sets the lines of this {@link ScoreboardBuilder} and updates them.
     *
     * <p>
     * The lines are hard-limited by {@link #MAX_LINES}, and any lines beyond that are stripped and will not be rendered.
     * </p>
     *
     * @param array - The new lines.
     */
    public void setLines(@NotNull ComponentList array) {
        this.array = array.stream().limit(MAX_LINES).collect(ComponentList.collector());
        this.updateLines();
    }
    
    /**
     * Sets the lines of this {@link ScoreboardBuilder} and updates them.
     *
     * @param components - The lines to set; each new {@link Component} is a new line.
     */
    public void setLines(@NotNull Component... components) {
        setLines(ComponentList.of(components));
    }
    
    /**
     * Clears the lines of this {@link ScoreboardBuilder} and updates them.
     */
    public void clear() {
        this.array = ComponentList.empty();
        this.updateLines();
    }
    
    /**
     * Hides this scoreboard.
     */
    public void hide() {
        // Unregister the teams
        scoreboard.getTeams().forEach(Team::unregister);
        
        // Set the main scoreboard
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }
    
    private void updateLines() {
        for (int i = 0; i < MAX_LINES; i++) {
            final Score score = objective.getScore(charAt(i));
            
            // If within bounds, update the line
            if (i < array.size()) {
                final Team team = teamAt(i);
                final int index = array.size() - i - 1;
                
                team.suffix(array.get(index));
                score.setScore(i + 1);
            }
            // Otherwise reset score
            else {
                score.resetScore();
            }
        }
    }
    
    @NotNull
    private Team teamAt(int index) {
        return Objects.requireNonNull(scoreboard.getTeam(DUMMY_TEAM_PREFIX + index), "Illegal teamAt() call");
    }
    
    @NotNull
    private String charAt(int index) {
        return "§%s§r".formatted(MAGIC_CHARS[index]);
    }
    
}
