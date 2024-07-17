package me.hapyl.spigotutils.module.reflect.packet.wrapped;

import me.hapyl.spigotutils.module.entity.packet.NMSEntityType;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class WrappedPacketPlayOutSpawnEntity extends WrappedPacket<ClientboundAddEntityPacket> {

    private final EntityType entityType;

    public WrappedPacketPlayOutSpawnEntity(ClientboundAddEntityPacket packet) {
        super(packet);

        this.entityType = NMSEntityType.fromNmsOr(packet.getType(), EntityType.EGG);
    }

    /**
     * Gets the Id of the spawned entity.
     *
     * @return the Id of the spawned entity.
     */
    public int getEntityId() {
        return packet.getId();
    }

    /**
     * Gets the {@link UUID} of the spawned entity.
     *
     * @return the UUID of the spawned entity.
     */
    @Nonnull
    public UUID getEntityUuid() {
        return packet.getUUID();
    }

    /**
     * Gets {@link EntityType} of the spawned entity.
     * <br>
     * This method may sometimes wrongly return {@link EntityType#EGG} as a type even if it's not. This is a fail-safe to make this method {@link Nonnull}.
     *
     * @return the entity type of the spawned entity.
     */
    @Nonnull
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * Gets the X coordinate of the spawned entity.
     *
     * @return the X coordinate of the spawned entity.
     */
    public double getX() {
        return packet.getX();
    }

    /**
     * Gets the Y coordinate of the spawned entity.
     *
     * @return the Y coordinate of the spawned entity.
     */
    public double getY() {
        return packet.getY();
    }

    /**
     * Gets the Z coordinate of the spawned entity.
     *
     * @return the Z coordinate of the spawned entity.
     */
    public double getZ() {
        return packet.getZ();
    }

    /**
     * Gets a {@link Location} of the spawned entity in {@link Player}'s current {@link World}.
     *
     * @param player - Player.
     * @return a location of the spawned entity.
     * @apiNote The packet doesn't have the world entity is spawned in.
     */
    @Nonnull
    public Location getLocation(@Nonnull Player player) {
        return new Location(player.getWorld(), getX(), getY(), getZ());
    }

    /**
     * Gets ths pitch of the spawned entity.
     *
     * @return the pitch of the spawned entity.
     */
    public float getPitch() {
        return packet.getXRot();
    }

    /**
     * Gets the yaw of the spawned entity.
     *
     * @return the yaw of the spawned entity.
     */
    public float getYaw() {
        return packet.getYRot();
    }

    /**
     * Gets the head rotation of the spawned entity.
     * <br>
     * The head rotation is calculated as following:
     * <pre><code>
     *     yHeadRot * 360 / 256
     * </code></pre>
     *
     * @return the head rotation of the spawned entity.
     */
    public float getHeadRotation() {
        return packet.getYHeadRot();
    }

    /**
     * Gets some magic data of this packet.
     *
     * @return some magic data of this packet.
     * @deprecated magic value.
     */
    @Deprecated
    public int getData() {
        return packet.getData();
    }

}
