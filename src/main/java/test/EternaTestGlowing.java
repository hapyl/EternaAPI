package test;

import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.math.Tick;
import me.hapyl.eterna.module.reflect.glowing.Glowing;
import me.hapyl.eterna.module.reflect.team.PacketTeamColor;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class EternaTestGlowing extends EternaTest {
    
    EternaTestGlowing(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Player player = context.player();
        final Player glowingPlayer = context.argument(0).toPlayer();
        
        if (glowingPlayer == null) {
            context.info(Component.text("No player named %s, using a piggy instead!".formatted(context.argument(0).toString())));
        }
        
        final Entity entity = glowingPlayer != null ? glowingPlayer : Entities.PIG.spawn(
                player.getLocation(), self -> {
                    self.setInvisible(false);
                    self.setAI(false);
                }
        );
        
        final PacketTeamColor glowingColor = PacketTeamColor.YELLOW;
        final Scoreboard scoreboard = player.getScoreboard();
        
        // Test with an existing team
        final Team team = Objects.requireNonNullElseGet(scoreboard.getTeam("test_glowing_team"), () -> scoreboard.registerNewTeam("test_glowing_team"));
        
        team.color(NamedTextColor.LIGHT_PURPLE);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.addEntity(entity);
        
        final int glowingTime = 200;
        final int colorChangeTime = glowingTime / 2;
        
        context.info(Component.text("Set glowing for %s.".formatted(Tick.round(glowingTime))));
        Glowing.setGlowing(player, entity, glowingColor, glowingTime);
        
        context.scheduler()
               .then(SchedulerTask.later(() -> {
                   PacketTeamColor newColor;
                   
                   do {
                       newColor = PacketTeamColor.ofRandom();
                   }
                   while (newColor == glowingColor);
                   
                   Glowing.setGlowing(player, entity, newColor);
                   context.info(Component.text("Update color to `%s`!".formatted(newColor)));
               }, colorChangeTime))
               .then(SchedulerTask.later(() -> {
                   entity.setGlowing(true);
                   
                   context.info(Component.text("Stopped glowing, made entity naturally glow."));
               }, colorChangeTime + 10))
               .then(SchedulerTask.later(() -> {
                   if (glowingPlayer == null) {
                       entity.remove();
                   }
                   else {
                       entity.setGlowing(false);
                   }
                   
                   team.unregister();
                   context.assertTestPassed();
               }, 40))
               .execute();
    }
    
}
