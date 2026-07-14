package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.npc.NpcBase;
import me.hapyl.eterna.module.npc.NpcPose;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Represents a base appearance class.
 *
 * <p>
 * Appearance is the way {@link Npc} looks, be it {@link AppearanceMannequin} or {@link AppearanceSheep}.
 * </p>
 */
public abstract class Appearance implements NpcBase {
    
    // -[ A Rule of Thumb ]-
    // If something has to do with entity data and packets, delegate
    // the implementation to Appearance rather than to Npc class!
    
    private static final EntityDataAccessor<Integer> ACCESSOR_SHAKING = EntityDataSerializers.INT.createAccessor(7);
    private static final EntityDataAccessor<Pose> ACCESSOR_POSE = EntityDataSerializers.POSE.createAccessor(6);
    private static final EntityDataAccessor<Byte> ACCESSOR_PROPERTIES = EntityDataSerializers.BYTE.createAccessor(0);
    
    protected final Npc npc;
    protected final LivingEntity handle;
    
    private NpcPose pose;
    
    /**
     * Creates a new {@link Appearance} for the given {@link Npc} with the given {@link Entity} handle.
     *
     * @param npc    - The npc this appearance belongs to.
     * @param handle - The entity handle.
     */
    public Appearance(@NotNull Npc npc, @NotNull LivingEntity handle) {
        this.handle = handle;
        this.npc = npc;
        this.pose = NpcPose.STANDING;
    }
    
    /**
     * Gets the height of the {@link Appearance}, which is used to offset holograms.
     *
     * @return the height of the appearance.
     */
    public double getHeight() {
        return handle.getEyeHeight() + 0.1;
    }
    
    /**
     * Gets the {@code y} offset of when the {@link Npc} is sitting.
     *
     * @return the {@code y} offset of when the Npc is sitting.
     */
    public double chairYOffset() {
        return 0;
    }
    
    /**
     * Gets the entity handle.
     *
     * @return the entity handle.
     */
    @NotNull
    public LivingEntity getHandle() {
        return handle;
    }
    
    /**
     * Gets the entity {@link UUID}.
     *
     * @return the entity {@link UUID}.
     */
    @NotNull
    public UUID getUuid() {
        return handle.getUUID();
    }
    
    /**
     * Sets whether this {@link Npc} should be shaking.
     *
     * @param shaking - Whether the {@link Npc} should be shaking.
     */
    @Override
    public void setShaking(boolean shaking) {
        this.editEntityData(data -> data.set(ACCESSOR_SHAKING, shaking ? Integer.MAX_VALUE : -100));
    }
    
    /**
     * Sets whether this {@link Npc} is invisible.
     *
     * @param invisible - Whether the npc should be invisible.
     */
    @Override
    public void setInvisible(boolean invisible) {
        this.editEntityData(data -> {
            final byte value = data.get(ACCESSOR_PROPERTIES);
            
            data.set(ACCESSOR_PROPERTIES, (byte) (invisible ? (value | 0x20) : (value & ~0x20)));
        });
    }
    
    /**
     * Edits the {@link SynchedEntityData} of this {@link Appearance} and updates the entity data.
     *
     * @param editor - The edit to perform.
     */
    @Override
    public void editEntityData(@NotNull Consumer<? super SynchedEntityData> editor) {
        final SynchedEntityData entityData = this.handle.getEntityData();
        
        editor.accept(entityData);
        this.updateEntityData0(entityData);
    }
    
    /**
     * Gets the current {@link NpcPose}.
     *
     * @return the current {@link NpcPose}.
     */
    @Override
    public @NotNull NpcPose getPose() {
        return this.pose;
    }
    
