package test;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerPageGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.LinkedList;

@RuntimeStaticTest
final class PlayerPageGUITest {

    private PlayerPageGUITest() {
    }

    static void test(Player player, String[] args) {
        final PlayerPageGUI<String> gui = new PlayerPageGUI<>(player, "Player Page GUI Test", 5) {
            @Override
            @Nonnull
            public ItemStack asItem(Player player, String content, int index, int page) {
                return new ItemBuilder(Material.STONE)
                        .setName(content)
                        .setAmount((index + 1) + (maxItemsPerPage() * page))
                        .toItemStack();
            }

            @Override
            public void onClick(Player player, String content, int index, int page) {
                player.sendMessage("You clicked " + content);
                final String last = getContents().removeLast();

                player.sendMessage("Removed " + last);
                openInventory(page);
            }
        };

        gui.setFit(PlayerPageGUI.Fit.SLIM);
        gui.setEmptyContentsItem(null);

        gui.openInventory(1);
    }


}
