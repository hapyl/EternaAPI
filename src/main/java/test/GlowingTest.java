package test;

import kz.hapyl.spigotutils.module.entity.Entities;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import kz.hapyl.spigotutils.module.reflect.glow.Glowing;
import kz.hapyl.spigotutils.module.util.CollectionUtils;
import kz.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

public class GlowingTest {

    public static void run(Player player, int i) {
        player.sendMessage("§aTesting glowing.");

        final Pig pig = Entities.PIG.spawn(player.getLocation());

        final Glowing glowing = new Glowing(pig, i) {

            @Override
            public void onGlowingStart() {
                PlayerLib.spawnParticle(getEntity().getLocation().add(0.0d, 1.0d, 0.0d), Particle.HEART, 1);
            }

            @Override
            public void onGlowingTick() {
                setColor(randomColor());
            }

            @Override
            public void onGlowingStop() {
                Runnables.runLater(pig::remove, 10L);
            }
        };

        glowing.addViewer(player);
        glowing.start();
    }

    private static ChatColor randomColor() {
        final ChatColor random = CollectionUtils.randomElement(ChatColor.values(), ChatColor.WHITE);
        return random.isColor() ? random : randomColor();
    }

}
