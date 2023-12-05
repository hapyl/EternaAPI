package test;

import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.reflect.glow.Glowing;
import me.hapyl.spigotutils.module.util.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

@RuntimeStaticTest
final class GlowingTest {

    private GlowingTest() {
    }

    static final String teamName = "dummyTeam";

    static void test(Player player, final int i) {
        final Player didenpro = Bukkit.getPlayer("DiDenPro");

        if (didenpro == null) {
            player.sendMessage("no");
            return;
        }

        player.sendMessage("§aTesting glowing.");

        final Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(teamName);

        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        team.setColor(ChatColor.BLACK);
        team.addEntry(didenpro.getName());

        final Team finalTeam = team;

        new Glowing(player, didenpro, ChatColor.YELLOW, i) {
            @Override
            public void onGlowingStart() {
                Bukkit.broadcastMessage("start glowing for " + i);
            }

            @Override
            public void onGlowingTick() {
                setColor(randomColor());
                finalTeam.setPrefix(UUID.randomUUID().toString());
            }

            private ChatColor randomColor() {
                final ChatColor color = CollectionUtils.randomElement(ChatColor.values());

                if (color == null || color.isFormat()) {
                    return randomColor();
                }

                return color;
            }

            @Override
            public void onGlowingStop() {
                Bukkit.broadcastMessage("stopped glowing");
            }
        }.glow();

    }


}
