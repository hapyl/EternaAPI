package test;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.ai.MobAI;
import me.hapyl.spigotutils.module.ai.goal.*;
import me.hapyl.spigotutils.module.block.display.DisplayData;
import me.hapyl.spigotutils.module.block.display.BlockStudioParser;
import me.hapyl.spigotutils.module.block.display.DisplayEntity;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.spigotutils.module.command.SimpleAdminCommand;
import me.hapyl.spigotutils.module.command.SimpleCommand;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.entity.Rope;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Geometry;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.math.geometry.WorldParticle;
import me.hapyl.spigotutils.module.nbt.NBT;
import me.hapyl.spigotutils.module.nbt.NBTType;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.quest.Quest;
import me.hapyl.spigotutils.module.quest.QuestManager;
import me.hapyl.spigotutils.module.quest.QuestProgress;
import me.hapyl.spigotutils.module.record.Record;
import me.hapyl.spigotutils.module.record.Replay;
import me.hapyl.spigotutils.module.reflect.border.PlayerBorder;
import me.hapyl.spigotutils.module.reflect.npc.Human;
import me.hapyl.spigotutils.module.scoreboard.Scoreboarder;
import me.hapyl.spigotutils.module.util.Padding;
import me.hapyl.spigotutils.module.util.Runnables;
import me.hapyl.spigotutils.module.util.Validate;
import me.hapyl.spigotutils.module.util.WeightedCollection;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.npc.EntityVillager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;

import java.util.*;

/**
 * This is a mess class supposed to be a mess class.
 * Ignore this, only me 😊 can use this.
 */
public final class RuntimeCommandsTest {

    private static final CommandProcessor processor = new CommandProcessor();
    private static final UUID HAPYL_UUID = UUID.fromString("b58e578c-8e36-4789-af50-1ee7400307c0");
    private static final UUID DIDEN_UUID = UUID.fromString("491c1d9a-357f-4a98-bd24-4ddbeb8555b0");
    private static JavaPlugin tester;

    public RuntimeCommandsTest(JavaPlugin tester) {
        if (RuntimeCommandsTest.tester != null) {
            throw new IllegalStateException("Already testing by %s.".formatted(RuntimeCommandsTest.tester.getName()));
        }

        RuntimeCommandsTest.tester = tester;
        createCommands();
    }

