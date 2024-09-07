package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.ai.AI;
import me.hapyl.eterna.module.ai.MobAI;
import me.hapyl.eterna.module.ai.goal.FloatGoal;
import me.hapyl.eterna.module.ai.goal.MeleeAttackGoal;
import me.hapyl.eterna.module.block.display.BDEngine;
import me.hapyl.eterna.module.block.display.DisplayData;
import me.hapyl.eterna.module.block.display.DisplayEntity;
import me.hapyl.eterna.module.block.display.animation.AnimationFrame;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.messagebuilder.Format;
import me.hapyl.eterna.module.chat.messagebuilder.Keybind;
import me.hapyl.eterna.module.chat.messagebuilder.MessageBuilder;
import me.hapyl.eterna.module.config.Config;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.entity.Rope;
import me.hapyl.eterna.module.hologram.DisplayHologram;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.hologram.StringArray;
import me.hapyl.eterna.module.inventory.*;
import me.hapyl.eterna.module.inventory.gui.*;
import me.hapyl.eterna.module.locaiton.LocationHelper;
import me.hapyl.eterna.module.math.Geometry;
import me.hapyl.eterna.module.math.geometry.WorldParticle;
import me.hapyl.eterna.module.nbt.NBT;
import me.hapyl.eterna.module.nbt.NBTType;
import me.hapyl.eterna.module.particle.ParticleBuilder;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.player.PlayerSkin;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.DialogEntry;
import me.hapyl.eterna.module.player.quest.*;
import me.hapyl.eterna.module.player.quest.objective.JumpQuestObjective;
import me.hapyl.eterna.module.player.sound.SoundQueue;
import me.hapyl.eterna.module.player.synthesizer.Synthesizer;
import me.hapyl.eterna.module.player.tablist.*;
import me.hapyl.eterna.module.record.Record;
import me.hapyl.eterna.module.record.Replay;
import me.hapyl.eterna.module.record.ReplayData;
import me.hapyl.eterna.module.reflect.Laser;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.border.PlayerBorder;
import me.hapyl.eterna.module.reflect.glow.Glowing;
import me.hapyl.eterna.module.reflect.npc.ClickType;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.reflect.npc.NPCPose;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scoreboard.Scoreboarder;
import me.hapyl.eterna.module.util.*;
import me.hapyl.eterna.module.util.collection.Cache;
import net.md_5.bungee.api.chat.*;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.*;

@SuppressWarnings("all")
public final class EternaRuntimeTest {

    private static final Map<String, EternaTest> allTests;

    static EternaRuntimeTest test;

