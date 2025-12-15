package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.npc.NpcPose;
import me.hapyl.eterna.module.reflect.DataWatcherType;
import me.hapyl.eterna.module.reflect.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Represents a base appearance class.
 */
public abstract class Appearance {
    
    private static final EntityDataAccessor<Integer> ACCESSOR_SHAKING = DataWatcherType.INT.createAccessor(7);
    private static final EntityDataAccessor<Pose> ACCESSOR_POSE = DataWatcherType.ENTITY_POSE.createAccessor(6);
    
    protected final Npc npc;
    protected final Entity handle;
    
    public Appearance(@Nonnull Npc npc, @Nonnull Entity handle) {
        this.handle = handle;
        this.npc = npc;
    }
    
    /**
     * Gets the height of the {@link Appearance}.
     * <p>Used to offset the hologram above the head.</p>
     *
     * @return the height of the appearance.
     */
    public double getHeight() {
        return handle.getEyeHeight() + 0.1;
    }
    
    /**
     * Gets the {@code Y} offset of when the {@link Npc} is sitting.
     *
     * @return the {@code Y} offset of when the Npc is sitting.
     */
    public double chairYOffset() {
        return 0;
    }
    
    @Nonnull
    public Entity getHandle() {
        return this.handle;
    }
    
    @Nonnull
    public UUID getUuid() {
        return handle.getUUID();
    }
    
    public void setShaking(boolean shaking) {
        this.handle.getEntityData().set(ACCESSOR_SHAKING, shaking ? Integer.MAX_VALUE : -100);
        this.updateMetadata();
    }
    
    @Nonnull
    public NpcPose getPose() {
        return NpcPose.fromNms(this.handle.getPose());
    }
    
    public void setPose(@Nonnull NpcPose pose) {
        final SynchedEntityData entityData = this.handle.getEntityData();
        
        // Yes, STANDING is STILL fucked up, so have to use this fuckass workaround
        // to fix the fuckass fucking hitbox...
        if (pose == NpcPose.STANDING) {
            entityData.set(ACCESSOR_POSE, NpcPose.fuckassFakeStandingPoseBecauseIFuckingHateEverythingThatExistsEspeciallyMojang());
            updateMetadata();
        }
        
        entityData.set(ACCESSOR_POSE, pose.toNms());
        this.updateMetadata();
    }
    
    @Nonnull
    public String getScoreboardEntry() {
        return handle.getScoreboardName();
    }
    
    public void show(@Nonnull Player player, @Nonnull Location location) {
        Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(handle, location));
        Reflect.sendPacket(player, PacketFactory.makePacketSetEntityData(handle));
    }
    
    public void hide(@Nonnull Player player) {
        Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(handle));
    }
    
    public void setLocation(@Nonnull Location location) {
        this.handle.absSnapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.updateLocation();
    }
    
    public void updateLocation() {
        final ClientboundTeleportEntityPacket packet = PacketFactory.makePacketTeleportEntity(handle);
        
        this.npc.showingTo().forEach(player -> Reflect.sendPacket(player, packet));
    }
    
    public void updateMetadata() {
        final ClientboundSetEntityDataPacket packet = PacketFactory.makePacketSetEntityData(handle);
        
        this.npc.showingTo().forEach(player -> Reflect.sendPacket(player, packet));
    }
    
    public <T> void setMetadataValue(@Nonnull DataWatcherType<T> type, int id, @Nonnull T value) {
        getMetadata().set(type.createAccessor(id), value);
    }
    
    public <T> T getMetadataValue(@Nonnull DataWatcherType<T> type, int id) {
        return getMetadata().get(type.createAccessor(id));
    }
    
    @Nonnull
    public SynchedEntityData getMetadata() {
        return this.handle.getEntityData();
    }
    
    @Nonnull
    protected static Level dummyWorld() {
        class Holder {
            static final Level level = Reflect.getMinecraftWorld(Bukkit.getWorlds().getFirst());
        }
        
        return Holder.level;
    }
    
    
}
