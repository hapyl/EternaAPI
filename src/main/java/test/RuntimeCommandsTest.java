package test;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.ai.MobAI;
import me.hapyl.spigotutils.module.ai.goal.*;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.entity.Rope;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.quest.Quest;
import me.hapyl.spigotutils.module.quest.QuestManager;
import me.hapyl.spigotutils.module.quest.QuestProgress;
import me.hapyl.spigotutils.module.record.Record;
import me.hapyl.spigotutils.module.record.Replay;
import me.hapyl.spigotutils.module.reflect.border.PlayerBorder;
import me.hapyl.spigotutils.module.reflect.npc.Human;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.UUID;

/**
 * This is a mess class that is supposed to be a mess class.
 * Ignore this, only me 😊 can use this.
 */
public final class RuntimeCommandsTest {

    private static JavaPlugin tester;

    public RuntimeCommandsTest(JavaPlugin tester) {
        if (RuntimeCommandsTest.tester != null) {
            throw new IllegalStateException("Already testing by %s.".formatted(RuntimeCommandsTest.tester.getName()));
        }

        RuntimeCommandsTest.tester = tester;
        createCommands();
    }

    private void createCommands() {

        registerTestCommand("pose", NPCPoseTest::work);

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

        registerTestCommand("itembuilder", (player, args) -> {
            player.getInventory().addItem(ItemBuilderTest.ITEM_TEST);
        });

        registerTestCommand("synthesizer", (player, args) -> {
            SynthesizerTest.randomStuff.play(player);
        });

        registerTestCommand("tablist", (player, args) -> {
            TablistTest.test(player, args.length == 1 ? args[0] : "");
        });

        registerTestCommand("ai", (player, args) -> {
            final Wolf entity = Entities.WOLF.spawn(player.getLocation());
            final LivingEntity sheep = Entities.SHEEP.spawn(player.getLocation());
            final AI ai = MobAI.of(entity);

            ai.removeAllGoals();
            ai.addGoal(new AvoidTargetGoal(ai, EntityType.PLAYER, 5, 1.0d, 2.0d));
            ai.addGoal(new FollowEntityGoal(ai, sheep, 1.0d, 1.0f, 1.0f));
            ai.addGoal(new LookAtPlayerGoal(ai, 8.0f));
            ai.addGoal(new RandomlyLookAroundGoal(ai));
            ai.addGoal(new RandomlyStrollGoal(ai, 1.0f));
            ai.addGoal(new TemptGoal(ai, Material.WHEAT, 1.0f, false));

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

    }

    private static final CommandProcessor processor = new CommandProcessor();
    private static final UUID HAPYL_UUID = UUID.fromString("b58e578c-8e36-4789-af50-1ee7400307c0");
    private static final UUID DIDEN_UUID = UUID.fromString("491c1d9a-357f-4a98-bd24-4ddbeb8555b0");

    private static void registerTestCommand(String test, Action action) {
        processor.registerCommand(new SimplePlayerAdminCommand("test" + test) {
            @Override
            protected void execute(Player player, String[] args) {
                final UUID uuid = player.getUniqueId();

                if (!uuid.equals(HAPYL_UUID)) {
                    if (uuid.equals(DIDEN_UUID) || player.getName().equalsIgnoreCase("DiDenPro")) {
                        player.setVelocity(new Vector(0.0d, 1.0d, 0.0d));
                        player.setGameMode(GameMode.ADVENTURE);
                        PlayerLib.addEffect(player, PotionEffectType.DARKNESS, 60, 1);
                        Chat.sendMessage(player, "&cNice try, idiot.");
                        return;
                    }

                    Chat.sendMessage(player, "&cNice try.");
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
