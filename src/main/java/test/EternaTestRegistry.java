package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.persistence.PersistentDataContainerView;
import me.hapyl.eterna.*;
import me.hapyl.eterna.module.block.BlockOutline;
import me.hapyl.eterna.module.block.display.BDEngine;
import me.hapyl.eterna.module.block.display.DisplayData;
import me.hapyl.eterna.module.block.display.DisplayEntity;
import me.hapyl.eterna.module.block.display.animation.AnimationFrame;
import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.component.Described;
import me.hapyl.eterna.module.component.Keybind;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.entity.packet.PacketBlockDisplay;
import me.hapyl.eterna.module.entity.packet.PacketGuardian;
import me.hapyl.eterna.module.entity.packet.PacketItem;
import me.hapyl.eterna.module.entity.packet.PacketSquid;
import me.hapyl.eterna.module.entity.rope.Rope;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.hologram.HologramImplTextDisplay;
import me.hapyl.eterna.module.inventory.Equipment;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.builder.ItemEventListener;
import me.hapyl.eterna.module.inventory.builder.ItemFunction;
import me.hapyl.eterna.module.inventory.menu.*;
import me.hapyl.eterna.module.inventory.menu.action.HotbarPlayerMenuAction;
import me.hapyl.eterna.module.inventory.menu.action.PlayerMenuAction;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPattern;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPatternApplier;
import me.hapyl.eterna.module.inventory.sign.SignInput;
import me.hapyl.eterna.module.inventory.sign.SignResponse;
import me.hapyl.eterna.module.inventory.sign.SignType;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.location.Position;
import me.hapyl.eterna.module.math.Tick;
import me.hapyl.eterna.module.math.geometry.*;
import me.hapyl.eterna.module.npc.*;
import me.hapyl.eterna.module.npc.appearance.*;
import me.hapyl.eterna.module.npc.tag.TagLayout;
import me.hapyl.eterna.module.npc.tag.TagPart;
import me.hapyl.eterna.module.parkour.Parkour;
import me.hapyl.eterna.module.parkour.ParkourPosition;
import me.hapyl.eterna.module.particle.ParticleBuilder;
import me.hapyl.eterna.module.player.PlayerOutline;
import me.hapyl.eterna.module.player.PlayerSkin;
import me.hapyl.eterna.module.player.ScoreboardBuilder;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntry;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntryOptions;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntryText;
import me.hapyl.eterna.module.player.dialog.entry.OptionIndex;
import me.hapyl.eterna.module.player.input.DefaultInputKey;
import me.hapyl.eterna.module.player.input.PlayerInput;
import me.hapyl.eterna.module.player.quest.*;
import me.hapyl.eterna.module.player.quest.objective.QuestObjectiveJump;
import me.hapyl.eterna.module.player.sequencer.Sequencer;
import me.hapyl.eterna.module.player.sequencer.Track;
import me.hapyl.eterna.module.player.tablist.*;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.reflect.access.ObjectInstance;
import me.hapyl.eterna.module.reflect.access.ReflectAccess;
import me.hapyl.eterna.module.reflect.access.ReflectFieldAccess;
import me.hapyl.eterna.module.reflect.glowing.Glowing;
import me.hapyl.eterna.module.reflect.team.PacketTeamColor;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import me.hapyl.eterna.module.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApiStatus.Internal
public final class EternaTestRegistry {
    
    private static final Map<Key, EternaTest> theRegistry;
    
    private static final EternaPlugin thePlugin;
    private static final Random theRandom;
    
