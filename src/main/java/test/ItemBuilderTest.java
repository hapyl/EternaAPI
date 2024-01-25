package test;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.ItemEventHandler;
import me.hapyl.spigotutils.module.nbt.NBTType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;

@RuntimeStaticTest
final class ItemBuilderTest {

    static final ItemBuilder ITEM_TEST = new ItemBuilder(Material.STONE, "itembuildertest_0")
            .setName("&aTest Item")
            .addClickEvent(pl -> pl.sendMessage("YOU CLICKED!!!"))
            .withCooldown(60, player -> !player.isSneaking(), "NO SNEAKING!!!")
            .addSmartLore("This is a very long string that will be split or wrapped, or I don't &areally care what its &7called.")
            .setAllowInventoryClick(true)
            .setNbt("hello.world", NBTType.SHORT, (short) 12345);

    private ItemBuilderTest() {
    }

    static void test(Player player) {
        final PlayerInventory inventory = player.getInventory();
        inventory.addItem(ITEM_TEST.build());

        final ItemBuilder cloned = ITEM_TEST.clone();
        cloned.setName("CLONED BUT WITH A DIFFERENT NAME AND TYPE");
        cloned.setType(Material.BLUE_WOOL);

        cloned.addFunction(p -> {
            p.sendMessage("CLONED EXLUSIVE");
        }).accept(Action.LEFT_CLICK_AIR).setCdSec(1);

        inventory.addItem(cloned.build());
        inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner("hapyl", Sound.ENTITY_IRON_GOLEM_DAMAGE).build());

        inventory.addItem(new ItemBuilder(Material.DIAMOND_BLOCK, "itembuildertest_1")
                .setEventHandler(new ItemEventHandler() {
                    @Override
                    public void onClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
                        player.sendMessage("click");
                    }

                    @Override
                    public void onRightClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
                        player.sendMessage("right click");
                    }

                    @Override
                    public void onLeftClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
                        player.sendMessage("left click");
                    }

                    @Override
                    public void onBlockPlace(@Nonnull Player player, @Nonnull BlockPlaceEvent ev) {
                        player.sendMessage("block place");
                    }

                    @Override
                    public void onBlockBreak(@Nonnull Player player, @Nonnull BlockBreakEvent ev) {
                        player.sendMessage("block break");
                    }

                    @Override
                    public void onItemDrop(@Nonnull Player player, @Nonnull PlayerDropItemEvent ev) {
                        player.sendMessage("item drop");
                    }

                    @Override
                    public void onPhysicalClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
                        player.sendMessage("physical");
                    }
                }).build());

        inventory.addItem(ITEM_TEST.clone().removeLore().build());
    }

}
