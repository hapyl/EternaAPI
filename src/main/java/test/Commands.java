package test;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.quest.Quest;
import me.hapyl.spigotutils.module.quest.QuestManager;
import me.hapyl.spigotutils.module.quest.QuestProgress;
import me.hapyl.spigotutils.module.record.Record;
import me.hapyl.spigotutils.module.record.Replay;
import me.hapyl.spigotutils.module.reflect.border.PlayerBorder;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.UUID;

/**
 * This is a mess class that is supposed to be a mess class.
 * Ignore this, only me 😊 can use this.
 */
public class Commands {

    public static void createCommands() {
        registerTestCommand("pose", NPCPoseTest::work);

        registerTestCommand("holo", HologramTest::run);
        registerTestCommand("npc", (p, a) -> NPCTest.create(p, a.length >= 1 ? a[0] : p.getName()));
        registerTestCommand("scoreboard", ScoreboardTest::create);
        registerTestCommand(
                "showplugins",
                (p, a) -> p.sendMessage(Arrays.toString(EternaPlugin.getPlugin().getServer().getPluginManager().getPlugins()))
        );
        registerTestCommand("signgui", (p, a) -> SignGUITest.run(p, Numbers.getInt(a[0], 1)));
        registerTestCommand("glowing", (p, a) -> GlowingTest.run(p, a.length >= 1 ? NumberConversions.toInt(a[0]) : 40));

        registerTestCommand("laser", (p, a) -> LaserTest.test(p.getPlayer()));
        registerTestCommand("gui", (p, a) -> GUITest.test(p));
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
            TablistTest.run(player, args.length == 1 ? args[0] : "");
        });

        registerTestCommand("visibility", ((player, args) -> {
            TestVisibility.run(player);
        }));

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
