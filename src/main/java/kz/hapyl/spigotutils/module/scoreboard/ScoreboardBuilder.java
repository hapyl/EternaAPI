package kz.hapyl.spigotutils.module.scoreboard;

import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Create custom per-player (or global(why?)) scoreboards.
 *
 * @see Scoreboarder
 * @deprecated create new class
 */
@Deprecated
public class ScoreboardBuilder {
    private Objective obj;
    private Scoreboard score;
    private int scoreLines;

    public ScoreboardBuilder(String scoreName, String displayName, int lines) {

        if (lines > ChatColor.ALL_CODES.length()) {
            throw new IndexOutOfBoundsException("Lines is out of bounds. Max lines is " + ChatColor.ALL_CODES.length());
        }

        if (lines > 17) {
            Bukkit.getLogger()
                  .warning("Creating scoreboard with more than 17 lines, not all lines will render client side!");
        }

        this.score = Bukkit.getScoreboardManager().getNewScoreboard();
        // If this doesn't work I don't know what will
        HumanNPC.hideAllNames(this.score);
        this.obj = this.score.registerNewObjective(scoreName, "dummy", "def");
        this.obj.setDisplayName(format(displayName));
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 1; i < (lines + 1); ++i) {
            this.score.registerNewTeam("line" + i).addEntry("§" + ChatColor.ALL_CODES.charAt(i));
            this.obj.getScore("§" + ChatColor.ALL_CODES.charAt(i)).setScore(i);
        }

