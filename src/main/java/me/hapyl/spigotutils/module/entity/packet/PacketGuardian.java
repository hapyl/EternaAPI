package me.hapyl.spigotutils.module.entity.packet;

import me.hapyl.spigotutils.module.reflect.DataWatcherType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import org.bukkit.Location;

public class PacketGuardian extends PacketEntity<Guardian> {

    public PacketGuardian(Location location) {
        super(new Guardian(EntityType.GUARDIAN, getWorld(location)), location);
    }

    public void setBeamTarget(PacketEntity<?> entity) {
        setDataWatcherValue(DataWatcherType.INT, 17, entity.getId());
    }
}
