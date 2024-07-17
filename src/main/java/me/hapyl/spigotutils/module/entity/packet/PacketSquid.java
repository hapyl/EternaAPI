package me.hapyl.spigotutils.module.entity.packet;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Squid;
import org.bukkit.Location;

public class PacketSquid extends PacketEntity<Squid> {
    public PacketSquid(Location location) {
        super(new Squid(EntityType.SQUID, getWorld(location)), location);
    }
}
