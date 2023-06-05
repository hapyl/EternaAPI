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
        if (string[0].equalsIgnoreCase("move") && hologram != null) {
            hologram.move(player.getLocation());
            Chat.sendMessage(player, "&aMoved.");
            return;
        }

        if (string[0].equalsIgnoreCase("addlines") && hologram != null) {
            hologram.setLines("1", "2", "3", "deez", "nuts", "are", "very", "good");
            hologram.showAll();
            Chat.sendMessage(player, "&aAdded lines.");
            return;
        }

        if (hologram != null) {
            Chat.sendMessage(player, "&aRemoved hologram.");
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
        Chat.sendMessage(player, "&aCreated Hologram.");
    }

}
