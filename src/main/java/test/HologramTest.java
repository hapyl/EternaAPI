package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.hologram.Hologram;
import org.bukkit.entity.Player;

@RuntimeStaticTest
final class HologramTest {

    static Hologram hologram;

    private HologramTest() {
    }

    static void test(Player player, String... string) {
        if ((hologram != null) && (string == null || string.length == 0)) {
            Chat.sendMessage(player, "&aRemoved hologram.");
            hologram.destroy();
            hologram = null;
            return;
        }

        final String args = string[0].toLowerCase();

        if (hologram != null) {
            switch (args) {
                case "move" -> {
                    hologram.move(player.getLocation());
                    Chat.sendMessage(player, "&aMoved.");
                }

                case "addlines" -> {
                    hologram.setLines("1", "2", "3", "deez", "nuts", "are", "very", "good");
                    hologram.showAll();
                    Chat.sendMessage(player, "&aAdded lines.");
                }

                case "status" -> {
                    Chat.sendMessage(player, "&aIs showing? &l" + hologram.isShowingTo(player));
                }
            }

            return;
        }

        hologram = new Hologram();

        for (String s : string) {
            hologram.addLine(s);
        }

        hologram.setVisibility(10);
        hologram.create(player.getLocation());
        hologram.show(player);

        Chat.sendMessage(player, "&aCreated Hologram.");
    }

}
