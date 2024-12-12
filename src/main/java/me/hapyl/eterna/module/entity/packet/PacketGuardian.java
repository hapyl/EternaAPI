package me.hapyl.eterna.module.entity.packet;

import me.hapyl.eterna.module.reflect.DataWatcherType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PacketGuardian extends PacketEntity<Guardian> {

    public PacketGuardian(@Nonnull Location location) {
        super(new Guardian(EntityType.GUARDIAN, getWorld(location)), location);
    }

    /**
     * Sets the bean target of the guardian to be the given entity.
     *
     * @param entity - The beam target; null entity will remove the beam.
     */
    public void setBeamTarget(@Nullable PacketEntity<?> entity) {
        setBeamTarget(entity != null ? entity.entityId : 0);
    }

    /**
     * Sets the bean target of the guardian to be the given entity.
     *
     * @param entity - The beam target; null entity will remove the beam.
     */
    public void setBeamTarget(@Nullable net.minecraft.world.entity.Entity entity) {
        setBeamTarget(entity != null ? entity.getId() : 0);
    }

    /**
     * Sets the bean target of the guardian to be the given entity.
     *
     * @param entity - The beam target; null entity will remove the beam.
     */
    public void setBeamTarget(@Nullable org.bukkit.entity.Entity entity) {
        setBeamTarget(entity != null ? entity.getEntityId() : 0);
    }

    /**
     * Sets the bean target of the guardian to be the given entity.
     *
     * @param entityId - The entity numeric Id; null entity will remove the beam.
     */
    public void setBeamTarget(int entityId) {
        setDataWatcherValue(DataWatcherType.INT, 17, entityId);
        updateMetadata();
    }
}
