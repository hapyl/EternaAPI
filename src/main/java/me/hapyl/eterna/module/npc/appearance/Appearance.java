package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.npc.NpcPose;
import me.hapyl.eterna.module.reflect.EntityDataType;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
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
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.UUID;

/**
 * Represents a base appearance class.
 * <p>Appearance is the way {@link Npc} looks, be it {@link AppearanceMannequin} or {@link AppearanceSheep}.</p>
 */
public abstract class Appearance {
    
    // -[ A Rule of Thumb ]-
    // If something has to do with entity data and packets, delegate
    // the implementation to Appearance rather than to Npc class!
    
    private static final EntityDataAccessor<Integer> ACCESSOR_SHAKING = EntityDataType.INT.createAccessor(7);
    private static final EntityDataAccessor<Pose> ACCESSOR_POSE = EntityDataType.ENTITY_POSE.createAccessor(6);
    
    protected final Npc npc;
    protected final Entity handle;
    
    private NpcPose pose;
    
    /**
     * Creates a new {@link Appearance} for the given {@link Npc} with the given {@link Entity} handle.
     *
     * @param npc    - The npc this appearance belongs to.
     * @param handle - The entity handle.
     */
    public Appearance(@Nonnull Npc npc, @Nonnull Entity handle) {
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
     * Gets the {@code Y} offset of when the {@link Npc} is sitting.
     *
     * @return the {@code Y} offset of when the Npc is sitting.
     */
    public double chairYOffset() {
        return 0;
    }
    
    /**
     * Gets the entity handle.
     *
     * @return the entity handle.
     */
    @Nonnull
    public Entity getHandle() {
        return this.handle;
    }
    
    /**
     * Gets the entity {@link UUID}.
     *
     * @return the entity {@link UUID}.
     */
    @Nonnull
    public UUID getUuid() {
        return handle.getUUID();
    }
    
    /**
     * Sets whether this {@link Npc} should be shaking.
     *
     * @param shaking - Whether the {@link Npc} should be shaking.
     */
    public void setShaking(boolean shaking) {
        this.handle.getEntityData().set(ACCESSOR_SHAKING, shaking ? Integer.MAX_VALUE : -100);
        this.updateEntityData();
    }
    
    /**
     * Gets the current {@link NpcPose}.
     *
     * @return the current {@link NpcPose}.
     */
    @Nonnull
    public NpcPose getPose() {
        return this.pose;
    }
    
    /**
     * Sets the {@link NpcPose}.
     *
     * @param pose - The new pose to set.
     * @return {@code ture} whether the pose was set, {@code false} otherwise.
     */
    public boolean setPose(@Nonnull NpcPose pose) {
        if (this.pose == pose) {
            return false;
        }
        
        this.pose = pose;
        
        final SynchedEntityData entityData = getEntityData();
        
        // Unfuckup hitbox by updating the pose to something with a standing hitbox first
        if (pose == NpcPose.STANDING) {
            entityData.set(ACCESSOR_POSE, NpcPose.fuckassFakeStandingPoseBecauseIFuckingHateEverythingThatExistsEspeciallyMojang());
            this.updateEntityData();
        }
        
        entityData.set(ACCESSOR_POSE, pose.toNms());
        this.updateEntityData();
        
        return true;
    }
    
    /**
     * Gets the scoreboard entity name, which is always {@link UUID#toString()}.
     *
     * @return the scoreboard entity name.
     */
    @Nonnull
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
    public void show(@Nonnull Player player, @Nonnull Location location) {
        Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(handle, location));
        Reflect.sendPacket(player, PacketFactory.makePacketSetEntityData(handle));
    }
    
    /**
     * Hides this appearance from the given {@link Player}.
     *
     * @param player - The player for whom to hide the appearance.
     */
    @OverridingMethodsMustInvokeSuper
    public void hide(@Nonnull Player player) {
        Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(handle));
    }
    
    /**
     * Sets the {@link Location} of this appearance.
     * <p>The location will be synced for each player who can see the {@link Npc}</p>
     *
     * @param location - The location to set.
     */
    public void setLocation(@Nonnull Location location) {
        this.handle.absSnapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.updateLocation();
    }
    
    /**
     * Updates the location of this appearance for each player who can see the {@link Npc}.
     */
    public void updateLocation() {
        final ClientboundTeleportEntityPacket packet = PacketFactory.makePacketTeleportEntity(handle);
        
        this.npc.showingTo().forEach(player -> Reflect.sendPacket(player, packet));
    }
    
    /**
     * Updates the entity data of this appearance for each player who can see the {@link Npc}.
     */
    public void updateEntityData() {
        final ClientboundSetEntityDataPacket packet = PacketFactory.makePacketSetEntityData(handle);
        
        this.npc.showingTo().forEach(player -> Reflect.sendPacket(player, packet));
    }
    
    /**
     * Gets the entity data.
     *
     * @return the entity data.
     */
    @Nonnull
    public SynchedEntityData getEntityData() {
        return this.handle.getEntityData();
    }
    
    /**
     * Sets the entity data value for this appearance.
     * <p>You must manually {@link #updateEntityData()} to see the changes, see <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata">minecraft.wiki</a>
     * for help with entity data.</p>
     *
     * @param type  - The target entity data type.
     * @param id    - The target entity data id.
     * @param value - The new entity data value.
     * @param <T>   - The target entity data type.
     */
    public <T> void setEntityDataValue(@Nonnull EntityDataType<T> type, int id, @Nonnull T value) {
        getEntityData().set(type.createAccessor(id), value);
    }
    
    /**
     * Gets the entity data value for this appearance.
     * <p>See <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata">minecraft.wiki</a> for help with entity data.</p>
     *
     * @param type - The target entity data type.
     * @param id   - The target entity data id.
     * @param <T>  - The target entity data type.
     * @return the entity data value.
     */
    public <T> T getEntityDataValue(@Nonnull EntityDataType<T> type, int id) {
        return getEntityData().get(type.createAccessor(id));
    }
    
    /**
     * A dummy world helper method, which is required for packet entities.
     *
     * @return a dummy world.
     */
    @Nonnull
    protected static Level dummyWorld() {
        class Holder {
            static final Level level = Reflect.getHandle(Bukkit.getWorlds().getFirst());
        }
        
        return Holder.level;
    }
    
    
}
