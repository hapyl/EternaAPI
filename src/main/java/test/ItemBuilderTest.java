package test;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.nbt.NBTType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RuntimeStaticTest
final class ItemBuilderTest {

    static final ItemStack ITEM_TEST = new ItemBuilder(Material.STONE, "id")
            .setName("&aTest Item")
            .addClickEvent(pl -> pl.sendMessage("YOU CLICKED!!!"))
            .addSmartLore("This is a very long string that will be split or wrapped, or I don't &areally care what its &7called.")
            .setAllowInventoryClick(true)
            .setNbt("hello.world", NBTType.SHORT, (short) 12345)
            .build();

    private ItemBuilderTest() {
    }

    static void test(Player player) {
        player.getInventory().addItem(ITEM_TEST);
    }

}
