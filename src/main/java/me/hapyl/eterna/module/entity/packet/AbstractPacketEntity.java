package me.hapyl.eterna.module.entity.packet;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.team.PacketTeam;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Represents an abstract {@link PacketEntity} implementation.
 *
 * @param <T> - The entity type.
 */
@ApiStatus.Internal
public abstract class AbstractPacketEntity<T extends Entity> implements PacketEntity {
    
    protected final T entity;
    protected final PacketTeam packetTeam;
    
    private final Set<Player> showingTo;
    
    @NotNull private Team.OptionStatus collision;
    
    AbstractPacketEntity(@NotNull T entity, @NotNull Location location) {
        this.entity = entity;
        this.packetTeam = new PacketTeam("packet_entity_" + entity.getUUID());
        
        this.showingTo = Sets.newHashSet();
        this.collision = Team.OptionStatus.ALWAYS;
        
        Reflect.setEntityLocation(entity, location);
    }
    
    @Override
    @NotNull
    public T getEntity() {
        return entity;
    }
    
    @NotNull
    @Override
    public org.bukkit.entity.Entity bukkit() {
        return entity.getBukkitEntity();
    }
    
    @Override
    public void show(@NotNull Player player) {
        Reflect.createEntity(entity, player);
        Reflect.updateEntityData(player, entity);
        
        // Handle packet team
        this.packetTeam.create(player);
        this.packetTeam.entry(player, entity.getScoreboardName());
        
        this.updateEntityData(player);
        
        this.showingTo.add(player);
    }
    
    @Override
    public void hide(@NotNull Player player) {
        this.hide0(player);
        this.showingTo.remove(player);
    }
    
    @Override
    public boolean isShowingTo(@NotNull Player player) {
        return this.showingTo.contains(player);
    }
    
    @Override
    public void dispose() {
        this.showingTo.forEach(this::hide0);
        this.showingTo.clear();
    }
    
    @Override
    public void setVisible(boolean visibility) {
        this.entity.setInvisible(!visibility);
        this.updateEntityDataForAll();
    }
    
    @Override
    public void setCollision(boolean collision) {
        this.collision = collision ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER;
        this.updateEntityDataForAll();
    }
    
    @Override
    public void setSilent(boolean silent) {
        this.entity.setSilent(silent);
        this.updateEntityDataForAll();
    }
    
    @Override
    public void setGravity(boolean gravity) {
        this.entity.setNoGravity(!gravity);
        this.updateEntityDataForAll();
    }
    
    @Override
    public void teleport(@NotNull Location location) {
        Reflect.setEntityLocation(entity, location);
        showingTo.forEach(player -> Reflect.updateEntityLocation(entity, player));
    }
    
    @NotNull
    public Location getLocation() {
        return Reflect.getEntityLocation(entity);
    }
    
    @NotNull
    @Override
    public Collection<Player> showingTo() {
        return List.copyOf(showingTo);
    }
    
    @NotNull
    protected <D> Optional<D> getEntityDataValue(@NotNull EntityDataSerializer<D> type, int id) {
        return Reflect.getEntityDataValue(entity, type, id);
    }
    
    protected <D> void setEntityDataValue(@NotNull EntityDataSerializer<D> type, int id, @NotNull D value) {
        entity.getEntityData().set(type.createAccessor(id), value);
    }
    
    protected void updateEntityData(@NotNull Player player) {
        this.packetTeam.option(player, Team.Option.COLLISION_RULE, collision);
        
        // Other flags are set on the entity object directly into entity data, so just send the packet
        Reflect.updateEntityData(player, entity);
    }
    
    protected void updateEntityDataForAll() {
        this.showingTo.forEach(this::updateEntityData);
    }
    
    @ApiStatus.Internal
    private void hide0(@NotNull Player player) {
        Reflect.destroyEntity(entity, player);
        
        this.packetTeam.destroy(player);
    }
    
    @NotNull
    protected static ServerLevel getWorld(@NotNull Location location) {
        return Reflect.getHandle(location.getWorld());
    }
    
}
