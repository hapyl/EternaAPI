package test;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.SlotPattern;
import me.hapyl.spigotutils.module.inventory.gui.SmartComponent;
import me.hapyl.spigotutils.module.inventory.gui.SmartGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GUITest {

    public static void test(Player player) {
        final SmartComponent smart = new SmartComponent();

        for (int i = 0; i < 16; i++) {
            int finalI = i;
            smart.add(new ItemBuilder(Material.APPLE).setAmount(i + 1).build(), click -> {
                click.sendMessage("You clicked %sth apple.".formatted(finalI + 1));
            });
        }

        final SmartGUI gui = new SmartGUI(player, "My Menu", smart, 1);
        gui.openInventory(SlotPattern.DEFAULT, 0);
    }

}
