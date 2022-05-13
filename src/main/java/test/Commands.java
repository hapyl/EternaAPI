package test;

import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.command.CommandProcessor;
import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
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
