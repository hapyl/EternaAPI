package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.hologram.Hologram;
import org.bukkit.entity.Player;

@RuntimeStaticTest
public final class HologramTest {

    private HologramTest() {
    }

    static Hologram hologram;

    static void test(Player player, String... string) {
        if (hologram != null) {
            Chat.broadcast("&aRemoved hologram.");
            hologram.destroy();
            hologram = null;
            return;
        }

        hologram = new Hologram();
        for (String s : string) {
            hologram.addLine(s);
        }

        hologram.create(player.getLocation());
        hologram.show(player);
        Chat.broadcast("&aCreated Hologram.");
    }

}
