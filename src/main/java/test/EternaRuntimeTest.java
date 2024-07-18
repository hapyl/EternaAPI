package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.ai.MobAI;
import me.hapyl.spigotutils.module.ai.goal.FloatGoal;
import me.hapyl.spigotutils.module.ai.goal.MeleeAttackGoal;
import me.hapyl.spigotutils.module.block.display.BlockStudioParser;
import me.hapyl.spigotutils.module.block.display.DisplayData;
import me.hapyl.spigotutils.module.block.display.DisplayEntity;
import me.hapyl.spigotutils.module.chat.messagebuilder.Format;
import me.hapyl.spigotutils.module.chat.messagebuilder.Keybind;
import me.hapyl.spigotutils.module.chat.messagebuilder.MessageBuilder;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.entity.Rope;
import me.hapyl.spigotutils.module.hologram.DisplayHologram;
import me.hapyl.spigotutils.module.hologram.Hologram;
import me.hapyl.spigotutils.module.inventory.*;
import me.hapyl.spigotutils.module.inventory.gui.*;
import me.hapyl.spigotutils.module.locaiton.LocationHelper;
import me.hapyl.spigotutils.module.math.Geometry;
import me.hapyl.spigotutils.module.math.geometry.WorldParticle;
import me.hapyl.spigotutils.module.nbt.NBT;
import me.hapyl.spigotutils.module.nbt.NBTType;
import me.hapyl.spigotutils.module.particle.ParticleBuilder;
import me.hapyl.spigotutils.module.player.PlayerSkin;
import me.hapyl.spigotutils.module.player.synthesizer.Synthesizer;
import me.hapyl.spigotutils.module.player.tablist.*;
import me.hapyl.spigotutils.module.record.Record;
import me.hapyl.spigotutils.module.record.Replay;
import me.hapyl.spigotutils.module.record.ReplayData;
import me.hapyl.spigotutils.module.reflect.Laser;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.border.PlayerBorder;
import me.hapyl.spigotutils.module.reflect.glow.Glowing;
import me.hapyl.spigotutils.module.reflect.npc.ClickType;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.NPCPose;
import me.hapyl.spigotutils.module.scoreboard.Scoreboarder;
import me.hapyl.spigotutils.module.util.ArgumentList;
import me.hapyl.spigotutils.module.util.CollectionBuilder;
import me.hapyl.spigotutils.module.util.Compute;
import me.hapyl.spigotutils.module.util.WeightedCollection;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

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

            static final ItemBuilder testItem = new ItemBuilder(Material.STONE, "itembuildertest_0")
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

                cloned.setId("itembuildertest_1");
                cloned.addFunction(p -> {
                    p.sendMessage("CLONED EXLUSIVE");
                }).accept(Action.LEFT_CLICK_AIR).setCdSec(1);

                inventory.addItem(cloned.build());
                inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner("hapyl", Sound.ENTITY_IRON_GOLEM_DAMAGE).build());

                final ItemBuilder clonedClone = cloned.clone();
                clonedClone.setName("CLONED CLONE");
                clonedClone.setId("itembuildertest_2");
                clonedClone.clearFunctions();
                clonedClone.addFunction(p -> {
                    p.sendMessage("Clicked with clone clone.");
                }).accept(Action.LEFT_CLICK_AIR);

                inventory.addItem(clonedClone.build());

                inventory.addItem(new ItemBuilder(Material.DIAMOND_BLOCK, "itembuildertest_3")
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
                }.setInteractionDelayTick(20);

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
            @Override
            public boolean test(@NotNull Player player, @NotNull ArgumentList args) throws EternaTestException {
                final HumanNPC npc = new HumanNPC(player.getLocation(), "sitting");

                npc.showAll();
                npc.setSitting(true);

                later(() -> {
                    npc.setSitting(false);
                }, 40);

                return true;
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
                    final String s = cls.get();

                    mapped.compute(s, (_s, _i) -> _i == null ? 1 : _i + 1);
                }

                final Integer mostCommon = mapped.get("MORE COMMON");
                final Integer notSoCommon = mapped.get("you");
                final Integer rarest = mapped.get("rarest");

                assertTrue(mostCommon > notSoCommon, "mostCommon <= notSoCommon");
                assertTrue(rarest < notSoCommon, "mostCommon >= notSoCommon");

                info(player, mapped.toString());

                assertTestPassed();
                return false;
            }
        });

        addTest(new EternaTest("blockStudioParser") {

            static final String data = "{Passengers:[{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:acacia_fence\",Properties:{south:\"true\"}},transformation:[1.0000f,0.0000f,0.0000f,0.5625f,0.0000f,1.0000f,0.0000f,0.0000f,0.0000f,0.0000f,1.0000f,0.0000f,0.0000f,0.0000f,0.0000f,1.0000f]},{id:\"minecraft:block_display\",block_state:{Name:\"minecraft:lectern\",Properties:{facing:\"east\"}},transformation:[0.0000f,0.0000f,1.0000f,0.0000f,0.7071f,0.7071f,0.0000f,-0.5000f,-0.7071f,0.7071f,0.0000f,0.8750f,0.0000f,0.0000f,0.0000f,1.0000f]},{id:\"minecraft:item_display\",item:{id:\"minecraft:golden_boots\",Count:1},item_display:\"none\",transformation:[0.7071f,0.0000f,0.7071f,-0.1875f,0.5000f,0.7071f,-0.5000f,0.0000f,-0.5000f,0.7071f,0.5000f,0.5625f,0.0000f,0.0000f,0.0000f,1.0000f]}]}";
            static final String failData = "{null: false, abc: 1231312}";

            @Override
            public boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException {
                final DisplayData model = BlockStudioParser.parse(data);
                final DisplayEntity entity = model.spawn(player.getLocation());

                assertThrows(() -> {
                    BlockStudioParser.parse(failData);
                }, "Parser did not throw for invalid data!");

                later(() -> {
                    info(player, "Teleported");
                    entity.teleport(player.getLocation());

                    later(() -> {
                        entity.remove();
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
                                            ParticleBuilder.vibration(location, LocationHelper.addAsNew(location, 0, 5, 0), 20)
                                                    .display(location);

                                            later(() -> {
                                                info(player, "virationEntity");
                                                ParticleBuilder.vibration(location, player, 20).display(location);

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

            EternaLogger.test("&eTesting '%s'...".formatted(testName));
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

        EternaLogger.test("&aTest '%s' passed!".formatted(test));
    }

    static void handleTestFail(EternaTest test, Exception ex) {
        EternaLogger.test("&4Test '%s' failed! &c%s".formatted(test, ex.getMessage()));
        ex.printStackTrace();
    }

    static void handleTestSkipped(EternaTest test, String message) {
        EternaLogger.test("&8Test '%s' skipped! %s".formatted(test, message));
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
