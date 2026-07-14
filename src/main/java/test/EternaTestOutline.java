package test;

import me.hapyl.eterna.module.player.PlayerOutline;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class EternaTestOutline extends EternaTest {
    
    EternaTestOutline(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final PlayerOutline border = new PlayerOutline(context.player());
        
        context.info(Component.text("Set color to RED"));
        border.setColor(PlayerOutline.Color.RED, 5);
        
        context.scheduler()
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Set color to GREEN"));
                   border.setColor(PlayerOutline.Color.GREEN, 5);
               }, 60))
               .then(SchedulerTask.later(() -> {
                   context.assertTestPassed();
                   border.dispose();
               }, 60))
               .execute();
    }
    
}