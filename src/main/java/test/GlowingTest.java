package test;

import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.reflect.glow.Glowing;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class GlowingTest {


    public static void run(Player player, int i) {
        player.sendMessage("§aTesting glowing.");

        final LivingEntity target = Entities.PIG.spawn(player.getLocation());
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
