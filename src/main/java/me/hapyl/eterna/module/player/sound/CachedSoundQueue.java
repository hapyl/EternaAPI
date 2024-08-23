package me.hapyl.eterna.module.player.sound;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Compute;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CachedSoundQueue {

    private final Map<Integer, Set<SoundQueueSound>> cachedSounds;
    private final int maxFrame;

    CachedSoundQueue(LinkedList<SoundQueueSound> sounds) {
        this.cachedSounds = Maps.newHashMap();
        int delay = 0;

        for (final SoundQueueSound sound : sounds) {
            delay += sound.getDelay();

            cachedSounds.compute(delay, Compute.setAdd(sound));
        }

        this.maxFrame = delay;
    }

    public int getMaxFrame() {
        return maxFrame;
    }

    @Nonnull
    public Set<SoundQueueSound> get(int frame) {
        final Set<SoundQueueSound> sounds = cachedSounds.get(frame);

        return sounds != null ? sounds : Set.of();
    }

}
