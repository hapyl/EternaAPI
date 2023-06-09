package test;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerAutoGUI;
import me.hapyl.spigotutils.module.inventory.gui.SlotPattern;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@RuntimeStaticTest
final class PlayerAutoGUITest {

    private PlayerAutoGUITest() {
    }

    static void test(Player player) {
        final PlayerAutoGUI gui = new PlayerAutoGUI(player, "My Menu", 5);

        for (int i = 0; i < 16; i++) {
            int finalI = i;
            gui.addItem(new ItemBuilder(Material.APPLE).setAmount(i + 1).build(), click -> {
                click.sendMessage("You clicked %sth apple.".formatted(finalI + 1));
            });
        }

        gui.setPattern(SlotPattern.DEFAULT);
        gui.openInventory();
    }

}
