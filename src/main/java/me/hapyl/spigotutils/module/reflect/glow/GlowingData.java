package me.hapyl.spigotutils.module.reflect.glow;

import com.google.common.collect.Maps;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Stored entity glowing data.
 */
public class GlowingData {

    private final Player player;
    private final Map<Entity, Glowing> data;

    public GlowingData(Player player) {
        this.player = player;
        this.data = Maps.newConcurrentMap();
    }

    public Player getPlayer() {
        return player;
    }

    @Nullable
    public Glowing getGlowing(Entity entity) {
        return data.get(entity);
    }

    public void tickAll() {
        data.forEach((entity, glowing) -> {
            if (!glowing.isGlowing()) { // actually remove if finished
                data.remove(entity);
            }

            glowing.tick();
        });
    }

    @Nullable
    public Entity byId(int entityId) {
        for (Glowing value : data.values()) {
            final Entity entity = value.getEntity();

            if (entity.getEntityId() == entityId) {
                return entity;
            }
        }

        return null;
    }

    public void add(Entity entity, Glowing glowing) {
        final Glowing oldGlowing = getGlowing(entity);

        if (oldGlowing != null) {
            oldGlowing.forceStop();
        }

        data.put(entity, glowing);
    }

    public void remove(Entity entity) {
        data.remove(entity);
    }

    public boolean isGlowing(Entity entity) {
        final Glowing glowing = data.get(entity);

        return glowing != null && glowing.isGlowing();
    }
}
