package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.module.reflect.team.PacketTeamColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Provides per-player entity glowing with color changing support.
 *
 * <h2>Note!</h2>
 * <p>
 * Because Minecraft is a stupid game, the glowing color is client-side and based on the entity's team.
 * </p>
 *
 * <p>
 * The module uses packet teams, but changing the <b>actual</b> entity team might break the glowing or even result in a crash; I either can't or won't fix it. -h
 * </p>
 *
 * @see Glowing#setGlowing(Player, Entity, PacketTeamColor, int)
 * @see Glowing#stopGlowing(Player, Entity)
 */
@ApiStatus.NonExtendable
public interface Glowing {
    
    /**
     * Defines the magic value for an infinite glowing duration.
     */
    int INFINITE_DURATION = -1;
    
    /**
     * Defines the default {@link PacketTeamColor} used for glowing.
     */
    @NotNull
    PacketTeamColor DEFAULT_COLOR = PacketTeamColor.WHITE;
    
    
    /**
     * Sets the glowing for the given {@link Player}.
     *
     * <p>
     * If the entity is already glowing for the player, it's color or/and duration will be modified.
     * </p>
     *
     * @param player   - The entity for whom to glow the entity.
     * @param entity   - The entity to glow.
     * @param color    - The glowing color.
     * @param duration - The glowing duration.
     */
    static void setGlowing(@NotNull Player player, @NotNull Entity entity, @NotNull PacketTeamColor color, int duration) {
        setGlowing0(player, entity, color, duration);
    }
    
    /**
     * Sets the glowing for the given {@link Player}.
     *
     * <p>
     * If the entity is already glowing for the player, it's color or/and duration will be modified.
     * </p>
     *
     * @param player - The entity for whom to glow the entity.
     * @param entity - The entity to glow.
     * @param color  - The glowing color.
     */
    static void setGlowing(@NotNull Player player, @NotNull Entity entity, @NotNull PacketTeamColor color) {
        setGlowing0(player, entity, color, null);
    }
    
    /**
     * Sets the glowing for the given {@link Player}.
     *
     * <p>
     * If the entity is already glowing for the player, it's color or/and duration will be modified.
     * </p>
     *
     * @param player   - The entity for whom to glow the entity.
     * @param entity   - The entity to glow.
     * @param duration - The glowing duration.
     */
    static void setGlowing(@NotNull Player player, @NotNull Entity entity, int duration) {
        setGlowing0(player, entity, null, duration);
    }
    
    /**
     * Stops the glowing for the given {@link Player}.
     *
     * @param player - The player for whom to stop the glowing.
     * @param entity - The entity to stop glowing.
     */
    static void stopGlowing(@NotNull Player player, @NotNull Entity entity) {
        GlowingHandler.handler.get(player).ifPresent(glowing -> {
            final GlowingInstance glowingInstance = glowing.entityMap.remove(entity);
            
            if (glowingInstance != null) {
                glowingInstance.remove();
            }
        });
    }
    
    /**
     * Stops all the glowing for the given {@link Player}.
     *
     * @param player - The player for whom to stop the glowing.
     */
    static void stopGlowing(@NotNull Player player) {
        final GlowingImpl glowing = GlowingHandler.handler.unregister(player);
        
        if (glowing != null) {
            glowing.entityMap.values().forEach(GlowingInstance::remove);
            glowing.entityMap.clear();
        }
    }
    
    /**
     * Stops all the glowing for the given {@link Entity}.
     *
     * <p>
     * This will stop entity from glowing for any players who it is glowing for.
     * </p>
     *
     * @param entity - The entity to stop glowing.
     */
    static void stopGlowing(@NotNull Entity entity) {
        GlowingHandler.handler.forEach(glowing -> {
            final GlowingInstance glowingInstance = glowing.entityMap.remove(entity);
            
            if (glowingInstance != null) {
                glowingInstance.remove();
            }
        });
    }
    
    /**
     * Gets a {@link GlowingInstance} for the given {@link Player}.
     *
     * @param player - The player for whom to retrieve the glowing.
     * @param entity - The entity for whom to retrieve the glowing.
     * @return a glowing instance wrapped in an optional.
     */
    @NotNull
    static Optional<GlowingInstance> getGlowing(@NotNull Player player, @NotNull Entity entity) {
        final GlowingImpl glowing = GlowingHandler.handler.get(player).orElse(null);
        
        return glowing != null ? Optional.ofNullable(glowing.entityMap.get(entity)) : Optional.empty();
    }
    
    /**
     * Gets whether the given {@link Entity} is glowing the given {@link Player}.
     *
     * @param player - The player to check.
     * @param entity - The entity to check.
     * @return {@code true} if the given entity is glowing for the given player; {@code false} otherwise.
     */
    static boolean isGlowing(@NotNull Player player, @NotNull Entity entity) {
        return GlowingHandler.handler.get(player).map(glowing -> glowing.entityMap.containsKey(entity)).orElse(false);
    }
    
    private static void setGlowing0(@NotNull Player player, @NotNull Entity entity, @Nullable PacketTeamColor color, @Nullable Integer duration) {
        if (Objects.equals(player, entity)) {
            throw new IllegalArgumentException("Cannot set glowing for self!");
        }
        
        final GlowingImpl glowing = GlowingHandler.handler.getOrCompute(player, GlowingImpl::new);
        final GlowingInstance instance = glowing.entityMap.computeIfAbsent(
                entity,
                _entity -> new GlowingInstance(
                        player,
                        _entity,
                        color != null ? color : DEFAULT_COLOR,
                        duration != null ? duration : INFINITE_DURATION
                )
        );
        
        // Modify the instance
        if (color != null) {
            instance.setColor(color);
        }
        
        if (duration != null) {
            instance.setDuration(duration);
        }
    }
    
}
