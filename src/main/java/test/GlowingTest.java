package test;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.reflect.glow.Glowing;
import kz.hapyl.spigotutils.module.util.CollectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GlowingTest {

    public static void run(Player player, int i) {
        player.sendMessage("§aTesting glowing.");

        // keep last
        final Glowing glowing = new Glowing(player, i);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (glowing.isGlowing()) {
                    glowing.setColor(randomColor());
                }
            }
        }.runTaskTimer(SpigotUtilsPlugin.getPlugin(), 0, 2);

        glowing.addViewer(player);
        glowing.start();

    }

    private static ChatColor randomColor() {
        final ChatColor random = CollectionUtils.randomElement(ChatColor.values(), ChatColor.WHITE);
        return random.isColor() ? random : randomColor();
    }

}
