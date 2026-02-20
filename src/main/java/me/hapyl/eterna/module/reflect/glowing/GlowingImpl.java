package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Ticking;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

@ApiStatus.Internal
public class GlowingImpl implements Ticking {
    
    protected final Player player;
    protected final Map<Entity, GlowingInstance> entityMap;
    
    GlowingImpl(@NotNull Player player) {
        this.player = player;
        this.entityMap = Maps.newHashMap();
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
    @NotNull
    Stream<GlowingInstance> byEntityId(int entityId) {
        return entityMap.values().stream().filter(glowingInstance -> glowingInstance.entity().getEntityId() == entityId);
    }
    
}
