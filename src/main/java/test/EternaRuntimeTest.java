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
import me.hapyl.eterna.module.player.dialog.DialogOptionEntry;
import me.hapyl.eterna.module.player.dialog.NPCDialog;
import me.hapyl.eterna.module.player.quest.*;
import me.hapyl.eterna.module.player.quest.objective.BreakBlockQuestObjective;
import me.hapyl.eterna.module.player.quest.objective.GiveItemToNpcQuestObjective;
import me.hapyl.eterna.module.player.quest.objective.TalkToNpcQuestObjective;
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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
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
            private HumanNPC npc;

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                if (registry == null) {
                    final Config config = new Config(EternaPlugin.getPlugin(), "quest_data");

                    registry = new QuestHandler(EternaPlugin.getPlugin()) {
                        @Override
                        public void saveQuests(@Nonnull Player player, @Nonnull Set<QuestData> questDataSet) {
                            final String uuid = player.getUniqueId().toString();
                            final YamlConfiguration yaml = config.getConfig();

                            for (QuestData data : questDataSet) {
                                final Quest quest = data.getQuest();
                                final int currentStage = data.getCurrentStage();
                                final double progress = data.getCurrentStageProgress();

                                yaml.set("%s.%s.stage".formatted(uuid, quest.getKeyAsString()), currentStage);
                                yaml.set("%s.%s.progress".formatted(uuid, quest.getKeyAsString()), progress);
                            }

                            config.save();
                        }

                        @Nonnull
                        @Override
                        public Set<QuestData> loadQuests(@Nonnull Player player) {
                            final String uuid = player.getUniqueId().toString();

                            final YamlConfiguration yaml = config.getConfig();
                            final ConfigurationSection section = yaml.getConfigurationSection(uuid);
                            final Set<QuestData> questDataSet = new HashSet<>();

                            if (section == null) {
                                return questDataSet;
                            }

                            for (String questKey : section.getValues(false).keySet()) {
                                final Quest quest = registry.get(questKey);
                                final int stage = yaml.getInt("%s.%s.stage".formatted(uuid, questKey));
                                final double progress = yaml.getDouble("%s.%s.progress".formatted(uuid, questKey));

                                // Dont' load completed quests
                                if (hasCompleted(player, quest)) {
                                    continue;
                                }

                                questDataSet.add(QuestData.load(this, player, quest, stage, progress));
                            }

                            return questDataSet;
                        }

                        @Override
                        public boolean hasCompleted(@Nonnull Player player, @Nonnull Quest quest) {
                            return config.getBoolean("%s.%s.has_completed".formatted(
                                    player.getUniqueId().toString(),
                                    quest.getKeyAsString()
                            ), false);
                        }

                        @Override
                        public void completeQuest(@Nonnull Player player, @Nonnull Quest quest) {
                            config.set("%s.%s.has_completed".formatted(player.getUniqueId().toString(), quest.getKeyAsString()), true);
                            config.save();
                        }
                    };

                    npc = new HumanNPC(player.getLocation(), "Test Npc", "hapyl");
                    npc.show(player);

                    final QuestChain questChain = new QuestChain(Key.ofString("the_eye"));

                    final Quest quest1 = new Quest(EternaPlugin.getPlugin(), Key.ofString("eterna_test_quest1"));
                    quest1.setName("Hello World!");
                    quest1.setDescription("Someone wants to talk to you...");
                    quest1.addObjective(new TalkToNpcQuestObjective(new NPCDialog(npc)
                            .addEntry(
                                    "Hello there!",
                                    "This is a test dialog!",
                                    "Select the first option to finish dialog.",
                                    "Select the second option to leave the dialog."
                            )
                            .addEntry(new DialogOptionEntry()
                                    .setOption(1, DialogOptionEntry
                                            .builder()
                                            .prompt("First option")
                                    )
                                    .setOption(2, DialogOptionEntry.goodbye("Second option"))
                            ).addEntry("Okay then, go mine some diamong ore!")
                    ));

                    final Quest quest2 = new Quest(EternaPlugin.getPlugin(), Key.ofString("eterna_test_quest2"));
                    quest2.setName("Get to Mining!");
                    quest2.setDescription("As they said, it's time to go mining!");
                    quest2.addObjective(new BreakBlockQuestObjective(Material.DIAMOND_ORE, 6));

                    final Quest quest3 = new Quest(EternaPlugin.getPlugin(), Key.ofString("eterna_test_quest3"));
                    quest3.setName("Diamonds!");
                    quest3.setDescription("Now that you have mined the ore, it's time to show the diamonds.");
                    quest3.addObjective(new GiveItemToNpcQuestObjective(npc, Material.DIAMOND, 2));
                    quest3.addObjective(new TalkToNpcQuestObjective(new NPCDialog(npc)
                            .addEntry(
                                    "You're done? That was fast!",
                                    "Thanks for the diamons."
                            )));

                    questChain.addQuests(quest1, quest2, quest3);
                    registry.register(questChain);

                    questChain.startNextQuest(player); // for testing, impl should not do this
                }
                else {
                    final String argument = args.getString(0);

                    switch (argument.toLowerCase()) {
                        case "npc" -> {
                            npc.show(player);
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
                    "/summon block_display ~-0.5 ~ ~-0.5 {Passengers:[{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[0.0576f,-0.113f,0f,-0.9465f,0.0243f,0.2676f,0f,1.6294f,0f,0f,0.2969f,-0.175f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;178114518,1982321168,242534006,260565558],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE4YzRlYzZmMTQ5MDk0MDNkNmZiMWNlMzExODRmZTkyZTFkOGI5MGI4ZTE5MTFlNDM4NjEyMTg4ZjUwMmE5MyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1791f,-0.4651f,0.4193f,-1.5431f,0.3409f,0.4448f,0.3354f,2.3925f,-0.5314f,0.1286f,0.3565f,0.0238f,0f,0f,0f,1f],Tags: [\"ball\"]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.5685f,-0.1075f,0f,-0.9059f,0.232f,0.2634f,0f,1.6481f,0f,0f,0.265f,-0.1631f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;586354698,545165729,2034582074,183282528],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0911f,-0.9509f,0f,-1.3709f,0.3399f,0.2548f,0f,1.8694f,0f,0f,0.3188f,-0.0331f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;374598941,-913107637,-1472924559,-1949656655],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0466f,-0.5594f,0.0278f,-1.659f,0.1397f,0.1875f,0.0099f,1.95f,-0.0051f,0.0264f,0.5192f,-0.015f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-245639213,412257144,-1938843909,931407691],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFiOTIzMjIyNzY1MWYzYzk2YWE1M2Q2OGJiNDAxYWFjZWRiOTE3ODM2MDA1YTJhNGZmMDE3NWQ3YmVmOWU3YiJ9fX0=\"}]}}},item_display:\"none\",transformation:[1f,0f,0f,0.0006f,0f,1f,0f,2.8281f,0f,0f,1f,-0.0669f,0f,0f,0f,1f],Tags: [\"head\"]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;188677409,801218360,887224149,544576407],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.5f,0f,0f,0f,0f,0.8673f,0.0661f,2.3125f,0f,-0.1157f,0.4956f,0f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-2079872719,1299240855,-764553259,-1134204684],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.5f,0f,0f,0f,0f,0.875f,0f,1.9169f,0f,0f,0.5f,0.0481f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-2009414445,-1066942448,1288008313,-472753248],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.5f,0f,0f,0f,0f,0.8536f,-0.1099f,1.5275f,0f,0.1924f,0.4878f,0.0544f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-36848142,591568929,-1410572942,-97791350],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.5f,0f,0f,0f,0f,0.875f,0f,1.1306f,0f,0f,0.5f,-0.0375f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-285532456,1884863398,1918004841,-2020978645],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.0514f,0.2022f,0.26f,-0.1875f,0f,0f,2.1181f,0f,-0.5542f,-0.0188f,-0.1469f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[0.0066f,0.3689f,0.0174f,-0.6169f,-0.0099f,0.246f,-0.026f,2.0756f,-0.3998f,0f,0.0009f,0.1606f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[0.0083f,0.084f,0.0312f,-0.3919f,-0.0009f,0.8883f,-0.0029f,0.5481f,-0.3999f,-0.0003f,0.0007f,0.1881f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1473307483,215390368,2026212526,-1718866127],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.3813f,0f,0f,0f,0f,0.8671f,0.0533f,0.7956f,0f,-0.1172f,0.3939f,-0.0375f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[-0.3999f,-0.0003f,0.0007f,0.2075f,-0.0009f,1.0549f,-0.0029f,0.3869f,-0.0083f,-0.0997f,-0.0312f,0.3712f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[0.0083f,-0.084f,-0.0312f,0.4106f,0.0009f,0.8883f,-0.0029f,0.5472f,0.3999f,-0.0003f,0.0007f,-0.2312f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.1465f,-0.0592f,-0.021f,0.3888f,0.0005f,0.9538f,-0.0029f,0.4894f,0.1338f,-0.068f,-0.023f,0.2044f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:basalt\",Properties:{axis:\"x\"}},transformation:[0.3375f,0f,0f,-0.1525f,0f,0.0949f,0f,1.4231f,0f,0f,0.1078f,0.16f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:basalt\",Properties:{axis:\"x\"}},transformation:[0f,0f,0.1078f,-0.3244f,0f,0.0949f,0f,1.4231f,-0.3375f,0f,0f,0.1644f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:basalt\",Properties:{axis:\"x\"}},transformation:[0f,0f,0.1078f,0.2338f,0f,0.0949f,0f,1.4231f,-0.3375f,0f,0f,0.1488f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:basalt\",Properties:{axis:\"x\"}},transformation:[0.1736f,0f,-0.0612f,0.18f,0f,0.0949f,0f,1.4231f,0.1198f,0f,0.0887f,-0.3256f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:basalt\",Properties:{axis:\"x\"}},transformation:[0.1736f,0f,-0.0612f,-0.2506f,0f,0.0949f,0f,1.4231f,0.1198f,0f,0.0887f,0.0725f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-989663862,116699665,250886015,2144437472],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWM5ZmE3ODI4YjRiMzdlODI2MmQyYzIyMmEwNGY4ZTMyNjFhZjJhYjNiZDQ5NGVhODA3YTkzMmNjZDE4NGE1MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0f,-0.58f,0.0038f,0.4215f,0f,0f,1.45f,0f,-0.4125f,0f,-0.3675f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:basalt\",Properties:{axis:\"x\"}},transformation:[0.1736f,0f,0.0612f,-0.3312f,0f,0.0949f,0f,1.4231f,-0.1198f,0f,0.0887f,-0.1956f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:basalt\",Properties:{axis:\"x\"}},transformation:[0.1736f,0f,0.0612f,0.1025f,0f,0.0949f,0f,1.4231f,-0.1198f,0f,0.0887f,0.1919f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.1465f,0.0592f,0.021f,-0.225f,-0.0005f,0.9538f,-0.0029f,0.4899f,-0.1338f,-0.068f,-0.023f,0.3475f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.1465f,-0.0592f,0.021f,0.3677f,0.0005f,0.9538f,0.0029f,0.4865f,-0.1338f,0.068f,-0.023f,-0.2375f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.1465f,0.0592f,-0.021f,-0.1977f,-0.0005f,0.9538f,0.0029f,0.4869f,0.1338f,0.068f,-0.023f,-0.3562f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;719599825,-959218246,-975894463,-1654365077],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc3ZTQwYmIwYjM3MWIzNzQ1ZGZlNWVkNTIwMzNmZmNmYzhjODJmZWVmYzI1YjM0YmFjN2FlZDFmZjljZTU4ZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2417f,0.0076f,0.1849f,-0.4006f,-0.0457f,0.0068f,0.9822f,1.1925f,0.0061f,-0.2498f,0.0323f,-0.1519f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.0068f,0.0995f,-0.0311f,-0.3838f,-0.0003f,1.0552f,0.0029f,0.3856f,0.1486f,0.0069f,-0.0014f,-0.0912f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.0068f,-0.0995f,0.0311f,0.4175f,0.0003f,1.0552f,0.0029f,0.3853f,-0.1486f,0.0069f,-0.0014f,0.0574f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-2061658441,-1248560224,1806999732,113743712],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.4809f,0.1297f,0.0225f,-0.1875f,0f,0f,2.1223f,0f,-0.3992f,-0.1563f,-0.3212f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;2079148625,-1672010573,-1995536219,625100177],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.522f,0.0705f,0.3225f,-0.1875f,0f,0f,2.1223f,0f,-0.1931f,0.1905f,0.0638f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1851721479,-313109709,1494494942,-2098433164],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.511f,0.1297f,0.0331f,-0.1875f,0f,0f,1.9661f,0f,-0.4242f,-0.1563f,-0.3487f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1472261590,1239653814,1460786558,1786791772],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.0525f,0.2022f,0.2831f,-0.1875f,0f,0f,1.9661f,0f,-0.5658f,-0.0188f,-0.1712f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1705160601,-1386567641,870110217,463857700],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.4786f,0.0705f,0.3225f,-0.1875f,0f,0f,1.9661f,0f,-0.177f,0.1905f,0.0638f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;808175905,209715436,-303015031,-630775952],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.4809f,0.1297f,0.0775f,-0.1875f,0f,0f,1.8204f,0f,-0.3992f,-0.1563f,-0.3719f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;400245785,753060954,-2120039901,-1443791535],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.0533f,0.2022f,0.3181f,-0.1875f,0f,0f,1.8204f,0f,-0.5741f,-0.0188f,-0.2075f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;924159184,1474479034,117793474,1334317403],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.5384f,0.0705f,0.3588f,-0.1875f,0f,0f,1.8242f,0f,-0.1992f,0.1905f,0.0638f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1557975247,-978173314,-1979936347,106068115],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.4809f,0.1297f,0.0813f,-0.1875f,0f,0f,1.6829f,0f,-0.3992f,-0.1563f,-0.3937f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-42458879,-1294328878,-1309506235,1960052267],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.0104f,0.2031f,0.3106f,-0.1875f,0f,0f,1.6829f,0f,-0.5965f,-0.0035f,-0.205f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-764042,-1103412804,-2006224816,753605695],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.4786f,0.0705f,0.3225f,-0.1875f,0f,0f,1.6829f,0f,-0.177f,0.1905f,0.0712f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1237652723,-1091263637,-1381935088,-2064425611],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.1875f,0f,0f,-0.005f,0f,-0.6232f,0.0156f,1.8773f,0f,-0.0479f,-0.2025f,-0.3381f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1018025634,1866315341,2100382101,1690815167],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.4809f,-0.1297f,-0.0275f,0.1875f,0f,0f,2.1223f,0f,-0.3992f,-0.1563f,-0.3325f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1428413343,-1551382068,-2052396981,2129403964],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0514f,-0.2022f,-0.265f,0.1875f,0f,0f,2.1223f,0f,-0.5542f,-0.0188f,-0.1719f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-2062343657,640011819,456677408,-1998808206],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.522f,-0.0705f,-0.3275f,0.1875f,0f,0f,2.1223f,0f,-0.1931f,0.1905f,0.0431f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1367622318,1789657157,41182022,-612198702],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.511f,-0.1297f,-0.0381f,0.1875f,0f,0f,1.9661f,0f,-0.4242f,-0.1563f,-0.36f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-2089999427,-1197021584,1404050069,1159904264],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0525f,-0.2022f,-0.2881f,0.1875f,0f,0f,1.9661f,0f,-0.5658f,-0.0188f,-0.1919f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1905167938,1651321543,-1884340916,-2003008696],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.4786f,-0.0705f,-0.3275f,0.1875f,0f,0f,1.9661f,0f,-0.177f,0.1905f,0.0431f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1819214624,1491580126,-1104961623,-2140270590],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.4809f,-0.1297f,-0.0825f,0.1875f,0f,0f,1.8204f,0f,-0.3992f,-0.1563f,-0.3831f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1136104341,1190098300,1039701683,960842656],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0533f,-0.2022f,-0.3231f,0.1875f,0f,0f,1.8204f,0f,-0.5741f,-0.0188f,-0.2281f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1810910257,1303732369,-1940450190,1732197155],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.5384f,-0.0705f,-0.3638f,0.1875f,0f,0f,1.8242f,0f,-0.1992f,0.1905f,0.0431f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-350904404,2113669350,974744588,1340281714],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.4809f,-0.1297f,-0.0862f,0.1875f,0f,0f,1.6829f,0f,-0.3992f,-0.1563f,-0.39f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-1561121913,-1963052270,730970286,1736774704],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,0.0104f,-0.2031f,-0.3156f,0.1875f,0f,0f,1.6829f,0f,-0.5965f,-0.0035f,-0.2256f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-654712915,-195213588,-1298576436,-1465273231],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhNzljNTEyY2ZmMWE5ZjQ3MzE5ZmQ3N2VmZDA2ZjY3M2E3NzJhYTk3NTk3NDM2YjAxNjBjNDA5YzhhMmU2MCJ9fX0=\"}]}}},item_display:\"none\",transformation:[0f,-0.4786f,-0.0705f,-0.3275f,0.1875f,0f,0f,1.6829f,0f,-0.177f,0.1905f,0.0506f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0346f,-0.0681f,0f,-0.3456f,0.0075f,0.315f,0f,1.5031f,0f,0f,0.4438f,-0.2681f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.0354f,0.0157f,0f,-0.4188f,-0.0017f,0.3219f,0f,1.8119f,0f,0f,0.4438f,-0.2681f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0346f,0.0672f,0.0011f,0.3213f,-0.0074f,0.3152f,-0.0016f,1.5106f,-0.0001f,0.0009f,0.4438f,-0.2681f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.0354f,-0.0157f,0f,0.3836f,0.0017f,0.3219f,0f,1.8101f,0f,0f,0.4438f,-0.2681f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0f,0f,-0.4438f,0.2244f,-0.0075f,0.315f,0f,1.5106f,0.0346f,0.0681f,0f,0.2188f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0f,0f,-0.4438f,0.2244f,0.0017f,0.3219f,0f,1.8101f,0.0354f,-0.0157f,0f,0.2811f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0201f,0.0396f,-0.1895f,0.3456f,-0.0075f,0.315f,0f,1.5105f,0.0281f,0.0554f,0.1356f,0.1044f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.0206f,-0.0092f,-0.1895f,0.3819f,0.0017f,0.3219f,0f,1.81f,0.0287f,-0.0128f,0.1356f,0.1551f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0201f,-0.0396f,0.1895f,-0.3625f,0.0075f,0.315f,0f,1.503f,-0.0281f,0.0554f,0.1356f,0.1325f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.0206f,0.0092f,0.1895f,-0.3992f,-0.0017f,0.3219f,0f,1.8117f,-0.0287f,-0.0128f,0.1356f,0.1838f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0f,0f,-0.4272f,0.2244f,0.0237f,0.1094f,0f,2.1269f,0.0263f,-0.0989f,0f,0.2663f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[-0.0208f,0.1193f,0f,-0.3881f,0.0286f,0.0867f,0f,2.1269f,0f,0f,-0.3994f,0.1356f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[-0.0207f,-0.1196f,-0.001f,0.4061f,-0.0287f,0.0864f,0.0014f,2.1543f,0f,0.0006f,-0.405f,0.1414f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.0155f,-0.0621f,-0.1893f,0.3819f,0.0241f,0.1181f,0.0043f,2.1106f,0.0208f,-0.0903f,0.1358f,0.1551f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.0155f,0.0621f,0.1893f,-0.4f,-0.0241f,0.1181f,0.0043f,2.1347f,-0.0208f,-0.0903f,0.1358f,0.1759f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0201f,0.0396f,0.1895f,0.1462f,-0.0075f,0.315f,0f,1.5105f,-0.0281f,-0.0554f,0.1356f,-0.3294f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.0201f,-0.0396f,-0.1895f,-0.1594f,0.0075f,0.315f,0f,1.503f,0.0281f,-0.0554f,0.1356f,-0.3575f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.0209f,-0.0093f,0.1209f,0.2581f,0.0017f,0.3219f,0f,1.81f,-0.0285f,0.0127f,0.0888f,-0.3306f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.0209f,0.0093f,-0.1209f,-0.2631f,-0.0017f,0.3219f,0f,1.8117f,0.0285f,0.0127f,0.0888f,-0.3591f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;147463733,826493588,37865600,405576761],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc3ZTQwYmIwYjM3MWIzNzQ1ZGZlNWVkNTIwMzNmZmNmYzhjODJmZWVmYzI1YjM0YmFjN2FlZDFmZjljZTU4ZiJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.2351f,0.0083f,0.2932f,-0.4006f,-0.0703f,-0.0363f,0.9473f,1.1731f,0.0182f,-0.2472f,-0.1291f,0.0263f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[0.0072f,-0.3527f,-0.019f,0.6106f,0.0094f,0.2687f,-0.0249f,2.0563f,0.3998f,0f,0.0009f,-0.2392f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[-0.0783f,-0.4569f,0.0132f,0.3962f,0.5001f,-0.0716f,-0.084f,0.9431f,0.0802f,0f,0.5371f,-0.6425f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0.008f,-0.4624f,-0.0013f,0.1162f,0.5061f,0.0073f,-0.0851f,0.9231f,0.0802f,0f,0.5371f,-0.6425f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:crimson_roots\",Properties:{}},transformation:[0.7f,0f,0f,-0.3306f,0f,1f,0f,1.5563f,0f,0f,0.45f,-0.3919f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[-0.3999f,0.0039f,0.0005f,0.1987f,-0.0011f,0.3573f,-0.0185f,2.0256f,-0.0074f,-0.2625f,-0.0252f,0.4287f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[-0.3286f,-0.0836f,0.013f,-0.1031f,-0.1742f,0.1944f,-0.0223f,2.1731f,-0.021f,-0.3052f,-0.0177f,0.4263f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[-0.3286f,0.0836f,-0.013f,0.425f,0.1742f,0.1944f,-0.0223f,1.9989f,0.021f,-0.3052f,-0.0177f,0.4053f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0.1894f,-0.0965f,-0.3481f,0.3663f,0.0616f,0.4486f,-0.0207f,2.0194f,0.1611f,-0.0582f,0.4173f,-0.5606f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0.1894f,0.0965f,0.3481f,-0.5556f,-0.0616f,0.4486f,-0.0207f,2.081f,-0.1611f,-0.0582f,0.4173f,-0.3995f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[0.3519f,0.0154f,-0.0193f,-0.1788f,-0.0125f,0.4613f,-0.0334f,2.0194f,0.0117f,0.029f,0.5424f,-0.6619f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.0873f,-0.1592f,0.0231f,0.5988f,0.3833f,0.0009f,0.009f,1.7269f,-0.0741f,0.192f,0.0191f,0.1506f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.049f,-0.2241f,0.0132f,0.4094f,0.3833f,0.0009f,0.009f,1.7269f,-0.1035f,0.1095f,0.0269f,0.365f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.0873f,0.1592f,-0.0231f,-0.5338f,-0.3833f,0.0009f,0.009f,2.1101f,0.0741f,0.192f,0.0191f,0.0765f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.049f,0.2241f,-0.0132f,-0.3826f,-0.3833f,0.0009f,0.009f,2.1101f,0.1035f,0.1095f,0.0269f,0.2615f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.1767f,-0.0484f,-0.021f,0.5288f,0.0005f,0.7809f,-0.0029f,1.0738f,0.1613f,-0.0557f,-0.023f,0.3138f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:black_concrete_powder\",Properties:{}},transformation:[-0.1767f,0.0484f,0.021f,-0.4019f,-0.0005f,0.7809f,-0.0029f,1.0806f,-0.1613f,-0.0557f,-0.023f,0.4575f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[-0.0573f,-0.4596f,0.0017f,0.7169f,0.5069f,-0.0514f,0.053f,0.6463f,-0.0495f,0.0065f,0.5412f,0.1756f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:chain\",Properties:{axis:\"x\"}},transformation:[-0.0573f,0.4596f,-0.0017f,-0.6763f,-0.5069f,-0.0514f,0.053f,1.1531f,0.0495f,0.0065f,0.5412f,0.07f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[-0.3999f,-0.0004f,0.0007f,0.2075f,-0.0004f,1.2574f,-0.0012f,0.7506f,-0.0084f,-0.0483f,-0.0313f,0.4706f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.11f,0f,0.0585f,0.2269f,-0.039f,0.0929f,0.0739f,2.2056f,-0.0433f,-0.0836f,0.0821f,-0.1581f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:netherite_block\",Properties:{}},transformation:[0.11f,0f,-0.0585f,-0.3206f,0.039f,0.0929f,0.0739f,2.1666f,0.0433f,-0.0836f,0.0821f,-0.2014f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weeping_vines\",Properties:{}},transformation:[-0.0797f,0f,-0.0661f,0.4562f,0f,1f,0f,-0.4562f,0.9113f,0f,-0.0058f,-0.4775f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weeping_vines\",Properties:{}},transformation:[0.0208f,0f,0.0664f,-0.3862f,0f,1f,0f,-0.4375f,-0.9146f,0f,0.0015f,0.4914f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:weeping_vines\",Properties:{}},transformation:[-0.9148f,0f,0.0001f,0.4662f,0f,1f,0f,-0.5f,-0.0016f,0f,-0.0664f,0.3746f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:polished_blackstone\",Properties:{}},transformation:[0.5685f,0.1075f,0f,0.3125f,-0.232f,0.2634f,0f,1.8801f,0f,0f,0.265f,-0.1631f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;1298701341,2003525783,-1154601074,2129243781],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0911f,0.9509f,0f,1.346f,-0.3399f,0.2548f,0f,1.8694f,0f,0f,0.3188f,-0.0331f,0f,0f,0f,1f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:player_head\",Count:1,components:{\"minecraft:profile\":{id:[I;-995153529,-1704699703,335324178,1544116282],properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdmY2E4NjY0ODY0YTI0NmViNzY1ZmM3MjQ5N2U4YjFjOTE2MjI0NjE0MGUwOGU4ZWQ3Y2EwNGQ4MmRlNjBjMyJ9fX0=\"}]}}},item_display:\"none\",transformation:[0.0339f,0.5359f,-0.1829f,1.5954f,-0.1058f,0.2376f,0.2954f,1.9956f,0.0968f,0.0719f,0.3869f,-0.0006f,0f,0f,0f,1f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:nether_wart_block\",Properties:{}},transformation:[0.0576f,0.113f,0f,0.864f,-0.0243f,0.2676f,0f,1.6537f,0f,0f,0.2969f,-0.175f,0f,0f,0f,1f]}]}"
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
