package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Ticking;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

@ApiStatus.Internal
public class GlowingImpl implements Ticking {
    
    protected final Player player;
    protected final Map<Entity, GlowingInstance> entityMap;
    
    GlowingImpl(@NotNull Player player) {
        this.player = player;
        // This can, is VERY VERY VERY rare cases throw a concurrent exception, probably because some race
        // condition with packets, hence it's concurrent map
        this.entityMap = Maps.newConcurrentMap();
    }
    
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public void tick() {
        final Iterator<GlowingInstance> iterator = entityMap.values().iterator();
        
        while (iterator.hasNext()) {
            final GlowingInstance glowingInstance = iterator.next();
            
            if (glowingInstance.shouldRemove()) {
                // Make sure to remove from the hash map before calling the remove() method on the instance, since
                // that sends a packet offload that we would otherwise intercept and change it to a glowing packet
                iterator.remove();
                glowingInstance.remove();
            }
            else {
                glowingInstance.tick();
            }
        }
    }
    
    @ApiStatus.Internal
    @Nullable
    GlowingInstance byEntityId(int entityId) {
        for (Map.Entry<Entity, GlowingInstance> entry : entityMap.entrySet()) {
            if (entry.getKey().getEntityId() == entityId) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    
}
