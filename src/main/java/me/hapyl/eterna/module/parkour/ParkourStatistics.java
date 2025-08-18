package me.hapyl.eterna.module.parkour;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Compute;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Holds stats of parkour.
 */
public class ParkourStatistics {

    private final Map<Type, Long> valueMap;

    ParkourStatistics() {
        this.valueMap = Maps.newHashMap();
    }

    public void increment(@Nonnull Type type, long value) {
        this.valueMap.compute(type, Compute.longAdd(value));
    }

    public long getStat(@Nonnull Type type) {
        return valueMap.getOrDefault(type, 0L);
    }

    public void reset() {
        this.valueMap.clear();
    }

    public enum Type {
        JUMP,
        CHECKPOINT_TELEPORT
    }

}
