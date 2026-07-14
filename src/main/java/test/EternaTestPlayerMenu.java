package test;

import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.component.ButtonComponents;
import me.hapyl.eterna.module.component.Described;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.menu.ChestSize;
import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import me.hapyl.eterna.module.inventory.menu.PlayerMenuFilter;
import me.hapyl.eterna.module.inventory.menu.PlayerMenuType;
import me.hapyl.eterna.module.inventory.menu.action.HotbarPlayerMenuAction;
import me.hapyl.eterna.module.inventory.menu.action.PlayerMenuAction;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPattern;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPatternApplier;
import me.hapyl.eterna.module.registry.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;

public final class EternaTestPlayerMenu extends EternaTest {
    
    EternaTestPlayerMenu(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final String argument = context.argument(0).toString().toLowerCase();
        
        final PlayerMenuType guiType = switch (argument) {
            case "chest" -> PlayerMenuType.chest(ChestSize.SIZE_6);
            case "dispenser" -> PlayerMenuType.dispenser();
            case "dropper" -> PlayerMenuType.dropper();
            case "workbench" -> PlayerMenuType.workbench();
            case "ender_chest" -> PlayerMenuType.enderChest();
            case "anvil" -> PlayerMenuType.anvil();
            case "hopper" -> PlayerMenuType.hopper();
            case "shulker" -> PlayerMenuType.shulker();
            case "barrel" -> PlayerMenuType.barrel();
            case "crafter" -> PlayerMenuType.crafter();
            default -> null;
        };
        
        if (guiType == null) {
            context.assertTestFailed("Invalid menu type `%s`!".formatted(argument));
            return;
        }
        
        enum TestFilter implements Described {
            LOWERCASE {
                @Override
                @NotNull
                public Component getDescription() {
                    return Component.text("Only filter lowercase strings.");
                }
            },
            
            UPPERCASE {
                @Override
                @NotNull
                public Component getDescription() {
                    return Component.text("Only filter uppercase strings.");
                }
            },
            
            @PlayerMenuFilter.Exclude
            SHOULD_BE_IGNORED {
                @NotNull
                @Override
                public Component getDescription() {
                    return Component.empty();
                }
            }
        }
        
        class TestGUI extends PlayerMenu {
            private final PlayerMenuFilter<String, TestFilter> filter;
            
            public TestGUI(@NotNull Player player, @NotNull PlayerMenuType guiType) {
                super(player, () -> Component.text("Test Menu", TextColor.color(0x0307FC)), guiType);
                
                this.filter = new PlayerMenuFilter<>(TestFilter.class) {
                    @Override
                    public boolean filter(@NotNull String s, @NotNull TestFilter testFilter) {
                        return switch (testFilter) {
                            case LOWERCASE -> s.toLowerCase().equals(s);
                            case UPPERCASE -> s.toUpperCase().equals(s);
                            case SHOULD_BE_IGNORED -> false;
                        };
                    }
                };
            }
            
            @Override
            public void onOpen() {
                setItem(
                        0,
                        new ItemBuilder(Material.DIAMOND)
                                .setName(Component.text("Reopen"))
                                .addLore(Component.text("Click to reopen this menu", EternaColors.GOLD))
                                .asIcon(),
                        PlayerMenuAction.ofMenuUpdate()
                );
                
                setItem(
                        1,
                        new ItemBuilder(Material.LAPIS_LAZULI)
                                .addLore(ButtonComponents.swapOffhand("say hello"))
                                .asIcon(),
                        PlayerMenuAction.builder()
                                        .swapOffhand(player -> {
                                            player.sendMessage(Component.text("Hello!"));
                                        })
                                        .build()
                );
                
                setItem(3,
                        new ItemBuilder(Material.SPAWNER)
                                .setName(Component.text("Secure Action"))
                                .build(),
                        PlayerMenuAction.ofSecure(_player -> {
                            _player.sendMessage(Component.text("Clicked securely!"));
                        })
                );
                
                setItem(
                        4,
                        new ItemBuilder(Material.MINECART)
                                .setName(Component.text("Click via number!"))
                                .asIcon(),
                        new HotbarPlayerMenuAction() {
                            @Override
                            public void use(@NotNull Player player, @Range(from = 0, to = 8) int hotbarNumber) {
                                player.sendMessage(Component.text("You clicked at %s slot!".formatted(hotbarNumber + 1), NamedTextColor.GREEN));
                            }
                            
                            @Override
                            public void useNonHotbar(@NotNull Player player) {
                                player.sendMessage(Component.text("I said use hotbar >:(", NamedTextColor.RED));
                            }
                        }
                );
                
                filter.apply(
                        this,
                        5,
                        List.of("Hello", "world", "MY", "Name", "IS", "hapyl", "MORECAPS"),
                        filtered -> {
                            int slot = 37;
                            
                            for (String value : filtered) {
                                setItem(
                                        slot++, new ItemBuilder(Material.GRASS_BLOCK)
                                                .setName(Component.text(value))
                                                .build()
                                );
                            }
                        }
                );
                
                final SlotPattern slotPattern = context.argument(1)
                                                       .toStaticConstant(SlotPattern.class, SlotPattern.class)
                                                       .orElse(SlotPattern.DEFAULT);
                
                final SlotPatternApplier applier = newSlotPatternApplier(slotPattern, ChestSize.SIZE_2, ChestSize.SIZE_4);
                
                for (int i = 0; i < 99; i++) {
                    final int finalI = i;
                    
                    applier.add(
                            new ItemBuilder(Material.COPPER_INGOT)
                                    .setAmount(i + 1)
                                    .build(),
                            PlayerMenuAction.of(player -> {
                                player.sendMessage(Component.text("You clicked %s item!".formatted(finalI + 1)));
                            })
                    );
                }
                
                applier.apply();
                
                setReturnButton(Component.text("Nowhere"), _player -> new PlayerMenu(_player, () -> Component.text("Nowhere"), PlayerMenuType.crafter()) {
                    @Override
                    public void onOpen() {
                    }
                });
            }
            
            @Override
            public void onClose() {
                context.info(Component.text("Closed Menu!"));
                context.assertTestPassed();
            }
            
        }
        
        final TestGUI testGui = new TestGUI(context.player(), guiType);
        testGui.setCooldownSeconds(0.5f);
        testGui.openMenu();
    }
}
