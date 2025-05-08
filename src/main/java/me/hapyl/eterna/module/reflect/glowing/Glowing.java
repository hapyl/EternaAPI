package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Maps;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.util.Ticking;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides per-player entity glowing with color changing support.
 * <p>
 * <h2>Warning!</h2>
 * Because how stupid Minecraft is, the glowing color is based on entity's team and there is no way to override that.
 * Meaning changing entity's team or player's scoreboard <b><u>will</u></b> break glowing.
 * </p>
 *
 * @see #setGlowing(Player, Entity, GlowingColor, int)
 * @see #stopGlowing(Player, Entity)
 */
public class Glowing implements Ticking {
    
    protected static final byte BITMASK = 0x40;
    protected static final int INFINITE_DURATION = -1;
    protected static final GlowingColor DEFAULT_COLOR = GlowingColor.WHITE;
    
    protected final Scoreboard scoreboard;
    
    private final Player player;
    private final Map<Entity, GlowingInstance> entityMap;
    
    private Glowing(@Nonnull Player player) {
        this.player = player;
        this.scoreboard = new net.minecraft.world.scores.Scoreboard();
        this.entityMap = Maps.newConcurrentMap(); // We're dealing with packets so concurrent it is
    }
    
    @Nonnull
    public Player player() {
        return player;
    }
    
    @Override
    public void tick() {
        final Collection<GlowingInstance> values = entityMap.values();
        
        for (final Iterator<GlowingInstance> iterator = values.iterator(); iterator.hasNext(); ) {
            final GlowingInstance instance = iterator.next();
            
            if (instance.shouldRemove()) {
                iterator.remove();
                instance.remove();
            }
        }
        
        values.forEach(GlowingInstance::tick);
    }
    
    /**
     * Gets the {@link GlowingInstance} for the given entity by that entity's id.
     *
     * @param entityId - The entity id.
     * @return the instance for the entity with the given id or {@code null} if not glowing.
     */
    @Nullable
    public GlowingInstance byId(int entityId) {
        for (Map.Entry<Entity, GlowingInstance> entry : entityMap.entrySet()) {
            if (entry.getKey().getEntityId() == entityId) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * Sets the glowing for the given {@link Entity} for the given {@link Player}.
     * <p>If the entity is already glowing for the player, the {@link GlowingColor} and {@code duration} will replace the previous color and duration.</p>
     *
     * @param player   - The player to glow for.
     * @param entity   - The entity to glow.
     * @param color    - The glowing color.
     * @param duration - The duration of glowing.
     */
    public static void setGlowing(@Nonnull Player player, @Nonnull Entity entity, @Nonnull GlowingColor color, int duration) {
        setGlowing0(player, entity, color, duration);
    }
    
    /**
     * Sets the glowing for the given {@link Entity} for the given {@link Player} indefinitely.
     * <p>If the entity is already glowing for the player, the {@link GlowingColor} will replace the previous color and duration.</p>
     *
     * @param player - The player to glow for.
     * @param entity - The entity to glow.
     * @param color  - The glowing color.
     */
    public static void setGlowing(@Nonnull Player player, @Nonnull Entity entity, @Nonnull GlowingColor color) {
        setGlowing0(player, entity, color, null);
    }
    
    /**
     * Sets the glowing for the given {@link Entity} for the given {@link Player}.
     * <p>If the entity is already glowing for the player, the duration will replace the previous color and duration.</p>
     *
     * @param player   - The player to glow for.
     * @param entity   - The entity to glow.
     * @param duration - The duration of glowing.
     */
    public static void setGlowing(@Nonnull Player player, @Nonnull Entity entity, int duration) {
        setGlowing0(player, entity, null, duration);
    }
    
    /**
     * Stops the glowing for the given {@link Entity} for the given {@link Player}.
     *
     * @param player - The player to stop glowing for.
     * @param entity - The entity to stop glowing.
     */
    public static void stopGlowing(@Nonnull Player player, @Nonnull Entity entity) {
        final GlowingInstance instance = playerGlowing(player).entityMap.remove(entity);
        
        if (instance != null) {
            instance.remove();
        }
    }
    
    /**
     * Stops any existing glowing instances for the given {@link Player}.
     *
     * @param player - The player to stop instances for.
     */
    public static void stopGlowing(@Nonnull Player player) {
        for (final Iterator<GlowingInstance> iterator = playerGlowing(player).entityMap.values().iterator(); iterator.hasNext(); ) {
            final GlowingInstance instance = iterator.next();
            
            iterator.remove();
            instance.remove();
        }
    }
    
    /**
     * Stops any existing glowing instance of the given {@link Entity} for any player it's glowing for.
     *
     * @param entity - The entity to stop instance of.
     */
    public static void stopGlowing(@Nonnull Entity entity) {
        Eterna.getManagers().glowing.forEach(glowing -> {
            final GlowingInstance instance = glowing.entityMap.remove(entity);
            
            if (instance != null) {
                instance.remove();
            }
        });
    }
    
    /**
     * Gets the {@link GlowingInstance} for the given {@link Player} and {@link Entity}.
     *
     * @param player - The player.
     * @param entity - The entity.
     * @return an existing instance or {@code null} if none.
     */
    @Nullable
    public static GlowingInstance getGlowing(@Nonnull Player player, @Nonnull Entity entity) {
        return playerGlowing(player).entityMap.get(entity);
    }
    
    /**
     * Gets whether the given {@link Entity} is currently glowing for the given {@link Player}.
     *
     * @param player - The player.
     * @param entity - The entity.
     * @return {@code true} if the entity is glowing, {@code false} otherwise.
     */
    public static boolean isGlowing(@Nonnull Player player, @Nonnull Entity entity) {
        return playerGlowing(player).entityMap.containsKey(entity);
    }
    
    private static void setGlowing0(Player player, Entity entity, @Nullable GlowingColor color, @Nullable Integer duration) {
        final Glowing glowing = playerGlowing(player);
        final GlowingInstance instance = glowing.entityMap.computeIfAbsent(
                entity,
                e -> new GlowingInstance(
                        player,
                        entity,
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
    
    private static Glowing playerGlowing(Player player) {
        return Eterna.getManagers().glowing.get(player, Glowing::new);
    }
    
}
