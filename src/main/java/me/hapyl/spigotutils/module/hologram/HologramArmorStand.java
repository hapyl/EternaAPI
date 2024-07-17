package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.PacketFactory;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class HologramArmorStand {

    private final net.minecraft.world.entity.decoration.ArmorStand armorStand;
    private final ArmorStand bukkit;
    private Location location;

    protected HologramArmorStand(Location location, String name) {
        this.armorStand = new net.minecraft.world.entity.decoration.ArmorStand(
                Reflect.getMinecraftWorld(location.getWorld()),
                location.getX(),
                location.getY(),
                location.getZ()
        );
        this.bukkit = (ArmorStand) armorStand.getBukkitEntity();
        this.location = location;

        armorStand.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setCustomName(Chat.component(name));
        armorStand.setCustomNameVisible(true);

        //bukkit.teleport(bukkit.getLocation());
        //
        //bukkit.setInvisible(true);
        //bukkit.setSmall(true);
        //bukkit.setMarker(true);
        //bukkit.setCustomName(Chat.format(name));
        //bukkit.setCustomNameVisible(true);
    }

    public void show(Player player) {
        Reflect.sendPacket(player, PacketFactory.makePacketPlayOutSpawnEntity(armorStand, location));
        update(player);
    }

    public void update(Player player) {
        Reflect.sendPacket(player, new ClientboundSetEntityDataPacket(
                armorStand.getId(),
                Reflect.getDataWatcherNonDefaultValues(armorStand)
        ));
    }

    public void hide(Player player) {
        Reflect.sendPacket(player, new ClientboundRemoveEntitiesPacket(bukkit.getEntityId()));
    }

    public void setLine(@Nullable String newText) {
        armorStand.setCustomName(newText == null ? Component.empty() : Chat.component(newText));
    }

    public void updateLocation(@Nonnull Player player) {
        Reflect.sendPacket(player, new ClientboundTeleportEntityPacket(armorStand));
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
        this.armorStand.absMoveTo(location.getX(), location.getY(), location.getZ());
    }

    public ArmorStand bukkitEntity() {
        return bukkit;
    }
}
