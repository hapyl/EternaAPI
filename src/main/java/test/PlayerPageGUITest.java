package test;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerPageGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

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
            public void onClick(@Nonnull Player player, @Nonnull String content, int index, int page) {
                player.sendMessage("Clicked deprecated");
            }

            @Override
            public void onClick(@Nonnull Player player, @Nonnull String content, int index, int page, @Nonnull ClickType clickType) {
                player.sendMessage("You clicked " + content);
                player.sendMessage("Using " + clickType);
            }

        };

        gui.setContents(List.of("a", "b", "c", "d", "e", "f", "g", "h"));
        gui.setFit(PlayerPageGUI.Fit.SLIM);
        gui.setEmptyContentsItem(null);

        gui.openInventory(1);
    }


}
