package me.hapyl.eterna.module.parkour;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Holds stats of parkour.
 */
public class ParkourStatistics {

    private final Player player;
    private final Map<Type, Long> valueMap;

    public ParkourStatistics(Player player) {
        this.player = player;
        valueMap = Maps.newHashMap();
    }

    public void increment(Type type, long value) {
        valueMap.put(type, getStat(type) + value);
    }

    public void decrement(Type type, long value) {
        valueMap.put(type, getStat(type) - value);
    }

    public long getStat(Type type) {
        return valueMap.getOrDefault(type, 0L);
    }

    public void reset(Type type) {
        valueMap.put(type, 0L);
    }

    public void resetAll() {
        for (Type value : Type.values()) {
            reset(value);
        }
    }

    public enum Type {
        JUMP,
        CHECKPOINT_TELEPORT
        // More to come. Maybe
    }

}
