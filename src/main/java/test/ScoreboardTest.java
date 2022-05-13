package test;

import kz.hapyl.spigotutils.module.scoreboard.Scoreboarder;
import org.bukkit.entity.Player;

public class ScoreboardTest {

    private static Scoreboarder scoreboarder;

    public static void create(Player player, String[] strings) {
        if (scoreboarder == null) {
            scoreboarder = new Scoreboarder("&a&lScoreboarder Test");
        }

        scoreboarder.setLines(strings.length > 0 ? strings : new String[] { "Line1", "Line2", "Line3", "",
                                                                            "hapyl.net" });
        scoreboarder.addPlayer(player);

    }

}
