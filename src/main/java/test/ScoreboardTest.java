package test;

import me.hapyl.spigotutils.module.scoreboard.Scoreboarder;
import org.bukkit.entity.Player;

@RuntimeStaticTest
public class ScoreboardTest {

    private ScoreboardTest() {
    }

    static Scoreboarder scoreboarder;

    static void test(Player player, String[] strings) {
        if (scoreboarder == null) {
            scoreboarder = new Scoreboarder("&a&lScoreboarder test.Test");
        }

        scoreboarder.setLines(strings.length > 0 ? strings : new String[] {
                "Line1", "Line2", "Line3", "",
                "hapyl.net"
        });
        scoreboarder.addPlayer(player);

    }

}
