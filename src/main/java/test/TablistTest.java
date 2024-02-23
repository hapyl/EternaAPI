package test;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.player.tablist.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

@RuntimeStaticTest
final class TablistTest {

    private TablistTest() {
    }

    static Tablist tablist;
    static BukkitTask task;

    static void test(Player player, String args) {
        if (tablist != null && args.equalsIgnoreCase("reset")) {
            tablist.destroy();
            tablist = null;
            task.cancel();
            task = null;

            player.sendMessage(ChatColor.RED + "Reset!");
            return;
        }

        if (tablist != null) {
            tablist.destroy();
            task.cancel();
        }

        final Tablist oldTablist = Tablist.getPlayerTabList(player);
        if (oldTablist != null) {
            oldTablist.destroy();
        }

        tablist = new Tablist();

        Bukkit.getOnlinePlayers().forEach(p -> {
            tablist.show(p);
        });

        tablist.show(player);

        task = new BukkitRunnable() {
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

        player.sendMessage("showing");
    }

}
