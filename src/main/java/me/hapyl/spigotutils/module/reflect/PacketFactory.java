package me.hapyl.spigotutils.module.reflect;

import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.UUID;

public final class PacketFactory {

    private PacketFactory() {
    }

    @Nonnull
    public static PacketPlayOutSpawnEntity makePacketPlayOutSpawnEntity(@Nonnull Entity entity, double x, double y, double z) {
        final int entityId = Reflect.getEntityId(entity);
        final UUID uuid = Reflect.getEntityUuid(entity);

        return new PacketPlayOutSpawnEntity(
                entityId,
                uuid,
                x,
                y,
                z,
                entity.dG(), // getXRot()
                entity.dE(), // getYRot()
                entity.am(), // getType()
                entityId,    // Why 2 entity id????
                entity.dr(), // getDeltaMovement()
                entity.ct()  // getYHeadRot()
        );
    }

    @Nonnull
    public static PacketPlayOutSpawnEntity makePacketPlayOutSpawnEntity(@Nonnull Entity entity, @Nonnull Location location) {
        return makePacketPlayOutSpawnEntity(entity, location.getX(), location.getY(), location.getZ());
    }

    @Nonnull
    public static PacketPlayOutSpawnEntity makePacketPlayOutSpawnEntity(@Nonnull Entity entity) {
        return makePacketPlayOutSpawnEntity(entity, Reflect.getEntityLocation(entity));
    }

}
