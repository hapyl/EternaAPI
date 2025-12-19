package me.hapyl.eterna.module.reflect;

import me.hapyl.eterna.module.inventory.Equipment;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A static factory class for creating packets.
 */
public final class PacketFactory {
    
    private PacketFactory() {
    }
    
    @Nonnull
    public static ClientboundAddEntityPacket makePacketAddEntity(@Nonnull Entity entity, double x, double y, double z) {
        final int entityId = Reflect.getEntityId(entity);
        final UUID uuid = Reflect.getEntityUuid(entity);
        
        return new ClientboundAddEntityPacket(
                entityId,
                uuid,
                x,
                y,
                z,
                entity.getXRot(),
                entity.getYRot(),
                entity.getType(),
                entityId,
                entity.getDeltaMovement(),
                entity.getYHeadRot()
        );
    }
    
    @Nonnull
    public static ClientboundAddEntityPacket makePacketAddEntity(@Nonnull Entity entity, @Nonnull Location location) {
        return makePacketAddEntity(entity, location.getX(), location.getY(), location.getZ());
    }
    
    @Nonnull
    public static ClientboundAddEntityPacket makePacketAddEntity(@Nonnull Entity entity) {
        return makePacketAddEntity(entity, Reflect.getEntityLocation(entity));
    }
    
    @Nonnull
    public static ClientboundSetEntityDataPacket makePacketSetEntityData(@Nonnull Entity entity) {
        return makePacketSetEntityData(entity, entity.getEntityData().getNonDefaultValues());
    }
    
    @Nonnull
    public static ClientboundSetEntityDataPacket makePacketSetEntityData(@Nonnull Entity entity, @Nullable List<SynchedEntityData.DataValue<?>> valuesToUpdate) {
        return new ClientboundSetEntityDataPacket(entity.getId(), valuesToUpdate);
    }
    
    @Nonnull
    public static ClientboundRemoveEntitiesPacket makePacketRemoveEntity(@Nonnull Entity entity) {
        return new ClientboundRemoveEntitiesPacket(entity.getId());
    }
    
    @Nonnull
    public static ClientboundTeleportEntityPacket makePacketTeleportEntity(Entity entity) {
        return ClientboundTeleportEntityPacket.teleport(
                entity.getId(),
                PositionMoveRotation.of(entity),
                Set.of(),
                entity.onGround
        );
    }
    
    @Nonnull
    public static ClientboundRotateHeadPacket makePacketRotateHead(@Nonnull Entity entity, float yaw) {
        return new ClientboundRotateHeadPacket(entity, (byte) ((yaw * 256) / 360));
    }
    
    @Nonnull
    public static ClientboundPlayerInfoUpdatePacket makePacketPlayerInfoUpdate(@Nonnull ServerPlayer player, @Nonnull ClientboundPlayerInfoUpdatePacket.Action action) {
        return new ClientboundPlayerInfoUpdatePacket(action, player);
    }
    
    @Nonnull
    public static ClientboundPlayerInfoUpdatePacket makePacketPlayerInitialization(@Nonnull ServerPlayer player) {
        return net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player));
    }
    
    @Nonnull
    public static ClientboundPlayerInfoRemovePacket makePacketPlayerInfoRemove(@Nonnull ServerPlayer player) {
        return new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID()));
    }
    
    @Nonnull
    public static ClientboundSetEquipmentPacket makePacketSetEquipment(@Nonnull Entity entity, @Nonnull Equipment equipment) {
        return new ClientboundSetEquipmentPacket(entity.getId(), equipment.wrapToNms());
    }
    
    @Nonnull
    public static ClientboundMoveEntityPacket.Rot makePacketMoveEntityRot(@Nonnull Entity entity, float yaw, float pitch) {
        return new ClientboundMoveEntityPacket.Rot(entity.getId(), (byte) (yaw * 256 / 360), (byte) (pitch * 256 / 360), true);
    }
    
    @Nonnull
    public static ClientboundSetPassengersPacket makePacketSetPassengers(@Nonnull Entity entity) {
        return new ClientboundSetPassengersPacket(entity);
    }
    
    @Nonnull
    public static ClientboundSetEntityLinkPacket makePacketSetEntityLink(@Nonnull Entity firstEntity, @Nullable Entity secondEntity) {
        return new ClientboundSetEntityLinkPacket(firstEntity, secondEntity);
    }
    
    @Nonnull
    public static ClientboundUpdateAttributesPacket makePacketUpdateAttributes(@Nonnull Entity entity, @Nonnull Collection<AttributeInstance> attributes) {
        return new ClientboundUpdateAttributesPacket(entity.getId(), attributes);
    }
}
