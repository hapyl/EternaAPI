package test;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.quest.Quest;
import me.hapyl.spigotutils.module.quest.QuestManager;
import me.hapyl.spigotutils.module.quest.QuestProgress;
import me.hapyl.spigotutils.module.record.Record;
import me.hapyl.spigotutils.module.record.Replay;
import me.hapyl.spigotutils.module.reflect.border.PlayerBorder;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;
import java.util.UUID;

public class Commands {

    public static void createCommands() {
        registerCommand("testholo", HologramTest::run);
        registerCommand("testnpc", (p, a) -> {
            NPCTest.create(p, a.length >= 1 ? a[0] : p.getName());
        });
        registerCommand("testscoreboard", ScoreboardTest::create);
        registerCommand(
                "testshowplugins",
                (p, a) -> p.sendMessage(Arrays.toString(EternaPlugin.getPlugin().getServer()
                                                                .getPluginManager()
                                                                .getPlugins()))
        );
        registerCommand("testsigngui", (p, a) -> SignGUITest.run(p));
        registerCommand(
                "testglowing",
                (p, a) -> GlowingTest.run(p, a.length >= 1 ? NumberConversions.toInt(a[0]) : 40)
        );

        registerCommand("testlaser", (p, a) -> LaserTest.test(p.getPlayer()));
        registerCommand("testgui", (p, a) -> GUITest.test(p));
        registerCommand("testabandonallquests", (player, args) -> {
            for (QuestProgress progress : QuestManager.current().getActiveQuests(player)) {
                progress.abandon();
            }
        });
        registerCommand("testquestautoclaim", (player, args) -> {
            final Quest quest = QuestManager.current().getById("start_quest");
            if (quest == null) {
                return;
            }
            quest.startQuest(player);
        });

        registerCommand("testworldborder", ((player, args) -> {
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

        registerCommand("testrecording", (player, args) -> {
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

        registerCommand("testitembuilder", (player, args) -> {
            player.getInventory().addItem(ItemBuilderTest.ITEM_TEST);
        });

        registerCommand("testsynthesizer", (player, args) -> {
            SynthesizerTest.randomStuff.play(player);
        });

        registerCommand("testtablist", (player, args) -> {
            TablistTest.run(player, args.length == 1 ? args[0] : "");
        });

    }

    private static final CommandProcessor processor = new CommandProcessor();
    private static final UUID uuid = UUID.fromString("b58e578c-8e36-4789-af50-1ee7400307c0");

    private static void registerCommand(String command, Action action) {
        processor.registerCommand(new SimplePlayerAdminCommand(command) {
            @Override
            protected void execute(Player player, String[] args) {
                if (!player.getUniqueId().equals(uuid)) {
                    Chat.sendMessage(player, "&cUUID not whitelisted.");
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
