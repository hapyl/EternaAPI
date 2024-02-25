package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
        bukkit.setCustomName(Chat.translateColor(name));
        bukkit.setCustomNameVisible(true);

        // Create packets
    }

    public void show(Player player) {
        Reflect.sendPacket(player, new PacketPlayOutSpawnEntity(armorStand));
        update(player);
    }

    public void update(Player player) {
        Reflect.sendPacket(player, new PacketPlayOutEntityMetadata(
                bukkit.getEntityId(),
                Reflect.getDataWatcherNonDefaultValues(armorStand)
        ));
    }

    public void hide(Player player) {
        Reflect.sendPacket(player, new PacketPlayOutEntityDestroy(bukkit.getEntityId()));
    }

    public void setLine(@Nullable String newText) {
        bukkit.setCustomName(newText == null ? "" : Chat.translateColor(newText));
    }

    public void updateLocation(@Nonnull Player player) {
        Reflect.sendPacket(player, new PacketPlayOutEntityTeleport(armorStand));
    }

    public void updateLocation(Set<Player> players) {
        for (Player player : players) {
            updateLocation(player);
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.armorStand.a(location.getX(), location.getY(), location.getZ());
    }

    public ArmorStand bukkitEntity() {
        return bukkit;
    }
}