    static {
        allTests = Maps.newLinkedHashMap(); // keep order for testSq

        addTest(new EternaTest("compute") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) {
                Map<String, Integer> intMap = new HashMap<>();

                intMap.compute("hapyl", Compute.intAdd(69));
                intMap.compute("didenpro", Compute.intAdd(420));

                final Integer hVal = intMap.get("hapyl");
                final Integer dVal = intMap.get("didenpro");

                assertEquals(hVal, 69);
                assertEquals(dVal, 420);

                assertNotEquals(hVal, null);
                assertNotEquals(dVal, null);

                intMap.clear();

                // material
                Map<String, List<Material>> listMap = Maps.newHashMap();

                listMap.compute("stone", Compute.listAdd(Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE));
                listMap.compute("dirt", Compute.listAdd(Material.GRASS_BLOCK, Material.DIRT, Material.DIRT_PATH, Material.PODZOL));
                listMap.compute("dirt", Compute.listRemove(Material.DIRT_PATH));

                final List<Material> sMat = listMap.get("stone");
                final List<Material> dMat = listMap.get("dirt");

                assertEquals(sMat, List.of(Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE));

                assertEquals(dMat, List.of(Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL));
                assertNotEquals(dMat, List.of(Material.GRASS_BLOCK, Material.DIRT, Material.DIRT_PATH, Material.PODZOL));

                return true;
            }
        });

        addTest(new EternaTest("displayHolo") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final DisplayHologram hologram = new DisplayHologram(player.getLocation());

                hologram.setBackground(false);
                hologram.showAll();

                hologram.setLines(
                        "Hello",
                        "World",
                        "My name",
                        "is",
                        "&6Color!"
                );

                info(player, "Spawned");

                later(() -> {
                    info(player, "Removed");

                    hologram.remove();
                    assertTestPassed();
                }, 60);

                return false;
            }
        });

        addTest(new EternaTest("fakePlayer") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final TablistEntry fake = new TablistEntry("test test 1").setTexture(EntryTexture.BLACK);
                final TablistEntry fake2 = new TablistEntry("test test 2").setTexture(EntryTexture.RED);

                fake.show(player);
                fake2.show(player);

                info(player, "Shown");

                later(() -> {
                    info(player, "Hid");

                    fake.hide(player);
                    fake2.hide(player);

                    assertTestPassed();
                }, 20);

                return false;
            }
        });

        addTest(new EternaTest("glowing") {

            final String teamName = "dummyTeam";

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Player diden = Bukkit.getPlayer("DiDenPro");

                final Entity entity = diden != null ? diden : Entities.PIG.spawn(player.getLocation(), self -> {
                    self.setInvisible(true);
                    self.setAI(false);
                });

                final Scoreboard scoreboard = player.getScoreboard();
                Team team = scoreboard.getTeam(teamName);

                if (team == null) {
                    team = scoreboard.registerNewTeam(teamName);
                }

                team.setColor(ChatColor.BLACK);
                team.addEntry(entity instanceof Player ? entity.getName() : entity.getUniqueId().toString());

                final Team finalTeam = team;

                new Glowing(player, entity, ChatColor.YELLOW, 100) {
                    @Override
                    public void onGlowingStart() {
                        info(player, "Glowing for " + 100);
                    }

                    @Override
                    public void onGlowingStop() {
                        info(player, "Stopped glowing.");

                        if (!(entity instanceof Player)) {
                            entity.remove();
                        }

                        assertTestPassed();
                    }
                }.glow();

                return false;
            }
        });

        addTest(new EternaTest("hologram") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Hologram hologram = new Hologram();
                hologram.setLines("1", "2", "3");
                hologram.create(player.getLocation());
                hologram.show(player);

                info(player, "Created");

                later(() -> {
                    info(player, "Moved");

                    hologram.move(player.getLocation());

                    later(() -> {
                        info(player, "Changed lines");

                        hologram.setLinesAndUpdate("1", "2", "3", "deez", "nuts", "are", "very", "good");

                        later(() -> {
                            info(player, "Removed");

                            hologram.destroy();
                            assertTestPassed();
                        }, 60);
                    }, 60);
                }, 60);

                return false;
            }
        });

        addTest(new EternaTest("itemBuilder") {

            static final ItemBuilder testItem = new ItemBuilder(Material.STONE, Key.ofString("itembuildertest_0"))
                    .setName("&aTest Item")
                    .addClickEvent(pl -> pl.sendMessage("YOU CLICKED!!!"))
                    .withCooldown(60, player -> !player.isSneaking(), "NO SNEAKING!!!")
                    .addSmartLore("This is a very long string that will be split or wrapped, or I don't &areally care what its &7called.")
                    .setAllowInventoryClick(true)
                    .setNbt("hello.world", NBTType.SHORT, (short) 12345)
                    .setNbt("goodbye.world", NBTType.STR, "I hecking love Java! ☕");

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final PlayerInventory inventory = player.getInventory();
                final ItemStack oItem = testItem.build();

                inventory.addItem(oItem);

                assertEquals(oItem.getType(), Material.STONE);
                assertEquals(NBT.getValue(oItem.getItemMeta(), "hello.world", NBTType.SHORT), (short) 12345);
                assertEquals(NBT.getValue(oItem.getItemMeta(), "goodbye.world", NBTType.STR), "I hecking love Java! ☕");

                final ItemBuilder cloned = testItem.clone();
                cloned.setName("CLONED BUT WITH A DIFFERENT NAME AND TYPE");
                cloned.setType(Material.BLUE_WOOL);

                cloned.setKey(Key.ofString("itembuildertest_1"));
                cloned.addFunction(p -> {
                    p.sendMessage("CLONED EXLUSIVE");
                }).accept(Action.LEFT_CLICK_AIR).setCdSec(1);

                inventory.addItem(cloned.build());
                inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner("hapyl", Sound.ENTITY_IRON_GOLEM_DAMAGE).build());

                final ItemBuilder clonedClone = cloned.clone();
                clonedClone.setName("CLONED CLONE");
                clonedClone.setKey(Key.ofString("itembuildertest_2"));
                clonedClone.clearFunctions();
                clonedClone.addFunction(p -> {
                    p.sendMessage("Clicked with clone clone.");
                }).accept(Action.LEFT_CLICK_AIR);

                inventory.addItem(clonedClone.build());

                inventory.addItem(new ItemBuilder(Material.DIAMOND_BLOCK, Key.ofString("itembuildertest_3"))
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

                inventory.addItem(testItem.clone().removeLore().build());

                // Test text block
                final ItemBuilder textBlock = new ItemBuilder(Material.GHAST_TEAR);
                textBlock.addTextBlockLore("""
                        TEXT BLOCK LORE
                        """);

                textBlock.addTextBlockLore("""
                        TEXT BLOCK LORE
                        """, "CUSTOM PREFIX + ");

                textBlock.addTextBlockLore("TEXT BLOCK LORE", 5);

                textBlock.addTextBlockLore("""
                        TEXT BLOCK LORE WITH AAAAAAAA
                        """, "CUSTOM PREFIX + C", 10);

                inventory.addItem(textBlock.toItemStack());
                inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD)
                        .setHeadTextureUrl("2a084f78cbd787481eaee173002ac6c081916142b9d9ccc2c3c232cb79c75595")
                        .toItemStack());
                return true;
            }
        });

        addTest(new EternaTest("laser") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Laser laser = new Laser(player.getLocation(), player.getLocation().add(0.0d, 2.0d, 0.0d));
                laser.show(player);

                info(player, "Spawned");

                later(() -> {
                    info(player, "Moved");

                    laser.move(player.getLocation(), player.getLocation().add(0, 3, 0));

                    later(() -> {
                        info(player, "Removed");

                        laser.remove(player);
                        assertTestPassed();
                    }, 60);
                }, 60);

                return false;
            }
        });

        addTest(new EternaTest("npcPose") {

            private final List<NPCPose> testPoses = CollectionBuilder.<NPCPose>ofList()
                    .addAll(NPCPose.values())
                    .putLast(NPCPose.STANDING) // move standing last to test if it works or not
                    .build();

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final HumanNPC npc = new HumanNPC(player.getLocation(), "pose test", player.getName());
                npc.show(player);

                int delay = 20;
                for (NPCPose pose : testPoses) {
                    later(() -> {
                        info(player, "Posing " + pose.name());
                        npc.setPose(pose);
                    }, delay += 20);
                }

                later(() -> {
                    info(player, "Removed");
                    npc.remove();

                    assertTestPassed();
                }, delay + 30);

                return false;
            }
        });

        addTest(new EternaTest("npc") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final HumanNPC npc = new HumanNPC(player.getLocation(), player.getName(), player.getName()) {
                    @Override
                    public void onClick(@Nonnull Player player, @Nonnull ClickType type) {
                        sendNpcMessage(player, "You clicked!");
                    }

                    @Override
                    public void onSpawn(@Nonnull Player player) {
                        info(player, "Spawned");
                    }

                    @Override
                    public void onDespawn(@Nonnull Player player) {
                        info(player, "Despawned");
                    }

                    @Override
                    public void onTeleport(@Nonnull Player player, @Nonnull Location location) {
                        //step(player, "Teleported");
                    }
                }.setInteractionDelay(20);

                npc.setAboveHead(p -> {
                    return StringArray.of("&athird line above head", "second line above head", "first line above head");
                });
                npc.setBelowHead(p -> {
                    return StringArray.of("first line below head", "&csecond line below head", "third line below head");
                });
                npc.show(player);

                final var later = 30;

                later(() -> {
                    info(player, "Teleported");
                    npc.teleport(player.getLocation());
                    later(() -> {
                        info(player, "Updating skin...");
                        npc.setSkin("DiDenPro");
                        later(() -> {
                            info(player, "Sitting");
                            npc.setSitting(true);
                            later(() -> {
                                info(player, "Stopped sitting");
                                npc.setSitting(false);
                                later(() -> {
                                    info(player, "Shaking");
                                    npc.setShaking(true);
                                    later(() -> {
                                        info(player, "Stopped shaking");
                                        npc.setShaking(false);
                                        later(() -> {
                                            npc.remove();
                                            assertTestPassed();
                                        }, later);
                                    }, later);
                                }, later);
                            }, later);
                        }, later);
                    }, later);
                }, later);

                return false;
            }
        });

        addTest(new EternaTest("gui") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final PlayerGUI gui = new PlayerGUI(player);

                gui.setItem(0, new ItemBuilder(Material.STONE).asIcon(), new StrictAction() {
                    @Override
                    public void onLeftClick(@Nonnull Player player) {
                        info(player, "onLeftClick");
                    }

                    @Override
                    public void onShiftLeftClick(@Nonnull Player player) {
                        info(player, "onShiftLeftClick");
                    }

                    @Override
                    public void onRightClick(@Nonnull Player player) {
                        info(player, "onRightClick");
                    }

                    @Override
                    public void onShiftRightClick(@Nonnull Player player) {
                        info(player, "onShiftRightClick");
                    }

                    @Override
                    public void onWindowBorderLeftClick(@Nonnull Player player) {
                        info(player, "onWindowBorderLeftClick");
                    }

                    @Override
                    public void onWindowBorderRightClick(@Nonnull Player player) {
                        info(player, "onWindowBorderRightClick");
                    }

                    @Override
                    public void onMiddleClick(@Nonnull Player player) {
                        info(player, "onMiddleClick");
                    }

                    @Override
                    public void onNumberKeyClick(@Nonnull Player player) {
                        info(player, "onNumberKeyClick");
                    }

                    @Override
                    public void onDoubleClick(@Nonnull Player player) {
                        info(player, "onDoubleClick");
                    }

                    @Override
                    public void onDropClick(@Nonnull Player player) {
                        info(player, "onDropClick");
                    }

                    @Override
                    public void onControlDropClick(@Nonnull Player player) {
                        info(player, "onControlDropClick");
                    }

                    @Override
                    public void onCreativeDropClick(@Nonnull Player player) {
                        info(player, "onCreativeDropClick");
                    }

                    @Override
                    public void onSwapOffhandClick(@Nonnull Player player) {
                        info(player, "onSwapOffhandClick");
                    }

                    @Override
                    public void onUnknownClick(@Nonnull Player player) {
                        info(player, "onUnknownClick");
                    }
                });

                later(() -> {
                    info(player, "Renamed");
                    gui.rename("Hello world!");
                }, 60);

                gui.setCloseEvent(pp -> {
                    assertTestPassed();
                });
                gui.setCancelType(CancelType.NEITHER);
                gui.openInventory();
                return false;
            }
        });

        addTest(new EternaTest("npcSit") {

            static HumanNPC npc;

            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                if (npc == null) {
                    npc = new HumanNPC(player.getLocation(), "sitting");
                    npc.showAll();

                    info(player, "Spawned!");
                    return false;
                }
                else if (args.get(0).toString().equalsIgnoreCase("remove")) {
                    npc.remove();
                    npc = null;
                    return false;
                }

                npc.setSitting(!npc.isSitting());
                info(player, "NPC is now " + (npc.isSitting() ? "SITTING" : "STANDING"));

                return false;
            }

            @Override
            protected boolean doShowFeedback() {
                return false;
            }
        });

        addTest(new EternaTest("autoGUI") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final PlayerAutoGUI gui = new PlayerAutoGUI(player, "My Menu", 5);

                for (int i = 0; i < 16; i++) {
                    int finalI = i;
                    gui.addItem(new ItemBuilder(Material.APPLE).setAmount(i + 1).build(), click -> {
                        click.sendMessage("You clicked %sth apple.".formatted(finalI + 1));
                    });
                }

                gui.setCloseEvent(pp -> {
                    assertTestPassed();
                });
                gui.setPattern(SlotPattern.DEFAULT);
                gui.openInventory();

                return false;
            }
        });

        addTest(new EternaTest("pageGUI") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final PlayerPageGUI<String> gui = new PlayerPageGUI<>(player, "Player Page GUI Test", 5) {
                    @Override
                    @Nonnull
                    public ItemStack asItem(@Nonnull Player player, String content, int index, int page) {
                        return new ItemBuilder(Material.STONE)
                                .setName(content)
                                .setAmount((index + 1) + (maxItemsPerPage() * page))
                                .toItemStack();
                    }

                    @Override
                    public void onClick(@Nonnull Player player, @Nonnull String content, int index, int page, @Nonnull org.bukkit.event.inventory.ClickType clickType) {
                        player.sendMessage("You clicked " + content);
                        player.sendMessage("Using " + clickType);
                    }

                };

                gui.setContents(List.of(
                        "a",
                        "b",
                        "c",
                        "d",
                        "e",
                        "f",
                        "g",
                        "h",
                        "i",
                        "j",
                        "k",
                        "l",
                        "m",
                        "n",
                        "o",
                        "p",
                        "q",
                        "r",
                        "s",
                        "t",
                        "u",
                        "v",
                        "w",
                        "x",
                        "y",
                        "z"
                ));
                gui.setCloseEvent(pp -> {
                    assertTestPassed();
                });
                gui.setFit(PlayerPageGUI.Fit.SLIM);
                gui.setEmptyContentsItem(null);

                gui.openInventory(1);
                return false;
            }
        });

        addTest(new EternaTest("scoreboard") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Scoreboarder scoreboard = new Scoreboarder("&a&lScoreboard &6Test&b!");

                scoreboard.setLines(
                        "&aLine1",
                        "&bLine2",
                        "&6Line3",
                        "",
                        "&bYOU ARE " + player.getName(),
                        "",
                        "hapyl.github.io"
                );
                scoreboard.addPlayer(player);

                later(() -> {
                    info(player, "Hid numbers");

                    scoreboard.setHideNumbers(true);

                    assertTestPassed();
                }, 60);

                return false;
            }
        });

        addTest(new EternaTest("signGUI") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                // ** Correct test input:
                // Yes, testing
                // 123456789
                new SignGUI(player, SignType.random(), "Testing?") {
                    @Override
                    public void onResponse(Response response) {
                        assertEquals(response.getString(0), "Yes, testing");
                        assertEquals(response.getInt(1), 123456789);

                        assertTestPassed();
                    }
                };

                return false;
            }
        });

        addTest(new EternaTest("synthesizer") {

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Synthesizer b = new Synthesizer() {
                    @Override
                    public void onStopPlaying(@Nonnull Collection<Player> players) {
                        assertTestPassed();
                    }
                }
                        .addTrack("a-a-a--DB--DB")
                        .plingWhere('a', 2.0f)
                        .plingWhere('D', 1.0f)
                        .plingWhere('B', 0.5f)
                        .toSynthesizer()
                        .setBPT(2);

                final Synthesizer a = new Synthesizer() {
                    @Override
                    public void onStopPlaying(@Nonnull Collection<Player> players) {
                        info(player, "Playing b");
                        b.play(player);
                    }
                }
                        .addTrack("aaaaaaaaaaaaaa").plingWhere('a', 1.75f).toSynthesizer()
                        .addTrack("---a-aa---a-aa").plingWhere('a', 0.75f).toSynthesizer().setBPT(2);

                info(player, "Playing a");
                a.play(player);

                return false;
            }
        });

        addTest(new EternaTest("tablist") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Tablist tablist = new Tablist();
                final Tablist oldTablist = Tablist.getPlayerTabList(player);

                if (oldTablist != null) {
                    oldTablist.destroy();
                }

                Bukkit.getOnlinePlayers().forEach(tablist::show);

                final BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        final List<Player> players = Lists.newArrayList(Bukkit.getOnlinePlayers());

                        final EntryList entryList = new EntryList();
                        entryList.append("&aPlayers:");
                        entryList.append("");

                        players.forEach(player -> {
                            entryList.append(player.getName(), EntryTexture.of(player));
                        });

                        final EntryList entryList2 = new EntryList();

                        for (ChatColor color : ChatColor.values()) {
                            if (!color.isColor()) {
                                continue;
                            }

                            entryList2.append(color.name(), EntryTexture.of(color));
                        }

                        tablist.setColumn(0, entryList);

                        tablist.setColumn(
                                1,
                                entryList2
                        );

                        tablist.setColumn(
                                2,
                                "&2Friends:",
                                "&8Friends? What friends?"
                        );

                        int pingIndex = 0;
                        for (PingBars ping : PingBars.values()) {
                            final TablistEntry entry = tablist.getEntry(3, pingIndex++);

                            entry.setPing(ping);
                        }

                    }
                }.runTaskTimer(EternaPlugin.getPlugin(), 0, 20);

                tablist.setColumn(
                        1,
                        "&e&lHELLO WORLD",
                        "This is a test",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                );

                later(() -> {
                    task.cancel();
                    tablist.destroy();

                    assertTestPassed();
                }, 100);

                return false;
            }
        });

        addTest(new EternaTest("worldBorder") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final PlayerBorder border = new PlayerBorder(player);

                info(player, "Red");
                border.update(PlayerBorder.Operation.BORDER_RED, 5);

                later(() -> {
                    info(player, "Green");
                    border.update(PlayerBorder.Operation.BORDER_GREEN, 10);

                    later(() -> {
                        info(player, "Reset");
                        border.reset(player);

                        assertTestPassed();
                    }, 60);
                }, 60);

                return false;
            }
        });

        addTest(new EternaTest("recording") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Record record = new Record(player);
                record.startRecording();

                info(player, "Recording...");

                later(() -> {
                    info(player, "Finished recording");
                    record.stopRecording();

                    later(() -> {
                        info(player, "Playing recording");

                        final Replay replay = record.replay(r -> {
                            return new Replay(r) {
                                @Override
                                public void onStart() {
                                    info(player, "Start custom replay.");
                                }

                                @Override
                                public void onStop() {
                                    assertTestPassed();
                                }

                                @Override
                                public void onPause(boolean pause) {
                                    info(player, "Paused custom replay: " + pause);
                                }

                                @Override
                                public void onStep(@Nonnull ReplayData data, long frame) {
                                    info(player, "Step custom replay: " + frame);
                                }
                            };
                        });

                        replay.start(player);

                        later(() -> {
                            info(player, "Paused replay.");

                            replay.pause();

                            later(() -> {
                                info(player, "Unpause replay.");

                                replay.pause();
                            }, 30);
                        }, 30);

                    }, 20);
                }, 100);

                return false;
            }
        });

        addTest(new EternaTest("nbt") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final ItemStack item = player.getInventory().getItemInMainHand();
                assertFalse(item.getType().isAir(), "Must hold an item.");

                final ItemMeta meta = item.getItemMeta();
                assertTrue(meta != null, "Meta must not be null!");

                NBT.setValue(meta, "test.bool", NBTType.BOOL, true);
                NBT.setValue(meta, "test.str", NBTType.STR, "lorem|$&c_abc");
                NBT.setValue(meta, "test.byte", NBTType.BYTE, (byte) 0x10);
                NBT.setValue(meta, "test.int", NBTType.INT, 6_152_709);
                NBT.setValue(meta, "test.long", NBTType.LONG, 1_345_207_914_212_453L);
                NBT.setValue(meta, "test.float", NBTType.FLOAT, 12.12345f);
                NBT.setValue(meta, "test.double", NBTType.DOUBLE, 12.123456789d);

                NBT.setValue(meta, "test.removeme", NBTType.INT, 69420);

                // TODO (hapyl): 003, Mar 3: Test containers and arrays

                item.setItemMeta(meta);

                assertEquals(NBT.getValue(meta, "test.bool", NBTType.BOOL), true);
                assertNotEquals(NBT.getValue(meta, "test.bool", NBTType.BOOL), 1);

                assertEquals(NBT.getValue(meta, "test.str", NBTType.STR), "lorem|$&c_abc");

                assertEquals(NBT.getValue(meta, "test.byte", NBTType.BYTE), (byte) 0x10);
                assertEquals(NBT.getValue(meta, "test.int", NBTType.INT), 6_152_709);
                assertEquals(NBT.getValue(meta, "test.long", NBTType.LONG), 1_345_207_914_212_453L);
                assertEquals(NBT.getValue(meta, "test.float", NBTType.FLOAT), 12.12345f);
                assertEquals(NBT.getValue(meta, "test.double", NBTType.DOUBLE), 12.123456789d);

                NBT.remove(meta, "test.removeme");
                item.setItemMeta(meta);

                assertFalse(NBT.hasNbt(meta, "test.removeme", NBTType.STR));

                assertTestPassed();

                return false;
            }
        });

        addTest(new EternaTest("ai") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final LivingEntity entity = Entities.PILLAGER.spawn(player.getLocation(), self -> self.setSilent(true));
                final AI ai = MobAI.of(entity);

                ai.removeAllGoals();
                ai.addGoal(new FloatGoal(ai));
                ai.addGoal(new MeleeAttackGoal(ai, 1.0d, true));

                assertEquals(ai.getGoals().size(), 2);

                ai.getGoals().forEach(goal -> {
                    info(player, goal.getClass().getSimpleName());
                });

                return true;
            }
        });

        addTest(new EternaTest("rope") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Location location = player.getLocation();
                final Rope rope = new Rope(location, location.clone().add(7.0d, 3.0d, 0.0d)).spawn();

                info(player, "Created");

                later(() -> {
                    rope.remove();
                    assertTestPassed();
                }, 60);

                return false;
            }
        });

        addTest(new EternaTest("weightedCollection") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final WeightedCollection<String> cls = new WeightedCollection<>();

                cls.add("rarest", 1);
                cls.add("world", 10);
                cls.add("how", 10);
                cls.add("are", 10);
                cls.add("you", 10);
                cls.add("MORE COMMON", 20);

                Map<String, Integer> mapped = new HashMap<>();

                for (int i = 0; i < 1000; i++) {
                    final String s = cls.getRandomElement();

                    mapped.compute(s, (_s, _i) -> _i == null ? 1 : _i + 1);
                }

                final Integer mostCommon = mapped.get("MORE COMMON");
                final Integer notSoCommon = mapped.get("you");
                final Integer rarest = mapped.get("rarest");

                assertTrue(mostCommon > notSoCommon, "mostCommon <= notSoCommon");
                assertTrue(rarest < notSoCommon, "mostCommon >= notSoCommon");

                info(player, mapped.toString());
                info(player, cls.toString());

                for (WeightedCollection<String>.WeightedElement element : cls.getWeightedElements()) {
                    info(player, element.toString());
                }

                assertTestPassed();
                return false;
            }
        });

        addTest(new EternaTest("bdEngine") {

            static final String data = "/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-112185605,1447459445,769677829,553711541],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOWQ2OTViYjM3YjU5MzdhOTU5NjNkYWIyYmJjMmE2ZTFjZDhhMmEyNTY4MWUyOTkxNTQ5YzYxYzFkMzNkYyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.9988f,-0.0467f,-0.0161f,-0.0298f,0.0479f,0.9957f,0.0795f,2.1254f,0.0124f,-0.0801f,0.9967f,-0.0544f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:hay_block\",Properties:{axis:\"x\"}},transformation:[-0.0387f,-0.7242f,-0.004f,0.3959f,0.7944f,-0.0354f,0.0467f,0.7848f,-0.0492f,-0.0017f,0.7561f,-0.3526f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:spruce_fence\",Properties:{}},transformation:[1f,0f,0f,-0.5f,0f,1f,0f,0.1408f,0f,0f,1f,-0.5f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:spruce_fence\",Properties:{}},transformation:[0.7254f,-0.3659f,0.0056f,0.2724f,0.3659f,0.7253f,-0.0129f,0.594f,0.0008f,0.0141f,0.8124f,-0.4449f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:spruce_fence\",Properties:{}},transformation:[0.7687f,0.2631f,-0.003f,-0.935f,-0.2631f,0.7686f,-0.0145f,0.7246f,-0.0019f,0.0147f,0.8124f,-0.4576f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:stone_sword\",Count:1},item_display:\"none\",transformation:[-0.2806f,0.1365f,0.9155f,0.733f,0.2558f,-0.6866f,0.3033f,0.5198f,0.6787f,0.3153f,0.2642f,-0.1822f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[-0.0058f,-0.0201f,0.6249f,-0.2938f,-0.8748f,0.0186f,-0.0039f,0.9458f,-0.0162f,-0.9996f,-0.0126f,0.5336f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.0162f,0.9996f,0.0126f,-0.5085f,-0.8748f,0.0186f,-0.0039f,0.9458f,-0.0058f,-0.0201f,0.6249f,-0.2801f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.0074f,0.6927f,0.4508f,-0.5697f,-0.8748f,0.0186f,-0.0039f,0.9458f,-0.0155f,-0.721f,0.4329f,0.1712f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.0155f,0.721f,-0.4329f,-0.1461f,-0.8748f,0.0186f,-0.0039f,0.9458f,0.0074f,0.6927f,0.4508f,-0.556f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite_slab\",Properties:{type:\"bottom\"}},transformation:[0.5625f,0f,0f,-0.2819f,0f,0.375f,0f,0f,0f,0f,0.5625f,-0.2762f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.7876f,0.2564f,-0.2201f,-0.3989f,-0.1163f,0.9315f,0.2117f,0.4491f,0.363f,-0.258f,0.5453f,-0.3401f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.8511f,0.057f,0.1405f,-0.195f,-0.1163f,0.9315f,0.2117f,0.4491f,-0.1663f,-0.3593f,0.571f,-0.1846f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:feather\",Count:1},item_display:\"none\",transformation:[0.0204f,-0.0241f,-0.6258f,-0.045f,-0.5291f,-0.0608f,-0.0207f,2.245f,-0.0594f,0.5335f,-0.0307f,0.1325f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.6952f,0.4904f,0.2238f,-0.5172f,-0.5047f,0.794f,0.1201f,0.9203f,-0.1663f,-0.3593f,0.571f,-0.1846f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.8084f,0.2494f,0.1814f,-0.6402f,-0.2907f,0.8993f,0.1778f,0.8937f,-0.1663f,-0.3593f,0.571f,-0.1083f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:stripped_dark_oak_wood\",Properties:{axis:\"x\"}},transformation:[0.5661f,-0.0038f,-0.0032f,-0.2918f,0.0266f,0.081f,0.0407f,1.509f,0.0014f,-0.0063f,0.5262f,-0.28f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.0938f,-0.1597f,0.6133f,-0.0863f,-0.5047f,0.794f,0.1201f,0.9203f,-0.7086f,-0.5866f,-0.0043f,0.7316f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[0.7666f,0.1794f,0.2797f,-0.6635f,-0.2508f,0.9401f,0.1152f,0.7228f,-0.3393f,-0.2898f,0.5469f,0.1229f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:horn_coral_wall_fan\",Properties:{facing:\"east\"}},transformation:[-0.5354f,-0.5195f,-0.3727f,0.7857f,-0.649f,0.6387f,0.1279f,1.265f,0.2403f,0.5676f,-0.4851f,-0.0795f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:arrow\",Count:1},item_display:\"none\",transformation:[0.1374f,0.4567f,0.3432f,-0.1544f,0.4634f,0.0728f,-0.253f,1.4181f,-0.2211f,0.4365f,-0.317f,-0.5088f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:hay_block\",Properties:{axis:\"x\"}},transformation:[0.1587f,0.2108f,0.0047f,0.1285f,-0.1952f,0.1714f,-0.0008f,1.3998f,-0.0031f,-0.003f,0.2875f,-0.165f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:hay_block\",Properties:{axis:\"x\"}},transformation:[-0.1258f,0.2353f,0.001f,-0.3547f,-0.2179f,-0.1358f,-0.0047f,1.5668f,-0.0031f,-0.003f,0.2875f,-0.1721f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:barrel\",Properties:{facing:\"down\",open:\"false\"}},transformation:[0.3801f,-0.1486f,0.0364f,-0.7891f,-0.0564f,0.9898f,0.0128f,0.2349f,-0.4524f,-0.2484f,0.029f,0.1687f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:infested_stone\",Properties:{}},transformation:[0.4263f,-0.1623f,0.0312f,-0.8f,-0.0633f,1.0809f,0.0109f,0.1803f,-0.5074f,-0.2712f,0.0249f,0.2188f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone_slab\",Properties:{type:\"bottom\"}},transformation:[-0.0659f,-0.2376f,0.1102f,-0.5366f,0.417f,0.031f,0.0386f,0.4636f,-0.1005f,0.2847f,0.0878f,-0.1895f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:arrow\",Count:1},item_display:\"none\",transformation:[0.1374f,-0.2417f,0.4718f,0.3482f,0.4634f,0.3101f,-0.0239f,1.2136f,-0.2211f,0.4998f,0.2431f,-0.5003f,0f,0f,0f,1f]}]}";
            static final String textData = "/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[{id:\"minecraft:text_display\",text:'[{\"text\":\"this is a test\",\"color\":\"#ccaacc\",\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"font\":\"minecraft:uniform\"}]',text_opacity:255,background:-16777216,alignment:\"center\",line_width:210,default_background:false,transformation:[1f,0f,0f,0f,0f,1f,0f,0.5f,0f,0f,1f,0f,0f,0f,0f,1f]}]}";
            static final String failData = "{null: false, abc: 1231312}";

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final DisplayData model = BDEngine.parse(data);
                final DisplayData text = BDEngine.parse(textData);

                final Location location = player.getLocation();

                final DisplayEntity entity = model.spawn(location);
                final DisplayEntity textEntity = text.spawn(location);

                assertThrows(() -> {
                    BDEngine.parse(failData);
                }, "Parser did not throw for invalid data!");

                later(() -> {
                    info(player, "Teleported");

                    final Location newPlayerLocation = player.getLocation();

                    entity.teleport(newPlayerLocation);
                    textEntity.teleport(newPlayerLocation);

                    later(() -> {
                        entity.remove();
                        textEntity.remove();

                        assertTestPassed();
                    }, 60);
                }, 60);

                return false;
            }

        });

        addTest(new EternaTest("polygon") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final int point = args.getInt(0);
                final double distance = args.getDouble(1);

                info(player, "Drawing with %s points and %s distance".formatted(point, distance));

                Geometry.drawPolygon(player.getLocation(), point, distance, new WorldParticle(Particle.FLAME));
                return true;
            }
        });

        addTest(new EternaTest("messageBuilder") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                if (args.getString(0).equals("_")) {
                    return true;
                }

                final MessageBuilder builder = new MessageBuilder();

                builder.append("&cHello &aworld!");
                builder.append("&e&nClick ME!!");
                builder.event(ClickEvent.Action.RUN_COMMAND, "/eterna test messageBuilder _");
                builder.event(HoverEvent.Action.SHOW_TEXT, "&aClick to pass the test!");

                builder.append("       ");
                builder.append("&9yapping hover me");
                builder.eventShowText(
                        "&aFirst line",
                        "&2Second line",
                        "&6Third line",
                        "",
                        "&8Skipped a line, didn't expect that?"
                );

                builder.append("      ");
                builder.append("&cSMART YAPPING!!!");
                builder.eventShowTextBlock("""
                        This is the first line.
                        
                        &c;;This is a red text that will be wrapped after a given number of chars.
                        
                        
                        Another line!
                        """);

                builder.send(player);

                final MessageBuilder keybindBuilder = new MessageBuilder();

                keybindBuilder.append("Testing keybinds:");

                Keybind.getDefaultValues().forEach(key -> {
                    keybindBuilder.append(key);
                    keybindBuilder.appendNewLine();
                });

                keybindBuilder.send(player);

                final MessageBuilder bukkitComponent = new MessageBuilder();

                bukkitComponent.append(new SelectorComponent("@e[type=player]")).format(Format.ITALIC).nl();
                bukkitComponent.append(new ScoreComponent(".eterna", "test")).format(Format.BOLD).nl();
                bukkitComponent.append(new TranslatableComponent("block.minecraft.lily_pad"))
                        .color(ChatColor.YELLOW)
                        .format(Format.BOLD)
                        .nl();

                bukkitComponent.send(player);

                return false;
            }
        });

        addTest(new EternaTest("nmsHandle") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final World world = player.getWorld();

                final ServerPlayer playerHandle = Reflect.getHandle(player, ServerPlayer.class);
                final net.minecraft.world.level.Level worldHandle = Reflect.getHandle(world, net.minecraft.world.level.Level.class);

                assertThrows(() -> {
                    Reflect.getHandle(player, Void.class);
                });

                return true;
            }
        });

        addTest(new EternaTest("particleBuilder") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final Location location = player.getLocation();

                info(player, "redstoneDust");
                ParticleBuilder.redstoneDust(Color.ORANGE, 2).display(location);

                later(() -> {
                    info(player, "blockBreak");
                    ParticleBuilder.blockBreak(Material.EMERALD_BLOCK).display(location);

                    later(() -> {
                        info(player, "blockDust");
                        ParticleBuilder.blockDust(Material.GOLD_BLOCK).display(location);

                        later(() -> {
                            info(player, "mobSpell");
                            ParticleBuilder.mobSpell(Color.BLACK, false).display(location);

                            later(() -> {
                                info(player, "mobSpellAmbient");
                                ParticleBuilder.mobSpell(Color.WHITE, true).display(location);

                                later(() -> {
                                    info(player, "itemBreak");
                                    ParticleBuilder.itemBreak(new ItemStack(Material.EMERALD));

                                    later(() -> {
                                        info(player, "dustTransition");
                                        ParticleBuilder.dustTransition(Color.RED, Color.GREEN, 1).display(location);

                                        later(() -> {
                                            info(player, "vibrationBlock");
                                            ParticleBuilder.vibration(LocationHelper.addAsNew(location, 0, 5, 0), 20)
                                                    .display(location);

                                            later(() -> {
                                                info(player, "virationEntity");
                                                ParticleBuilder.vibration(player, 20).display(location);

                                                later(() -> {
                                                    info(player, "blockMarker");
                                                    ParticleBuilder.blockMarker(Material.BRICKS).display(location);

                                                    later(() -> {
                                                        assertThrows(() -> ParticleBuilder.blockBreak(Material.DIAMOND));
                                                        assertThrows(() -> ParticleBuilder.blockDust(Material.GOLD_NUGGET));
                                                        assertThrows(() -> ParticleBuilder.blockMarker(Material.EMERALD));

                                                        assertTestPassed();
                                                    }, 20);
                                                }, 20);
                                            }, 20);
                                        }, 20);
                                    }, 20);
                                }, 20);
                            }, 20);
                        }, 20);
                    }, 20);
                }, 20);

                return false;
            }
        });

        addTest(new EternaTest("playerSkin") {

            private final PlayerSkin testSkin = new PlayerSkin(
                    "ewogICJ0aW1lc3RhbXAiIDogMTcxNjU2NDQ4MzU5NSwKICAicHJvZmlsZUlkIiA6ICIyZGI4ZTYzYzFlMTc0NGE3ODIyZDNmNjBlYmNmYzI5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBcmtTYW5kYm94IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRjYTBkZGI2YTM0MWYyZmJhNDU5NWNiNmZlMWUzYmYxZTk2NGFmZDVjMDExZWU3ZDMxM2JiMDllNDZhNTIzMGIiCiAgICB9CiAgfQp9",
                    "QXdm98wIhaFJs4JeCFiy//EPfkyuHs2Ya7fH4uVnqlQ157b7p9z7cY1vKrGPoTZzTuRbXD6nNFPKwNjgFWyo+gUAW5kuoB0ZcaSquOl+Tvoi9w8z9jaG3xEIOhHxwf+5P6rJsP3blEFBko3invkjKreIUScugJXF0oWV+sY230jZ/uQZJ2faQBx0c6Ls8ISGsS2n3Vmr+rUs0TUo2vh6xQF+ZlQGyHg2rBAvjoeguD8x0hyqACzW3FTp8cy1hSMQc0NNWZ5x0aBIrJ4NTmOdBgbQdeZ1VYKmpQjgSTSMEmW4NUDWuSugekTWR+OsySaw0cvT9h2X0DSCrYjm5QiyB6O0tG4EIAweGR9kVOHZ6ib9bVIhuFy3BjKuW8TtbeBEBiw4t3ZAjSUgPHB8pXYzReZa4dqgVqX6cho7cF71asx8ZO6K12aS9dSs1SU7+Jp7WuIiVA7bsyCkoUJJD6DwQGBN+mp4s+pQbkpb78ZcojGm7Be0hvDm9trqSLOLZLpKLp7YxR/wHGKdQ1s86SLaMubwGmuD0RpjNW+EoSANYNMPoq3rpIk/CVVfJSR90Sx1S7vvIV5BkgiBDWcTSN7hbxAldRnB2y3gCgB4+TC4geZFdWVCOYZgMZTNBS2WotO/h619cZoEcKMFol//kB4DtvaRGxc9P6Y4Vp/C3mMrWnU="
            );

            private PlayerSkin playerSkin = null;

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                if (playerSkin == null) {
                    playerSkin = PlayerSkin.of(player);
                    info(player, "Saved skin!");
                }

                final String value = args.getString(0);

                if (value.equalsIgnoreCase("apply")) {
                    info(player, "Applying skin...");

                    testSkin.apply(player);
                    return false;
                }
                else if (value.equalsIgnoreCase("reset")) {
                    info(player, "Resetting skin...");

                    playerSkin.apply(player);
                    return true;
                }

                assertFail("Missing first argument (value), must be either: 'apply' or 'reset'.");
                return false;
            }

        });

        addTest(new EternaTest("cbClass") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final Class<?> clazz = Reflect.getCraftClass("entity.CraftPlayer");

                player.sendMessage(clazz.toString());
                return true;
            }
        });

        addTest(new EternaTest("hideFlags") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final ItemStack item = new ItemBuilder(Material.IRON_PICKAXE)
                        .hideFlags()
                        .build();

                final ItemStack item2 = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .hideFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build();

                player.getInventory().addItem(item);
                player.getInventory().addItem(item2);
                return true;
            }
        });

        addTest(new EternaTest("bformat") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final String format = BFormat.format("%s $$ {} :?", "$");

                Chat.sendMessage(player, format);
                return true;
            }
        });

        addTest(new EternaTest("mapWrap") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                Map<String, Integer> map = Map.of(
                        "hello", 1,
                        "world", 2,
                        "foo", 3,
                        "bar", 4
                );

                info(player, CollectionUtils.wrapToString(map, MapWrap.ofDefault()));
                return true;
            }
        });

        addTest(new EternaTest("itemComponents") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final ItemBuilder foodItem = new ItemBuilder(Material.DIAMOND_SWORD)
                        .setName("FOOD")
                        .setFood(food -> {
                            food.setCanAlwaysEat(true);
                            food.setEatSeconds(6.9f);
                            food.setSaturation(100);
                            food.setNutrition(100);
                            food.setUsingConvertsTo(new ItemStack(Material.BEDROCK));

                            final FoodComponent.FoodEffect effect = food.addEffect(
                                    new PotionEffect(PotionEffectType.SLOWNESS, 60, 6),
                                    1.0f
                            );
                        });

                final ItemBuilder toolItem = new ItemBuilder(Material.IRON_INGOT)
                        .setName("TOOL")
                        .setTool(tool -> {
                            tool.setDamagePerBlock(1);
                            tool.setDefaultMiningSpeed(1.5f);

                            tool.addRule(Material.GLASS, 5f, true);
                        });

                player.getInventory().addItem(foodItem.build());
                player.getInventory().addItem(toolItem.build());

                return true;
            }
        });

        addTest(new EternaTest("colorConverter") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final net.md_5.bungee.api.ChatColor color = ColorConverter.DYE_COLOR.toChatColor(DyeColor.GREEN);

                info(player, color + "THIS IS THE COLOR");

                convertAllAndInfo(player, ColorConverter.JAVA_COLOR, new java.awt.Color(123, 123, 123));
                convertAllAndInfo(player, ColorConverter.BUNGEE_CHAT_COLOR, net.md_5.bungee.api.ChatColor.RED);
                convertAllAndInfo(player, ColorConverter.CHAT_COLOR, ChatColor.BLUE);
                convertAllAndInfo(player, ColorConverter.BUKKIT_COLOR, Color.fromRGB(69, 69, 69));
                convertAllAndInfo(player, ColorConverter.DYE_COLOR, DyeColor.GREEN);
                return true;
            }

            private <E> void convertAllAndInfo(Player player, ColorConverter.Converter<E> converter, E e) {
                info(player, "----[ %s ] ----".formatted(e.toString()));
                info(player, "javaColor=" + String.valueOf(converter.toJavaColor(e)));
                info(player, "bungeeChatColor=" + String.valueOf(converter.toChatColor(e)));
                info(player, "chatColor=" + String.valueOf(converter.toBukkitColor(e)));
                info(player, "textColor=" + String.valueOf(converter.toTextColor(e)));
                info(player, "");
            }
        });

        addTest(new EternaTest("getKey") {

            Keyed dummyKeyed = new Keyed() {
                @Override
                //@Nonnull
                public NamespacedKey getKey() {
                    return null;
                }
            };

            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final NamespacedKey boldKey = BukkitUtils.getKey(TrimPattern.BOLT);
                final NamespacedKey amethystKey = BukkitUtils.getKey(TrimMaterial.AMETHYST);
                final NamespacedKey kebabKey = BukkitUtils.getKey(Art.KEBAB);

                final NamespacedKey dummyKey = BukkitUtils.getKey(dummyKeyed);
                final NamespacedKey nullKey = BukkitUtils.getKey(null);

                assertTrue(boldKey != null);
                assertTrue(amethystKey != null);
                assertTrue(kebabKey != null);

                assertEquals(dummyKey, BukkitUtils.DUMMY_KEY);
                assertTrue(BukkitUtils.isDummyKey(nullKey));

                return true;
            }
        });

        addTest(new EternaTest("collectionUtils") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final List<String> list = List.of("hello", "world", "goobye", "sunday");

                final String le0 = CollectionUtils.get(list, 0);
                final String le3 = CollectionUtils.get(list, 3);
                final String le99 = CollectionUtils.get(list, 99);
                final String le_1 = CollectionUtils.getOrDefault(list, -1, "yep");

                assertEquals(le0, list.get(0));
                assertEquals(le3, list.get(3));
                assertNull(le99);
                assertEquals(le_1, "yep");

                final Set<Integer> set = Sets.newLinkedHashSet(List.of(0, 1, 2, 3, 4, 5, 6));

                final Integer se2 = CollectionUtils.get(set, 2);
                final Integer se5 = CollectionUtils.get(set, 5);
                final Integer se_1 = CollectionUtils.get(set, -1);
                final Integer se_69 = CollectionUtils.getOrDefault(set, -1, 69);

                assertEquals(se2, 2);
                assertEquals(se5, 5);
                assertNull(se_1);
                assertEquals(se_69, 69);

                return true;
            }
        });

        addTest(new EternaTest("displayStudioInterpolation") {

            final DisplayData data = BDEngine.parse(
                    "/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0f,-1f,0f,0.5f,1f,0f,0f,0f,0f,0f,1f,-0.5f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0f,-1f,0f,0.5f,1f,0f,0f,1f,0f,0f,1f,-0.5f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:stone_sword\",Count:1},item_display:\"none\",transformation:[0.7071f,0.7071f,0f,0f,-0.7071f,0.7071f,0f,2.25f,0f,0f,1f,0f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:stone_sword\",Count:1},item_display:\"none\",transformation:[0f,0f,-1f,0f,-0.7071f,0.7071f,0f,2.25f,0.7071f,0.7071f,0f,0f,0f,0f,0f,1f]}]}"
            );

            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                data.spawnInterpolated(
                        player.getLocation(),
                        player.getLocation().add(0, 5, 0),
                        self -> {
                            self.setTeleportDuration(5);
                        }
                );

                return false;
            }
        });

        addTest(new EternaTest("soundQueue") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final SoundQueue soundQueue = new SoundQueue()
                        .append(Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 2)
                        .appendSameSound(Sound.ENCHANT_THORNS_HIT, 1.0f, 1, 5, 7)
                        .appendSameSound(Sound.BLOCK_LAVA_POP, 2, 0.5f, 0.75f, 1.0f);

                soundQueue.play(player);
                return true;
            }
        });

        addTest(new EternaTest("locationHelper") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final Location location = player.getLocation();

                LocationHelper.offset(location, 1, 2, 3, then -> {
                    PlayerLib.spawnParticle(location, Particle.FLAME, 1);
                });

                LocationHelper.offset(location, 3, 2, 1, then -> {
                    return Entities.PIG.spawn(location);
                });

                LocationHelper.offset(location, 1, 3, 2, () -> {
                    PlayerLib.spawnParticle(location, Particle.HAPPY_VILLAGER, 1);
                });

                return true;
            }
        });

        addTest(new EternaTest("iterators") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                List<String> list = Lists.newArrayList(
                        "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit", "sed", "do"
                );

                CollectionUtils.iterate(list, (iterator, item) -> {
                    if (item.equalsIgnoreCase("sit")) {
                        iterator.remove();
                        info(player, "Removed 'sit'");
                        return;
                    }

                    info(player, "> " + item);
                });

                info(player, list);
                return true;
            }
        });

        addTest(new EternaTest("mapMaker") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final MapMaker<Integer, String, LinkedHashMap<Integer, String>> mapMaker = MapMaker.<Integer, String>ofLinkedHashMap()
                        .put(1, "hello")
                        .put(2, "world");

                final Map<Integer, String> map = mapMaker.makeMap();
                final LinkedHashMap<Integer, String> genericMap = mapMaker.makeGenericMap();
                final Map<Integer, String> immutableMap = mapMaker.makeImmutableMap();

                info(player, map);

                genericMap.remove(1);
                info(player, genericMap);

                assertThrows(() -> {
                    immutableMap.remove(1);
                });

                info(player, immutableMap);
                return true;
            }
        });

        addTest(new EternaTest("cache") {

            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                Cache<String> listCacheString = Cache.ofList(1_000);

                listCacheString.add("hello");
                listCacheString.add("world");
                listCacheString.add("my");
                listCacheString.add("name");
                listCacheString.add("is");
                listCacheString.add("EternaAPI");
                listCacheString.add("lymme");
                listCacheString.add("gynn");
                listCacheString.add("fiyl");

                final Cache<Object> setCacheInt = Cache.ofSet(1_000);

                setCacheInt.add(1);
                setCacheInt.add(1);
                setCacheInt.add(1);
                setCacheInt.add(1);

                // check set
                assertTrue(setCacheInt.size() == 1);

                info(player, "setCacheInt=" + setCacheInt);

                final String firstY = listCacheString.match(s -> s.contains("y"));
                final List<String> allY = listCacheString.matchAll(s -> s.contains("y"));

                info(player, "listCacheString=" + listCacheString);
                info(player, "firsrY=" + firstY);
                info(player, "allY=" + allY);

                later(() -> {
                    assertTrue(listCacheString.isEmpty());
                    assertTrue(setCacheInt.isEmpty());
                    assertTestPassed();
                }, 21);

                return false;
            }
        });

        addTest(new EternaTest("displayAnimation") {

            private final DisplayData data = BDEngine.parse(
                    "/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:andesite\",Properties:{}},transformation:[1f,0f,0f,0f,0f,1f,0f,0f,0f,0f,1f,0f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:bamboo_block\",Properties:{axis:\"x\"}},transformation:[1f,0f,0f,-0.5625f,0f,1f,0f,1f,0f,0f,1f,0f,0f,0f,0f,1f]}]}"
            );

            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final Location original = player.getLocation();
                final DisplayEntity entity = data.spawnInterpolated(original);

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
                        .addFrame(AnimationFrame.builder()
                                .threshold(3)
                                .increment(Math.PI / 2)
                                .tick((frame, slf, theta) -> {
                                    slf.teleport(slf.getLocation().add(0, 1, 0));
                                })
                        ).addFrame(new AnimationFrame() {
                            @Override
                            public void tick(@NotNull DisplayEntity entity, double theta) {
                                entity.teleport(original);
                                assertTestPassed();
                            }
                        })
                        .start();

                return false;
            }
        });

        addTest(new EternaTest("typeConverter") {
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final int int1 = TypeConverter.from(1).toInt();
                final long long2 = TypeConverter.from(2L).toLong();
                final double double3 = TypeConverter.from(3.0d).toDouble();
                final String string = TypeConverter.from(new Object() {
                    @Override
                    public String toString() {
                        return "objstr";
                    }
                }).toString();
                final Material anEnum = TypeConverter.from("GHAST_SPAWN_EGG").toEnum(Material.class);

                assertTrue(int1 == 1);
                assertTrue(long2 == 2L);
                assertTrue(double3 == 3.0d);
                assertEquals(string, "objstr");
                assertTrue(anEnum != null);
                assertTrue(anEnum == Material.GHAST_SPAWN_EGG);

                // bool
                final boolean trueBool = TypeConverter.from("true").toBoolean();
                final boolean TRueBool = TypeConverter.from("TRue").toBoolean();
                final boolean falseBool = TypeConverter.from("false").toBoolean();
                final boolean flsBool = TypeConverter.from("fls").toBoolean();

                assertTrue(trueBool == true);
                assertTrue(trueBool == true);
                assertTrue(falseBool == false);
                assertTrue(flsBool == false);

                return true;
            }
        });

        addTest(new EternaTest("quest") {

            private QuestHandler registry;
            private HumanNPC npc1;
            private HumanNPC npc2;
            private HumanNPC npc3;

            private List<Quest> completedQuests = Lists.newArrayList();

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                if (registry == null) {
                    final Config config = new Config(EternaPlugin.getPlugin(), "quest_data");

                    registry = new QuestHandler(EternaPlugin.getPlugin()) {
                        @Override
                        public void saveQuests(@Nonnull Player player, @Nonnull Set<QuestData> questDataSet) {
                        }

                        @Nonnull
                        @Override
                        public Set<QuestData> loadQuests(@Nonnull Player player) {
                            return Set.of();
                        }

                        @Override
                        public boolean hasCompleted(@Nonnull Player player, @Nonnull Quest quest) {
                            return completedQuests.contains(quest);
                        }

                        @Override
                        public void completeQuest(@Nonnull Player player, @Nonnull Quest quest) {
                            completedQuests.add(quest);
                        }
                    };

                    final Location location = player.getLocation();

                    npc1 = new HumanNPC(location, "Test Npc", "hapyl");
                    npc1.show(player);

                    npc2 = new HumanNPC(location.add(1, 0, 0), "Test Npc 2", "DiDenPro");
                    npc2.show(player);

                    npc3 = new HumanNPC(location.add(-2, 0, 0), "Test Npc 3", "sdimas74");
                    npc3.show(player);

                    final QuestChain questChain = new QuestChain(Key.ofString("test_chain"));

                    final Quest quest1 = new Quest(EternaPlugin.getPlugin(), Key.ofString("quest1"));
                    quest1.addObjective(new JumpQuestObjective(1));
                    quest1.addStartBehaviour(QuestStartBehaviour.onJoin());

                    final Quest quest2 = new Quest(EternaPlugin.getPlugin(), Key.ofString("quest2"));
                    quest2.addObjective(new JumpQuestObjective(2));
                    quest2.addStartBehaviour(QuestStartBehaviour.talkToNpc(
                            npc1,
                            new Dialog().addEntry(DialogEntry.of(npc1, "Yes talk to me!"))
                    ));

                    final Quest quest3 = new Quest(EternaPlugin.getPlugin(), Key.ofString("quest3"));
                    quest3.addObjective(new JumpQuestObjective(3));
                    quest3.addStartBehaviour(QuestStartBehaviour.talkToNpc(
                            npc2,
                            new Dialog().addEntry(DialogEntry.of(npc2, "Oh yes do talk to me next quest will automatically start!"))
                    ));

                    final Quest quest4 = new Quest(EternaPlugin.getPlugin(), Key.ofString("quest4"));
                    quest4.addObjective(new JumpQuestObjective(4));

                    questChain.addQuests(quest1, quest2, quest3, quest4);
                    registry.register(questChain);
                }
                else {
                    final String argument = args.getString(0);

                    switch (argument.toLowerCase()) {
                        case "clear" -> {
                            completedQuests.clear();
                        }
                        case "npc" -> {
                            npc1.show(player);
                            npc2.show(player);
                            npc3.show(player);
                        }
                        default -> {
                            final QuestDataList questData = Eterna.getManagers().quest.get(player);

                            if (questData != null) {
                                questData.forEach(data -> {
                                    EternaLogger.debug(data.toString());

                                    final QuestObjective objective = data.getCurrentObjective();
                                    if (objective != null) {
                                        EternaLogger.debug("Current Objective: " + objective.toString());
                                        EternaLogger.debug("Progress: %s/%s".formatted(data.getCurrentStageProgress(), objective.getGoal()));
                                    }
                                });
                            }
                        }
                    }
                }

                return false;
            }
        });

        addTest(new EternaTest("bdenginemultipart") {

            private final DisplayData data = BDEngine.parse(
                    "/summon block_display ~-0.5 ~ ~-0.5 {Tags:[root], Passengers:[{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,2.3863f,0f,0.1235f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,2.4265f,0f,0.027f,0f,0.1235f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,2.3863f,0f,0.1235f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,2.4265f,0f,0.027f,0f,0.1494f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,0.0873f,0f,2.1244f,-0.2619f,0.0873f,0f,0.2619f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,0.0191f,0f,2.2401f,-0.2021f,0.0191f,0f,0.3208f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,0.0873f,0f,2.1244f,-0.2619f,0.0873f,0f,0.2619f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,0.0191f,0f,2.2585f,-0.2021f,0.0191f,0f,0.3391f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,0.0873f,0f,2.384f,-0.2619f,-0.0873f,0f,0.8941f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,0.0191f,0f,2.4408f,-0.2021f,-0.0191f,0f,0.7754f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,0.0873f,0f,2.384f,-0.2619f,-0.0873f,0f,0.8941f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,0.0191f,0f,2.4592f,-0.2021f,-0.0191f,0f,0.757f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,0.1235f,0f,2.123f,-0.3704f,0f,0f,0.6322f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,0.027f,0f,2.2465f,-0.2858f,0f,0f,0.5877f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,0.1235f,0f,2.123f,-0.3704f,0f,0f,0.6322f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,0.027f,0f,2.2724f,-0.2858f,0f,0f,0.5877f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,2.3863f,0f,0.1235f,0f,0.7706f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,2.4308f,0f,0.027f,0f,0.7437f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,2.3863f,0f,0.1235f,0f,0.7706f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,2.4308f,0f,0.027f,0f,0.7178f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,0.0873f,0f,2.6694f,-0.2619f,0.0873f,0f,0.8068f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,0.0191f,0f,2.6818f,-0.2021f,0.0191f,0f,0.7563f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,0.0873f,0f,2.6694f,-0.2619f,0.0873f,0f,0.8068f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,0.0191f,0f,2.6635f,-0.2021f,0.0191f,0f,0.738f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,0.0873f,0f,2.9337f,-0.2619f,-0.0873f,0f,0.3492f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,0.0191f,0f,2.8852f,-0.2021f,-0.0191f,0f,0.3399f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,0.0873f,0f,2.9337f,-0.2619f,-0.0873f,0f,0.3492f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,0.0191f,0f,2.8669f,-0.2021f,-0.0191f,0f,0.3582f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,0.1235f,0f,2.8966f,-0.3704f,0f,0f,0.6324f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,0.027f,0f,2.8696f,-0.2858f,0f,0f,0.5922f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,0.1235f,0f,2.8966f,-0.3704f,0f,0f,0.6324f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,0.027f,0f,2.8437f,-0.2858f,0f,0f,0.5922f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.5556f,0f,0f,2.298f,0f,0.5595f,0f,0.176f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0f,-0.5595f,0f,2.8463f,0.5556f,0f,0f,0.1766f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.3929f,-0.3956f,0f,2.5731f,0.3929f,0.3956f,0f,0.0559f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.3929f,0.3956f,0f,2.1786f,-0.3929f,0.3956f,0f,0.4488f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.1892f,0f,0f,2.4781f,0f,0.1852f,0f,0.3649f,0f,0f,0.1172f,0.4797f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1124f,0f,0f,0.9243f,0f,0.1547f,0f,1.1395f,0f,0f,0.2648f,0.4103f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1176086259,-476517789,-244091818,-485758749],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1213f,-0.1064f,0f,1.049f,0f,0f,-0.2944f,1.1604f,0.1228f,0.1077f,0f,0.5635f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;2022385831,-676319958,-716677132,345000748],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0943f,-1.2903f,1.4653f,0f,0.6881f,0.1767f,1.2407f,0.9295f,0f,0f,0.5385f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0f,-0.1307f,0.1245f,1.1435f,0.1133f,0f,0f,1.033f,0f,0.1323f,0.126f,0.4018f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1683940568,-1424624318,-406788946,-1373920059],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.3241f,0f,0f,1.2849f,0f,0.247f,0f,1.323f,0f,0f,0.3125f,0.5403f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1876790045,1276806962,459155598,-1442708742],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0136f,-1.3017f,1.4363f,0f,0.4586f,0.0386f,1.0539f,1.099f,0f,0f,0.5385f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:blast_furnace\",Properties:{facing:\"east\",lit:\"false\"}},transformation:[0.6442f,0.0035f,-0.0002f,1.1416f,-0.0928f,0.0241f,0.0002f,1.2721f,0.0008f,0f,0.1563f,0.46f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-281181163,-1056462683,605135636,-1213171291],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2331f,-0.0116f,0.1945f,1.1762f,0.0286f,-0.0014f,-1.5841f,0.8021f,0.0119f,0.2363f,0f,0.7951f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;661406857,-730891533,-1891703304,-1297528621],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0012f,0f,1.729f,1.7347f,0.2351f,0f,0.0091f,0.3482f,0f,0.2366f,0f,0.7944f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-852247414,-1932812276,1752807763,572167309],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0521f,0f,1.6375f,2f,0.2293f,0f,-0.372f,0.8323f,0f,0.2366f,0f,0.7964f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1156681659,-118690166,-2106981757,1314362217],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2122f,0f,0.0036f,2.5681f,0.0011f,-0.0011f,0.6791f,0.5208f,0f,0.2188f,0.0036f,0.8393f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weathered_copper\",Properties:{}},transformation:[0.55f,0f,0f,2.2952f,0f,0.028f,0f,1.0082f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weathered_copper\",Properties:{}},transformation:[0.2619f,-0.0198f,0f,2.0472f,0.2619f,0.0198f,0f,0.7518f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.0264f,-0.0276f,0f,2.0307f,0.1769f,0.0041f,0f,0.5972f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.5294f,0f,0f,2.3117f,0f,0.0146f,-0.0947f,1.0218f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2578f,-0.0104f,0.0669f,2.0427f,0.2578f,0.0104f,-0.0669f,0.7639f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0253f,-0.0145f,0.0936f,2.0163f,0.169f,0.0022f,-0.014f,0.5996f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weathered_copper\",Properties:{}},transformation:[0.5294f,0f,0f,2.3134f,0f,0.0146f,0.0947f,0.9271f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.2578f,-0.0104f,-0.0669f,2.1096f,0.2578f,0.0104f,0.0669f,0.697f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.0275f,-0.0145f,-0.0936f,2.1089f,0.1843f,0.0022f,0.014f,0.5789f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2702f,0.0044f,0f,1.943f,-0.0428f,0.0276f,0f,0.9955f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:waxed_exposed_copper\",Properties:{}},transformation:[0.2619f,0.0198f,0f,2.8175f,-0.2619f,0.0198f,0f,1.0195f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0411f,0.0276f,0f,3.0702f,-0.2748f,0.0041f,0f,0.7751f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:waxed_exposed_copper\",Properties:{}},transformation:[0.2578f,0.0104f,-0.0669f,2.8261f,-0.2578f,0.0104f,-0.0669f,1.0276f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:waxed_exposed_copper\",Properties:{}},transformation:[0.0404f,0.0145f,-0.0936f,3.0855f,-0.2704f,0.0022f,-0.014f,0.7711f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2578f,0.0104f,0.0669f,2.7592f,-0.2578f,0.0104f,0.0669f,0.9606f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0404f,0.0145f,0.0936f,2.9919f,-0.2704f,0.0022f,0.014f,0.7571f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-275107240,1434358461,-1861606057,450017469],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0f,-0.007f,2.4498f,-0.0045f,0f,-0.3627f,0.5877f,0f,0.2366f,0f,0.7964f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-983593658,1869747208,1367120630,-1196955635],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0535f,0.0428f,-1.2783f,1.4523f,-0.3567f,0.3392f,0.1764f,1.1587f,0.366f,0.3452f,-0.0154f,0.856f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-377211735,1036417341,585817325,-63008058],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0321f,0.0518f,-1.2443f,1.45f,-0.2326f,0.4144f,0.1593f,1.0966f,0.4116f,0.2144f,-0.0008f,0.8272f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1092699481,-1111954207,-250089244,-1561585444],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0535f,0.0428f,1.2783f,1.4523f,-0.3567f,0.3392f,-0.1764f,1.1587f,-0.366f,-0.3452f,-0.0154f,0.2104f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;288854540,-34331039,1244749389,-220276854],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0299f,0.05f,1.2441f,1.4495f,-0.2318f,0.415f,-0.1524f,1.0967f,-0.4116f,-0.2137f,0.0014f,0.2393f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;254058904,-314697492,-1993921578,-1896560939],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0008f,-0.2634f,1.2335f,0f,0.2337f,0.0009f,0.4038f,0.238f,0f,0f,0.7327f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1996471901,1989018322,-1988519406,1267819112],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1308f,0f,-0.2986f,2.3201f,-0.1954f,0f,-0.1999f,0.4472f,0f,0.2366f,0f,0.7964f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-200656596,1067471063,-1085009943,-1084671617],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.1097f,-0.2326f,2.1703f,0f,0.2064f,-0.1237f,0.4199f,0.238f,0f,0f,0.736f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-302270906,592364779,1047409377,481704195],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.1429f,-0.188f,2.3979f,0f,0.1849f,-0.1453f,0.5716f,0.238f,0f,0f,0.7366f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1194433194,-1974045523,1130696675,432332449],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.009f,0.2374f,2.4511f,0f,-0.2335f,-0.0091f,0.6762f,0.238f,0f,0f,0.7366f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;319443382,953086931,-2099909550,497101391],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2331f,-0.0116f,-0.1945f,1.1708f,0.0286f,-0.0014f,1.5841f,0.8015f,-0.0119f,-0.2363f,0f,0.2731f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1674898492,657279577,-453944897,878174959],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0012f,0f,1.729f,1.7368f,0.2351f,0f,0.0091f,0.3492f,0f,0.2366f,0f,0.3907f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;657513731,-1319754053,-1597421203,-1009380700],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0521f,0f,-1.6375f,2.0226f,0.2293f,0f,0.372f,0.835f,0f,-0.2366f,0f,0.2704f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1067417487,520960789,-454504824,-1505496133],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2122f,0f,0.0036f,2.5681f,0.0011f,-0.0011f,0.6791f,0.5245f,0f,0.2188f,0.0036f,0.3405f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1794827769,-920446163,-1615530120,-982130649],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0f,-0.007f,2.456f,-0.0045f,0f,-0.3627f,0.5901f,0f,0.2366f,0f,0.3887f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;880259150,1537311525,-1298980678,2048852518],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0008f,-0.2634f,1.235f,0f,0.2337f,0.0009f,0.4044f,0.238f,0f,0f,0.334f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;340686424,-1711459390,960281193,-1739175567],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1197f,0f,0.3093f,2.3325f,-0.2024f,0f,0.1829f,0.4382f,0f,-0.2366f,0f,0.2704f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-652910122,1790677426,-2029269655,-816993979],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.1097f,-0.2326f,2.168f,0f,0.2064f,-0.1237f,0.4164f,0.238f,0f,0f,0.3308f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-972840283,95015517,-400676521,-889577650],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.1429f,-0.188f,2.397f,0f,0.1849f,-0.1453f,0.5566f,0.238f,0f,0f,0.3302f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;178118692,-1948286535,-1657469128,-365661447],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.009f,0.2374f,2.446f,0f,-0.2335f,-0.0091f,0.68f,0.238f,0f,0f,0.3302f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-831606468,-942308013,-174740232,1742386091],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.15f,0.1528f,0.0044f,2.603f,0.15f,-0.1528f,-0.0044f,0.4216f,0f,0.0016f,-0.8592f,0.5295f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-17932528,1083490309,614497845,1982582052],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0521f,-0.2279f,-0.0003f,1.943f,0.2293f,0.0518f,-0.0013f,0.8452f,0.0004f,0f,0.7756f,0.5624f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1772320227,-978375638,-805710280,1714579416],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.3437f,0.3646f,0f,1.8682f,-0.3354f,0.3736f,0f,1.1212f,0f,0f,0.9064f,0.5385f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1968564800,651525041,51956641,-1377755632],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.188f,-0.1958f,-0.1137f,1.7312f,-0.0745f,-0.0007f,-0.5458f,0.9725f,0.1791f,0.2103f,-0.1103f,0.8267f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;613229293,151414610,717468430,-2117424572],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5YjAyZjExYTIyYmIyNGRiNzBiYzliOGZhMmM2MzM1ZGE5ZTVkNDBkM2YwZmQwMmJlN2RmZWVkZWM1NzQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.188f,-0.1958f,0.1137f,1.7258f,-0.0745f,-0.0007f,0.5458f,0.968f,-0.1791f,-0.2103f,-0.1103f,0.2393f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_wool\",Properties:{}},transformation:[-0.0211f,0.0484f,0f,2.2005f,-0.0872f,-0.0117f,0f,1.0939f,0f,0f,0.3094f,0.3785f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_wool\",Properties:{}},transformation:[0.1571f,0.0169f,0.1578f,1.777f,-0.0418f,0.0594f,-0.0478f,1.0566f,-0.1669f,0.0011f,0.1646f,0.5339f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_wool\",Properties:{}},transformation:[0.2863f,0.0065f,0f,1.9424f,-0.0423f,0.0439f,0f,1.0111f,0f,0f,0.375f,0.3441f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;462301837,-38698858,1420678757,-1541967078],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0001f,-0.2279f,-0.0862f,1.9624f,-0.0004f,0.0518f,-0.3793f,0.9303f,0.238f,0f,-0.0007f,0.5432f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1142935406,537392456,1787057949,1915359089],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1983f,0f,-0.2314f,2.5008f,-0.1263f,0f,-0.3632f,0.8564f,0f,0.2366f,0f,0.7964f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1822156183,-1811215269,1947285017,-410220273],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1983f,0f,-0.2314f,2.5054f,-0.1263f,0f,-0.3632f,0.8564f,0f,0.2366f,0f,0.397f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.1516f,0f,0f,2.4906f,0f,0.1148f,0f,0.9191f,0f,0f,0.4746f,0.298f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1067464189,881728101,1841326642,938715101],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2351f,0f,0f,2.5653f,0f,-0.3785f,0f,0.5075f,0f,0f,0.2344f,0.785f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;280121077,-436064048,324069579,1060713108],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2351f,0f,0f,2.5653f,0f,-0.3785f,0f,0.5083f,0f,0f,0.2344f,0.2875f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1678926876,1906721093,2135553066,-2089924581],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU0OGJhYjNjNDhkYzg1ZWQzMzI4YWQ4YWM4NzE5ZDBjMTM0ODM5NzFkODE4ZWU2ZDIwOTc0ZDkxYTZlYjVlZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.4835f,0f,0f,1.8912f,0f,0f,0.3894f,0.3193f,0f,-0.5175f,0f,0.3979f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-905373489,1182561835,1388843709,741158769],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkYTllZmZmMzY3ZWMxOTY3YzQwODA0NTFjMjNhMDBjMDhlMzRmNThiNTFmOTNlNmE5ZWE0ZTZlYjJiNzBjMiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.4835f,0f,1.7703f,0f,0f,0.3894f,0.514f,-0.5175f,0f,0f,0.5273f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1008984498,1922862785,1151555348,70707044],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.4786f,0f,0f,1.8912f,0f,0.3855f,0f,0.4896f,0f,0f,-0.5123f,0.5273f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;999178862,149658074,-1170638318,1000677394],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODRmYzU2ZDRiOGI3ZGNkZTlhNzQ4NzQ3MzQ1NDhiZDYyNzY4ZTFmY2E0ZTYzYjRlM2E1YmJjYTViYjMwYWE3MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.289f,0.289f,0f,1.7342f,-0.2661f,0.2661f,0f,0.7115f,0f,0f,0.5391f,0.3655f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1778819395,281305391,1826094892,123141110],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2919f,0f,-0.2919f,1.662f,0.2687f,0f,-0.2687f,0.645f,0f,-0.5444f,0f,0.2301f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:blast_furnace\",Properties:{facing:\"north\",lit:\"false\"}},transformation:[-0.4671f,0f,0f,1.662f,0f,0.3763f,0f,0.3158f,0f,0f,-0.5f,0.7819f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_deepslate_slab\",Properties:{type:\"bottom\"}},transformation:[0f,0.4671f,0f,1.662f,0f,0f,-0.3763f,0.692f,-0.5f,0f,0f,0.7819f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1681270912,-550464912,161671066,-1981675586],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjYwNGFkY2JkN2Y3NmE4Yjg0Y2IzMDY2ZWRmMTUxNTQ4ZWEzMmJlMWRjNzA3ODFiZTU2M2Q0M2NiMThhMzk5YiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.4671f,0f,0f,1.662f,0f,0.4233f,0f,0.7861f,0f,0f,-0.5f,0.6575f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-783109242,-1551620401,2053783825,-304897688],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.4671f,0f,0f,1.4284f,0f,0.4206f,0f,0.7855f,0f,0f,-0.5f,0.5319f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1313015839,-485383039,984117712,-99816633],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU0OGJhYjNjNDhkYzg1ZWQzMzI4YWQ4YWM4NzE5ZDBjMTM0ODM5NzFkODE4ZWU2ZDIwOTc0ZDkxYTZlYjVlZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.4671f,0f,0f,1.4284f,0f,0f,-0.4206f,0.8118f,0f,-0.5f,0f,0.4069f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1257102239,1929817979,-1549053900,1371032066],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ==\"}]}}},item_display:\"none\",transformation:[0.4939f,0f,0f,1.4176f,0f,0f,-0.5672f,0.5698f,0f,0.3008f,0f,0.8653f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-2030066014,425268078,192753193,162325303],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.0012f,-0.2337f,0f,1.1796f,0.2351f,-0.0012f,0f,0.3453f,0f,0f,0.7658f,0.5403f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;968832255,2007267692,1595778338,-1861489549],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0f,0.004f,2.174f,0.0012f,0f,-0.7564f,0.5751f,0f,0.2366f,0f,0.7944f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;873231071,1076270721,1659789613,924224299],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0f,0.004f,2.174f,0.0012f,0f,-0.7564f,0.5751f,0f,0.2366f,0f,0.3907f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:acacia_trapdoor\",Properties:{facing:\"east\",half:\"bottom\",open:\"false\"}},transformation:[0.4168f,0f,0f,2.361f,0f,0.1744f,0f,1.0437f,0f,0f,0.4999f,0.2854f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;122473973,181939982,1886650363,-306726866],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.0442f,0.2587f,2.1684f,0f,-0.2295f,-0.0498f,0.737f,0.238f,0f,0f,0.736f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-269714459,-1925060921,-276968421,-492622335],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjliZTg5NGFkMTBmZTQ4NGEyZDRmMjg0ZDZhOTBkZDBjYmJjNDI4YjNlMzAxYWY5MGU4YzBjNjBjMDY2YTg0YyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.0442f,0.2587f,2.1694f,0f,-0.2295f,-0.0498f,0.7424f,0.238f,0f,0f,0.3278f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_trapdoor\",Properties:{facing:\"east\",half:\"bottom\",open:\"false\"}},transformation:[0.1313f,0f,-0.1313f,2.5775f,-0.1313f,0f,-0.1313f,0.5907f,0f,0.125f,0f,0.6811f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0.9878f,0f,0f,1.7214f,0f,0.4322f,0f,0.3624f,0f,0f,0.2734f,0.5618f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0.9878f,0f,0f,1.7214f,0f,0.4322f,0f,0.1185f,0f,0f,0.2734f,0.5536f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0f,0.4322f,0f,2.4826f,-0.3125f,0f,0f,0.6174f,0f,0f,0.2734f,0.5536f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1216425361,-700253981,-1657514248,450069923],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDgzMDBjM2EyYjNhNGRmNDRhNmE1OWM3NTY2YjlhMmYxYTdkNWFlOTcxOWEwZWQzZGE1MGZmNWY4ZDViY2E4ZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0f,-0.1788f,2.966f,0f,0.1954f,0f,0.92f,0.188f,0f,0f,0.7178f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.055f,0.0493f,0f,2.8693f,-0.052f,0.0521f,0f,0.8684f,0f,0f,0.1012f,0.661f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:light_gray_wool\",Properties:{}},transformation:[0.1332f,0.0026f,0.0358f,1.9223f,-0.0393f,0.0093f,-0.0076f,1.072f,-0.0589f,-0.0003f,0.0882f,0.4611f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:light_gray_wool\",Properties:{}},transformation:[0.0325f,0.0013f,0.0854f,2.0365f,-0.0073f,0.0096f,-0.0093f,1.0335f,-0.0564f,-0.0005f,0.0516f,0.6453f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:light_gray_wool\",Properties:{}},transformation:[-0.0199f,0.0484f,0f,2.2035f,-0.0823f,-0.0117f,0f,1.0941f,0f,0f,0.058f,0.3776f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;23546867,985585297,1078760951,-1466809692],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVlNGNiNGZjMTRiYTY5ZDQ5YjJkZDJiMDQxOGQ2NTYyZGZmZGViZDBlYmYwMTY1YzQ0MTZhMDczZTYyZGRmOCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.6174f,0f,0f,2.5801f,0f,0.5016f,0f,1.0001f,0f,0f,-0.2344f,0.8041f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1157743421,970869475,-804819034,-1530896486],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVlNGNiNGZjMTRiYTY5ZDQ5YjJkZDJiMDQxOGQ2NTYyZGZmZGViZDBlYmYwMTY1YzQ0MTZhMDczZTYyZGRmOCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.6174f,0f,0f,2.5801f,0f,0.5016f,0f,1.0001f,0f,0f,0.2344f,0.2872f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1980744474,-22368190,2075191014,-1179726983],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1N2U0NmViMWVhZDkwNzFjZDhkNjEzYTgxY2Q1NWM1OTMxY2I2ZGJhZTRkZmJmMTQ1OGYyOGY5OWVlN2Y0NCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.614f,-0.0103f,0.0061f,1.5132f,-0.0292f,0.217f,0.0438f,1.189f,-0.0584f,-0.222f,0.0436f,0.8023f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-517764706,-303056686,-2134037992,930731444],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1N2U0NmViMWVhZDkwNzFjZDhkNjEzYTgxY2Q1NWM1OTMxY2I2ZGJhZTRkZmJmMTQ1OGYyOGY5OWVlN2Y0NCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2699f,-0.0066f,0.0015f,1.5134f,0.0082f,0.2139f,0.002f,0.3944f,-0.0069f,-0.007f,0.0624f,0.7816f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1551990647,-487852566,1960930965,1410844292],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1N2U0NmViMWVhZDkwNzFjZDhkNjEzYTgxY2Q1NWM1OTMxY2I2ZGJhZTRkZmJmMTQ1OGYyOGY5OWVlN2Y0NCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1556f,-0.175f,0.0014f,2.1487f,0.2198f,0.1233f,0.0047f,0.7865f,-0.0206f,-0.0054f,0.0623f,0.7865f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-135299326,-733225802,1791719826,-551086653],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1N2U0NmViMWVhZDkwNzFjZDhkNjEzYTgxY2Q1NWM1OTMxY2I2ZGJhZTRkZmJmMTQ1OGYyOGY5OWVlN2Y0NCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.1364f,-0.1846f,0.0026f,1.4239f,0.209f,-0.0921f,0.0287f,0.9234f,-0.1046f,0.0581f,0.0553f,0.2005f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,0.3434f,0f,0.1235f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,0.3878f,0f,0.027f,0f,0.1235f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,0.3434f,0f,0.1235f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,0.3878f,0f,0.027f,0f,0.1494f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,-0.0873f,0f,0.7138f,0.2619f,0.0873f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,-0.0191f,0f,0.6579f,0.2021f,0.0191f,0f,0.1187f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,-0.0873f,0f,0.7138f,0.2619f,0.0873f,0f,0f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,-0.0191f,0f,0.6396f,0.2021f,0.0191f,0f,0.1371f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,-0.0873f,0f,0.978f,0.2619f,-0.0873f,0f,0.6322f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,-0.0191f,0f,0.8614f,0.2021f,-0.0191f,0f,0.5733f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,-0.0873f,0f,0.978f,0.2619f,-0.0873f,0f,0.6322f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,-0.0191f,0f,0.843f,0.2021f,-0.0191f,0f,0.555f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,-0.1235f,0f,0.9771f,0.3704f,0f,0f,0.2618f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,-0.027f,0f,0.8536f,0.2858f,0f,0f,0.302f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,-0.1235f,0f,0.9771f,0.3704f,0f,0f,0.2618f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,-0.027f,0f,0.8277f,0.2858f,0f,0f,0.302f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,0.3434f,0f,0.1235f,0f,0.7706f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,0.3836f,0f,0.027f,0f,0.7437f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.3704f,0f,0f,0.3434f,0f,0.1235f,0f,0.7706f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2858f,0f,0f,0.3836f,0f,0.027f,0f,0.7178f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,-0.0873f,0f,0.1688f,0.2619f,0.0873f,0f,0.5449f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,-0.0191f,0f,0.2163f,0.2021f,0.0191f,0f,0.5542f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0.2619f,-0.0873f,0f,0.1688f,0.2619f,0.0873f,0f,0.5449f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.2021f,-0.0191f,0f,0.2346f,0.2021f,0.0191f,0f,0.5359f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,-0.0873f,0f,0.4284f,0.2619f,-0.0873f,0f,0.0873f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,-0.0191f,0f,0.417f,0.2021f,-0.0191f,0f,0.1378f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.2619f,-0.0873f,0f,0.4284f,0.2619f,-0.0873f,0f,0.0873f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[-0.2021f,-0.0191f,0f,0.4353f,0.2021f,-0.0191f,0f,0.1561f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,-0.1235f,0f,0.2036f,0.3704f,0f,0f,0.2619f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,-0.027f,0f,0.2305f,0.2858f,0f,0f,0.3064f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[0f,-0.1235f,0f,0.2036f,0.3704f,0f,0f,0.2619f,0f,0f,0.2491f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0f,-0.027f,0f,0.2565f,0.2858f,0f,0f,0.3064f,0f,0f,0.1563f,0.4616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.5556f,0f,0f,0.2464f,0f,0.5595f,0f,0.176f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0f,0.5595f,0f,0.2539f,-0.5556f,0f,0f,0.7322f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.3929f,0.3956f,0f,0.1341f,-0.3929f,0.3956f,0f,0.4488f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_bars\",Properties:{east:\"false\",north:\"false\",south:\"false\",west:\"false\"}},transformation:[0.3929f,-0.3956f,0f,0.5286f,0.3929f,0.3956f,0f,0.0559f,0f,0f,0.5396f,0.2685f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_andesite\",Properties:{}},transformation:[0.1892f,0f,0f,0.4329f,0f,0.1852f,0f,0.3649f,0f,0f,0.1172f,0.4797f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1662739117,1328620221,-836921029,1525097655],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.1832f,-0.0006f,0.4984f,0.6144f,0.1071f,-0.001f,0.8529f,0.6137f,0f,0.2188f,0.0052f,0.8153f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1218560275,-238432266,-1890746004,921992592],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2036f,-0.0926f,0f,0.7413f,0.1175f,-0.1604f,0f,0.8284f,0f,0f,0.2344f,0.7628f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-710592527,-1101243798,1736579352,-1259562534],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.204f,0f,-0.6247f,0.9435f,-0.1168f,0f,-1.0908f,1.1792f,0f,0.2366f,0f,0.8222f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-279751480,-559360169,-1414074316,1144375650],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.1832f,-0.0006f,0.4984f,0.6144f,0.1071f,-0.001f,0.8529f,0.6137f,0f,0.2188f,0.0052f,0.3797f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;484245012,-1971042783,81355301,-273163325],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJjZjI3YWY5Mzk2ODEzMzcxMTE5OGYyMTM4MTM0MDgwNzcyOTFlZGU4MmY1YmZkNDQ3ZTA0ODU4YTQwYjEwZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.2036f,-0.0926f,0f,0.7413f,0.1175f,-0.1604f,0f,0.8284f,0f,0f,0.2344f,0.3272f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1477614714,2029707730,-570869510,-1856262296],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdlZDVlZjUzMzAwNzczNWVlNDQwZTg0ZjU1M2ViZDY4ODYxZTAyZmM1MmE1ZmY5M2MyN2I1ODQyOTdiMzUwYSJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.204f,0f,-0.6247f,0.9435f,-0.1168f,0f,-1.0908f,1.1792f,0f,0.2366f,0f,0.3866f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-564110181,-1346831767,1984270607,2074265668],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc0NTU3MzhkMTk1NzY0MmU3Zjg2YWVmYjllZWQyMzVlNWUyZDU0ZTNiNmVkZjU0MTc4YTQ4ODg0YzZiOWVlNCJ9fX0=\"}]}}},item_display:\"none\",transformation:[-0.1503f,0.1526f,0.0052f,0.5633f,0.1498f,0.1531f,-0.0052f,0.4982f,-0.0016f,0f,-1f,0.54f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;399542448,1491323279,-1481961263,697673389],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkzN2VkY2YyYzI5NzZmZDQ2MTdiOTlmMzYzZDNkNjI4MzJhYjk0ODlhMjcwOWRlZTI3NGY3OTllMjk4MzY4OCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.3704f,0f,0.6576f,0.4862f,0f,0f,1.2673f,0f,0f,0.4922f,0.5453f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1231f,0f,0f,0.8097f,0f,0.2578f,0f,1.1395f,0f,0f,0.2648f,0.4111f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1069f,-0.1094f,0f,0.9342f,0.1069f,0.1094f,0f,1.1821f,0f,0f,0.2582f,0.4141f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.168f,0f,-0.0269f,0.8805f,0f,0.1627f,0f,1.1397f,0.0793f,0f,0.0585f,0.6197f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.168f,0f,0.0269f,0.8536f,0f,0.1627f,0f,1.1385f,-0.0793f,0f,0.0585f,0.4083f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0036f,0f,-0.0796f,1.0908f,0f,0.1657f,0f,1.1397f,0.3483f,0f,0.0008f,0.3768f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0f,-0.1307f,0.1245f,1.0188f,0.1133f,0f,0f,1.2003f,0f,0.1323f,0.126f,0.4143f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.2023f,0f,0f,0.7103f,0f,0.0195f,0f,1.3958f,0f,0f,0.2284f,0.4297f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1129059466,-1247541875,1599317420,663533210],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjkwNWRjODI0YWRhY2NlNTY1N2JjOThlNGFkNDFiNTA0NTQ0MDA2Mzc2Y2I4ZGJjZmY5ODE2MzQxNGY4MGQ2ZCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.2935f,-0.027f,0.9016f,0f,0.226f,-0.0351f,1.4135f,0.375f,0f,0f,0.5403f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:iron_block\",Properties:{}},transformation:[0.0149f,0f,-0.0142f,1.1064f,0f,0.0231f,0f,1.3022f,0.0148f,0f,0.0147f,0.5263f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1161f,-0.042f,0f,1.0602f,0.042f,0.1161f,0f,1.3736f,0f,0f,0.125f,0.7003f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.1161f,-0.042f,0f,1.0602f,0.042f,0.1161f,0f,1.3736f,0f,0f,0.125f,0.2616f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.3217f,0f,0f,0.7103f,0f,0.0195f,0f,1.1269f,0f,0f,0.2284f,0.4297f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:cobbled_deepslate_wall\",Properties:{up:\"true\"}},transformation:[0.116f,0f,0.0462f,1.021f,0.0422f,0f,-0.1269f,1.4985f,0f,0.7f,0f,0.1853f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:cobbled_deepslate_wall\",Properties:{up:\"true\"}},transformation:[0.1005f,0.1269f,0.0462f,1.0252f,0.0366f,0.0462f,-0.1269f,1.498f,-0.0625f,0.2368f,0f,0.8801f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:cobbled_deepslate_wall\",Properties:{up:\"true\"}},transformation:[0.1005f,0.1269f,-0.0462f,1.069f,0.0366f,0.0462f,0.1269f,1.3719f,0.0625f,-0.2368f,0f,0.1943f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0568f,0.0801f,0.0242f,1.1222f,0.0207f,0.0292f,-0.0665f,1.5006f,-0.0353f,0.1495f,0f,0.9895f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0568f,0.0801f,-0.0242f,1.1464f,0.0207f,0.0292f,0.0665f,1.4341f,0.0353f,-0.1495f,0f,0.088f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.4862f,0f,0f,0.3205f,0f,0.028f,0f,1.0082f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2619f,0.0198f,0f,0.791f,-0.2619f,0.0198f,0f,1.0137f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.0411f,0.0276f,0f,1.0441f,-0.2748f,0.0041f,0f,0.7692f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.1454f,-0.0169f,0f,0.1903f,0.11f,0.0223f,0f,0.9032f,0f,0f,0.25f,0.4153f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.4679f,0f,0f,0.3205f,0f,0.0146f,-0.0947f,1.0218f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.2578f,0.0104f,-0.0669f,0.7996f,-0.2578f,0.0104f,-0.0669f,1.0218f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:waxed_weathered_copper\",Properties:{}},transformation:[0.0404f,0.0145f,-0.0936f,1.0591f,-0.2704f,0.0022f,-0.014f,0.7652f,0f,0.0241f,0.0589f,0.6435f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.1439f,-0.0079f,0.0589f,0.1915f,0.1118f,0.0117f,-0.0723f,0.9124f,-0.0072f,0.0244f,0.0552f,0.6472f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.4679f,0f,0f,0.3205f,0f,0.0146f,0.0947f,0.9271f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weathered_copper\",Properties:{}},transformation:[0.2578f,0.0104f,0.0669f,0.7327f,-0.2578f,0.0104f,0.0669f,0.9548f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:exposed_copper\",Properties:{}},transformation:[0.0404f,0.0145f,0.0936f,0.9654f,-0.2704f,0.0022f,0.014f,0.7513f,0f,-0.0241f,0.0589f,0.376f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:copper_block\",Properties:{}},transformation:[0.1439f,-0.0079f,-0.0589f,0.2504f,0.1118f,0.0117f,0.0723f,0.8401f,0.0072f,-0.0244f,0.0552f,0.3759f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:end_rod\",Properties:{facing:\"down\"}},transformation:[0.2386f,0.0801f,0.0966f,0.9999f,0.0868f,0.0292f,-0.2654f,1.5694f,-0.1484f,0.1495f,0f,1.055f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:end_rod\",Properties:{facing:\"down\"}},transformation:[0.2386f,0.0801f,-0.0966f,1.0953f,0.0868f,0.0292f,0.2654f,1.3072f,0.1484f,-0.1495f,0f,0.0244f,0f,0f,0f,1f]}]}"
            );

            private class Instance extends BukkitRunnable {
                private Player player;
                private DisplayEntity entity;
                private double theta = 0.0d;

                public Instance(@Nonnull Player player, @NotNull Location location) {
                    location.setPitch(0.0f);

                    entity = data.spawnInterpolated(location);
                }

                @Override
                public void run() {
                    entity.asTagged("ball", display -> {
                        final Transformation transformation = display.getTransformation();
                        final Vector3f translation = transformation.getTranslation();
                        translation.y += Math.sin(theta) * 0.05d;

                        final Quaternionf leftRotation = transformation.getLeftRotation();
                        leftRotation.y += Math.cos(theta) * 0.001d;

                        display.setTransformation(transformation);
                    });

                    theta += Math.PI / 16;
                }
            }

            private Instance instance;

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                if (instance != null) {
                    instance.cancel();
                    instance.entity.remove();
                }

                instance = new Instance(player, player.getLocation());
                instance.runTaskTimer(EternaPlugin.getPlugin(), 0, 1);

                return true;
            }
        });

        // *=* Internal *=* //
        addTest(new EternaTest("fail") {
            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                assertSkipFail();
                assertFail("Force fail");
                return false;
            }
        });

    }

    private final Player tester;
    private final LinkedList<EternaTest> tests;
    private final ArgumentList args;

    EternaTest currentTest;
    boolean wait;

    EternaRuntimeTest(Player tester, EternaTest test, String[] args) {
        this.tester = tester;
        this.tests = Lists.newLinkedList();
        this.args = new ArgumentList(args);

        if (test == null) {
            tests.addAll(allTests.values());
        }
        else {
            tests.add(test);
        }
    }

    public void next() {
        if (wait) {
            return;
        }

        try {
            currentTest = tests.pollFirst();

            if (currentTest == null) {
                EternaRuntimeTest.test = null;
                EternaLogger.test("");
                EternaLogger.test("&a&lAll tests passed!");
                EternaLogger.test("");
                return;
            }

            final String testName = currentTest.toString();

            if (currentTest.doShowFeedback()) {
                EternaLogger.test("&eTesting '%s'...".formatted(testName));
            }

            final boolean auto = currentTest.test(tester, args);

            // Test 2 requires manual pass
            if (!auto) {
                wait = true;
                return;
            }

            handleTestPassed(currentTest);
        } catch (Exception e) {
            if (currentTest.skipFail) {
                currentTest.skipFail = false;

                handleTestSkipped(currentTest, e.getMessage());
                handleTestPassed(currentTest);
                return;
            }

            handleTestFail(currentTest, e);
        }
    }

    static void handleTestPassed(EternaTest test) {
        if (EternaRuntimeTest.test != null) {
            EternaRuntimeTest.test.wait = false;
        }

        if (test.doShowFeedback()) {
            EternaLogger.test("&aTest '%s' passed!".formatted(test));
        }
    }

    static void handleTestFail(EternaTest test, Exception ex) {
        if (test.doShowFeedback()) {
            EternaLogger.test("&4Test '%s' failed! &c%s:%s".formatted(test, ex.getClass().getSimpleName(), ex.getMessage()));
            ex.printStackTrace();
        }
    }

    static void handleTestSkipped(EternaTest test, String message) {
        if (test.doShowFeedback()) {
            EternaLogger.test("&8Test '%s' skipped! %s".formatted(test, message));
        }
    }

    static void addTest(EternaTest test) {
        final String name = test.toString();

        if (allTests.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate test add! '%s'".formatted(name));
        }

        allTests.put(name, test);
    }

    public static void test(@Nonnull Player tester, @Nonnull String testName, @Nonnull String[] args) {
        final EternaTest test = allTests.get(testName);

        if (test == null) {
            throw new IllegalArgumentException("Invalid test '%s'!".formatted(testName));
        }

        final EternaRuntimeTest rt = new EternaRuntimeTest(tester, test, args);
        rt.next();
    }

    public static void nextOrNewTestSq(@Nonnull Player tester) {
        if (EternaRuntimeTest.test != null) {
            EternaRuntimeTest.test.next();
            return;
        }

        EternaLogger.test("");
        EternaLogger.test("&6Creating a new test sequence, use the command again to continue.");
        EternaLogger.test("");
        EternaRuntimeTest.test = new EternaRuntimeTest(tester, null, new String[] {});
    }

    public static boolean testExists(@Nonnull String name) {
        for (String s : allTests.keySet()) {
            if (s.equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Nonnull
    public static List<String> listTests() {
        return Lists.newArrayList(allTests.keySet());
    }

}
