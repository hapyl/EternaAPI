package test;

import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.reflect.glow.Glowing;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class GlowingTest {


    public static void run(Player player, int i) {
        player.sendMessage("§aTesting glowing.");

        final Player target = Bukkit.getPlayer("DiDenPro");
        final Glowing glowing = new Glowing(target, ChatColor.YELLOW, i) {

            @Override
            public void onGlowingStart() {
                PlayerLib.spawnParticle(getEntity().getLocation().add(0.0d, 1.0d, 0.0d), Particle.HEART, 1);
            }

            @Override
            public void onGlowingStop() {
            }
        };

        glowing.addPlayer(player);
        glowing.start();
    }

}
