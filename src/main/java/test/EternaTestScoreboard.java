package test;

import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.player.ScoreboardBuilder;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class EternaTestScoreboard extends EternaTest {
    
    EternaTestScoreboard(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Player player = context.player();
        final ScoreboardBuilder builder = new ScoreboardBuilder(player, Component.text("Test Scoreboard"));
        
        builder.setLines(
                Component.text("line 1"),
                Component.text("line 2", NamedTextColor.GREEN),
                Component.text("line 3", NamedTextColor.GOLD),
                Component.text("line 4 + %s".formatted(player.getName()), NamedTextColor.YELLOW),
                Component.text("line 5", NamedTextColor.DARK_PURPLE)
        );
        
        context.scheduler()
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Shrunk scoreboard"));
                   
                   builder.setLines(
                           Component.text("Text 1", randomColor()),
                           Component.text("Text 2", randomColor()),
                           Component.text("Text 3", randomColor())
                   );
               }, 20))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Grew scoreboard"));
                   
                   builder.setLines(
                           Component.text("Big 1", randomColor()),
                           Component.text("Big 2", randomColor()),
                           Component.text("Big 3", randomColor()),
                           Component.text("Big 4", randomColor()),
                           Component.text("Big 5", randomColor()),
                           Component.text("Big 6", randomColor()),
                           Component.text("Big 7", randomColor())
                   );
               }, 20))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Overgrew scoreboard"));
                   
                   builder.setLines(
                           Component.text("Overflow 1", randomColor()),
                           Component.text("Overflow 2", randomColor()),
                           Component.text("Overflow 3", randomColor()),
                           Component.text("Overflow 4", randomColor()),
                           Component.text("Overflow 5", randomColor()),
                           Component.text("Overflow 6", randomColor()),
                           Component.text("Overflow 7", randomColor()),
                           Component.text("Overflow 8", randomColor()),
                           Component.text("Overflow 9", randomColor()),
                           Component.text("Overflow 10", randomColor()),
                           Component.text("Overflow 11", randomColor()),
                           Component.text("Overflow 12", randomColor()),
                           Component.text("Overflow 13", randomColor()),
                           Component.text("Overflow 14", randomColor()),
                           Component.text("Overflow 15", randomColor()),
                           Component.text("Overflow 16", randomColor()),
                           Component.text("Overflow 17", randomColor()),
                           Component.text("Overflow 18", randomColor()),
                           Component.text("Overflow 19", randomColor())
                   );
                   
               }, 20))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Empties scoreboard"));
                   
                   builder.setLines(ComponentList.empty());
               }, 20))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Conditioned scoreboard"));
                   
                   new BukkitRunnable() {
                       private int iterations = 50;
                       
                       @Override
                       public void run() {
                           if (iterations-- <= 0) {
                               builder.hide();
                               context.assertTestPassed();
                               cancel();
                               return;
                           }
                           
                           builder.setLines(
                                   Component.text("Your name: ", NamedTextColor.GRAY).append(Component.text(player.getName(), NamedTextColor.GOLD)),
                                   Component.text("Sneaking? ", NamedTextColor.GRAY).append(player.isSneaking()
                                                                                            ? Component.text("Yes!", NamedTextColor.GREEN)
                                                                                            : Component.text("No", NamedTextColor.RED)
                                   )
                           );
                       }
                   }.runTaskTimer(PLUGIN, 0, 1);
               }, 20))
               .execute();
    }
    
    private @NotNull TextColor randomColor() {
        return TextColor.color(RANDOM.nextInt(0x1000000));
    }
    
}