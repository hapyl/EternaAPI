package test;

import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.menu.ChestSize;
import me.hapyl.eterna.module.inventory.menu.PlayerMenuTitle;
import me.hapyl.eterna.module.inventory.menu.PlayerPageMenu;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPattern;
import me.hapyl.eterna.module.registry.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class EternaTestPlayerMenuPage extends EternaTest {
    
    EternaTestPlayerMenuPage(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final TestPlayerMenuPage menu = new TestPlayerMenuPage(context.player(), context);
        
        menu.setPattern(SlotPattern.CHUNKY);
        menu.setFrom(ChestSize.SIZE_2);
        menu.setTo(ChestSize.SIZE_3);
        
        menu.setContents(List.of(
                "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
                "adipiscing", "elit", "sed", "do", "eiusmod", "tempor",
                "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua",
                "apple", "banana", "pineapple", "vodka", "tomato", "carrot", "potato"
        ));
        menu.openMenu();
    }
    
    private static class TestPlayerMenuPage extends PlayerPageMenu<String> {
        private final @NotNull EternaTestContext context;
        
        public TestPlayerMenuPage(Player player, @NotNull EternaTestContext context) {
            super(player, PlayerMenuTitle.create(Component.text("This"), Component.text("That"), Component.text("Another")), ChestSize.SIZE_4);
            this.context = context;
        }
        
        @Override
        @NotNull
        public ItemStack asItem(@NotNull Player player, @NotNull String content, int index, int page) {
            return new ItemBuilder(Material.STONE)
                    .setName(Component.text(content))
                    .setAmount(index + 1 + ((page - 1) * getMaximumItemsPerPage()))
                    .asItemStack();
        }
        
        @Override
        public void onClick(@NotNull Player player, @NotNull String content, int index, int page, @NotNull org.bukkit.event.inventory.ClickType clickType) {
            player.sendMessage(Component.text("You clicked ", NamedTextColor.GRAY).append(Component.text(content, NamedTextColor.YELLOW)));
            player.sendMessage(Component.text("Using ", NamedTextColor.GRAY).append(Component.text(clickType.name(), NamedTextColor.YELLOW)));
        }
        
        @Override
        public void onClose() {
            context.assertTestPassed();
        }
    }
}