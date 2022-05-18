package kz.hapyl.spigotutils.module.parkour;

import com.google.common.collect.Maps;
import kz.hapyl.spigotutils.module.util.Holder;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Holds stats of parkour.
 */
public class Stats extends Holder<Player> {

    private final Map<Type, Long> valueMap;

    public Stats(Player player) {
        super(player);
        valueMap = Maps.newHashMap();
    }

    public void increment(Type type, long value) {
        valueMap.put(type, valueMap.computeIfAbsent(type, i -> 0L) + value);
    }

    public void decrement(Type type, long value) {
        valueMap.put(type, valueMap.computeIfAbsent(type, i -> 0L) - value);
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

    }

}
