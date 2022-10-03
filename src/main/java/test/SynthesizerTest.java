package test;

import me.hapyl.spigotutils.module.player.synthesizer.Synthesizer;

public class SynthesizerTest {

    public static final Synthesizer synthesizer = Synthesizer
            .singleTrack("a-a-a--DB--DB")
            .plingWhere('a', 2.0f)
            .plingWhere('D', 1.0f)
            .plingWhere('B', 0.5f)
            .toSynthesizer()
            .setBPT(2);

    public static final Synthesizer randomStuff = new Synthesizer()
            .addTrack("aaaaaaaaaaaaaa").plingWhere('a', 1.75f).toSynthesizer()
            .addTrack("---a-aa---a-aa").plingWhere('a', 0.75f).toSynthesizer().setBPT(2);

}
