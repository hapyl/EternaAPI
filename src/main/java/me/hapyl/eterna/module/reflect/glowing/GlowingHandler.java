package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@ApiStatus.Internal
public final class GlowingHandler extends EternaHandler<Player, GlowingImpl> {
    
    static GlowingHandler handler;
    
    public GlowingHandler(@NotNull EternaKey key, @NotNull EternaPlugin eterna) {
        super(key, eterna);
        
        handler = this;
    }
    
    @Override
    @NotNull
    public Optional<GlowingImpl> get(@NotNull Player player) {
        return Objects.requireNonNull(super.get(player), "Illegal get() call, use get(Player, Function)!");
    }
    
    @NotNull
    public List<GlowingInstance> getByEntityId(int entityId) {
        return registry.values().stream()
                       .flatMap(glowing -> glowing.byEntityId(entityId))
                       .toList();
    }
    
    public void emptyOrphans() {
        registry.keySet().removeIf(player -> !player.isOnline());
    }
    
    @NotNull
    @Override
    protected Map<Player, GlowingImpl> makeNewMap() {
        return Maps.newConcurrentMap(); // Dealing with packets, better to use concurrent
    }
}
