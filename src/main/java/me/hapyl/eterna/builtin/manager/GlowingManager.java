package me.hapyl.eterna.builtin.manager;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.reflect.glowing.Glowing;
import me.hapyl.eterna.module.reflect.glowing.GlowingInstance;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class GlowingManager extends EternaManager<Player, Glowing> {
    GlowingManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
    }
    
    @Nullable
    @Override
    public Glowing get(@Nonnull Player player) {
        return Objects.requireNonNull(super.get(player), "Illegal get() call, use get(Player, Function)!");
    }
    
    @Nonnull
    public Glowing get(@Nonnull Player player, @Nonnull Function<Player, Glowing> compute) {
        return managing.computeIfAbsent(player, compute);
    }
    
    @Nullable
    public GlowingInstance getGlowing(int entityId) {
        for (Glowing glowing : managing.values()) {
            final GlowingInstance instance = glowing.byId(entityId);
            
            if (instance != null) {
                return instance;
            }
        }
        
        return null;
    }
    
    public void emptyOrphans() {
        managing.keySet().removeIf(player -> !player.isOnline());
    }
    
    @Nonnull
    @Override
    protected Map<Player, Glowing> makeNewMap() {
        return Maps.newConcurrentMap(); // Dealing with packets, better to use concurrent
    }
}
