package me.hapyl.eterna.module.reflect.packet.wrapped;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a wrapped {@link ClientboundAddEntityPacket}.
 *
 * <p>
 * This packet is sent whenever an {@link Entity} is spawned.
 * </p>
 */
public class WrappedClientboundAddEntityPacket extends WrappedPacket<ClientboundAddEntityPacket> {
    
    private final EntityType entityType;
    
    @SuppressWarnings("UnstableApiUsage")
    WrappedClientboundAddEntityPacket(@NotNull ClientboundAddEntityPacket packet) {
        super(packet);
        
        this.entityType = Objects.requireNonNull(
                EntityType.fromName(net.minecraft.world.entity.EntityType.getKey(packet.getType()).getPath()),
                "Could not determine entity type!"
        );
    }
    
    /**
     * Gets the id of the spawned {@link Entity}.
     *
     * @return the id of the spawned entity.
     */
    public int getEntityId() {
        return packet.getId();
    }
    
    /**
     * Gets the {@link UUID} of the spawned {@link Entity}.
     *
     * @return the uuid of the spawned entity.
     */
    @NotNull
    public UUID getEntityUuid() {
        return packet.getUUID();
    }
    
    /**
     * Gets the {@link EntityType} of the spawned {@link Entity}.
     *
     * @return the entity type of the spawned entity.
     */
    @NotNull
    public EntityType getEntityType() {
        return entityType;
    }
    
    /**
     * Gets the {@code x} coordinate of the spawned {@link Entity}.
     *
     * @return the {@code x} coordinate of the spawned entity.
     */
    public double getX() {
        return packet.getX();
    }
    
    /**
     * Gets the {@code y} coordinate of the spawned {@link Entity}.
     *
     * @return the {@code y} coordinate of the spawned entity.
     */
    public double getY() {
        return packet.getY();
    }
    
    /**
     * Gets the {@code z} coordinate of the spawned {@link Entity}.
     *
     * @return the {@code z} coordinate of the spawned entity.
     */
    public double getZ() {
        return packet.getZ();
    }
    
    /**
     * Gets the bukkit {@link Entity} that is being spawned.
     *
     * <p>
     * Note that this may return an empty optional in cases where a non-bukkit (packet entity) is being spawned!
     * </p>
     *
     * @return the bukkit entity that is being spawned wrapped in an optional.
     */
    @NotNull
    public Optional<Entity> getEntity() {
        return Optional.ofNullable(Bukkit.getEntity(packet.getUUID()));
    }
    
    /**
     * Gets the {@code yaw} of the spawned {@link Entity}.
     *
     * @return the {@code yaw} of the spawned entity.
     */
    public float getYaw() {
        return packet.getXRot();
    }
    
    /**
     * Gets the {@code pitch} of the spawned {@link Entity}.
     *
     * @return the {@code pitch} of the spawned entity.
     */
    public float getPitch() {
        return packet.getYRot();
    }
    
    /**
     * Gets the head rotation of the spawned {@link Entity}.
     *
     * <p>
     * The head rotation is calculated as follows:
     * <pre>{@code yHeadRot * 360 / 256}</pre>
     * </p>
     *
     * @return the head rotation of the spawned entity.
     */
    public float getHeadRotation() {
        return packet.getYHeadRot();
    }
    
}