    static {
        EternaLogger.info("Tests instantiating...");
        final long startMillis = System.currentTimeMillis();
        
        theRegistry = Maps.newLinkedHashMap();
        
        thePlugin = Eterna.getPlugin();
        theRandom = new Random();
        
        // *-* Start Tests *-* //
        
        register(
                "compute", context -> {
                    final Map<String, Integer> intMap = Maps.newHashMap();
                    
                    intMap.compute("hapyl", Compute.intAdd(69));
                    intMap.compute("didenpro", Compute.intAdd(420));
                    
                    final Integer valueH = intMap.get("hapyl");
                    final Integer valueD = intMap.get("didenpro");
                    
                    context.assertEquals(valueH, 69);
                    context.assertEquals(valueD, 420);
                    
                    intMap.clear();
                    
                    // Test enums
                    final Map<String, List<Material>> enumMap = Maps.newHashMap();
                    
                    enumMap.compute("stone", Compute.listAdd(Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE));
                    enumMap.compute("dirt", Compute.listAdd(Material.GRASS_BLOCK, Material.DIRT, Material.DIRT_PATH, Material.PODZOL));
                    enumMap.compute("dirt", Compute.listRemove(Material.DIRT_PATH));
                    
                    final List<Material> materialsStone = enumMap.get("stone");
                    final List<Material> materialsDirt = enumMap.get("dirt");
                    
                    context.assertEquals(materialsStone, List.of(Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE));
                    context.assertEquals(materialsDirt, List.of(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL));
                    
                    context.assertTestPassed();
                }
        );
        
        register(
                "glowing", context -> {
                    final Player player = context.player();
                    final Player glowingPlayer = context.argument(0).toPlayer();
                    
                    if (glowingPlayer == null) {
                        context.info(Component.text("No player named %s, using a piggy instead!".formatted(context.argument(0).toString())));
                    }
                    
                    final Entity entity = glowingPlayer != null ? glowingPlayer : Entities.PIG.spawn(
                            player.getLocation(), self -> {
                                self.setInvisible(true);
                                self.setAI(false);
                            }
                    );
                    
                    final PacketTeamColor glowingColor = PacketTeamColor.YELLOW;
                    
                    final int glowingTime = 200;
                    final int colorChangeTime = glowingTime / 2;
                    
                    context.info(Component.text("Set glowing for %s.".formatted(Tick.round(glowingTime))));
                    Glowing.setGlowing(player, entity, glowingColor, glowingTime);
                    
                    context.scheduler()
                           .then(SchedulerTask.later(() -> {
                               PacketTeamColor newColor;
                               
                               do {
                                   newColor = PacketTeamColor.random();
                               }
                               while (newColor == glowingColor);
                               
                               Glowing.setGlowing(player, entity, newColor);
                               context.info(Component.text("Update color to `%s`!".formatted(newColor)));
                           }, colorChangeTime))
                           .then(SchedulerTask.later(() -> {
                               if (glowingPlayer == null) {
                                   entity.remove();
                               }
                               
                               context.assertTestPassed();
                           }, colorChangeTime))
                           .execute();
                }
        );
        
        register(
                "hologram", context -> {
                    final Player player = context.player();
                    final Location location = player.getLocation().add(0, 1, 0);
                    
                    final String hologramType = context.argument(0).toString().toLowerCase();
                    final Hologram hologram;
                    
                    if (hologramType.equalsIgnoreCase("text_display")) {
                        hologram = Hologram.ofTextDisplay(location);
                    }
                    else if (hologramType.equalsIgnoreCase("armor_stand")) {
                        hologram = Hologram.ofArmorStand(location);
                    }
                    else {
                        context.assertTestFailed("Invalid argument, must be either `text_display` or `armor_stand`, not `%s`".formatted(hologramType));
                        return;
                    }
                    
                    hologram.setLines(_player -> ComponentList.empty().append(Component.keybind(Keybind.SPRINT)));
                    hologram.showAll();
                    
                    context.info(Component.text("Created `%s` hologram!".formatted(hologram)));
                    
                    context.scheduler()
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Expanded"));
                               
                               hologram.setLines(_player -> ComponentList.of(
                                       Component.text()
                                                .append(Component.text(12345678, NamedTextColor.BLUE))
                                                .append(Component.text(" Hello World", NamedTextColor.RED))
                                                .build(),
                                       Component.text(123),
                                       null,
                                       Component.text("No?")
                               ));
                               
                               if (hologram instanceof HologramImplTextDisplay textDisplay) {
                                   textDisplay.background(org.bukkit.Color.fromARGB(100, 0, 0, 0));
                               }
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Shrunk"));
                               
                               hologram.setLines(_player -> ComponentList.of(
                                       Component.text("Hello", NamedTextColor.RED),
                                       Component.text("World", NamedTextColor.BLUE)
                               ));
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Per-player lines"));
                               
                               hologram.setLines(_player -> {
                                   final String playerName = _player.getName();
                                   final ComponentList list = ComponentList.empty().append(Component.text("Your name is %s".formatted(playerName), NamedTextColor.GREEN));
                                   
                                   if (playerName.equalsIgnoreCase("hapyl")) {
                                       list.append(Component.text("Extra line just for you!", NamedTextColor.GOLD));
                                   }
                                   
                                   return list;
                               });
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               hologram.dispose();
                               context.assertTestPassed();
                           }, 20))
                           .execute();
                }
        );
        
        register(
                "item_builder", context -> {
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
        );
        
        register(
                "player_menu", context -> {
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
                            super(player, Component.text("Test Menu", TextColor.color(0x0307FC)), guiType);
                            
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
                                            .setName(Component.text("Say hello"))
                                            .asIcon(),
                                    PlayerMenuAction.of(player -> {
                                        player.sendMessage(Component.text("Hello!"));
                                        this.openMenu();
                                    })
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
                        }
                        
                        @Override
                        public void onClose() {
                            context.info(Component.text("Closed Menu!"));
                        }
                        
                    }
                    
                    final TestGUI testGui = new TestGUI(context.player(), guiType);
                    testGui.setCooldownSec(0.5f);
                    testGui.openMenu();
                }
        );
        
        register(
                "player_page_menu", context -> {
                    class TestGUI extends PlayerPageMenu<String> {
                        public TestGUI(Player player) {
                            super(player, Component.text("gui_page"), ChestSize.SIZE_4);
                        }
                        
                        @Override
                        @NotNull
                        public ItemStack asItem(@NotNull Player player, @NotNull String content, int index, int page) {
                            return new ItemBuilder(Material.STONE)
                                    .setName(Component.text(content))
                                    .setAmount((index + 1) + ((page - 1) * getMaximumItemsPerPage()))
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
                    
                    final TestGUI testGui = new TestGUI(context.player());
                    
                    testGui.setPattern(SlotPattern.CHUNKY);
                    testGui.setFrom(ChestSize.SIZE_2);
                    testGui.setTo(ChestSize.SIZE_3);
                    
                    testGui.setContents(List.of(
                            "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
                            "adipiscing", "elit", "sed", "do", "eiusmod", "tempor",
                            "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua",
                            "apple", "banana", "pineapple", "vodka", "tomato", "carrot", "potato"
                    ));
                    testGui.openMenu();
                }
        );
        
        register(
                "sign_gui", context -> new SignInput(context.player(), Enums.getRandomValueOrFirst(SignType.class), "Is this a test?") {
                    @Override
                    public void onResponse(@NotNull SignResponse response) {
                        final String firstLine = response.get(0).toString();
                        final int secondLine = response.get(1).toInt();
                        
                        try {
                            context.assertEquals(firstLine, "Yes");
                            context.assertEquals(secondLine, 123456789);
                            
                            context.assertTestPassed();
                        }
                        catch (EternaTestException ex) {
                            context.assertTestFailed(ex.getMessage());
                        }
                    }
                }
        );
        
        register(
                "tablist", context -> {
                    final Tablist tablist = new Tablist(context.player());
                    
                    tablist.show();
                    
                    new BukkitRunnable() {
                        private int iterations = 5;
                        
                        @Override
                        public void run() {
                            if (iterations-- <= 0) {
                                context.assertTestPassed();
                                tablist.dispose();
                                
                                cancel();
                                return;
                            }
                            
                            final List<Player> players = Lists.newArrayList(Bukkit.getOnlinePlayers());
                            
                            final EntryList entryListPlayers = EntryList.ofEmpty();
                            entryListPlayers.append(Component.text("Players:", NamedTextColor.GREEN));
                            entryListPlayers.append(Component.empty());
                            
                            players.forEach(player -> {
                                entryListPlayers.append(player.name(), EntryTexture.of(player));
                            });
                            
                            entryListPlayers.append(
                                    Component.text(theRandom.nextDouble() * 1000), EntryTexture.random(), Enums.getRandomValue(PingBars.class)
                            );
                            
                            final EntryList entryListColors = EntryList.ofEmpty();
                            
                            for (NamedTextColor color : NamedTextColor.NAMES.values()) {
                                entryListColors.append(Component.text(color.toString()), EntryTexture.of(color));
                            }
                            
                            tablist.setColumn(TablistColumn.FIRST, entryListPlayers);
                            tablist.setColumn(TablistColumn.SECOND, entryListColors);
                            
                            tablist.setColumn(
                                    TablistColumn.THIRD,
                                    Component.text("Frinds:", NamedTextColor.DARK_GREEN),
                                    Component.text("Friends? What friends?", NamedTextColor.DARK_GRAY)
                            );
                            
                            int pingIndex = 0;
                            
                            for (PingBars ping : PingBars.values()) {
                                final TablistEntry entry = tablist.getEntry(TablistColumn.THIRD, pingIndex++);
                                
                                entry.setPing(ping);
                            }
                            
                        }
                    }.runTaskTimer(Eterna.getPlugin(), 0, 20);
                    
                    tablist.setColumn(
                            TablistColumn.FOURTH,
                            Component.text("HELLO WORLD", NamedTextColor.YELLOW, TextDecoration.BOLD),
                            Component.text("This is a test"),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.empty(),
                            Component.text("Third from last..."),
                            Component.text("Almost last one..."),
                            Component.text("Last one!!!", NamedTextColor.GREEN)
                    );
                }
        );
        
        register(
                "outline", context -> {
                    final PlayerOutline border = new PlayerOutline(context.player());
                    
                    context.info(Component.text("Set color to RED"));
                    border.setColor(PlayerOutline.Color.RED, 5);
                    
                    context.scheduler()
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Set color to GREEN"));
                               border.setColor(PlayerOutline.Color.GREEN, 5);
                           }, 60))
                           .then(SchedulerTask.later(() -> {
                               context.assertTestPassed();
                               border.dispose();
                           }, 60))
                           .execute();
                }
        );
        
        register(
                "rope", context -> {
                    final Rope rope = new Rope();
                    final Player player = context.player();
                    final Location location = player.getLocation();
                    
                    rope.addPoint(LocationHelper.copyOf(location).add(3, 2, 4));
                    rope.addPoint(LocationHelper.copyOf(location).add(1, 5, 7));
                    rope.addPoint(LocationHelper.copyOf(location).add(9, 6, 2));
                    rope.addPoint(LocationHelper.copyOf(location).add(-2, 2, -4));
                    rope.addPoint(LocationHelper.copyOf(location).add(3, 1.5, -6));
                    
                    context.info(Component.text("Created rope"));
                    rope.show(player);
                    
                    context.scheduler()
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Removed rope"));
                               context.assertTestPassed();
                               
                               rope.dispose();
                           }, 60))
                           .execute();
                }
        );
        
        register(
                "bd_engine", context -> {
                    class Holder {
                        static final String stringDataModel =
                                "/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-112185605,1447459445,769677829,553711541],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOWQ2OTViYjM3YjU5MzdhOTU5NjNkYWIyYmJjMmE2ZTFjZDhhMmEyNTY4MWUyOTkxNTQ5YzYxYzFkMzNkYyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.9988f,-0.0467f,-0.0161f,-0.0298f,0.0479f,0.9957f,0.0795f,2.1254f,0.0124f,-0.0801f,0.9967f,-0.0544f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:hay_block\",Properties:{axis:\"x\"}},transformation:[-0.0387f,-0.7242f,-0.004f,0.3959f,0.7944f,-0.0354f,0.0467f,0.7848f,-0.0492f,-0.0017f,0.7561f,-0.3526f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:spruce_fence\",Properties:{}},transformation:[1f,0f,0f,-0.5f,0f,1f,0f,0.1408f,0f,0f,1f,-0.5f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:spruce_fence\",Properties:{}},transformation:[0.7254f,-0.3659f,0.0056f,0.2724f,0.3659f,0.7253f,-0.0129f,0.594f,0.0008f,0.0141f,0.8124f,-0.4449f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:spruce_fence\",Properties:{}},transformation:[0.7687f,0.2631f,-0.003f,-0.935f,-0.2631f,0.7686f,-0.0145f,0.7246f,-0.0019f,0.0147f,0.8124f,-0.4576f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:stone_sword\",Count:1},item_display:\"none\",transformation:[-0.2806f,0.1365f,0.9155f,0.733f,0.2558f,-0.6866f,0.3033f,0.5198f,0.6787f,0.3153f,0.2642f,-0.1822f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[-0.0058f,-0.0201f,0.6249f,-0.2938f,-0.8748f,0.0186f,-0.0039f,0.9458f,-0.0162f,-0.9996f,-0.0126f,0.5336f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.0162f,0.9996f,0.0126f,-0.5085f,-0.8748f,0.0186f,-0.0039f,0.9458f,-0.0058f,-0.0201f,0.6249f,-0.2801f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.0074f,0.6927f,0.4508f,-0.5697f,-0.8748f,0.0186f,-0.0039f,0.9458f,-0.0155f,-0.721f,0.4329f,0.1712f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.0155f,0.721f,-0.4329f,-0.1461f,-0.8748f,0.0186f,-0.0039f,0.9458f,0.0074f,0.6927f,0.4508f,-0.556f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite_slab\",Properties:{type:\"bottom\"}},transformation:[0.5625f,0f,0f,-0.2819f,0f,0.375f,0f,0f,0f,0f,0.5625f,-0.2762f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.7876f,0.2564f,-0.2201f,-0.3989f,-0.1163f,0.9315f,0.2117f,0.4491f,0.363f,-0.258f,0.5453f,-0.3401f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.8511f,0.057f,0.1405f,-0.195f,-0.1163f,0.9315f,0.2117f,0.4491f,-0.1663f,-0.3593f,0.571f,-0.1846f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:feather\",Count:1},item_display:\"none\",transformation:[0.0204f,-0.0241f,-0.6258f,-0.045f,-0.5291f,-0.0608f,-0.0207f,2.245f,-0.0594f,0.5335f,-0.0307f,0.1325f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.6952f,0.4904f,0.2238f,-0.5172f,-0.5047f,0.794f,0.1201f,0.9203f,-0.1663f,-0.3593f,0.571f,-0.1846f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.8084f,0.2494f,0.1814f,-0.6402f,-0.2907f,0.8993f,0.1778f,0.8937f,-0.1663f,-0.3593f,0.571f,-0.1083f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:stripped_dark_oak_wood\",Properties:{axis:\"x\"}},transformation:[0.5661f,-0.0038f,-0.0032f,-0.2918f,0.0266f,0.081f,0.0407f,1.509f,0.0014f,-0.0063f,0.5262f,-0.28f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.0938f,-0.1597f,0.6133f,-0.0863f,-0.5047f,0.794f,0.1201f,0.9203f,-0.7086f,-0.5866f,-0.0043f,0.7316f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.7666f,0.1794f,0.2797f,-0.6635f,-0.2508f,0.9401f,0.1152f,0.7228f,-0.3393f,-0.2898f,0.5469f,0.1229f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[-0.5354f,-0.5195f,-0.3727f,0.7857f,-0.649f,0.6387f,0.1279f,1.265f,0.2403f,0.5676f,-0.4851f,-0.0795f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:arrow\",Count:1},item_display:\"none\",transformation:[0.1374f,0.4567f,0.3432f,-0.1544f,0.4634f,0.0728f,-0.253f,1.4181f,-0.2211f,0.4365f,-0.317f,-0.5088f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:hay_block\",Properties:{axis:\"x\"}},transformation:[0.1587f,0.2108f,0.0047f,0.1285f,-0.1952f,0.1714f,-0.0008f,1.3998f,-0.0031f,-0.003f,0.2875f,-0.165f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:hay_block\",Properties:{axis:\"x\"}},transformation:[-0.1258f,0.2353f,0.001f,-0.3547f,-0.2179f,-0.1358f,-0.0047f,1.5668f,-0.0031f,-0.003f,0.2875f,-0.1721f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:barrel\",Properties:{facing:\"down\",open:\"false\"}},transformation:[0.3801f,-0.1486f,0.0364f,-0.7891f,-0.0564f,0.9898f,0.0128f,0.2349f,-0.4524f,-0.2484f,0.029f,0.1687f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:infested_stone\",Properties:{}},transformation:[0.4263f,-0.1623f,0.0312f,-0.8f,-0.0633f,1.0809f,0.0109f,0.1803f,-0.5074f,-0.2712f,0.0249f,0.2188f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone_slab\",Properties:{type:\"bottom\"}},transformation:[-0.0659f,-0.2376f,0.1102f,-0.5366f,0.417f,0.031f,0.0386f,0.4636f,-0.1005f,0.2847f,0.0878f,-0.1895f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:arrow\",Count:1},item_display:\"none\",transformation:[0.1374f,-0.2417f,0.4718f,0.3482f,0.4634f,0.3101f,-0.0239f,1.2136f,-0.2211f,0.4998f,0.2431f,-0.5003f,0f,0f,0f,1f]}]}";
                        
                        static final String stringDataText =
                                "/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[{id:\"minecraft:text_display\",text:'[{\"text\":\"this is a test\",\"color\":\"#ccaacc\",\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"font\":\"minecraft:uniform\"}]',text_opacity:255,background:-16777216,alignment:\"center\",line_width:210,default_background:false,transformation:[1f,0f,0f,0f,0f,1f,0f,0.5f,0f,0f,1f,0f,0f,0f,0f,1f]}]}";
                        
                        static final String stringDataIllegal
                                = "{invalid_data: false, null: true, 123}";
                    }
                    
                    final DisplayData displayDataModel = BDEngine.parse(Holder.stringDataModel);
                    final DisplayData displayDataText = BDEngine.parse(Holder.stringDataText);
                    
                    final Location location = context.player().getLocation();
                    
                    final DisplayEntity displayEntityModel = displayDataModel.spawn(location);
                    final DisplayEntity displayEntityText = displayDataText.spawn(location.add(0, 2, 0));
                    
                    context.assertThrows(
                            () -> BDEngine.parse(Holder.stringDataIllegal), "Parser should have thrown for illegal data!"
                    );
                    
                    context.scheduler()
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Teleported"));
                               
                               final Location newLocation = context.player().getLocation();
                               
                               displayEntityModel.teleport(newLocation);
                               displayEntityText.teleport(newLocation);
                           }, 60))
                           .then(SchedulerTask.later(() -> {
                               displayEntityModel.remove();
                               displayEntityText.remove();
                               
                               context.assertTestPassed();
                           }, 60))
                           .execute();
                }
        );
        
        register(
                "particle_builder", context -> {
                    final Player player = context.player();
                    final Location location = player.getLocation();
                    
                    context.scheduler()
                           .then(SchedulerTask.run(() -> {
                               context.info(Component.text("particle"));
                               ParticleBuilder.particle(Particle.ENCHANT).display(location);
                           }))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("effect"));
                               ParticleBuilder.effect(org.bukkit.Color.fromRGB(0, 255, 0), 0).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("instantEffect"));
                               ParticleBuilder.instantEffect(org.bukkit.Color.fromRGB(30, 39, 227), 0).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("entityEffect"));
                               ParticleBuilder.entityEffect(org.bukkit.Color.fromRGB(35, 46, 124)).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("dust"));
                               ParticleBuilder.dust(org.bukkit.Color.fromRGB(89, 127, 6), 2).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("item"));
                               ParticleBuilder.item(new ItemStack(Material.DIAMOND_AXE)).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("block"));
                               ParticleBuilder.block(Material.LAPIS_BLOCK).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("dragonBreath"));
                               ParticleBuilder.dragonBreath(2).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("fallingDust"));
                               ParticleBuilder.fallingDust(Material.BRICKS).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("flash"));
                               ParticleBuilder.flash(org.bukkit.Color.fromRGB(1, 2, 3)).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("dustColorTransition"));
                               ParticleBuilder.dustColorTransition(org.bukkit.Color.fromRGB(1, 2, 3), org.bukkit.Color.fromRGB(200, 100, 50), 1).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("vibration"));
                               ParticleBuilder.vibration(player.getLocation().add(4, 2, -5), 100).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("sculkCharge"));
                               ParticleBuilder.sculkCharge((float) Math.toRadians(90)).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("tintedLeaves"));
                               ParticleBuilder.tintedLeaves(org.bukkit.Color.fromRGB(69, 69, 69)).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("dustPillar"));
                               ParticleBuilder.dustPillar(Material.GOLD_BLOCK).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("blockCrumble"));
                               ParticleBuilder.blockCrumble(Material.DIAMOND_BLOCK).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("blockCrumble"));
                               ParticleBuilder.trail(player.getLocation().add(3, -5, 6), org.bukkit.Color.fromRGB(55, 105, 205), 100).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("blockMarker"));
                               ParticleBuilder.blockMarker(Material.SEA_LANTERN).display(location);
                           }, 10))
                           .then(SchedulerTask.later(() -> {
                               context.assertThrows(() -> ParticleBuilder.block(Material.DIAMOND));
                               context.assertThrows(() -> ParticleBuilder.fallingDust(Material.GOLD_NUGGET));
                               context.assertThrows(() -> ParticleBuilder.blockMarker(Material.EMERALD));
                               
                               context.assertTestPassed();
                           }, 10))
                           .execute()
                           .exceptionally(ex -> {
                               context.assertTestFailed(ex);
                               return null;
                           });
                    
                }
        );
        
        register(
                "player_skin", context -> {
                    class Holder {
                        static final PlayerSkin testSkin = PlayerSkin.of(
                                "ewogICJ0aW1lc3RhbXAiIDogMTcxNjU2NDQ4MzU5NSwKICAicHJvZmlsZUlkIiA6ICIyZGI4ZTYzYzFlMTc0NGE3ODIyZDNmNjBlYmNmYzI5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBcmtTYW5kYm94IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRjYTBkZGI2YTM0MWYyZmJhNDU5NWNiNmZlMWUzYmYxZTk2NGFmZDVjMDExZWU3ZDMxM2JiMDllNDZhNTIzMGIiCiAgICB9CiAgfQp9",
                                "QXdm98wIhaFJs4JeCFiy//EPfkyuHs2Ya7fH4uVnqlQ157b7p9z7cY1vKrGPoTZzTuRbXD6nNFPKwNjgFWyo+gUAW5kuoB0ZcaSquOl+Tvoi9w8z9jaG3xEIOhHxwf+5P6rJsP3blEFBko3invkjKreIUScugJXF0oWV+sY230jZ/uQZJ2faQBx0c6Ls8ISGsS2n3Vmr+rUs0TUo2vh6xQF+ZlQGyHg2rBAvjoeguD8x0hyqACzW3FTp8cy1hSMQc0NNWZ5x0aBIrJ4NTmOdBgbQdeZ1VYKmpQjgSTSMEmW4NUDWuSugekTWR+OsySaw0cvT9h2X0DSCrYjm5QiyB6O0tG4EIAweGR9kVOHZ6ib9bVIhuFy3BjKuW8TtbeBEBiw4t3ZAjSUgPHB8pXYzReZa4dqgVqX6cho7cF71asx8ZO6K12aS9dSs1SU7+Jp7WuIiVA7bsyCkoUJJD6DwQGBN+mp4s+pQbkpb78ZcojGm7Be0hvDm9trqSLOLZLpKLp7YxR/wHGKdQ1s86SLaMubwGmuD0RpjNW+EoSANYNMPoq3rpIk/CVVfJSR90Sx1S7vvIV5BkgiBDWcTSN7hbxAldRnB2y3gCgB4+TC4geZFdWVCOYZgMZTNBS2WotO/h619cZoEcKMFol//kB4DtvaRGxc9P6Y4Vp/C3mMrWnU="
                        );
                    }
                    
                    final Player player = context.player();
                    final PlayerSkin playerSkin = PlayerSkin.of(player);
                    
                    context.scheduler()
                           .then(SchedulerTask.run(() -> {
                               Holder.testSkin.apply(player);
                               
                               context.info(Component.text("Applied skin"));
                           }))
                           .then(SchedulerTask.later(() -> {
                               playerSkin.apply(player);
                               
                               context.info(Component.text("Reset skin"));
                               context.assertTestPassed();
                           }, 100))
                           .execute();
                }
        );
        
        register(
                "hide_components", context -> {
                    final ItemStack itemHideComponentsAll = new ItemBuilder(Material.IRON_PICKAXE)
                            .setName(Component.text("Hides all components"))
                            .hideComponents()
                            .build();
                    
                    @SuppressWarnings("UnstableApiUsage") final ItemStack itemHideComponentsEnchants = new ItemBuilder(Material.BLACK_BANNER)
                            .setName(Component.text("Enchants should not show, banner patterns should"))
                            .hideComponents(DataComponentTypes.ENCHANTMENTS)
                            .addEnchant(Enchantment.UNBREAKING, 1)
                            .addBannerPattern(PatternType.CIRCLE, DyeColor.YELLOW)
                            .build();
                    
                    final ItemStack itemHideComponentsAllButMojangCantFuckingCode = new ItemBuilder(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)
                            .setName(Component.text("Check whether mojang fucking this shit", NamedTextColor.GREEN))
                            .addLore(Component.text("Surprise! They haven't.", NamedTextColor.DARK_GREEN))
                            .build();
                    
                    final PlayerInventory inventory = context.player().getInventory();
                    
                    inventory.addItem(itemHideComponentsAll);
                    inventory.addItem(itemHideComponentsEnchants);
                    inventory.addItem(itemHideComponentsAllButMojangCantFuckingCode);
                }
        );
        
        register(
                "item_components", context -> {
                    @SuppressWarnings("UnstableApiUsage") final ItemBuilder itemFood = new ItemBuilder(Material.DIAMOND_SWORD)
                            .setName(Component.text("Food"))
                            .setFood(food -> {
                                food.setCanAlwaysEat(true);
                                food.setSaturation(100);
                                food.setNutrition(100);
                            });
                    
                    @SuppressWarnings("UnstableApiUsage") final ItemBuilder itemTool = new ItemBuilder(Material.IRON_INGOT)
                            .setName(Component.text("Tool"))
                            .addLore(Component.text("Breaks glass fast", NamedTextColor.AQUA))
                            .setTool(tool -> {
                                tool.setDamagePerBlock(1);
                                tool.setDefaultMiningSpeed(1.5f);
                                
                                tool.addRule(Material.GLASS, 5f, true);
                            });
                    
                    @SuppressWarnings("UnstableApiUsage") final ItemBuilder itemEquippable = new ItemBuilder(Material.MAGENTA_BANNER)
                            .setName(Component.text("Equippable"))
                            .setEquippable(equippable -> {
                                equippable.setSlot(EquipmentSlot.HEAD);
                                equippable.setEquipSound(Sound.ENTITY_BAT_DEATH);
                            });
                    
                    final PlayerInventory inventory = context.player().getInventory();
                    
                    inventory.addItem(itemFood.build());
                    inventory.addItem(itemTool.build());
                    inventory.addItem(itemEquippable.build());
                }
        );
        
        register(
                "display_animation", context -> {
                    final DisplayData model = BDEngine.parse(
                            "/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:andesite\",Properties:{}},transformation:[1f,0f,0f,0f,0f,1f,0f,0f,0f,0f,1f,0f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:bamboo_block\",Properties:{axis:\"x\"}},transformation:[1f,0f,0f,-0.5625f,0f,1f,0f,1f,0f,0f,1f,0f,0f,0f,0f,1f]}]}"
                    );
                    
                    final Location location = context.player().getLocation();
                    final DisplayEntity entity = model.spawnInterpolated(location);
                    
                    entity.newAnimation()
                          .addFrame(new AnimationFrame(Math.PI * 2, Math.PI / 8) {
                              @Override
                              public void tick(@NotNull DisplayEntity entity, double theta) {
                                  final double y = Math.cos(theta) * 0.25;
                                  
                                  entity.teleport(entity.getLocation().add(0, y, 0));
                              }
                          })
                          .addFrame(new AnimationFrame(Math.PI, Math.PI / 4) {
                              @Override
                              public void tick(@NotNull DisplayEntity entity, double theta) {
                                  final double x = Math.sin(theta) * 1.5;
                                  final double z = Math.cos(theta) * 1.5;
                                  
                                  entity.teleport(entity.getLocation().add(x, 0, z));
                              }
                          })
                          .addFrame(AnimationFrame.builder((frame, slf, theta) -> slf.teleport(slf.getLocation().add(0, 1, 0)))
                                                  .threshold(3)
                                                  .increment(Math.PI / 2)
                          ).addFrame(new AnimationFrame() {
                              @Override
                              public void tick(@NotNull DisplayEntity entity, double theta) {
                                  entity.teleport(location);
                                  
                                  context.scheduler()
                                         .then(SchedulerTask.later(() -> {
                                             entity.remove();
                                             context.assertTestPassed();
                                         }, 20))
                                         .execute();
                              }
                          })
                          .start();
                }
        );
        
        register(
                "quest", context -> {
                    class Holder {
                        static final QuestRegistry registry;
                        static final Quest quest;
                        
                        static final Npc npc;
                        
                        static final Dialog dialog1;
                        static final Dialog dialog2;
                        
                        static final ItemStack itemToGive;
                        
                        @Nullable static Set<QuestData> cachedQuestData;
                        
                        static {
                            registry = new QuestRegistry(Eterna.getPlugin()) {
                                @Override
                                public void save(@NotNull Player player, @NotNull Set<QuestData> questData) {
                                    cachedQuestData = questData;
                                    
                                    EternaLogger.debug("Saved quest data: " + CollectionUtils.wrapToString(questData));
                                }
                                
                                @NotNull
                                @Override
                                public Set<QuestData> load(@NotNull Player player) {
                                    if (cachedQuestData != null) {
                                        return cachedQuestData.stream()
                                                              // Explicitly call QuestData.load()
                                                              .map(questData -> QuestData.load(
                                                                      player,
                                                                      questData.getQuest(),
                                                                      questData.getCurrentStage(),
                                                                      questData.getCurrentStageProgress(),
                                                                      questData.getStartedAt(),
                                                                      questData.getCompletedAt())
                                                              )
                                                              .collect(Collectors.toSet());
                                    }
                                    
                                    return Set.of();
                                }
                            };
                            
                            npc = new Npc(
                                    LocationHelper.defaultLocation(-17, -57, -1),
                                    Component.text("Herobrine"),
                                    AppearanceBuilder.ofMannequin(Skin.of(
                                            "ewogICJ0aW1lc3RhbXAiIDogMTczODEwMDAwNzExMCwKICAicHJvZmlsZUlkIiA6ICIxNzM1MGE5OWQ3MzQ0NDBjYTY0YzJjMDU3YTNjMWM4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHaWxkZWRoZXJvNTY5MSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80MTczN2FhZTNhMmNiNDFkMzI4ZTdhYzliNjFkZjFhYzg0YWQ1M2I3NDBjZjQ4NTBjOGNjNGY0NGY0MGJiOGYiCiAgICB9CiAgfQp9",
                                            "ROX1J4x56zzVAd1M34lRtxgmldowgIVCmcIARhaJa2+F6RSeEj5ujVu2aFmXjVRDaslTz5N5d+0xQxIMnAabwefwFxLyp6FBl/UrXg4yZczfdaZhqHeHXzgoKFhuK9ebpuABOehyRb0eRpO7YLoaC9QaS+EpzUA/kRvDPf/mTdJmQgoKIk0fDCCUNwlqpq0MP/59nF4qPPbQ0EMjzjXLNALXPSzJQRMKKMS0+oQwQE0vPlCbqj2vaZGtiM6GnAHKqQU5ZvloR7CnV0umcLuLuRYmE/agWLiK5LCQVTHC3XmG3FE+GRLttyIc0CqHeGjcbVkMZnu1mN71ZIeqneURpEIfltiYNZVBB8gaJUwu7jLVc9TvXCdsI1aHbW6NyDXl2qOckOogIi/29cf3+eHA/LZIcJM883ydGskcQVI1SEZtzjZ2ZI9vKHZf+vNIqWSaYk/gmU+xMXyWPMMKJRQgWmQ0Ug+NWLKJeurggR/H1d86ksYuSn3wk9GHIMRj9c3p5CQSk7J/D5UmbEB4wvuJZNKap2TC0yhdct6qgj9omAV5leQQ1BuyZKo7bPMaiM2pM9rQnZyScLmMqHjhtOvkpMoJ7Z+oJYhmfQUa4wt+e+g+Z73WTkEZDCJLXXGTP2Bvaol5GGi5yGr7uE6ONAbsJXKVmEM8urAPXJm5iM0bSj8="
                                    ))
                            );
                            
                            dialog1 = new Dialog(Key.ofString("test_dialog_1"), Component.text("Test Dialog 1"))
                                    .addEntry(DialogEntry.ofText(
                                            Component.text("You start hearing voices inside your head."),
                                            Component.text("You begin to wonder whether you're crazy."),
                                            Component.text("You realize that it's just a test dialog.")
                                    ));
                            
                            dialog2 = new Dialog(Key.ofString("test_dialog_2"), Component.text("Test Dialog 2"))
                                    .addEntry(
                                            DialogEntry.ofNpc(npc,
                                                              Component.text("Hello {player}, I'm the real {npc_name}!"),
                                                              Component.text("Send $300 paypal."),
                                                              Component.text("Here is proof ").append(Component.text("👁👁", NamedTextColor.WHITE))
                                            )
                                    );
                            
                            final Key itemToGiveKey = Key.ofString("item_to_give_to_npc");
                            
                            itemToGive = new ItemBuilder(Material.STONE_SHOVEL, itemToGiveKey)
                                    .setName(Component.text("Magic Shovel"))
                                    .setEnchantmentGlintOverride(true)
                                    .build();
                            
                            quest = new Quest(registry, Key.ofString("test_quest_0"), List.of(
                                    new QuestObjectiveJump(99999999)
                            ));
                            
                            quest.setPreRequirement(
                                    QuestPreRequirement.of(player -> player.getName().equals("hapyl"), Component.text("You must be `hapyl`!"))
                            );
                            
                            quest.setStartBehaviour(
                                    QuestStartBehaviour.goTo(new Position(-11, -57, -8, -8, -57, -4), dialog1)
                            );
                        }
                    }
                    
                    final Player player = context.player();
                    
                    Holder.npc.show(player);
                }
        );
        
        register(
                "bd_engine_multipart", context -> {
                    final DisplayData model = BDEngine.parse(
                            "/summon block_display ~-0.5 ~ ~-0.5 {Tags:[root], Passengers:[{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,2.3863f,0f,0.1235f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,2.4265f,0f,0.027f,0f,0.1235f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,2.3863f,0f,0.1235f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,2.4265f,0f,0.027f,0f,0.1494f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,0.0873f,0f,2.1244f,-0.2619f,0.0873f,0f,0.2619f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,0.0191f,0f,2.2401f,-0.2021f,0.0191f,0f,0.3208f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,0.0873f,0f,2.1244f,-0.2619f,0.0873f,0f,0.2619f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,0.0191f,0f,2.2585f,-0.2021f,0.0191f,0f,0.3391f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,0.0873f,0f,2.384f,-0.2619f,-0.0873f,0f,0.8941f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,0.0191f,0f,2.4408f,-0.2021f,-0.0191f,0f,0.7754f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,0.0873f,0f,2.384f,-0.2619f,-0.0873f,0f,0.8941f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,0.0191f,0f,2.4592f,-0.2021f,-0.0191f,0f,0.757f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,0.1235f,0f,2.123f,-0.3704f,0f,0f,0.6322f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,0.027f,0f,2.2465f,-0.2858f,0f,0f,0.5877f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,0.1235f,0f,2.123f,-0.3704f,0f,0f,0.6322f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,0.027f,0f,2.2724f,-0.2858f,0f,0f,0.5877f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,2.3863f,0f,0.1235f,0f,0.7706f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,2.4308f,0f,0.027f,0f,0.7437f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,2.3863f,0f,0.1235f,0f,0.7706f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,2.4308f,0f,0.027f,0f,0.7178f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,0.0873f,0f,2.6694f,-0.2619f,0.0873f,0f,0.8068f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,0.0191f,0f,2.6818f,-0.2021f,0.0191f,0f,0.7563f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,0.0873f,0f,2.6694f,-0.2619f,0.0873f,0f,0.8068f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,0.0191f,0f,2.6635f,-0.2021f,0.0191f,0f,0.738f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,0.0873f,0f,2.9337f,-0.2619f,-0.0873f,0f,0.3492f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,0.0191f,0f,2.8852f,-0.2021f,-0.0191f,0f,0.3399f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,0.0873f,0f,2.9337f,-0.2619f,-0.0873f,0f,0.3492f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,0.0191f,0f,2.8669f,-0.2021f,-0.0191f,0f,0.3582f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,0.1235f,0f,2.8966f,-0.3704f,0f,0f,0.6324f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,0.027f,0f,2.8696f,-0.2858f,0f,0f,0.5922f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,0.1235f,0f,2.8966f,-0.3704f,0f,0f,0.6324f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,0.027f,0f,2.8437f,-0.2858f,0f,0f,0.5922f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.5556f,0f,0f,2.298f,0f,0.5595f,0f,0.176f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0f,-0.5595f,0f,2.8463f,0.5556f,0f,0f,0.1766f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.3929f,-0.3956f,0f,2.5731f,0.3929f,0.3956f,0f,0.0559f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.3929f,0.3956f,0f,2.1786f,-0.3929f,0.3956f,0f,0.4488f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.1892f,0f,0f,2.4781f,0f,0.1852f,0f,0.3649f,0f,0f,0.1172f,0.4797f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1124f,0f,0f,0.9243f,0f,0.1547f,0f,1.1395f,0f,0f,0.2648f,0.4103f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1176086259,-476517789,-244091818,-485758749],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1213f,-0.1064f,0f,1.049f,0f,0f,-0.2944f,1.1604f,0.1228f,0.1077f,0f,0.5635f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;2022385831,-676319958,-716677132,345000748],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0943f,-1.2903f,1.4653f,0f,0.6881f,0.1767f,1.2407f,0.9295f,0f,0f,0.5385f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0f,-0.1307f,0.1245f,1.1435f,0.1133f,0f,0f,1.033f,0f,0.1323f,0.126f,0.4018f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1683940568,-1424624318,-406788946,-1373920059],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.3241f,0f,0f,1.2849f,0f,0.247f,0f,1.323f,0f,0f,0.3125f,0.5403f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1876790045,1276806962,459155598,-1442708742],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0136f,-1.3017f,1.4363f,0f,0.4586f,0.0386f,1.0539f,1.099f,0f,0f,0.5385f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:blast_furnace\",Properties:{facing:\"east\",lit:\"false\"}},transformation:[0.6442f,0.0035f,-0.0002f,1.1416f,-0.0928f,0.0241f,0.0002f,1.2721f,0.0008f,0f,0.1563f,0.46f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-281181163,-1056462683,605135636,-1213171291],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2331f,-0.0116f,0.1945f,1.1762f,0.0286f,-0.0014f,-1.5841f,0.8021f,0.0119f,0.2363f,0f,0.7951f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;661406857,-730891533,-1891703304,-1297528621],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0012f,0f,1.729f,1.7347f,0.2351f,0f,0.0091f,0.3482f,0f,0.2366f,0f,0.7944f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-852247414,-1932812276,1752807763,572167309],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0521f,0f,1.6375f,2f,0.2293f,0f,-0.372f,0.8323f,0f,0.2366f,0f,0.7964f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1156681659,-118690166,-2106981757,1314362217],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2122f,0f,0.0036f,2.5681f,0.0011f,-0.0011f,0.6791f,0.5208f,0f,0.2188f,0.0036f,0.8393f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weathered_copper\",Properties:{}},transformation:[0.55f,0f,0f,2.2952f,0f,0.028f,0f,1.0082f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weathered_copper\",Properties:{}},transformation:[0.2619f,-0.0198f,0f,2.0472f,0.2619f,0.0198f,0f,0.7518f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.0264f,-0.0276f,0f,2.0307f,0.1769f,0.0041f,0f,0.5972f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.5294f,0f,0f,2.3117f,0f,0.0146f,-0.0947f,1.0218f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2578f,-0.0104f,0.0669f,2.0427f,0.2578f,0.0104f,-0.0669f,0.7639f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0253f,-0.0145f,0.0936f,2.0163f,0.169f,0.0022f,-0.014f,0.5996f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weathered_copper\",Properties:{}},transformation:[0.5294f,0f,0f,2.3134f,0f,0.0146f,0.0947f,0.9271f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.2578f,-0.0104f,-0.0669f,2.1096f,0.2578f,0.0104f,0.0669f,0.697f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.0275f,-0.0145f,-0.0936f,2.1089f,0.1843f,0.0022f,0.014f,0.5789f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2702f,0.0044f,0f,1.943f,-0.0428f,0.0276f,0f,0.9955f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:waxed_exposed_copper\",Properties:{}},transformation:[0.2619f,0.0198f,0f,2.8175f,-0.2619f,0.0198f,0f,1.0195f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0411f,0.0276f,0f,3.0702f,-0.2748f,0.0041f,0f,0.7751f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:waxed_exposed_copper\",Properties:{}},transformation:[0.2578f,0.0104f,-0.0669f,2.8261f,-0.2578f,0.0104f,-0.0669f,1.0276f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:waxed_exposed_copper\",Properties:{}},transformation:[0.0404f,0.0145f,-0.0936f,3.0855f,-0.2704f,0.0022f,-0.014f,0.7711f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2578f,0.0104f,0.0669f,2.7592f,-0.2578f,0.0104f,0.0669f,0.9606f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0404f,0.0145f,0.0936f,2.9919f,-0.2704f,0.0022f,0.014f,0.7571f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-275107240,1434358461,-1861606057,450017469],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0f,-0.007f,2.4498f,-0.0045f,0f,-0.3627f,0.5877f,0f,0.2366f,0f,0.7964f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-983593658,1869747208,1367120630,-1196955635],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0535f,0.0428f,-1.2783f,1.4523f,-0.3567f,0.3392f,0.1764f,1.1587f,0.366f,0.3452f,-0.0154f,0.856f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-377211735,1036417341,585817325,-63008058],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0321f,0.0518f,-1.2443f,1.45f,-0.2326f,0.4144f,0.1593f,1.0966f,0.4116f,0.2144f,-0.0008f,0.8272f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1092699481,-1111954207,-250089244,-1561585444],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0535f,0.0428f,1.2783f,1.4523f,-0.3567f,0.3392f,-0.1764f,1.1587f,-0.366f,-0.3452f,-0.0154f,0.2104f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;288854540,-34331039,1244749389,-220276854],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0299f,0.05f,1.2441f,1.4495f,-0.2318f,0.415f,-0.1524f,1.0967f,-0.4116f,-0.2137f,0.0014f,0.2393f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;254058904,-314697492,-1993921578,-1896560939],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0008f,-0.2634f,1.2335f,0f,0.2337f,0.0009f,0.4038f,0.238f,0f,0f,0.7327f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1996471901,1989018322,-1988519406,1267819112],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1308f,0f,-0.2986f,2.3201f,-0.1954f,0f,-0.1999f,0.4472f,0f,0.2366f,0f,0.7964f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-200656596,1067471063,-1085009943,-1084671617],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.1097f,-0.2326f,2.1703f,0f,0.2064f,-0.1237f,0.4199f,0.238f,0f,0f,0.736f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-302270906,592364779,1047409377,481704195],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.1429f,-0.188f,2.3979f,0f,0.1849f,-0.1453f,0.5716f,0.238f,0f,0f,0.7366f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1194433194,-1974045523,1130696675,432332449],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.009f,0.2374f,2.4511f,0f,-0.2335f,-0.0091f,0.6762f,0.238f,0f,0f,0.7366f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;319443382,953086931,-2099909550,497101391],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2331f,-0.0116f,-0.1945f,1.1708f,0.0286f,-0.0014f,1.5841f,0.8015f,-0.0119f,-0.2363f,0f,0.2731f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1674898492,657279577,-453944897,878174959],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0012f,0f,1.729f,1.7368f,0.2351f,0f,0.0091f,0.3492f,0f,0.2366f,0f,0.3907f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;657513731,-1319754053,-1597421203,-1009380700],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0521f,0f,-1.6375f,2.0226f,0.2293f,0f,0.372f,0.835f,0f,-0.2366f,0f,0.2704f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1067417487,520960789,-454504824,-1505496133],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2122f,0f,0.0036f,2.5681f,0.0011f,-0.0011f,0.6791f,0.5245f,0f,0.2188f,0.0036f,0.3405f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1794827769,-920446163,-1615530120,-982130649],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0f,-0.007f,2.456f,-0.0045f,0f,-0.3627f,0.5901f,0f,0.2366f,0f,0.3887f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;880259150,1537311525,-1298980678,2048852518],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0008f,-0.2634f,1.235f,0f,0.2337f,0.0009f,0.4044f,0.238f,0f,0f,0.334f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;340686424,-1711459390,960281193,-1739175567],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1197f,0f,0.3093f,2.3325f,-0.2024f,0f,0.1829f,0.4382f,0f,-0.2366f,0f,0.2704f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-652910122,1790677426,-2029269655,-816993979],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.1097f,-0.2326f,2.168f,0f,0.2064f,-0.1237f,0.4164f,0.238f,0f,0f,0.3308f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-972840283,95015517,-400676521,-889577650],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.1429f,-0.188f,2.397f,0f,0.1849f,-0.1453f,0.5566f,0.238f,0f,0f,0.3302f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;178118692,-1948286535,-1657469128,-365661447],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.009f,0.2374f,2.446f,0f,-0.2335f,-0.0091f,0.68f,0.238f,0f,0f,0.3302f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-831606468,-942308013,-174740232,1742386091],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.15f,0.1528f,0.0044f,2.603f,0.15f,-0.1528f,-0.0044f,0.4216f,0f,0.0016f,-0.8592f,0.5295f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-17932528,1083490309,614497845,1982582052],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0521f,-0.2279f,-0.0003f,1.943f,0.2293f,0.0518f,-0.0013f,0.8452f,0.0004f,0f,0.7756f,0.5624f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1772320227,-978375638,-805710280,1714579416],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.3437f,0.3646f,0f,1.8682f,-0.3354f,0.3736f,0f,1.1212f,0f,0f,0.9064f,0.5385f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1968564800,651525041,51956641,-1377755632],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.188f,-0.1958f,-0.1137f,1.7312f,-0.0745f,-0.0007f,-0.5458f,0.9725f,0.1791f,0.2103f,-0.1103f,0.8267f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;613229293,151414610,717468430,-2117424572],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.188f,-0.1958f,0.1137f,1.7258f,-0.0745f,-0.0007f,0.5458f,0.968f,-0.1791f,-0.2103f,-0.1103f,0.2393f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_wool\",Properties:{}},transformation:[-0.0211f,0.0484f,0f,2.2005f,-0.0872f,-0.0117f,0f,1.0939f,0f,0f,0.3094f,0.3785f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_wool\",Properties:{}},transformation:[0.1571f,0.0169f,0.1578f,1.777f,-0.0418f,0.0594f,-0.0478f,1.0566f,-0.1669f,0.0011f,0.1646f,0.5339f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_wool\",Properties:{}},transformation:[0.2863f,0.0065f,0f,1.9424f,-0.0423f,0.0439f,0f,1.0111f,0f,0f,0.375f,0.3441f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;462301837,-38698858,1420678757,-1541967078],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0001f,-0.2279f,-0.0862f,1.9624f,-0.0004f,0.0518f,-0.3793f,0.9303f,0.238f,0f,-0.0007f,0.5432f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1142935406,537392456,1787057949,1915359089],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1983f,0f,-0.2314f,2.5008f,-0.1263f,0f,-0.3632f,0.8564f,0f,0.2366f,0f,0.7964f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1822156183,-1811215269,1947285017,-410220273],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1983f,0f,-0.2314f,2.5054f,-0.1263f,0f,-0.3632f,0.8564f,0f,0.2366f,0f,0.397f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.1516f,0f,0f,2.4906f,0f,0.1148f,0f,0.9191f,0f,0f,0.4746f,0.298f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1067464189,881728101,1841326642,938715101],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2351f,0f,0f,2.5653f,0f,-0.3785f,0f,0.5075f,0f,0f,0.2344f,0.785f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;280121077,-436064048,324069579,1060713108],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2351f,0f,0f,2.5653f,0f,-0.3785f,0f,0.5083f,0f,0f,0.2344f,0.2875f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1678926876,1906721093,2135553066,-2089924581],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU0OGJhYjNjNDhkYzg1ZWQzMzI4YWQ4YWM4NzE5ZDBjMTM0ODM5NzFkODE4ZWU2ZDIwOTc0ZDkxYTZlYjVlZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.4835f,0f,0f,1.8912f,0f,0f,0.3894f,0.3193f,0f,-0.5175f,0f,0.3979f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-905373489,1182561835,1388843709,741158769],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkYTllZmZmMzY3ZWMxOTY3YzQwODA0NTFjMjNhMDBjMDhlMzRmNThiNTFmOTNlNmE5ZWE0ZTZlYjJiNzBjMiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.4835f,0f,1.7703f,0f,0f,0.3894f,0.514f,-0.5175f,0f,0f,0.5273f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1008984498,1922862785,1151555348,70707044],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.4786f,0f,0f,1.8912f,0f,0.3855f,0f,0.4896f,0f,0f,-0.5123f,0.5273f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;999178862,149658074,-1170638318,1000677394],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODRmYzU2ZDRiOGI3ZGNkZTlhNzQ4NzQ3MzQ1NDhiZDYyNzY4ZTFmY2E0ZTYzYjRlM2E1YmJjYTViYjMwYWE3MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.289f,0.289f,0f,1.7342f,-0.2661f,0.2661f,0f,0.7115f,0f,0f,0.5391f,0.3655f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1778819395,281305391,1826094892,123141110],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2919f,0f,-0.2919f,1.662f,0.2687f,0f,-0.2687f,0.645f,0f,-0.5444f,0f,0.2301f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:blast_furnace\",Properties:{facing:\"north\",lit:\"false\"}},transformation:[-0.4671f,0f,0f,1.662f,0f,0.3763f,0f,0.3158f,0f,0f,-0.5f,0.7819f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_deepslate_slab\",Properties:{type:\"bottom\"}},transformation:[0f,0.4671f,0f,1.662f,0f,0f,-0.3763f,0.692f,-0.5f,0f,0f,0.7819f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1681270912,-550464912,161671066,-1981675586],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjYwNGFkY2JkN2Y3NmE4Yjg0Y2IzMDY2ZWRmMTUxNTQ4ZWEzMmJlMWRjNzA3ODFiZTU2M2Q0M2NiMThhMzk5YiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.4671f,0f,0f,1.662f,0f,0.4233f,0f,0.7861f,0f,0f,-0.5f,0.6575f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-783109242,-1551620401,2053783825,-304897688],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.4671f,0f,0f,1.4284f,0f,0.4206f,0f,0.7855f,0f,0f,-0.5f,0.5319f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1313015839,-485383039,984117712,-99816633],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU0OGJhYjNjNDhkYzg1ZWQzMzI4YWQ4YWM4NzE5ZDBjMTM0ODM5NzFkODE4ZWU2ZDIwOTc0ZDkxYTZlYjVlZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.4671f,0f,0f,1.4284f,0f,0f,-0.4206f,0.8118f,0f,-0.5f,0f,0.4069f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1257102239,1929817979,-1549053900,1371032066],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ==\"}]}}},item_display:\"none\",transformation:[0.4939f,0f,0f,1.4176f,0f,0f,-0.5672f,0.5698f,0f,0.3008f,0f,0.8653f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-2030066014,425268078,192753193,162325303],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0012f,-0.2337f,0f,1.1796f,0.2351f,-0.0012f,0f,0.3453f,0f,0f,0.7658f,0.5403f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;968832255,2007267692,1595778338,-1861489549],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0f,0.004f,2.174f,0.0012f,0f,-0.7564f,0.5751f,0f,0.2366f,0f,0.7944f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;873231071,1076270721,1659789613,924224299],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0f,0.004f,2.174f,0.0012f,0f,-0.7564f,0.5751f,0f,0.2366f,0f,0.3907f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:acacia_trapdoor\",Properties:{facing:\"east\",half:\"bottom\",open:\"false\"}},transformation:[0.4168f,0f,0f,2.361f,0f,0.1744f,0f,1.0437f,0f,0f,0.4999f,0.2854f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;122473973,181939982,1886650363,-306726866],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.0442f,0.2587f,2.1684f,0f,-0.2295f,-0.0498f,0.737f,0.238f,0f,0f,0.736f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-269714459,-1925060921,-276968421,-492622335],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.0442f,0.2587f,2.1694f,0f,-0.2295f,-0.0498f,0.7424f,0.238f,0f,0f,0.3278f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_trapdoor\",Properties:{facing:\"east\",half:\"bottom\",open:\"false\"}},transformation:[0.1313f,0f,-0.1313f,2.5775f,-0.1313f,0f,-0.1313f,0.5907f,0f,0.125f,0f,0.6811f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_chain\",Properties:{axis:\"x\"}},transformation:[0.9878f,0f,0f,1.7214f,0f,0.4322f,0f,0.3624f,0f,0f,0.2734f,0.5618f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_chain\",Properties:{axis:\"x\"}},transformation:[0.9878f,0f,0f,1.7214f,0f,0.4322f,0f,0.1185f,0f,0f,0.2734f,0.5536f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_chain\",Properties:{axis:\"x\"}},transformation:[0f,0.4322f,0f,2.4826f,-0.3125f,0f,0f,0.6174f,0f,0f,0.2734f,0.5536f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1216425361,-700253981,-1657514248,450069923],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDgzMDBjM2EyYjNhNGRmNDRhNmE1OWM3NTY2YjlhMmYxYTdkNWFlOTcxOWEwZWQzZGE1MGZmNWY4ZDViY2E4ZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0f,-0.1788f,2.966f,0f,0.1954f,0f,0.92f,0.188f,0f,0f,0.7178f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.055f,0.0493f,0f,2.8693f,-0.052f,0.0521f,0f,0.8684f,0f,0f,0.1012f,0.661f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:light_gray_wool\",Properties:{}},transformation:[0.1332f,0.0026f,0.0358f,1.9223f,-0.0393f,0.0093f,-0.0076f,1.072f,-0.0589f,-0.0003f,0.0882f,0.4611f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:light_gray_wool\",Properties:{}},transformation:[0.0325f,0.0013f,0.0854f,2.0365f,-0.0073f,0.0096f,-0.0093f,1.0335f,-0.0564f,-0.0005f,0.0516f,0.6453f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:light_gray_wool\",Properties:{}},transformation:[-0.0199f,0.0484f,0f,2.2035f,-0.0823f,-0.0117f,0f,1.0941f,0f,0f,0.058f,0.3776f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;23546867,985585297,1078760951,-1466809692],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVlNGNiNGZjMTRiYTY5ZDQ5YjJkZDJiMDQxOGQ2NTYyZGZmZGViZDBlYmYwMTY1YzQ0MTZhMDczZTYyZGRmOCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.6174f,0f,0f,2.5801f,0f,0.5016f,0f,1.0001f,0f,0f,-0.2344f,0.8041f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1157743421,970869475,-804819034,-1530896486],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVlNGNiNGZjMTRiYTY5ZDQ5YjJkZDJiMDQxOGQ2NTYyZGZmZGViZDBlYmYwMTY1YzQ0MTZhMDczZTYyZGRmOCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.6174f,0f,0f,2.5801f,0f,0.5016f,0f,1.0001f,0f,0f,0.2344f,0.2872f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1980744474,-22368190,2075191014,-1179726983],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1N2U0NmViMWVhZDkwNzFjZDhkNjEzYTgxY2Q1NWM1OTMxY2I2ZGJhZTRkZmJmMTQ1OGYyOGY5OWVlN2Y0NCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.614f,-0.0103f,0.0061f,1.5132f,-0.0292f,0.217f,0.0438f,1.189f,-0.0584f,-0.222f,0.0436f,0.8023f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-517764706,-303056686,-2134037992,930731444],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1N2U0NmViMWVhZDkwNzFjZDhkNjEzYTgxY2Q1NWM1OTMxY2I2ZGJhZTRkZmJmMTQ1OGYyOGY5OWVlN2Y0NCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2699f,-0.0066f,0.0015f,1.5134f,0.0082f,0.2139f,0.002f,0.3944f,-0.0069f,-0.007f,0.0624f,0.7816f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1551990647,-487852566,1960930965,1410844292],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1N2U0NmViMWVhZDkwNzFjZDhkNjEzYTgxY2Q1NWM1OTMxY2I2ZGJhZTRkZmJmMTQ1OGYyOGY5OWVlN2Y0NCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1556f,-0.175f,0.0014f,2.1487f,0.2198f,0.1233f,0.0047f,0.7865f,-0.0206f,-0.0054f,0.0623f,0.7865f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-135299326,-733225802,1791719826,-551086653],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1N2U0NmViMWVhZDkwNzFjZDhkNjEzYTgxY2Q1NWM1OTMxY2I2ZGJhZTRkZmJmMTQ1OGYyOGY5OWVlN2Y0NCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.1364f,-0.1846f,0.0026f,1.4239f,0.209f,-0.0921f,0.0287f,0.9234f,-0.1046f,0.0581f,0.0553f,0.2005f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,0.3434f,0f,0.1235f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,0.3878f,0f,0.027f,0f,0.1235f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,0.3434f,0f,0.1235f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,0.3878f,0f,0.027f,0f,0.1494f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,-0.0873f,0f,0.7138f,0.2619f,0.0873f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,-0.0191f,0f,0.6579f,0.2021f,0.0191f,0f,0.1187f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,-0.0873f,0f,0.7138f,0.2619f,0.0873f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,-0.0191f,0f,0.6396f,0.2021f,0.0191f,0f,0.1371f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,-0.0873f,0f,0.978f,0.2619f,-0.0873f,0f,0.6322f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,-0.0191f,0f,0.8614f,0.2021f,-0.0191f,0f,0.5733f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,-0.0873f,0f,0.978f,0.2619f,-0.0873f,0f,0.6322f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,-0.0191f,0f,0.843f,0.2021f,-0.0191f,0f,0.555f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,-0.1235f,0f,0.9771f,0.3704f,0f,0f,0.2618f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,-0.027f,0f,0.8536f,0.2858f,0f,0f,0.302f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,-0.1235f,0f,0.9771f,0.3704f,0f,0f,0.2618f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,-0.027f,0f,0.8277f,0.2858f,0f,0f,0.302f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,0.3434f,0f,0.1235f,0f,0.7706f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,0.3836f,0f,0.027f,0f,0.7437f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,0.3434f,0f,0.1235f,0f,0.7706f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,0.3836f,0f,0.027f,0f,0.7178f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,-0.0873f,0f,0.1688f,0.2619f,0.0873f,0f,0.5449f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,-0.0191f,0f,0.2163f,0.2021f,0.0191f,0f,0.5542f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,-0.0873f,0f,0.1688f,0.2619f,0.0873f,0f,0.5449f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,-0.0191f,0f,0.2346f,0.2021f,0.0191f,0f,0.5359f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,-0.0873f,0f,0.4284f,0.2619f,-0.0873f,0f,0.0873f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,-0.0191f,0f,0.417f,0.2021f,-0.0191f,0f,0.1378f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,-0.0873f,0f,0.4284f,0.2619f,-0.0873f,0f,0.0873f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,-0.0191f,0f,0.4353f,0.2021f,-0.0191f,0f,0.1561f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,-0.1235f,0f,0.2036f,0.3704f,0f,0f,0.2619f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,-0.027f,0f,0.2305f,0.2858f,0f,0f,0.3064f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,-0.1235f,0f,0.2036f,0.3704f,0f,0f,0.2619f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,-0.027f,0f,0.2565f,0.2858f,0f,0f,0.3064f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.5556f,0f,0f,0.2464f,0f,0.5595f,0f,0.176f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0f,0.5595f,0f,0.2539f,-0.5556f,0f,0f,0.7322f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.3929f,0.3956f,0f,0.1341f,-0.3929f,0.3956f,0f,0.4488f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.3929f,-0.3956f,0f,0.5286f,0.3929f,0.3956f,0f,0.0559f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.1892f,0f,0f,0.4329f,0f,0.1852f,0f,0.3649f,0f,0f,0.1172f,0.4797f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1662739117,1328620221,-836921029,1525097655],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.1832f,-0.0006f,0.4984f,0.6144f,0.1071f,-0.001f,0.8529f,0.6137f,0f,0.2188f,0.0052f,0.8153f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1218560275,-238432266,-1890746004,921992592],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2036f,-0.0926f,0f,0.7413f,0.1175f,-0.1604f,0f,0.8284f,0f,0f,0.2344f,0.7628f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-710592527,-1101243798,1736579352,-1259562534],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.204f,0f,-0.6247f,0.9435f,-0.1168f,0f,-1.0908f,1.1792f,0f,0.2366f,0f,0.8222f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-279751480,-559360169,-1414074316,1144375650],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.1832f,-0.0006f,0.4984f,0.6144f,0.1071f,-0.001f,0.8529f,0.6137f,0f,0.2188f,0.0052f,0.3797f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;484245012,-1971042783,81355301,-273163325],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2036f,-0.0926f,0f,0.7413f,0.1175f,-0.1604f,0f,0.8284f,0f,0f,0.2344f,0.3272f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1477614714,2029707730,-570869510,-1856262296],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.204f,0f,-0.6247f,0.9435f,-0.1168f,0f,-1.0908f,1.1792f,0f,0.2366f,0f,0.3866f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-564110181,-1346831767,1984270607,2074265668],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.1503f,0.1526f,0.0052f,0.5633f,0.1498f,0.1531f,-0.0052f,0.4982f,-0.0016f,0f,-1f,0.54f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;399542448,1491323279,-1481961263,697673389],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkzN2VkY2YyYzI5NzZmZDQ2MTdiOTlmMzYzZDNkNjI4MzJhYjk0ODlhMjcwOWRlZTI3NGY3OTllMjk4MzY4OCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.3704f,0f,0.6576f,0.4862f,0f,0f,1.2673f,0f,0f,0.4922f,0.5453f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1231f,0f,0f,0.8097f,0f,0.2578f,0f,1.1395f,0f,0f,0.2648f,0.4111f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1069f,-0.1094f,0f,0.9342f,0.1069f,0.1094f,0f,1.1821f,0f,0f,0.2582f,0.4141f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.168f,0f,-0.0269f,0.8805f,0f,0.1627f,0f,1.1397f,0.0793f,0f,0.0585f,0.6197f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.168f,0f,0.0269f,0.8536f,0f,0.1627f,0f,1.1385f,-0.0793f,0f,0.0585f,0.4083f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0036f,0f,-0.0796f,1.0908f,0f,0.1657f,0f,1.1397f,0.3483f,0f,0.0008f,0.3768f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0f,-0.1307f,0.1245f,1.0188f,0.1133f,0f,0f,1.2003f,0f,0.1323f,0.126f,0.4143f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.2023f,0f,0f,0.7103f,0f,0.0195f,0f,1.3958f,0f,0f,0.2284f,0.4297f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1129059466,-1247541875,1599317420,663533210],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjkwNWRjODI0YWRhY2NlNTY1N2JjOThlNGFkNDFiNTA0NTQ0MDA2Mzc2Y2I4ZGJjZmY5ODE2MzQxNGY4MGQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.2935f,-0.027f,0.9016f,0f,0.226f,-0.0351f,1.4135f,0.375f,0f,0f,0.5403f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_block\",Properties:{}},transformation:[0.0149f,0f,-0.0142f,1.1064f,0f,0.0231f,0f,1.3022f,0.0148f,0f,0.0147f,0.5263f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1161f,-0.042f,0f,1.0602f,0.042f,0.1161f,0f,1.3736f,0f,0f,0.125f,0.7003f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1161f,-0.042f,0f,1.0602f,0.042f,0.1161f,0f,1.3736f,0f,0f,0.125f,0.2616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.3217f,0f,0f,0.7103f,0f,0.0195f,0f,1.1269f,0f,0f,0.2284f,0.4297f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:cobbled_deepslate_wall\",Properties:{up:\"true\"}},transformation:[0.116f,0f,0.0462f,1.021f,0.0422f,0f,-0.1269f,1.4985f,0f,0.7f,0f,0.1853f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:cobbled_deepslate_wall\",Properties:{up:\"true\"}},transformation:[0.1005f,0.1269f,0.0462f,1.0252f,0.0366f,0.0462f,-0.1269f,1.498f,-0.0625f,0.2368f,0f,0.8801f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:cobbled_deepslate_wall\",Properties:{up:\"true\"}},transformation:[0.1005f,0.1269f,-0.0462f,1.069f,0.0366f,0.0462f,0.1269f,1.3719f,0.0625f,-0.2368f,0f,0.1943f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0568f,0.0801f,0.0242f,1.1222f,0.0207f,0.0292f,-0.0665f,1.5006f,-0.0353f,0.1495f,0f,0.9895f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0568f,0.0801f,-0.0242f,1.1464f,0.0207f,0.0292f,0.0665f,1.4341f,0.0353f,-0.1495f,0f,0.088f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.4862f,0f,0f,0.3205f,0f,0.028f,0f,1.0082f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2619f,0.0198f,0f,0.791f,-0.2619f,0.0198f,0f,1.0137f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0411f,0.0276f,0f,1.0441f,-0.2748f,0.0041f,0f,0.7692f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.1454f,-0.0169f,0f,0.1903f,0.11f,0.0223f,0f,0.9032f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.4679f,0f,0f,0.3205f,0f,0.0146f,-0.0947f,1.0218f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2578f,0.0104f,-0.0669f,0.7996f,-0.2578f,0.0104f,-0.0669f,1.0218f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:waxed_weathered_copper\",Properties:{}},transformation:[0.0404f,0.0145f,-0.0936f,1.0591f,-0.2704f,0.0022f,-0.014f,0.7652f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.1439f,-0.0079f,0.0589f,0.1915f,0.1118f,0.0117f,-0.0723f,0.9124f,-0.0072f,0.0244f,0.0552f,0.6472f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.4679f,0f,0f,0.3205f,0f,0.0146f,0.0947f,0.9271f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weathered_copper\",Properties:{}},transformation:[0.2578f,0.0104f,0.0669f,0.7327f,-0.2578f,0.0104f,0.0669f,0.9548f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.0404f,0.0145f,0.0936f,0.9654f,-0.2704f,0.0022f,0.014f,0.7513f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.1439f,-0.0079f,-0.0589f,0.2504f,0.1118f,0.0117f,0.0723f,0.8401f,0.0072f,-0.0244f,0.0552f,0.3759f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:end_rod\",Properties:{facing:\"down\"}},transformation:[0.2386f,0.0801f,0.0966f,0.9999f,0.0868f,0.0292f,-0.2654f,1.5694f,-0.1484f,0.1495f,0f,1.055f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:end_rod\",Properties:{facing:\"down\"}},transformation:[0.2386f,0.0801f,-0.0966f,1.0953f,0.0868f,0.0292f,0.2654f,1.3072f,0.1484f,-0.1495f,0f,0.0244f,0f,0f,0f,1f]}]}"
                    );
                    
                    final DisplayEntity displayEntity = model.spawnInterpolated(context.player().getLocation());
                    
                    new BukkitRunnable() {
                        private int iterations = 100;
                        private double theta;
                        
                        @Override
                        public void run() {
                            if (iterations-- <= 0) {
                                displayEntity.remove();
                                context.assertTestPassed();
                                cancel();
                                return;
                            }
                            
                            displayEntity.asTagged(
                                    "ball", display -> {
                                        final Transformation transformation = display.getTransformation();
                                        final Vector3f translation = transformation.getTranslation();
                                        translation.y += (float) (Math.sin(theta) * 0.05d);
                                        
                                        final Quaternionf leftRotation = transformation.getLeftRotation();
                                        leftRotation.y += (float) (Math.cos(theta) * 0.001d);
                                        
                                        display.setTransformation(transformation);
                                    }
                            );
                            
                            theta += Math.PI / 16;
                        }
                    }.runTaskTimer(thePlugin, 0, 1);
                }
        );
        
        register(
                "block_outline", context -> {
                    final Player player = context.player();
                    final Location location = player.getLocation();
                    
                    final BlockOutline blockOutline = new BlockOutline(
                            player,
                            LocationHelper.copyOf(location).add(5, 5, 5),
                            LocationHelper.copyOf(location).add(-5, -5, -5)
                    );
                    
                    context.scheduler()
                           .then(SchedulerTask.run(() -> {
                               blockOutline.show();
                               
                               context.info(Component.text("Shown bounding box"));
                           }))
                           .then(SchedulerTask.later(() -> {
                               blockOutline.hide();
                               
                               context.info(Component.text("Hid bounding box"));
                               context.assertTestPassed();
                           }, 40))
                           .execute();
                }
        );
        
        register(
                "player_input", context -> {
                    final Player player = context.player();
                    
                    new BukkitRunnable() {
                        private static final TextColor colorHeld = NamedTextColor.GREEN;
                        private static final TextColor colorUnheld = NamedTextColor.DARK_GRAY;
                        
                        private int iterations = 100;
                        
                        @Override
                        public void run() {
                            if (iterations-- <= 0) {
                                context.assertTestPassed();
                                cancel();
                                return;
                            }
                            
                            final boolean isW = PlayerInput.isKeyHeld(player, DefaultInputKey.W);
                            final boolean isA = PlayerInput.isKeyHeld(player, DefaultInputKey.A);
                            final boolean isS = PlayerInput.isKeyHeld(player, DefaultInputKey.S);
                            final boolean isD = PlayerInput.isKeyHeld(player, DefaultInputKey.D);
                            final boolean isShift = PlayerInput.isKeyHeld(player, DefaultInputKey.SHIFT);
                            final boolean isControl = PlayerInput.isKeyHeld(player, DefaultInputKey.CONTROL);
                            final boolean isSpace = PlayerInput.isKeyHeld(player, DefaultInputKey.SPACE);
                            
                            player.sendMessage(Component.text("                                     ", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH));
                            player.sendMessage(Component.text("         W", isW ? colorHeld : colorUnheld, TextDecoration.BOLD));
                            player.sendMessage(
                                    Component.text()
                                             .append(Component.text("     A  ", isA ? colorHeld : colorUnheld, TextDecoration.BOLD))
                                             .append(Component.text(" S  ", isS ? colorHeld : colorUnheld, TextDecoration.BOLD))
                                             .append(Component.text(" D  ", isD ? colorHeld : colorUnheld, TextDecoration.BOLD))
                            
                            );
                            
                            player.sendMessage(Component.text("  SHIFT", isShift ? colorHeld : colorUnheld, TextDecoration.BOLD));
                            player.sendMessage(
                                    Component.empty()
                                             .append(Component.text("  CTRL  ", isControl ? colorHeld : colorUnheld, TextDecoration.BOLD))
                                             .append(Component.text("  SPACE  ", isSpace ? colorHeld : colorUnheld, TextDecoration.BOLD))
                            );
                            player.sendMessage(Component.empty());
                            
                            final boolean isWASD = PlayerInput.isAnyKeyHeld(player, DefaultInputKey.wasd());
                            final boolean isSpaceAndShift = PlayerInput.isAllKeysHeld(player, Set.of(DefaultInputKey.SPACE, DefaultInputKey.SHIFT));
                            
                            player.sendMessage(Component.empty());
                            
                            player.sendMessage(
                                    Component.empty()
                                             .append(Component.text("Is WASD? ", NamedTextColor.GRAY, TextDecoration.BOLD))
                                             .append(isWASD ? Component.text("YES", NamedTextColor.GREEN, TextDecoration.BOLD) : Component.text("NO", NamedTextColor.RED, TextDecoration.BOLD))
                            );
                            
                            player.sendMessage(
                                    Component.empty()
                                             .append(Component.text("Is Space & Shift? ", NamedTextColor.GRAY, TextDecoration.BOLD))
                                             .append(isSpaceAndShift ? Component.text("YES", NamedTextColor.GREEN, TextDecoration.BOLD) : Component.text("NO", NamedTextColor.RED, TextDecoration.BOLD))
                            );
                            
                            player.sendMessage(Component.text("                                     ", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH));
                        }
                    }.runTaskTimer(thePlugin, 0, 1);
                }
        );
        
        register(
                "packet_entity", context -> {
                    final Player player = context.player();
                    final Location location = player.getLocation();
                    
                    // Packet item
                    final ItemStack itemStackInitial = new ItemBuilder(Material.STONE).asItemStack();
                    final ItemStack itemStackReplace = new ItemBuilder(Material.COPPER_SWORD).addEnchant(Enchantment.SWEEPING_EDGE, 1).asItemStack();
                    
                    final PacketItem packetItem = new PacketItem(location, itemStackInitial);
                    packetItem.setGravity(false);
                    packetItem.showAll();
                    
                    // Packet block display
                    final PacketBlockDisplay packetBlockDisplay = new PacketBlockDisplay(location);
                    
                    // Packet guardian & squid
                    final PacketGuardian packetGuardian = new PacketGuardian(location.clone().add(0, 5, 0));
                    final PacketSquid packetSquid = new PacketSquid(location);
                    
                    context.info(Component.text("Spawned packet item"));
                    
                    context.scheduler()
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Replace item"));
                               
                               packetItem.setItem(itemStackReplace);
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               packetItem.dispose();
                               
                               context.info(Component.text("Spawned packet block display"));
                               
                               packetBlockDisplay.setBlockData(Material.STONE);
                               packetBlockDisplay.setTransformation(new Matrix4f(
                                       0.5000f, 0.0000f, 0.0000f, -0.2500f,
                                       0.0000f, 0.5000f, 0.0000f, -0.2500f,
                                       0.0000f, 0.0000f, 0.5000f, -0.2500f,
                                       0.0000f, 0.0000f, 0.0000f, 1.0000f
                               ));
                               
                               packetBlockDisplay.showAll();
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               packetBlockDisplay.dispose();
                               
                               context.info(Component.text("Spawned packet guardian & squid"));
                               
                               packetGuardian.setVisible(false);
                               packetSquid.setVisible(false);
                               
                               packetGuardian.showAll();
                               packetSquid.showAll();
                               
                               packetGuardian.setBeamTarget(packetSquid);
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               packetGuardian.dispose();
                               packetSquid.dispose();
                               
                               context.assertTestPassed();
                           }, 40))
                           .execute();
                }
        );
        
        register(
                "dialog", context -> {
                    class Holder {
                        private static Npc npc;
                        
                        static Npc create(EternaTestContext context) {
                            if (npc == null) {
                                npc = new Npc(context.player().getLocation(), Component.text("My Name is Jeff"), AppearanceBuilder.ofSheep(SheepColor.RED));
                                npc.showAll();
                            }
                            
                            return npc;
                        }
                    }
                    
                    final Npc npc = Holder.create(context);
                    final Player player = context.player();
                    
                    if (context.argument(0).toString().equalsIgnoreCase("skip")) {
                        Dialog.getCurrentDialog(player).ifPresentOrElse(
                                instance -> {
                                    instance.skip();
                                    context.assertTestPassed();
                                },
                                () -> {
                                    context.assertTestFailed("There aren't any dialogs to skip!");
                                }
                        );
                    }
                    else {
                        new Dialog(Key.ofString("test_dialog"), Component.text("Test Dialog")) {
                            @Override
                            public int getEntryDelay(@NotNull Player player, @NotNull DialogEntry entry) {
                                return 20;
                            }
                        }
                                .addEntry(DialogEntry.ofText(
                                        Component.text("Hello there, stranger!"),
                                        Component.text("This is a test dialog, that is working as intended!"),
                                        Component.text("Why? Because I rewrote dialogs to use components!"),
                                        Component.text("Yes, and it works perfectly!")
                                ))
                                .addEntry(
                                        DialogEntry.ofNpc(npc,
                                                          Component.text("Oh yes I'm the npc!"),
                                                          Component.text("And I'm talking to you...")
                                        )
                                )
                                .addEntry(
                                        DialogEntry.ofSelectableOptions(DialogEntry.ofText(Component.text("Do you like how the system works?")))
                                                   .setOption(
                                                           OptionIndex.OPTION_1,
                                                           DialogEntryOptions.builder(Component.text("It's nice!"))
                                                                             .append(DialogEntry.ofText(
                                                                                     Component.text("Oh stop it!"),
                                                                                     Component.text("I know it's nice but don't flatter me!")
                                                                             ))
                                                   )
                                                   .setOption(
                                                           OptionIndex.OPTION_2,
                                                           DialogEntryOptions.builder(Component.text("No, it sucks."))
                                                                             .append(DialogEntry.ofText(
                                                                                     Component.text("Damn.")
                                                                             ))
                                                   )
                                                   .setOption(
                                                           OptionIndex.OPTION_3,
                                                           DialogEntryOptions.builder(Component.text("It's okay I guess..."))
                                                                             .append(DialogEntry.ofText(
                                                                                     Component.text("Your guess is correct!"),
                                                                                     Component.text("It's indeed... okay.'")
                                                                             ))
                                                   )
                                                   .setOption(
                                                           OptionIndex.OPTION_4, DialogEntryOptions.goodbye(Component.text("Leave..."))
                                                   )
                                )
                                .addEntry(
                                        new DialogEntryText(
                                                Key.ofString("explaining"),
                                                Component.text("So you need to do this and that and this and that and this and that.", NamedTextColor.GREEN)
                                        )
                                )
                                .addEntry(
                                        DialogEntry.ofSelectableOptions(DialogEntry.ofText(Component.text("Got all that?")))
                                                   .setOption(
                                                           OptionIndex.OPTION_1,
                                                           DialogEntryOptions.builder(Component.text("Got it!")).advanceDialog(true)
                                                   )
                                                   .setOption(
                                                           OptionIndex.OPTION_2,
                                                           DialogEntryOptions.builder(Component.text("Can you repeat that?"))
                                                                             .append(DialogEntry.ofEntry(instance -> instance.jumpToEntry(Key.ofString("explaining"))))
                                                   )
                                )
                                .addEntry(
                                        DialogEntry.ofText(Component.text("All right then, goodbye!"))
                                )
                                .addEntry(
                                        DialogEntry.ofEntry(instance -> context.assertTestPassed())
                                )
                                .setSummary(Component.text("Nothing important, just some useless dialog..."))
                                .start(player);
                    }
                }
        );
        
        register(
                "parkour", context -> {
                    class Holder {
                        static Parkour theParkour;
                        
                        static void register(@NotNull EternaTestContext context) {
                            if (theParkour == null) {
                                final World defaultWorld = LocationHelper.defaultWorld();
                                
                                theParkour = new Parkour(
                                        Key.ofString("test_parkour"),
                                        Component.text("My Test Parkour"),
                                        ParkourPosition.builder(
                                                               ParkourPosition.of(defaultWorld, -44, 74, 2),
                                                               ParkourPosition.of(defaultWorld, -37, 77, 5)
                                                       )
                                                       .checkpoint(ParkourPosition.of(defaultWorld, -41, 75, 2, 0, 0))
                                                       .checkpoint(ParkourPosition.of(defaultWorld, -38, 76, 2, 0, 0))
                                                       .checkpoint(ParkourPosition.of(defaultWorld, -40, 77, 5, 0, 0))
                                );
                                
                                theParkour.setQuitLocation(LocationHelper.defaultLocation(-44, 74, -1));
                                
                                context.player().teleport(theParkour.getStart().getLocation().add(1, 0, 0));
                                
                                context.info(Component.text("Registered parkour"));
                                context.assertTestPassed();
                            }
                            else {
                                context.player().teleport(theParkour.getStart().getLocation().add(1, 0, 0));
                                
                                context.warning(Component.text("Parkour already created!"));
                                context.assertTestPassed();
                            }
                        }
                    }
                    
                    Holder.register(context);
                }
        );
        
        register(
                "scoreboard", context -> {
                    class Holder {
                        static TextColor randomColor() {
                            return TextColor.color(theRandom.nextInt(0x1000000));
                        }
                    }
                    
                    final Player player = context.player();
                    final ScoreboardBuilder builder = new ScoreboardBuilder(player, Component.text("Test Scoreboard"));
                    
                    builder.setLines(
                            Component.text("line 1"),
                            Component.text("line 2", NamedTextColor.GREEN),
                            Component.text("line 3", NamedTextColor.GOLD),
                            Component.text("line 4 + %s".formatted(player.getName()), NamedTextColor.YELLOW),
                            Component.text("line 5", NamedTextColor.DARK_PURPLE)
                    );
                    
                    context.scheduler()
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Shrunk scoreboard"));
                               
                               builder.setLines(
                                       Component.text("Text 1", Holder.randomColor()),
                                       Component.text("Text 2", Holder.randomColor()),
                                       Component.text("Text 3", Holder.randomColor())
                               );
                           }, 20))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Grew scoreboard"));
                               
                               builder.setLines(
                                       Component.text("Big 1", Holder.randomColor()),
                                       Component.text("Big 2", Holder.randomColor()),
                                       Component.text("Big 3", Holder.randomColor()),
                                       Component.text("Big 4", Holder.randomColor()),
                                       Component.text("Big 5", Holder.randomColor()),
                                       Component.text("Big 6", Holder.randomColor()),
                                       Component.text("Big 7", Holder.randomColor())
                               );
                           }, 20))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Overgrew scoreboard"));
                               
                               builder.setLines(
                                       Component.text("Overflow 1", Holder.randomColor()),
                                       Component.text("Overflow 2", Holder.randomColor()),
                                       Component.text("Overflow 3", Holder.randomColor()),
                                       Component.text("Overflow 4", Holder.randomColor()),
                                       Component.text("Overflow 5", Holder.randomColor()),
                                       Component.text("Overflow 6", Holder.randomColor()),
                                       Component.text("Overflow 7", Holder.randomColor()),
                                       Component.text("Overflow 8", Holder.randomColor()),
                                       Component.text("Overflow 9", Holder.randomColor()),
                                       Component.text("Overflow 10", Holder.randomColor()),
                                       Component.text("Overflow 11", Holder.randomColor()),
                                       Component.text("Overflow 12", Holder.randomColor()),
                                       Component.text("Overflow 13", Holder.randomColor()),
                                       Component.text("Overflow 14", Holder.randomColor()),
                                       Component.text("Overflow 15", Holder.randomColor()),
                                       Component.text("Overflow 16", Holder.randomColor()),
                                       Component.text("Overflow 17", Holder.randomColor()),
                                       Component.text("Overflow 18", Holder.randomColor()),
                                       Component.text("Overflow 19", Holder.randomColor())
                               );
                               
                           }, 20))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Empties scoreboard"));
                               
                               builder.setLines(ComponentList.empty());
                           }, 20))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Conditioned scoreboard"));
                               
                               new BukkitRunnable() {
                                   private int iterations = 50;
                                   
                                   @Override
                                   public void run() {
                                       if (iterations-- <= 0) {
                                           builder.hide();
                                           context.assertTestPassed();
                                           cancel();
                                           return;
                                       }
                                       
                                       builder.setLines(
                                               Component.text("Your name: ", NamedTextColor.GRAY).append(Component.text(player.getName(), NamedTextColor.GOLD)),
                                               Component.text("Sneaking? ", NamedTextColor.GRAY).append(player.isSneaking()
                                                                                                        ? Component.text("Yes!", NamedTextColor.GREEN)
                                                                                                        : Component.text("No", NamedTextColor.RED)
                                               )
                                       );
                                   }
                               }.runTaskTimer(thePlugin, 0, 1);
                           }, 20))
                           .execute();
                }
        );
        
        register(
                "npc", context -> {
                    // Register custom placeholder
                    class Holder {
                        private static void registerPlaceholders() {
                            NpcPlaceholder.register("health", (_npc, _player) -> Component.text("%.1f ❤".formatted(_player.getHealth()), NamedTextColor.RED));
                            NpcPlaceholder.register(
                                    "held_item", (_npc, _player) -> {
                                        final ItemStack itemInMainHand = _player.getInventory().getItemInMainHand();
                                        
                                        return itemInMainHand.isEmpty() ? Component.text("Nothing!", NamedTextColor.AQUA) : itemInMainHand.displayName().color(NamedTextColor.AQUA);
                                    }
                            );
                        }
                    }
                    
                    Holder.registerPlaceholders();
                    
                    final Player player = context.player();
                    final String appearanceType = context.argument(0).toString().toLowerCase();
                    
                    final AppearanceBuilder<? extends Appearance> appearance = switch (appearanceType) {
                        case "player" -> AppearanceBuilder.ofMannequin(Skin.ofPlayer(player));
                        case "sheep" -> AppearanceBuilder.ofSheep(SheepColor.RED);
                        case "fox" -> AppearanceBuilder.ofFox(FoxType.RED);
                        case "husk" -> AppearanceBuilder.ofHusk();
                        case "villager" -> AppearanceBuilder.ofVillager(VillagerVariant.PLAINS, VillagerProfession.LEATHERWORKER, VillagerLevel.NOVICE);
                        default -> null;
                    };
                    
                    if (appearance == null) {
                        context.assertTestFailed("Invalid argument `type`: `%s` isn't a valid type!".formatted(appearanceType));
                        return;
                    }
                    
                    final Npc npc = new Npc(
                            player.getLocation(),
                            Component.text("Emilie"),
                            appearance
                    ) {
                        @Override
                        public void tick() {
                            super.tick();
                            this.updateHologram();
                        }
                        
                        @Override
                        public void onClick(@NotNull Player player, @NotNull ClickType clickType) {
                            sendMessage(player, Component.text("Thank you for clicking me, {player}.", NamedTextColor.AQUA));
                            sendMessage(player, Component.text("My name is {npc_name}.", NamedTextColor.GOLD));
                            sendMessage(player, Component.text("You health is: {health}"));
                            sendMessage(player, Component.text("You're holding: {held_item}"));
                        }
                    };
                    
                    npc.setTagLayout(new TagLayout(
                            TagPart.literal(Component.text("First Line", NamedTextColor.YELLOW)),
                            TagPart.of((_npc, _player) -> Component.text("Hello %s!".formatted(_player.getName()), NamedTextColor.AQUA)),
                            TagPart.literal(Component.text("-------------------", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH)),
                            TagPart.name(),
                            TagPart.literal(Component.text("-------------------", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH)),
                            TagPart.literal(Component.text("Hello World!", NamedTextColor.BLUE)),
                            TagPart.linebreak(),
                            TagPart.of((_npc, _player) -> {
                                if (_player.isSneaking()) {
                                    return Component.text("You're sneaking!", NamedTextColor.YELLOW);
                                }
                                
                                return null;
                            })
                    ));
                    
                    final NpcProperties properties = npc.getProperties();
                    
                    properties.setViewDistance(20);
                    properties.setLookAtClosePlayerDistance(5);
                    properties.setCollidable(false);
                    properties.setRestPosition(new RestHeadPosition(100.0f, -25.0f));
                    
                    npc.showAll();
                    
                    context.info(Component.text("Created `%s` npc".formatted(appearanceType)));
                    context.scheduler()
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Sitting: ").append(Component.text("true", NamedTextColor.GREEN)));
                               npc.setSitting(true);
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Sitting: ").append(Component.text("false", NamedTextColor.RED)));
                               npc.setSitting(false);
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Shaking: ").append(Component.text("true", NamedTextColor.GREEN)));
                               npc.setShaking(true);
                           }, 40))
                           .then(SchedulerTask.later(() -> {
                               context.info(Component.text("Shaking: ").append(Component.text("false", NamedTextColor.RED)));
                               npc.setShaking(false);
                           }, 40))
                           .then(SchedulerTask.await(await -> {
                               int delay = 0;
                               
                               for (NpcAnimation npcAnimation : NpcAnimation.values()) {
                                   Runnables.later(() -> {
                                       context.info(Component.text("Animation: ").append(Component.text(npcAnimation.name(), NamedTextColor.GREEN)));
                                       
                                       npc.playAnimation(npcAnimation);
                                   }, delay += 10);
                               }
                               
                               Runnables.later(() -> await.complete(null), delay + 10);
                           }))
                           .then(SchedulerTask.await(await -> {
                               final List<NpcPose> npcPoses
                                       = ListMaker.<NpcPose>ofList()
                                                  .addAll(NpcPose.values())
                                                  // Move SWIMMING -> STANDING around to validate that mojang
                                                  // unfucked the poses
                                                  .putLast(NpcPose.SWIMMING)
                                                  .putLast(NpcPose.STANDING)
                                                  .makeList();
                               
                               int delay = 0;
                               
                               for (NpcPose pose : npcPoses) {
                                   Runnables.later(() -> {
                                       context.info(Component.text("Pose: ").append(Component.text(pose.name(), NamedTextColor.LIGHT_PURPLE)));
                                       
                                       npc.setPose(pose);
                                   }, delay += 10);
                               }
                               
                               Runnables.later(() -> await.complete(null), delay + 10);
                           }))
                           .then(SchedulerTask.later(() -> {
                               if (npc.getAppearance() instanceof AppearanceHumanoid appearanceHumanoid) {
                                   context.info(Component.text("Set equipment"));
                                   
                                   appearanceHumanoid.setEquipment(
                                           Equipment.builder()
                                                    .helmet(Material.DIAMOND_HELMET)
                                                    .chestPlate(Material.GOLDEN_CHESTPLATE)
                                                    .leggings(Material.COPPER_LEGGINGS)
                                                    .boots(Material.NETHERITE_BOOTS)
                                                    .mainHand(Material.BREEZE_ROD)
                                                    .offHand(Material.BEDROCK)
                                                    .body(Material.SADDLE)
                                                    .build()
                                   );
                               }
                               else {
                                   context.warning(Component.text("Could not test equipment because appearance isn't a humanoid!"));
                               }
                           }, 20))
                           .then(SchedulerTask.later(() -> {
                               npc.dispose();
                               context.assertTestPassed();
                           }, 40))
                           .execute();
                }
        );
        
        register(
                "reflect_access", context -> {
                    class TestClass {
                        public final byte myPrivateByte = 5;
                        private static final short myStaticShort = 69;
                        
                        private int getInteger() {
                            return 123;
                        }
                        
                        private int getIntegerWithArgs(int plus) {
                            return 1 + plus;
                        }
                        
                        static String getStaticMethod() {
                            return "Hello";
                        }
                        
                        private void voidMethod(@NotNull EternaTestContext context) {
                            context.info(Component.text("Called voidMethod()"));
                        }
                    }
                    
                    final ObjectInstance instance = TestClass::new;
                    
                    final Integer getInteger
                            = ReflectAccess.ofMethod(TestClass.class, Integer.class, "getInteger")
                                           .invoke(instance)
                                           .orElseThrow();
                    
                    final Integer getIntegerWithArgs
                            = ReflectAccess.ofMethod(TestClass.class, Integer.class, "getIntegerWithArgs", int.class)
                                           .invoke(instance, 2)
                                           .orElseThrow();
                    
                    final String getStaticMethod
                            = ReflectAccess.ofMethod(TestClass.class, String.class, "getStaticMethod")
                                           .invoke(ObjectInstance.STATIC)
                                           .orElseThrow();
                    
                    ReflectAccess.ofMethod(TestClass.class, Void.class, "voidMethod", EternaTestContext.class).invoke(instance, context);
                    
                    context.assertEquals(getInteger, 123);
                    context.assertEquals(getIntegerWithArgs, 3);
                    context.assertEquals(getStaticMethod, "Hello");
                    
                    final ReflectFieldAccess<Byte> myPrivateByteAccess = ReflectAccess.ofField(TestClass.class, Byte.class, "myPrivateByte");
                    final byte myPrivateByte = myPrivateByteAccess.get(instance).orElseThrow();
                    
                    context.assertEquals(myPrivateByte, (byte) 5);
                    
                    final ReflectFieldAccess<Short> myStaticShortAccess = ReflectAccess.ofField(TestClass.class, Short.class, "myStaticShort");
                    final short myStaticShort = myStaticShortAccess.get(ObjectInstance.STATIC).orElseThrow();
                    
                    context.assertEquals(myStaticShort, (short) 69);
                    context.assertTestPassed();
                }
        );
        
        register("geometry", context -> {
            final String operation = context.argument(0).toString().toLowerCase();
            
            final Location location = context.player().getLocation();
            final double radius = context.argument(1).toDouble(3);
            final Quality quality = context.argument(2).toStaticConstant(Quality.class, Quality.class).orElse(Quality.NORMAL);
            
            final WorldParticle particle = Drawable.worldParticle(Particle.FLAME);
            
            switch (operation) {
                case "circle" -> Geometry.drawCircle(location, radius, quality, particle);
                case "circle_anchored" -> Geometry.drawCircleAnchored(location, radius, quality, particle);
                case "line" -> Geometry.drawLine(location, LocationHelper.copyOf(location).add(5, 5, 5), 0.5, particle);
                case "sphere" -> Geometry.drawSphere(location, radius, quality, particle, SphereType.BOTTOM_ONLY);
                case "polygon" -> Geometry.drawPolygon(location, 3, radius, 0.5, particle);
                default -> throw new IllegalArgumentException("Invalid operation `%s`!".formatted(operation));
            }
            
            context.info(Component.text("Drawn `%s`!".formatted(operation)));
            context.assertTestPassed();
        });
        
        register("sequencer", context -> {
            final Sequencer sequencer = new Sequencer(Eterna.getPlugin()) {
                @Override
                public void onStopPlaying() {
                    context.assertTestPassed();
                }
            };
            
            sequencer.addTrack(
                    Track.builder("---p---P---p---P---p---P------------s")
                         .plingWhere('p', 1.0f)
                         .plingWhere('P', 0.75f)
                         .snareWhere('s', 0.75f)
            );
            
            sequencer.addTrack(
                    Track.builder("---b---B---b---B---b---B")
                         .bassWhere('b', 1.0f)
                         .bassWhere('B', 0.75f)
            );
            
            sequencer.play(List.of(context.player()));
        });
        
        register("component_wrap", context -> {
            enum Element implements ComponentLike {
                ELEMENT;
                
                @NotNull
                @Override
                public Component asComponent() {
                    return Component.empty()
                                    .append(Component.text("&", TextColor.color(0xAF2C0C)))
                                    .append(Component.text(" "))
                                    .append(Component.text(name()).color(TextColor.color(0xf887ff)));
                }
            }
            
            context.player().getInventory().addItem(new ItemBuilder(Material.EMERALD).addWrappedLore(
                    Component.text("Shoots three arrows in front of you that deal & ELEMENT damage.")
            ).asIcon());
        });
        
        // *-* End Tests *-* //
        
        final long tookMillis = System.currentTimeMillis() - startMillis;
        
        EternaLogger.info("Tests successfully instantiated! (%sms)".formatted(tookMillis));
    }
    
    @ApiStatus.Internal
    private EternaTestRegistry() {
    }
    
    @Nullable
    @ApiStatus.Internal
    public static EternaTest get(@NotNull Key key) {
        return theRegistry.get(key);
    }
    
    @NotNull
    public static List<String> listTests() {
        return theRegistry.keySet().stream().map(Key::getKey).toList();
    }
    
    @ApiStatus.Internal
    static void register(@NotNull String key, @NotNull Consumer<EternaTestContext> consumer) {
        if (key.toLowerCase().contains("test")) {
            throw new IllegalArgumentException("Test key cannot contain `test`!");
        }
        
        final Key testKey = Key.ofString(key);
        
        if (theRegistry.containsKey(testKey)) {
            throw new IllegalArgumentException("A test with id %s is already registered!".formatted(key));
        }
        
        theRegistry.put(
                testKey, new EternaTest(testKey) {
                    @Override
                    protected void test(@NotNull EternaTestContext context) throws EternaTestException {
                        consumer.accept(context);
                    }
                }
        );
    }
    
}
