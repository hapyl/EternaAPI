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

@RuntimeStaticTest
public final class GlowingTest {

    private GlowingTest() {
    }

    static void test(Player player, final int i) {
        player.sendMessage("§aTesting glowing.");

        final LivingEntity target = Entities.PIG.spawn(player.getLocation(), self -> {
            self.setSilent(true);
            self.setCustomNameVisible(true);
        });

        final Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("test");

        if (team == null) {
            team = scoreboard.registerNewTeam("test");
            team.setPrefix("&CCCCCCCCCCCCCCCC | ");
            team.setColor(ChatColor.RED);
        }

        team.addEntry(target.getUniqueId().toString());

        new Glowing(player, target, ChatColor.YELLOW, i) {
            @Override
            public void onGlowingStart() {
                Bukkit.broadcastMessage("start glowing for " + i);
            }

            @Override
            public void onGlowingTick() {
                setColor(randomColor());
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
