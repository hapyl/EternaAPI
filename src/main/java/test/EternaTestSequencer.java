package test;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.player.sequencer.Sequencer;
import me.hapyl.eterna.module.player.sequencer.Track;
import me.hapyl.eterna.module.registry.Key;
import org.jetbrains.annotations.NotNull;

public final class EternaTestSequencer extends EternaTest {
    
    private final Sequencer sequencer;
    
    EternaTestSequencer(@NotNull Key key) {
        super(key);
        
        this.sequencer = Sequencer.builder(Eterna.getPlugin())
                                  .track(
                                          Track.builder("---p---P---p---P---p---P------------s")
                                               .plingWhere('p', 1.0f)
                                               .plingWhere('P', 0.75f)
                                               .snareWhere('s', 0.75f)
                                  )
                                  .track(
                                          Track.builder("---b---B---b---B---b---B")
                                               .bassWhere('b', 1.0f)
                                               .bassWhere('B', 0.75f)
                                  )
                                  .track(
                                          Track.builder("---h---h---h---h---h---h")
                                               .where('h', (player, volume) -> {
                                                   player.sendHurtAnimation(0.0f);
                                               })
                                  )
                                  .build();
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        sequencer.play(context.player());
    }
    
}