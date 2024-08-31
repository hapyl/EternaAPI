package me.hapyl.eterna.module.reflect.glow;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Ticking;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Stored entity glowing data.
 */
public class GlowingData implements Ticking {

    private final Player player;
    private final Map<Entity, Glowing> data;

    public GlowingData(@Nonnull Player player) {
        this.player = player;
        this.data = Maps.newConcurrentMap();
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nullable
    public Glowing getGlowing(@Nonnull Entity entity) {
        return data.get(entity);
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

    public void add(@Nonnull Entity entity, @Nonnull Glowing glowing) {
        final Glowing oldGlowing = getGlowing(entity);

        if (oldGlowing != null) {
            oldGlowing.forceStop();
        }

        data.put(entity, glowing);
    }

    @Nullable
    public Glowing remove(@Nonnull Entity entity) {
        return data.remove(entity);
    }

    public boolean isGlowing(@Nonnull Entity entity) {
        final Glowing glowing = data.get(entity);

        return glowing != null && glowing.isGlowing();
    }

    @Override
    public void tick() {
        data.forEach((entity, glowing) -> {
            if (!glowing.getPlayer().isOnline() || glowing.getEntity().isDead()) {
                data.remove(entity);
                glowing.stop();
            }

            glowing.tick();
        });
    }
}
