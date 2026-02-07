package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Ticking;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

@ApiStatus.Internal
public class GlowingImpl implements Ticking {
    
    protected final Player player;
    protected final Map<Entity, GlowingInstance> entityMap;
    
    GlowingImpl(@NotNull Player player) {
        this.player = player;
        this.entityMap = Maps.newConcurrentMap(); // We're dealing with packets so concurrent it is
    }
    
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public void tick() {
        final Collection<GlowingInstance> values = entityMap.values();
        
        values.removeIf(GlowingInstance::removeIfShould);
        values.forEach(GlowingInstance::tick);
    }
    
    @ApiStatus.Internal
    @NotNull
    Stream<GlowingInstance> byEntityId(int entityId) {
        return entityMap.values().stream().filter(glowingInstance -> glowingInstance.entity().getEntityId() == entityId);
    }
    
}