    private void createCommands() {
        registerTestCommand("pose", NPCPoseTest::test);
        registerTestCommand("holo", HologramTest::test);
        registerTestCommand("npc", (p, a) -> NPCTest.test(p, a.length >= 1 ? a[0] : p.getName()));
        registerTestCommand("scoreboard", ScoreboardTest::test);
        registerTestCommand(
                "showplugins",
                (p, a) -> p.sendMessage(Arrays.toString(EternaPlugin.getPlugin().getServer().getPluginManager().getPlugins()))
        );
        registerTestCommand("signgui", (p, a) -> SignGUITest.test(p, Numbers.getInt(a[0], 1)));
        registerTestCommand("glowing", (p, a) -> GlowingTest.test(p, a.length >= 1 ? NumberConversions.toInt(a[0]) : 40));
        registerTestCommand("laser", (p, a) -> LaserTest.test(p.getPlayer()));
        registerTestCommand("gui", (p, a) -> PlayerAutoGUITest.test(p));
        registerTestCommand("abandonallquests", (player, args) -> {
            for (QuestProgress progress : QuestManager.current().getActiveQuests(player)) {
                progress.abandon();
            }
        });
        registerTestCommand("questautoclaim", (player, args) -> {
            final Quest quest = QuestManager.current().getById("start_quest");
            if (quest == null) {
                return;
            }
            quest.startQuest(player);
        });

        registerTestCommand("worldborder", ((player, args) -> {
            if (args.length >= 2) {
                final String string = args[0].toLowerCase();
                final PlayerBorder border = new PlayerBorder(player);
                final double size = NumberConversions.toDouble(args[1]);

                if (string.equalsIgnoreCase("red")) {
                    border.update(PlayerBorder.Operation.BORDER_RED, size);
                    Chat.sendMessage(player, "&aShowing red outline.");
                }
                else if (string.equalsIgnoreCase("green")) {
                    border.update(PlayerBorder.Operation.BORDER_GREEN, size);
                    Chat.sendMessage(player, "&aShowing green outline.");
                }
                else {
                    PlayerBorder.reset(player);
                    Chat.sendMessage(player, "&aReset border.");
                }
            }
            else {
                Chat.sendMessage(player, "/{} (operation) (size)");
            }
        }));

        registerTestCommand("globalnpc", (player, args) -> {
            Human.create(player.getLocation(), "GLOBAL NPC", player.getName()).showAll();
            Chat.sendMessage(player, "&aCreated global npc.");
        });

        registerTestCommand("recording", (player, args) -> {
            // replay start
            // replay play
            // replay pause
            if (args.length != 1) {
                return;
            }

            final Record record = Record.getReplay(player);
            final String arg0 = args[0].toLowerCase();

            switch (arg0) {
                case "new" -> {
                    new Record(player);
                    Chat.sendMessage(player, "&aForced new recording...");
                }

                case "start" -> {
                    if (record == null) {
                        new Record(player);
                        Chat.sendMessage(player, "&aStarted recording...");
                        return;
                    }

                    record.stopRecording();
                    Chat.sendMessage(player, "&aStopped recording.");
                }
                case "play" -> {
                    if (record != null) {
                        final Replay rp = record.getReplay();
                        if (rp.isPlaying()) {
                            rp.stop();
                            Chat.sendMessage(player, "&bStopped replay.");
                            return;
                        }

                        rp.setGlobalReplay(true);
                        rp.start();
                        Chat.sendMessage(player, "&bStarted replay.");
                    }
                }
                case "pause" -> {
                    if (record != null) {
                        record.getReplay().pause();
                        Chat.sendMessage(player, "&e%s replay.", record.getReplay().isPaused() ? "Paused" : "Unpaused");
                    }
                }
            }

        });

        registerTestCommand("padding", (player, args) -> {
            final Padding padding = new Padding(20, 10, 5).setMargin(3).setFillCharacters('.');
            final String format = padding.formatPrefix(
                    " >",
                    "Hello world", 1, player.getPing()
            );

            Chat.sendMessage(player, format);
        });

        registerTestCommand("paddingScoreboard", (player, args) -> {
            final Scoreboarder scoreboard = new Scoreboarder("padding test");
            final Padding padding = new Padding(25, 3).setMargin(3).setFillCharacters('.');
            final HashSet<String> names = Sets.newHashSet();

            names.add(player.getName());
            names.add("Banana");
            names.add("Apple");
            names.add("Pineapple");
            names.add("Dragon-fruit");
            names.add("Kiwi");

            int index = 0;
            for (String name : names) {
                final String format = padding.formatPrefixIf(name.equals(player.getName()), " >", "", name, index++);

                scoreboard.addLine(format);
                System.out.println(format);
            }

            scoreboard.addPlayer(player);
        });

        registerTestCommand("nbtType", (player, args) -> {
            final ItemStack handItem = player.getInventory().getItemInMainHand();

            if (handItem.getType().isAir()) {
                Chat.sendMessage(player, "&cHold an item!");
                return;
            }

            final ItemMeta meta = handItem.getItemMeta();
            assert meta != null;

            NBT.setValue(meta, "hello.world", NBTType.BOOL, false);
            handItem.setItemMeta(meta);

            final Boolean value = NBT.getValue(meta, "hello.world", NBTType.BOOL);

            Chat.sendMessage(player, "value=" + value);
        });

        registerTestCommand("itembuilder", (player, args) -> {
            ItemBuilderTest.test(player);
        });

        registerTestCommand("synthesizer", (player, args) -> {
            SynthesizerTest.randomStuff.play(player);
        });

        registerTestCommand("tablist", (player, args) -> {
            TablistTest.test(player, args.length == 1 ? args[0] : "");
        });

        registerTestCommand("ai", (player, args) -> {
            final LivingEntity entity = Entities.PILLAGER.spawn(player.getLocation(), self -> self.setSilent(true));
            final AI ai = MobAI.of(entity);

            ai.removeAllGoals();
            ai.addGoal(new FloatGoal(ai));
            ai.addGoal(new MeleeAttackGoal(ai, 1.0d, true));

            ai.getGoals().forEach(goal -> {
                System.out.println(goal.getClass().getSimpleName());
            });

        });

        registerTestCommand("rope", (player, args) -> {
            final Location location = player.getLocation();
            final Rope rope = new Rope(location, location.clone().add(7.0d, 3.0d, 0.0d)).spawn();

            Chat.sendMessage(player, "&aCreated rope");

            Runnables.runLater(() -> {
                rope.remove();
                Chat.sendMessage(player, "&aRemoved rope");
            }, 80);
        });

        registerTestCommand("pagegui", PlayerPageGUITest::test);
        registerTestCommand("displayHologram", DisplayHologramTest::test);
        registerTestCommand("guiRename", PlayerGUITest::test);
        registerTestCommand("fakePlayer", FakePlayerTest::test);

        registerTestCommand("newItemBuilder", (player, args) -> {
            player.getInventory().addItem(new ItemBuilder(Material.IRON_BOOTS)
                    .setArmorTrim(TrimPattern.TIDE, TrimMaterial.DIAMOND).asIcon());
        });

        registerTestCommand("stringWrap", (player, args) -> {
            final ItemBuilder builder = new ItemBuilder(Material.IRON_INGOT);

            final ChatColor color = ChatColor.of("#abcdef");

            builder.addSmartLore(
                    "&cThis is a very long string that should be " + color +
                            "wrapped after certain &rchar limit &c&land &f&l&n&oit is also colored, &rbut the colors should not count as char limit."
            );

            player.getInventory().addItem(builder.asIcon());
        });

        registerTestCommand("weightedCollection", (player, args) -> {
            final WeightedCollection<String> cls = new WeightedCollection<>();

            cls.add("hello", 10);
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

            player.sendMessage(mapped.toString());
        });

        processor.registerCommand(new SimpleAdminCommand("testBSParser") {

            private DisplayData data;
            private DisplayEntity entity;

            @Override
            protected void execute(CommandSender sender, String[] args) {
                final Player player = Bukkit.getPlayer(HAPYL_UUID);

                if (player == null) {
                    return;
                }

                if (data == null) {
                    data = BlockStudioParser.parse(Chat.arrayToString(args, 0));
                    entity = data.spawn(player.getLocation());
                    Chat.sendMessage(player, "&aSpawned!");
                    return;
                }

                switch (args[0].toLowerCase()) {
                    case "tp" -> {
                        entity.teleport(player.getLocation());
                        Chat.sendMessage(player, "&aTeleported!");
                    }
                    case "remove" -> {
                        entity.remove();
                        entity = null;
                        data = null;
                        Chat.sendMessage(player, "&cRemoved!");
                    }
                    default -> Chat.sendMessage(player, "&eWhat?");
                }
            }
        });

        registerTestCommand("getPluginCommands", (player, args) -> {
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                final List<SimpleCommand> commands = CommandProcessor.getCommands(plugin);

                Chat.sendMessage(player, "Plugin: " + plugin.getName());
                Chat.sendMessage(player, "Commands: ");

                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < commands.size(); i++) {
                    if (i != 0) {
                        builder.append(", ");
                    }

                    final SimpleCommand command = commands.get(i);
                    builder.append(command.getName()).append(" + ").append(command.getHandle().getClass().getSimpleName());
                }

                Chat.sendMessage(player, builder.toString());
            }
        });

        final SimplePlayerAdminCommand cdCommand = new SimplePlayerAdminCommand("testCooldownCommand") {
            @Override
            protected void execute(Player player, String[] args) {
                Chat.sendMessage(player, "&aExecuted!");
            }
        };

        cdCommand.setCooldownTick(60);
        processor.registerCommand(cdCommand);

        registerTestCommand("computeModule", (player, args) -> {
            ComputeTest.test(player);
        });

        registerTestCommand("drawPolygon", (player, args) -> {
            final int point = Validate.getInt(args[0]);
            final double distance = Validate.getDouble(args[1]);

            Geometry.drawPolygon(player.getLocation(), point, distance, new WorldParticle(Particle.FLAME));
        });

        registerTestCommand("entitiesModule", (player, args) -> {
            Entities.ARMOR_STAND_MARKER.spawn(player.getLocation());

            Chat.sendMessage(player, "&aDone!");
        });
    }

    private static void registerTestCommand(String test, Action action) {
        processor.registerCommand(new SimplePlayerAdminCommand("test" + test) {
            @Override
            protected void execute(Player player, String[] args) {
                final UUID uuid = player.getUniqueId();

                if (!uuid.equals(HAPYL_UUID) && !uuid.equals(DIDEN_UUID)) {
                    Chat.sendMessage(player, "&4You're now allowed to use this.");
                    return;
                }

                action.use(player, args);
            }
        });

    }

    private interface Action {
        void use(Player player, String[] args);
    }

}
