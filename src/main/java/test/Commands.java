package test;

import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.command.CommandProcessor;
import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import kz.hapyl.spigotutils.module.quest.Quest;
import kz.hapyl.spigotutils.module.quest.QuestManager;
import kz.hapyl.spigotutils.module.quest.QuestProgress;
import kz.hapyl.spigotutils.module.reflect.border.PlayerBorder;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;

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

    }

    private static final CommandProcessor processor = new CommandProcessor();

    private static void registerCommand(String command, Action action) {
        processor.registerCommand(new SimplePlayerAdminCommand(command) {
            @Override
            protected void execute(Player player, String[] args) {
                action.use(player, args);
            }
        });

    }

    private interface Action {

        void use(Player player, String[] args);

    }

}
