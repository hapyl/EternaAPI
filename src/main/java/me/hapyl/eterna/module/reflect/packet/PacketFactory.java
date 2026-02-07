package me.hapyl.eterna.module.reflect.packet;

import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.inventory.Equipment;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a {@link Packet}-related utility class, allowing to easily create packets.
 *
 * <p>
 * Packets can be sent via {@link Reflect#sendPacket(Player, Packet[])}.
 * </p>
 */
@UtilityClass
public final class PacketFactory {
    
    private PacketFactory() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Creates a {@link ClientboundAddEntityPacket}.
     *
     * @param entity - The entity to add.
     * @param x      - The {@code x} coordinate.
     * @param y      - The {@code y} coordinate.
     * @param z      - The {@code z} coordinate.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundAddEntityPacket makePacketAddEntity(@NotNull Entity entity, double x, double y, double z) {
        final int entityId = entity.getId();
        final UUID uuid = entity.getUUID();
        
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
    
    /**
     * Creates a new {@link ClientboundAddEntityPacket}.
     *
     * @param entity   - The entity to add.
     * @param location - THe location to add at.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundAddEntityPacket makePacketAddEntity(@NotNull Entity entity, @NotNull Location location) {
        return makePacketAddEntity(entity, location.getX(), location.getY(), location.getZ());
    }
    
    /**
     * Creates a new {@link ClientboundAddEntityPacket}.
     *
     * @param entity - The entity to add.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundAddEntityPacket makePacketAddEntity(@NotNull Entity entity) {
        return makePacketAddEntity(entity, Reflect.getEntityLocation(entity));
    }
    
    /**
     * Creates a new {@link ClientboundSetEntityDataPacket}.
     *
     * @param entity - The entity whose data to update.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundSetEntityDataPacket makePacketSetEntityData(@NotNull Entity entity) {
        return makePacketSetEntityData(entity, Objects.requireNonNull(entity.getEntityData().getNonDefaultValues(), "Non-default values are null!"));
    }
    
    /**
     * Creates a new {@link ClientboundSetEntityDataPacket}.
     *
     * @param entity         - The entity whose data to update.
     * @param valuesToUpdate - The list of values to update.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundSetEntityDataPacket makePacketSetEntityData(@NotNull Entity entity, @NotNull List<SynchedEntityData.DataValue<?>> valuesToUpdate) {
        return new ClientboundSetEntityDataPacket(entity.getId(), valuesToUpdate);
    }
    
    /**
     * Creates a new {@link ClientboundRemoveEntitiesPacket}.
     *
     * @param entity - The entity to remove.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundRemoveEntitiesPacket makePacketRemoveEntity(@NotNull Entity entity) {
        return new ClientboundRemoveEntitiesPacket(entity.getId());
    }
    
    /**
     * Creates a new {@link ClientboundTeleportEntityPacket}.
     *
     * @param entity - The entity to teleport.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundTeleportEntityPacket makePacketTeleportEntity(Entity entity) {
        return ClientboundTeleportEntityPacket.teleport(
                entity.getId(),
                PositionMoveRotation.of(entity),
                Set.of(),
                entity.onGround
        );
    }
    
    /**
     * Creates a new {@link ClientboundRotateHeadPacket}.
     *
     * @param entity - The entity whose head to rotate.
     * @param yaw    - The desired yaw.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundRotateHeadPacket makePacketRotateHead(@NotNull Entity entity, float yaw) {
        return new ClientboundRotateHeadPacket(entity, (byte) ((yaw * 256) / 360));
    }
    
    /**
     * Creates a new {@link ClientboundPlayerInfoUpdatePacket}.
     *
     * @param player - The player whose info to update.
     * @param action - The update action.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundPlayerInfoUpdatePacket makePacketPlayerInfoUpdate(@NotNull ServerPlayer player, @NotNull ClientboundPlayerInfoUpdatePacket.Action action) {
        return new ClientboundPlayerInfoUpdatePacket(action, player);
    }
    
    /**
     * Creates a new {@link ClientboundPlayerInfoUpdatePacket}.
     *
     * <p>
     * The packet contains all necessary action to create it, as if they have just joined the server.
     * </p>
     *
     * @param player - The player whom to initialize.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundPlayerInfoUpdatePacket makePacketPlayerInitialization(@NotNull ServerPlayer player) {
        return net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player));
    }
    
    /**
     * Creates a new {@link ClientboundPlayerInfoRemovePacket}.
     *
     * @param player - The player whose info to remove.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundPlayerInfoRemovePacket makePacketPlayerInfoRemove(@NotNull ServerPlayer player) {
        return new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID()));
    }
    
    /**
     * Creates a new {@link ClientboundSetEquipmentPacket}.
     *
     * @param entity    - The entity whose equipment to update.
     * @param equipment - The equipment to update.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundSetEquipmentPacket makePacketSetEquipment(@NotNull Entity entity, @NotNull Equipment equipment) {
        return new ClientboundSetEquipmentPacket(entity.getId(), equipment.toNms());
    }
    
    /**
     * Creates a new {@link ClientboundMoveEntityPacket.Rot}.
     *
     * @param entity - The entity to rotate.
     * @param yaw    - The desired yaw.
     * @param pitch  - The desired pitch.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundMoveEntityPacket.Rot makePacketMoveEntityRot(@NotNull Entity entity, float yaw, float pitch) {
        return new ClientboundMoveEntityPacket.Rot(entity.getId(), (byte) (yaw * 256 / 360), (byte) (pitch * 256 / 360), true);
    }
    
    /**
     * Creates a new {@link ClientboundSetEquipmentPacket}.
     *
     * @param entity - The entity whose passengers to udpate.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundSetPassengersPacket makePacketSetPassengers(@NotNull Entity entity) {
        return new ClientboundSetPassengersPacket(entity);
    }
    
    /**
     * Creates a new {@link ClientboundSetEntityLinkPacket}.
     *
     * @param firstEntity  - The entity to link.
     * @param secondEntity - The entity to link to.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundSetEntityLinkPacket makePacketSetEntityLink(@NotNull Entity firstEntity, @Nullable Entity secondEntity) {
        return new ClientboundSetEntityLinkPacket(firstEntity, secondEntity);
    }
    
    /**
     * Creates a new {@link ClientboundUpdateAttributesPacket}.
     *
     * @param entity     - The entity whose attributes to update.
     * @param attributes - The attributes to update.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundUpdateAttributesPacket makePacketUpdateAttributes(@NotNull Entity entity, @NotNull Collection<AttributeInstance> attributes) {
        return new ClientboundUpdateAttributesPacket(entity.getId(), attributes);
    }
    
    /**
     * Creates a new {@link ClientboundOpenSignEditorPacket}.
     *
     * <p>
     * Note that the location <b>must</b> contain a sign on the client-side!
     * </p>
     *
     * @param location    - The location of the sign.
     * @param isFrontText - {@code true} to show the front text; {@code false} to show the back text.
     * @return a new packet.
     */
    @NotNull
    public static ClientboundOpenSignEditorPacket makePacketOpenSignEditor(@NotNull Location location, boolean isFrontText) {
        return new ClientboundOpenSignEditorPacket(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), isFrontText);
    }
}
