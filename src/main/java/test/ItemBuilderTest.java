package test;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilderTest {

    public static final ItemStack ITEM_TEST = new ItemBuilder(Material.STONE, "id").setName("&atest item").addClickEvent(pl -> {
        pl.sendMessage("YOU CLICKED!!!");
    }).setAllowInventoryClick(true).build();

}
