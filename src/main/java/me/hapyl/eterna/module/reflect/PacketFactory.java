package me.hapyl.eterna.module.reflect;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.UUID;

public final class PacketFactory {

    private PacketFactory() {
    }

    @Nonnull
    public static ClientboundAddEntityPacket makePacketPlayOutSpawnEntity(@Nonnull Entity entity, double x, double y, double z) {
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
    public static ClientboundAddEntityPacket makePacketPlayOutSpawnEntity(@Nonnull Entity entity, @Nonnull Location location) {
        return makePacketPlayOutSpawnEntity(entity, location.getX(), location.getY(), location.getZ());
    }

    @Nonnull
    public static ClientboundAddEntityPacket makePacketPlayOutSpawnEntity(@Nonnull Entity entity) {
        return makePacketPlayOutSpawnEntity(entity, Reflect.getEntityLocation(entity));
    }

}
