package test;

import com.google.common.collect.Lists;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.player.tablist.*;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.Enums;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class EternaTestTablist extends EternaTest {
    
    EternaTestTablist(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Tablist tablist = new Tablist(context.player());
        
        tablist.show();
        
        new BukkitRunnable() {
            private int iterations = 5;
            
            @Override
            public void run() {
                if (iterations-- <= 0) {
                    context.assertTestPassed();
                    tablist.dispose();
                    
                    cancel();
                    return;
                }
                
                final List<Player> players = Lists.newArrayList(Bukkit.getOnlinePlayers());
                
                final EntryList entryListPlayers = EntryList.ofEmpty();
                entryListPlayers.append(Component.text("Players:", NamedTextColor.GREEN));
                entryListPlayers.append(Component.empty());
                
                players.forEach(player -> {
                    entryListPlayers.append(player.name(), EntryTexture.of(player));
                });
                
                entryListPlayers.append(
                        Component.text(RANDOM.nextDouble() * 1000), EntryTexture.random(), Enums.getRandomValue(PingBars.class)
                );
                
                final EntryList entryListColors = EntryList.ofEmpty();
                
                for (NamedTextColor color : NamedTextColor.NAMES.values()) {
                    entryListColors.append(Component.text(color.toString()), EntryTexture.of(color));
                }
                
                tablist.setColumn(TablistColumn.FIRST, entryListPlayers);
                tablist.setColumn(TablistColumn.SECOND, entryListColors);
                
                tablist.setColumn(
                        TablistColumn.THIRD,
                        Component.text("Frinds:", NamedTextColor.DARK_GREEN),
                        Component.text("Friends? What friends?", NamedTextColor.DARK_GRAY)
                );
                
                int pingIndex = 0;
                
                for (PingBars ping : PingBars.values()) {
                    final TablistEntry entry = tablist.getEntry(TablistColumn.THIRD, pingIndex++);
                    
                    entry.setPing(ping);
                }
                
            }
        }.runTaskTimer(Eterna.getPlugin(), 0, 20);
        
        tablist.setColumn(
                TablistColumn.FOURTH,
                Component.text("HELLO WORLD", NamedTextColor.YELLOW, TextDecoration.BOLD),
                Component.text("This is a test"),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.empty(),
                Component.text("Third from last..."),
                Component.text("Almost last one..."),
                Component.text("Last one!!!", NamedTextColor.GREEN)
        );
    }
    
}