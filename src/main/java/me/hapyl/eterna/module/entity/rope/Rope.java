package me.hapyl.eterna.module.entity.rope;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.team.PacketTeam;
import me.hapyl.eterna.module.util.Destroyable;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Represents a rope made of leashed entities.
 */
public class Rope implements Showable, Destroyable {
    
    private static final int minSizeToCreate = 2;
    
    private final LinkedList<RopePoint> points;
    private final Map<Player, RopeData> showingTo;
    
    /**
     * Creates a new rope.
     */
    public Rope() {
        this.points = Lists.newLinkedList();
        this.showingTo = Maps.newHashMap();
    }
    
    /**
     * Adds a rope point.
     *
     * @param location - A new point location.
     */
    @SelfReturn
    public Rope addPoint(@Nonnull Location location) {
        this.points.add(new RopePoint(location.getX(), location.getY(), location.getZ()));
        return this;
    }
    
    /**
     * Shows this {@link Rope} to the give  player.
     *
     * @param player - The player for whom this entity should be shown.
     * @throws IllegalStateException if there are less than {@link #minSizeToCreate} points.
     */
    @Override
    public void show(@Nonnull Player player) {
        if (this.points.size() < minSizeToCreate) {
            throw new IllegalStateException("There must be at least %s points to create a rope!".formatted(minSizeToCreate));
        }
        
        final RopeData previousData = this.showingTo.remove(player);
        
        if (previousData != null) {
            previousData.dispose();
        }
        
        final LinkedList<Entity> linkedEntities = Lists.newLinkedList();
        final ServerLevel level = Reflect.getHandle(player.getLocation().getWorld());
        
        // Create packetTeam for collisions
        final PacketTeam packetTeam = new PacketTeam("rope_collisions");
        
        packetTeam.create(player);
        packetTeam.option(player, Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        
        // Iterate over each point and create an entity and if leash them
        Entity lastChicken = null;
        
        for (RopePoint point : this.points) {
            final Entity thisChicken = makeRopeEntity(level, point);
            linkedEntities.add(thisChicken);
            
            // Show the entity first
            Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(thisChicken));
            Reflect.sendPacket(player, PacketFactory.makePacketSetEntityData(thisChicken));
            
            // Remove collision
            packetTeam.entry(player, thisChicken.getScoreboardName());
            
            // If there is a `lastChicken`, least to self
            if (lastChicken != null) {
                Reflect.sendPacket(player, PacketFactory.makePacketSetEntityLink(lastChicken, thisChicken));
            }
            
            // Assign the lastChicken
            lastChicken = thisChicken;
        }
        
        // Set rope data
        showingTo.put(player, new RopeData(player, packetTeam, linkedEntities));
    }
    
    /**
     * Hides this {@link Rope} for the given player.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    @Override
    public void hide(@Nonnull Player player) {
        final RopeData ropeData = showingTo.remove(player);
        
        if (ropeData != null) {
            ropeData.dispose();
        }
    }
    
    /**
     * Gets a copy of players for whom this {@link Rope} is visible.
     *
     * @return a copy of players for whom this {@link Rope} is visible.
     */
    @Nonnull
    @Override
    public Collection<Player> showingTo() {
        return Set.copyOf(showingTo.keySet());
    }
    
    /**
     * Destroys this {@link Rope}.
     */
    @Override
    public void destroy() {
        this.showingTo.values().forEach(RopeData::dispose);
        this.showingTo.clear();
    }
    
    private static Entity makeRopeEntity(Level level, RopePoint point) {
        // We use CODs since in my testings, they have the smallest Y bounding box, making
        // the connections cleaner. I can't use scale attribute by the way, because it just
        // makes entity disappear for some reason, and I have -1 idea why nor any motivation
        // to find the issue, which is probably isn't mine...
        final Entity chicken = new Cod(EntityType.COD, level);
        
        chicken.absSnapTo(point.x(), point.y(), point.z());
        
        final SynchedEntityData entityData = chicken.getEntityData();
        
        // Make the entity invisible
        entityData.set(EntityDataSerializers.BYTE.createAccessor(0), (byte) 0x20);
        
        return chicken;
    }
    
    
}
