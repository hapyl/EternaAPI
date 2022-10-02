package test;

import me.hapyl.spigotutils.module.player.synthesizer.Synthesizer;
import org.bukkit.entity.Player;

import java.util.Set;

public class SynthesizerTest {

    public static final Synthesizer synthesizer = Synthesizer
            .singleTrack("a-a-a--DB--DB")
            .plingWhere('a', 2.0f)
            .plingWhere('D', 1.0f)
            .plingWhere('B', 0.5f)
            .toSynthesizer()
            .setBPT(2);

    public static final Synthesizer randomStuff = new Synthesizer() {
        @Override
        public void onStartPlaying(Set<Player> player) {
            player.forEach(pl -> pl.sendMessage("started"));
        }

        @Override
        public void onStopPlaying(Set<Player> players) {
            players.forEach(pl -> pl.sendMessage("finished playing"));
        }
    }
            .addTrack("aaaaaaaaaaaaaa").plingWhere('a', 1.75f).toSynthesizer()
            .addTrack("---a-aa---a-aa").plingWhere('a', 0.75f).toSynthesizer().setBPT(2);

}
