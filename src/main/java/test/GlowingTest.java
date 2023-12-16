package test;

import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.reflect.glow.Glowing;
import me.hapyl.spigotutils.module.util.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

@RuntimeStaticTest
final class GlowingTest {

    private GlowingTest() {
    }

    static final String teamName = "dummyTeam";

    static void test(Player player, final int i) {
        Entity entity = Bukkit.getPlayer("DiDenPro");

        if (entity == null) {
            entity = Entities.ARMOR_STAND.spawn(player.getLocation(), self -> {
                self.setInvisible(true);
                self.setSmall(true);
                self.setHelmet(new ItemStack(Material.STONE));
            });
        }

        player.sendMessage("§aTesting glowing.");

        final Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(teamName);

        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        team.setColor(ChatColor.BLACK);
        team.addEntry(entity instanceof Player ? entity.getName() : entity.getUniqueId().toString());

        final Team finalTeam = team;

        new Glowing(player, entity, ChatColor.YELLOW, i) {
            @Override
            public void onGlowingStart() {
                Bukkit.broadcastMessage("start glowing for " + i);
            }

            @Override
            public void onGlowingStop() {
                Bukkit.broadcastMessage("stopped glowing");
            }
        }.glow();

    }


}
