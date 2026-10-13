package test;

import me.hapyl.eterna.module.player.Vignette;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class EternaTestVignette extends EternaTest {
    
    EternaTestVignette(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Player player = context.player();
        
        context.info(Component.text("Set color to RED"));
        Vignette.red(player);
        
        context.scheduler()
               .then(SchedulerTask.later(() -> {
                   Vignette.clear(player);
                   context.assertTestPassed();
               }, 60))
               .execute();
    }
    
}