        this.scoreLines = lines;
    }

    /**
     * Shows scoreboard to the player.
     *
     * @param player who to show.
     */
    public void show(Player player) {
	/*	final List<NPC> list = NPC.getShownNPCs(player);
		if (!list.isEmpty()) {
			for (final NPC npc : list) {
				npc.hideDisplayName(this.score);
			}
		}*/
        player.setScoreboard(this.score);
    }

    public void showIfNotShown(Player player) {
        if (player.getScoreboard() == this.score) {
            return;
        }
        this.show(player);
    }

    /**
     * Adds a line to certain Scoreboard.
     */
    public void addLine() {
        linesWorker(1);
    }

    /**
     * Removes a line from certain scoreboard.
     */
    public void removeLine() {
        linesWorker(-1);
    }

    /**
     * Adds a line to certain scoreboard at certain position.
     *
     * @param line line that will be added. (Old lines will be shifted down)
     */
    public void addLineAt(int line) {
        lineMover(line, 1);
    }

    /**
     * Adds certain amount of lines at given line.
     *
     * @param lines - Amount of lines.
     * @param at    - Initial line.
     */
    @Deprecated
    public void addLinesAt(int lines, int at) {
        for (int i = 0; i < lines; i++) {
            addLineAt(at + i);
        }
    }

    /**
     * Removes certain amount of lines at given line.
     *
     * @param lines - Amount of lines.
     * @param at    - Initial line.
     */
    @Deprecated
    public void removeLinesAt(int lines, int at) {
        for (int i = 0; i < lines; i++) {
            removeLineAt(at + i);
        }
    }

    /**
     * Sets new amount of lines to the scoreboard.
     *
     * @param newLines - Amount of lines.
     */
    public void setLines(int newLines) {

        ScoreboardBuilder builder = new ScoreboardBuilder(this.obj.getName(), this.obj.getDisplayName(), newLines);

        this.reiterate(builder);

        this.scoreLines = builder.scoreLines;
        this.score = builder.score;

        // Copy teams as well

        this.obj = builder.obj;

    }

    /**
     * Fills scoreboard with lines from top to bottom, if more lines than score has, safely recreates scoreboard with more lines and vice versa with
     * more lines.
     *
     * @param lines - Lines.
     */
    public void setLines(String... lines) {

        if (lines.length != this.scoreLines) {
            final ScoreboardBuilder builder = new ScoreboardBuilder(
                    this.obj.getName(),
                    this.obj.getDisplayName(),
                    lines.length
            );
            this.scoreLines = builder.scoreLines;
            this.score = builder.score;
            this.obj = builder.obj;
        }

        int at = 0;
        for (int i = lines.length; i >= 1; --i, ++at) {
            this.set(i, lines[at] == null ? "" : lines[at]);
        }

        this.reiterate(this);
    }

    /**
     * Removed a line from certain scoreboard at certain position.
     *
     * @param line line that will be removed.
     */
    public void removeLineAt(int line) {
        lineMover(line, -1);
    }

    /**
     * Returns current value of the line.
     *
     * @param line line number.
     * @return String.
     */
    public String get(int line) {
        if (score.getTeam("line" + line) != null) {
            return score.getTeam("line" + line).getSuffix();
        }
        return "notFound";
    }

    /**
     * Sets text for certain line. If scoreboard doesn't have that line IndexOutBound will be thrown.
     *
     * @param line line number.
     * @param text text to set. (Supports '&' char as color code.)
     */
    public void set(int line, String text) {

        if (line > scoreLines) {
            Bukkit.getLogger().warning("Scoreboard has only " + scoreLines + " lines. Given " + line + " line.");
            return;
        }
        if (line < 0) {
            Bukkit.getLogger().warning("Scoreboard can't have less than 0 lines!");
            return;
        }

        score.getTeam("line" + line).setSuffix(format(text));
    }

    /**
     * Hides scoreboard for player.
     *
     * @param player who to hide.
     */
    public ScoreboardBuilder hide(Player player) {
        for (String entry : score.getEntries()) {
            score.resetScores(entry);
        }
        return this;
    }

    /**
     * Updates title for the scoreboard. By title it means DisplayName.
     *
     * @param newTitle String.
     */
    public void updateTitle(String newTitle) {
        obj.setDisplayName(newTitle);
    }

    /**
     * Works with lines.
     */
    private void linesWorker(int value) {

        ScoreboardBuilder dummy = rebuild(value);

        for (int i = 1; i < this.scoreLines; ++i) {
            dummy.set(i, this.get(i));
        }

        this.obj = dummy.obj;
        this.score = dummy.score;
        this.scoreLines = dummy.scoreLines;

    }

    /**
     * Moves lines to make don't lose them.
     */
    private void lineMover(int line, int add) {

        ScoreboardBuilder dummy = rebuild(add);

        if (add >= 1) {
            for (int i = 1; i < this.scoreLines + 1; ++i) {
                if (i >= line) {
                    dummy.set(i + 1, this.get(i));
                }
                else {
                    dummy.set(i, this.get(i));
                }
            }
        }
        else {
            for (int i = this.scoreLines - 1; i > 0; --i) {
                if (i < line) {
                    dummy.set(i, this.get(i));
                }
                else {
                    dummy.set(i, this.get(i + 1));
                }
            }
        }

        this.obj = dummy.obj;
        this.score = dummy.score;
        this.scoreLines = dummy.scoreLines;
    }

    /**
     * Rebuilds a scoreboard.
     */
    private ScoreboardBuilder rebuild(int add) {

        int result = this.scoreLines + add;
        if (result < 0 || result > ChatColor.ALL_CODES.length()) {
            throw new IndexOutOfBoundsException(
                    "Cannot have less than 0 or more than %%line%% lines!".replace(
                            "%%line%%",
                            ChatColor.ALL_CODES.length() + ""
                    ));
        }

        final ScoreboardBuilder dummy = new ScoreboardBuilder(
                this.obj.getName(),
                this.obj.getDisplayName(),
                this.scoreLines + add
        );
        reiterate(dummy);
        return dummy;
    }

    /**
     * Shows a new scoreboard to all players who has the old one (Update).
     */
    private void reiterate(ScoreboardBuilder newScore) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getScoreboard() == this.score) {
                newScore.show(player);
            }
        });
    }

    public List<String> getLines() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < this.scoreLines; i++) {
            list.add(get(i));
        }
        return list;
    }

    /**
     * Just an util to translate char color. Ignore it.
     */
    private static String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
