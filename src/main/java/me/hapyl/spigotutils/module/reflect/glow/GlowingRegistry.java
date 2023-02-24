package me.hapyl.spigotutils.module.reflect.glow;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.registry.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Set;

public class GlowingRegistry extends Registry<Entity, Set<Glowing>> {

    /**
     * Single entity may have multiple glowing tasks
     * for different players. This is why concurrent
     * set is used to store all possible glowing tasks.
     */
    public GlowingRegistry(EternaPlugin plugin) {
        super(plugin);
    }

    /**
     * Adds glowing for entity.
     *
     * @param entity  - Entity.
     * @param glowing - Glowing.
     */
    public void addGlowing(Entity entity, Glowing glowing) {
        final Set<Glowing> set = getGlowing(entity);
        set.add(glowing);

        this.registry.put(entity, set);
    }

    /**
     * Removes glowing from entity.
     *
     * @param entity  - Entity.
     * @param glowing - Glowing.
     */
    public void removeGlowing(Entity entity, Glowing glowing) {
        final Set<Glowing> set = getGlowing(entity);
        set.remove(glowing);

        this.registry.put(entity, set);
    }

    /**
     * Stops all glowing tasks from entity.
     *
     * @param entity - Entity.
     */
    public void stopGlowing(Entity entity) {
        final Set<Glowing> set = getGlowing(entity);

        for (Glowing glow : set) {
            glow.forceStop();
        }
    }

    public void removeGlowing(Entity entity) {
        this.registry.remove(entity);
    }

    public Set<Glowing> getGlowing(Entity entity) {
        return registry.getOrDefault(entity, Sets.newConcurrentHashSet());
    }

    @Nullable
    public Glowing getGlowing(Entity entity, Player viewer) {
        final Set<Glowing> glowings = getGlowing(entity);
        for (Glowing glow : glowings) {
            if (glow.isPlayer(viewer) && glow.getDuration() > 0) {
                return glow;
            }
        }

        return null;
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
            if (glow.isPlayer(player) && glow.getDuration() > 0) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    public Entity getById(int entityId) {
        for (Entity entity : getKeys()) {
            if (entity.getEntityId() == entityId) {
                return entity;
            }
        }

        return null;
    }

    public void tickAll() {
        forEachValues(set -> set.forEach(Glowing::tick));
    }
}
