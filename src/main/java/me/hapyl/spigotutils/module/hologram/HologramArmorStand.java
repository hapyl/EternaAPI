package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.ReflectPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Set;

public class HologramArmorStand {

    private final EntityArmorStand armorStand;
    private final ArmorStand bukkit;
    private Location location;

    protected HologramArmorStand(Location location, String name) {
        this.armorStand = new EntityArmorStand(
                Reflect.getMinecraftWorld(location.getWorld()),
                location.getX(),
                location.getY(),
                location.getZ()
        );
        this.bukkit = (ArmorStand) armorStand.getBukkitEntity();
        this.location = location;

        bukkit.teleport(bukkit.getLocation());

        bukkit.setInvisible(true);
        bukkit.setSmall(true);
        bukkit.setMarker(true);
        bukkit.setCustomName(name);
        bukkit.setCustomNameVisible(true);

        // Create packets
    }

    public void show(Player player) {
        ReflectPacket.send(new PacketPlayOutSpawnEntity(armorStand), player);
        ReflectPacket.send(
                new PacketPlayOutEntityMetadata(
                        bukkit.getEntityId(),
                        Reflect.getDataWatcherNonDefaultValues(armorStand)
                ),
                player
        );
    }

    public void hide(Player player) {
        ReflectPacket.send(new PacketPlayOutEntityDestroy(bukkit.getEntityId()), player);
    }

    public void setLocation(Location location) {
        this.location = location;
        this.armorStand.a(location.getX(), location.getY(), location.getZ());
    }

    public void updateLocation(Player... players) {
        ReflectPacket.send(new PacketPlayOutEntityTeleport(armorStand), players);
    }

    public void updateLocation(Set<Player> players) {
        for (Player player : players) {
            updateLocation(player);
        }
    }

    public Location getLocation() {
        return location;
    }

    public ArmorStand bukkitEntity() {
        return bukkit;
    }
}
