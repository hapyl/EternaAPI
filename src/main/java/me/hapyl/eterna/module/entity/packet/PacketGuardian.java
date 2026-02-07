package me.hapyl.eterna.module.entity.packet;

import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Guardian} packet entity.
 */
public class PacketGuardian extends AbstractPacketEntity<Guardian> {
    
    /**
     * Creates a new {@link PacketGuardian}.
     *
     * @param location - The initial location.
     */
    public PacketGuardian(@NotNull Location location) {
        super(new Guardian(EntityType.GUARDIAN, getWorld(location)), location);
    }
    
    /**
     * Sets the guardian beam target.
     *
     * @param entity - The beam target, or {@code null} to unset the beam.
     */
    public void setBeamTarget(@Nullable PacketEntity entity) {
        setBeamTarget0(entity != null ? entity.getId() : 0);
    }
    
    /**
     * Sets the guardian beam target.
     *
     * @param entity - The beat target, or {@code null} to unset the beam.
     */
    public void setBeamTarget(@Nullable net.minecraft.world.entity.Entity entity) {
        setBeamTarget0(entity != null ? entity.getId() : 0);
    }
    
    /**
     * Sets the guardian beam target.
     *
     * @param entity - The beat target, or {@code null} to unset the beam.
     */
    public void setBeamTarget(@Nullable org.bukkit.entity.Entity entity) {
        setBeamTarget0(entity != null ? entity.getEntityId() : 0);
    }
    
    @ApiStatus.Internal
    private void setBeamTarget0(int entityId) {
        this.setEntityDataValue(EntityDataSerializers.INT, 17, entityId);
        this.updateEntityDataForAll();
    }
}
