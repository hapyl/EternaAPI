package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.reflect.glow.Glowing;
import me.hapyl.eterna.module.reflect.glow.GlowingData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GlowingManager extends EternaManager<Player, GlowingData> {
    GlowingManager(@NotNull EternaPlugin eterna) {
        super(eterna);
    }

    public void register(@Nonnull Glowing glowing) {
        final Player player = glowing.getPlayer();
        final Entity entity = glowing.getEntity();

        getGlowing(player).add(entity, glowing);
    }

    public void unregister(@Nonnull Glowing glowing) {
        final Player player = glowing.getPlayer();
        final Entity entity = glowing.getEntity();

        getGlowing(player).remove(entity);
    }

    @Nonnull
    public GlowingData getGlowing(@Nonnull Player player) {
        return managing.computeIfAbsent(player, GlowingData::new);
    }

    @Nullable
    public Glowing getGlowing(@Nonnull Player player, @Nonnull Entity entity) {
        return getGlowing(player).getGlowing(entity);
    }

    @Nullable
    public Entity getById(int entityId) {
        for (GlowingData data : managing.values()) {
            final Entity entity = data.byId(entityId);

            if (entity != null) {
                return entity;
            }
        }

        return null;
    }

    public void stopGlowing(@Nonnull Player player, @Nonnull Entity entity) {
        final GlowingData data = getGlowing(player);
        final Glowing glowing = data.getGlowing(entity);

        if (glowing != null) {
            glowing.forceStop();
        }

        data.remove(entity);
    }

    public void stopGlowing(@Nonnull Entity entity) {
        managing.forEach((player, data) -> {
            final Glowing glowing = data.remove(entity);

            if (glowing != null) {
                glowing.forceStop();
            }
        });
    }

}
