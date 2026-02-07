package me.hapyl.eterna.module.entity.packet;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.squid.Squid;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a {@link Squid} packet entity.
 */
public class PacketSquid extends AbstractPacketEntity<Squid> {
    
    /**
     * Creates a new {@link PacketSquid}.
     *
     * @param location - The initial location.
     */
    public PacketSquid(@NotNull Location location) {
        super(new Squid(EntityType.SQUID, getWorld(location)), location);
    }
}