    /**
     * Sets the {@link NpcPose}.
     *
     * @param pose - The new pose to set.
     * @return {@code ture} whether the pose was set, {@code false} otherwise.
     */
    @Override
    public boolean setPose(@NotNull NpcPose pose) {
        if (this.pose == pose) {
            return false;
        }
        
        this.pose = pose;
        
        final SynchedEntityData entityData = handle.getEntityData();
        
        // Unfuckup hitbox by updating the pose to something with a standing hitbox first
        if (pose == NpcPose.STANDING) {
            entityData.set(ACCESSOR_POSE, NpcPose.fuckassFakeStandingPoseBecauseIFuckingHateEverythingThatExistsEspeciallyMojang());
            this.updateEntityData0(entityData);
        }
        
        entityData.set(ACCESSOR_POSE, pose.toNms());
        this.updateEntityData0(entityData);
        
        return true;
    }
    
    /**
     * Gets the scoreboard entity name, which is always {@link UUID#toString()}.
     *
     * @return the scoreboard entity name.
     */
    @NotNull
    public String getScoreboardEntry() {
        return handle.getScoreboardName();
    }
    
    /**
     * Shows this appearance to the given {@link Player} at the given {@link Location}.
     *
     * @param player   - The player for whom to show the appearance.
     * @param location - The location to show at.
     */
    @OverridingMethodsMustInvokeSuper
    public void show(@NotNull Player player, @NotNull Location location) {
        // Update entity metadata
        final SynchedEntityData entityData = handle.getEntityData();
        
        this.onDataUpdated(entityData);
        
        // Send packets
        Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(handle, location));
        Reflect.sendPacket(player, PacketFactory.makePacketSetEntityData(handle, entityData, false));
    }
    
    /**
     * Hides this appearance from the given {@link Player}.
     *
     * @param player - The player for whom to hide the appearance.
     */
    @OverridingMethodsMustInvokeSuper
    public void hide(@NotNull Player player) {
        Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(handle));
    }
    
    /**
     * Sets the {@link Location} of this appearance.
     *
     * <p>The location will be synced for each player who can see the {@link Npc}</p>
     *
     * @param location - The location to set.
     */
    public void setLocation(@NotNull Location location) {
        this.handle.absSnapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        
        // Send teleport packet
        this.sendPacket(PacketFactory.makePacketTeleportEntity(handle));
    }
    
    /**
     * Updates the entity data of this {@link Appearance} for each player who can see the {@link Npc}.
     *
     * <p>
     * This method should be used to manually update the entity data after an appearance change while the {@link Npc} is spawned.
     * </p>
     */
    @OverridingMethodsMustInvokeSuper
    public final void updateEntityData() {
        this.updateEntityData0(getHandle().getEntityData());
    }
    
    /**
     * Update the given {@link SynchedEntityData} with the local data of this {@link Appearance}.
     *
     * <p>
     * This method is called each time before sending a {@link ClientboundSetEntityDataPacket}, therefore any values
     * from this {@link Appearance} must be written in this method.
     * </p>
     *
     * @param entityData - The entity data to update.
     */
    @EventLike
    public abstract void onDataUpdated(@NotNull SynchedEntityData entityData);
    
    /**
     * Sends the given {@link Packet} to all players who can see this {@link Npc}.
     *
     * @param packet - The packet to send.
     */
    protected void sendPacket(@NotNull Packet<?> packet) {
        this.npc.showingTo().forEach(player -> Reflect.sendPacket(player, packet));
    }
    
    private void updateEntityData0(@NotNull SynchedEntityData entityData) {
        // Call update on protected method
        this.onDataUpdated(entityData);
        
        // Send packet to all players who can see the npc
        sendPacket(PacketFactory.makePacketSetEntityData(handle, true));
    }
    
    /**
     * A helper method for retrieving dummy world, which is required for packet entities.
     *
     * @return a dummy world.
     */
    @NotNull
    protected static Level dummyWorld() {
        class Holder {
            static final Level LEVEL = Reflect.getHandle(Bukkit.getWorlds().getFirst());
        }
        
        return Holder.LEVEL;
    }
    
    
}
