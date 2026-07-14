package test;

import io.papermc.paper.persistence.PersistentDataContainerView;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.builder.ItemEventListener;
import me.hapyl.eterna.module.inventory.builder.ItemFunction;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.DescriptivePredicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class EternaTestItemBuilder extends EternaTest {
    
    EternaTestItemBuilder(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Key keyHelloWorld = Key.ofString("hello_world");
        final Key keyGoodbyeWorld = Key.ofString("goodbye_world");
        
        final Key keyBuilder0 = Key.ofString("item_builder_test_0");
        
        final ItemBuilder testItem = new ItemBuilder(Material.STONE, keyBuilder0)
                .setName(Component.text("Test Item Cooldown Predicate"))
                .addFunction(
                        ItemFunction.builder(_player -> _player.sendMessage(Component.text("You clicked without sneaking!")))
                                    .cooldown(60)
                                    .predicate(DescriptivePredicate.of(Predicate.not(Player::isSneaking), "No sneaking!"))
                                    .build()
                )
                .addWrappedLore(Component.text("This is a very long string that will be wrapped, or at least it should if I know what I'm doing, but I really don't..."))
                .setPersistentData(keyHelloWorld, PersistentDataType.SHORT, (short) 12345)
                .setPersistentData(keyGoodbyeWorld, PersistentDataType.STRING, "I hecking love Java ☕!");
        
        final Player player = context.player();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack originalItem = testItem.build();
        
        inventory.clear();
        inventory.addItem(originalItem);
        
        context.assertEquals(originalItem.getType(), Material.STONE);
        
        {
            final PersistentDataContainerView data = originalItem.getPersistentDataContainer();
            
            context.assertEquals(data.get(keyHelloWorld.asNamespacedKey(), PersistentDataType.SHORT), (short) 12345);
            context.assertEquals(data.get(keyGoodbyeWorld.asNamespacedKey(), PersistentDataType.STRING), "I hecking love Java ☕!");
        }
        
        {
            final ItemBuilder builder = testItem.cloneAs(Key.ofString("itembuildertest_1"));
            builder.setName(Component.text("Cloned, Differnt Name & Type & Exclusive LEFT_CLICK_AIR"));
            builder.setType(Material.BLUE_WOOL);
            
            builder.addFunction(
                    ItemFunction.builder(_player -> _player.sendMessage(Component.text("Exclusive to this item!", NamedTextColor.LIGHT_PURPLE)))
                                .accepts(Action.LEFT_CLICK_AIR)
                                .build()
            );
            
            inventory.addItem(builder.build());
        }
        
        inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD).setName(Component.text("Your Head")).setHeadTexture(Skin.ofPlayer(player)).build());
        
        {
            final ItemBuilder builder = testItem.cloneAs(Key.ofString("itembuildertest_2"));
            
            builder.setName(Component.text("Cloned, only LEFT_CLICK_AIR"));
            builder.setAmount(88);
            
            builder.clearFunctions();
            builder.addFunction(
                    ItemFunction.builder(_player -> _player.sendMessage(Component.text("Exclusive to LEFT_CLICK_AIR", NamedTextColor.DARK_PURPLE)))
                                .accepts(Action.LEFT_CLICK_AIR)
                                .build()
            );
            
            inventory.addItem(builder.build());
        }
        
        {
            final ItemBuilder builder = new ItemBuilder(Material.DIAMOND_BLOCK, Key.ofString("itembuildertest_3"));
            
            builder.setListener(new ItemEventListener() {
                @Override
                public void onClick(@NotNull Player player, @NotNull PlayerInteractEvent ev) {
                    player.sendMessage(Component.text("onClick"));
                }
                
                @Override
                public void onRightClick(@NotNull Player player, @NotNull PlayerInteractEvent ev) {
                    player.sendMessage(Component.text("onRightClick"));
                }
                
                @Override
                public void onLeftClick(@NotNull Player player, @NotNull PlayerInteractEvent ev) {
                    player.sendMessage(Component.text("onLeftClick"));
                }
                
                @Override
                public void onBlockPlace(@NotNull Player player, @NotNull BlockPlaceEvent ev) {
                    player.sendMessage(Component.text("onBlockPlace"));
                }
                
                @Override
                public void onBlockBreak(@NotNull Player player, @NotNull BlockBreakEvent ev) {
                    player.sendMessage(Component.text("onBlockBreak"));
                }
                
                @Override
                public void onItemDrop(@NotNull Player player, @NotNull PlayerDropItemEvent ev) {
                    player.sendMessage(Component.text("onItemDrop"));
                }
                
                @Override
                public void onPhysicalClick(@NotNull Player player, @NotNull PlayerInteractEvent ev) {
                    player.sendMessage(Component.text("onPhysicalClick"));
                }
            });
            
            inventory.addItem(builder.build());
        }
        
        inventory.addItem(
                testItem.cloneAs(Key.ofString("testitembuilder_4")).setName(Component.text("Item Without Lore")).clearLore().build()
        );
        
        inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD)
                                  .setName(Component.text("Head Texture Test"))
                                  .setHeadTexture("2a084f78cbd787481eaee173002ac6c081916142b9d9ccc2c3c232cb79c75595")
                                  .asItemStack()
        );
        
        context.assertTestPassed();
    }
    
}