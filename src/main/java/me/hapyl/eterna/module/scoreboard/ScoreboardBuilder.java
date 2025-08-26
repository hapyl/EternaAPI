package me.hapyl.eterna.module.scoreboard;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import me.hapyl.eterna.module.annotate.ScoreboardSensitive;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.util.list.ComponentList;
import me.hapyl.eterna.module.util.list.StringList;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a more modern version of {@link Scoreboarder}, with {@link Component} support.
 */
@ScoreboardSensitive
public class ScoreboardBuilder {
    
    public static final String dummyTeamPrefix = "%";
    public static final int maxLines = 15;
    
    private static final char[] magicChars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    
    private final Player player;
    private final Scoreboard scoreboard;
    private final Objective objective;
    
    @Nonnull private ComponentList array;
    
    /**
     * Creates a new {@link ScoreboardBuilder} instance.
     *
     * @param player - The owner of this scoreboard.
     * @param title  - The default title of this scoreboard.
     */
    public ScoreboardBuilder(@Nonnull Player player, @Nonnull Component title) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        
        this.objective = scoreboard.registerNewObjective(UUID.randomUUID().toString(), Criteria.DUMMY, title);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.numberFormat(NumberFormat.blank());
        
        this.array = ComponentList.empty();
        
        // Prepare teams
        for (int i = 0; i < maxLines; i++) {
            scoreboard.registerNewTeam(dummyTeamPrefix + i).addEntry(charAt(i));
        }
        
        // Show the scoreboard
        player.setScoreboard(scoreboard);
    }
    
    /**
     * Creates a new {@link ScoreboardBuilder} instance.
     *
     * @param player - The owner of this scoreboard.
     * @param title  - The default title of this scoreboard.
     */
    public ScoreboardBuilder(@Nonnull Player player, @Nonnull String title) {
        this(player, Components.ofLegacy(title));
    }
    
    /**
     * Sets whether the scoreboard should hide the red numbers.
     * <p>This modern version default to hiding the numbers.</p>
     *
     * @param hideNumbers - Whether to hide the red numbers.
     */
    public void setHideNumbers(boolean hideNumbers) {
        this.objective.numberFormat(hideNumbers ? NumberFormat.blank() : null);
    }
    
    /**
     * Sets the {@link NumberFormat} of this scoreboard.
     *
     * @param numberFormat - The new number format, or {@code null} for default.
     */
    public void setNumberFormat(@Nullable NumberFormat numberFormat) {
        this.objective.numberFormat(numberFormat);
    }
    
    /**
     * Sets the title of this scoreboard.
     *
     * @param component - The new title.
     */
    public void setTitle(@Nonnull Component component) {
        this.objective.displayName(component);
    }
    
    /**
     * Sets the title of this scoreboard.
     *
     * @param string - The new title.
     */
    public void setTitle(@Nonnull String string) {
        this.objective.displayName(Components.ofLegacy(string));
    }
    
    /**
     * Sets the lines of this scoreboard and updates them.
     * <p>The lines are hard-limited by {@link #maxLines}, and any lines beyond that are stripped and will not be rendered.</p>
     *
     * @param array - The new lines.
     */
    public void setLines(@Nonnull ComponentList array) {
        this.array = array.stream().limit(maxLines).collect(ComponentList.collector());
        this.updateLines();
    }
    
    /**
     * Sets the lines of this scoreboard and updates them.
     * <p>The lines are hard-limited by {@link #maxLines}, and any lines beyond that are stripped and will not be rendered.</p>
     *
     * @param array - The new lines.
     */
    public void setLines(@Nonnull StringList array) {
        setLines(array.stream().map(Components::ofLegacy).collect(ComponentList.collector()));
    }
    
    /**
     * Sets the lines of this scoreboard and updates them.
     * <p>The lines are hard-limited by {@link #maxLines}, and any lines beyond that are stripped and will not be rendered.</p>
     *
     * @param lines - The new lines.
     */
    public void setLines(@Nonnull Component... lines) {
        setLines(Arrays.stream(lines).collect(ComponentList.collector()));
    }
    
    /**
     * Sets the lines of this scoreboard and updates them.
     * <p>The lines are hard-limited by {@link #maxLines}, and any lines beyond that are stripped and will not be rendered.</p>
     *
     * @param lines - The new lines.
     */
    public void setLines(@Nonnull String... lines) {
        setLines(Arrays.stream(lines).map(Components::ofLegacy).collect(ComponentList.collector()));
    }
    
    /**
     * Clears the lines of this scoreboard and updates them.
     */
    public void clear() {
        this.array = ComponentList.empty();
        this.updateLines();
    }
    
    /**
     * Hides this scoreboard.
     */
    public void hide() {
        // TODO @Aug 26, 2025 (xanyjl) -> Properly unregister teams when eterna-scoreboard is impl
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }
    
    private void updateLines() {
        for (int i = 0; i < maxLines; i++) {
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
    
    @Nonnull
    private Team teamAt(int index) {
        return Objects.requireNonNull(scoreboard.getTeam(dummyTeamPrefix + index), "Illegal teamAt() call");
    }
    
    @Nonnull
    private String charAt(int index) {
        return "§%s§r".formatted(magicChars[index]);
    }
    
}
