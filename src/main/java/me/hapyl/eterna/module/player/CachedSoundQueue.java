package me.hapyl.eterna.module.player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class CachedSoundQueue {

    private final Map<Integer, Set<BukkitSound>> info;
    private final int maxFrame;

    public CachedSoundQueue(LinkedList<BukkitSound> sounds) {
        this.info = Maps.newHashMap();
        int i = 0;
        for (final BukkitSound sound : sounds) {
            i += sound.getDelay();
            final Set<BukkitSound> set = getOrNew(i);
            set.add(sound);
            info.put(i, set);
        }
        this.maxFrame = i;
    }

    public int getMaxFrame() {
        return maxFrame;
    }

    public Set<BukkitSound> getIfExists(int frame) {
        return getOrNew(frame);
    }

    private Set<BukkitSound> getOrNew(int frame) {
        return info.getOrDefault(frame, Sets.newHashSet());
    }

}
