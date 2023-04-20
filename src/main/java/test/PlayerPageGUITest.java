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
final class PlayerPageGUITest extends PlayerPageGUI<String> {

    public PlayerPageGUITest(Player player) {
        super(player, "Player Page GUI Test", 5);

        setFit(Fit.SLIM);
        setEmptyContentsItem(null);

        openInventory(1);
    }

    private LinkedList<String> lorem(int amount) {
        final String[] loremWords = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus.".split(" ");
        final LinkedList<String> list = Lists.newLinkedList();

        for (int i = 0; i < amount; i++) {
            list.add(loremWords[i % loremWords.length]);
        }

        return list;
    }

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

}
