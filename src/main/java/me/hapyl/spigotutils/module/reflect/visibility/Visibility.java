package me.hapyl.spigotutils.module.reflect.visibility;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.reflect.Reflect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Visibility {

    private static final Map<Integer, Visibility> VISIBILITY_MAP = Maps.newConcurrentMap();

    private final Entity entity;
    private final Set<UUID> visibleTo;

    private Visibility(Entity entity) {
        this.entity = entity;
        this.visibleTo = Sets.newHashSet();
    }

    public Entity getEntity() {
        return entity;
    }

    public void hide() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!canSee(player)) {
                Reflect.hideEntity(entity, player);
            }
        }
    }

    public void show() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Reflect.showEntity(entity, player);
        }
    }

    public boolean canSee(Player player) {
        return visibleTo.contains(player.getUniqueId());
    }

    public void setCanSee(Player player, boolean flag) {
        final UUID uuid = player.getUniqueId();
        if (flag) {
            visibleTo.add(uuid);
        }
        else {
            visibleTo.remove(uuid);
        }
    }

    public static Visibility of(Entity entity, Player... players) {
        if (players == null || players.length == 0) {
            return of(entity);
        }

        final Visibility visibility = new Visibility(entity);
        for (Player player : players) {
            visibility.visibleTo.add(player.getUniqueId());
        }

        VISIBILITY_MAP.put(entity.getEntityId(), visibility);

        visibility.hide();
        return visibility;
    }

    @Nullable
    public static Visibility of(int entityId) {
        return VISIBILITY_MAP.get(entityId);
    }

    public static Visibility of(Entity entity) {
        return VISIBILITY_MAP.computeIfAbsent(entity.getEntityId(), v -> new Visibility(entity));
    }

}
