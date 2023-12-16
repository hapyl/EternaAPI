package me.hapyl.spigotutils.module.reflect.glow;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.util.DependencyInjector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class GlowingRegistry extends DependencyInjector<EternaPlugin> {

    private final Map<Player, GlowingData> playerGlowing;

    /**
     * A single entity may have multiple glowing tasks
     * for different players. This is why concurrent
     * set is used to store all possible glowing tasks.
     */
    public GlowingRegistry(EternaPlugin plugin) {
        super(plugin);
        playerGlowing = Maps.newHashMap();
    }

    /**
     * Adds glowing for entity.
     *
     * @param glowing - Glowing.
     */
    public void addGlowing(@Nonnull Glowing glowing) {
        final Player player = glowing.getPlayer();
        final Entity entity = glowing.getEntity();

        getGlowing(player).add(entity, glowing);
    }

    /**
     * Removes glowing from entity.
     *
     * @param glowing - Glowing.
     */
    protected void removeGlowing(@Nonnull Glowing glowing) {
        final Player player = glowing.getPlayer();
        final Entity entity = glowing.getEntity();

        getGlowing(player).remove(entity);
    }

    /**
     * Stops all glowing tasks from entity.
     *
     * @param entity - Entity.
     */
    protected void stopGlowing(@Nonnull Player player, @Nonnull Entity entity) {
        final GlowingData data = getGlowing(player);
        final Glowing glowing = data.getGlowing(entity);

        if (glowing != null) {
            glowing.forceStop();
        }

        data.remove(entity);
    }

    protected void stopGlowing(@Nonnull Entity entity) {
        for (GlowingData data : playerGlowing.values()) {
            final Glowing glowing = data.getGlowing(entity);

            if (glowing != null) {
                glowing.forceStop();
            }

            data.remove(entity);
        }
    }

    @Nonnull
    public GlowingData getGlowing(@Nonnull Player player) {
        return playerGlowing.computeIfAbsent(player, GlowingData::new);
    }

    @Nullable
    public Glowing getGlowing(@Nonnull Player player, @Nonnull Entity entity) {
        return getGlowing(player).getGlowing(entity);
    }

    public boolean isGlowing(Player player, Entity entity) {
        return getGlowing(player).isGlowing(entity);
    }

    @Nullable
    public Entity getById(int entityId) {
        for (GlowingData value : playerGlowing.values()) {
            final Entity entity = value.byId(entityId);

            if (entity != null) {
                return entity;
            }
        }

        return null;
    }

    public void tickAll() {
        playerGlowing.values().forEach(GlowingData::tickAll);
    }
}
