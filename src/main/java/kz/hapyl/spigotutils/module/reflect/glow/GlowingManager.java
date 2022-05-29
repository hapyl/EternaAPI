package kz.hapyl.spigotutils.module.reflect.glow;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.HashRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class GlowingManager extends HashRegistry<Entity, Glowing> {

    private final Map<Entity, Set<Glowing>> glowing = Maps.newConcurrentMap();

    public GlowingManager(EternaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register(Entity entity, Glowing glowing) {
        addGlowing(entity, glowing);
    }

    @Override
    public void unregister(Entity entity, Glowing glowing) {
        removeGlowing(entity, glowing);
    }

    public void addGlowing(Entity entity, Glowing glowing) {
        final Set<Glowing> set = getGlowing(entity);
        set.add(glowing);

        this.glowing.put(entity, set);
    }

    public void removeGlowing(Entity entity, Glowing glowing) {
        final Set<Glowing> set = getGlowing(entity);
        set.remove(glowing);

        this.glowing.put(entity, set);
    }

    public void removeGlowing(Entity entity) {
        this.glowing.remove(entity);
    }

    public Set<Glowing> getGlowing(Entity entity) {
        return glowing.getOrDefault(entity, Sets.newConcurrentHashSet());
    }

    public boolean isGlowing(Entity entity) {
        return !getGlowing(entity).isEmpty();
    }

    public boolean isGlowing(Entity entity, Player player) {
        final Set<Glowing> set = getGlowing(entity);
        if (set.isEmpty()) {
            return false;
        }

        for (Glowing glow : set) {
            if (glow.isViewer(player)) {
                return glow.getDuration() > 0;
            }
        }

        return false;
    }

    @Nullable
    public Entity getById(int entityId) {
        for (Entity entity : glowing.keySet()) {
            if (entity.getEntityId() == entityId) {
                return entity;
            }
        }
        return null;
    }

    public void tickAll() {
        this.glowing.forEach((entity, set) -> set.forEach(Glowing::tick));
    }
}
