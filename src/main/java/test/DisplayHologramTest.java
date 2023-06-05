package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.hologram.DisplayHologram;
import org.bukkit.entity.Player;

@RuntimeStaticTest
public class DisplayHologramTest {

    private static DisplayHologram hologram;

    public static void test(Player player, String... args) {
        if (hologram == null) {
            hologram = new DisplayHologram(player.getLocation());
            hologram.setBackground(false);
            hologram.showAll();

            Chat.sendMessage(player, "&aSpawned!");
        } else {
            if (args == null || args.length == 0) {
                hologram.remove();
                hologram = null;
                Chat.sendMessage(player, "&aRemoved!");
                return;
            }

            hologram.setLines(args);
        }
    }

}
