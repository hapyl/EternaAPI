package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.ReflectPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

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

        armorStand.absMoveTo(location.getX(), location.getY(), location.getZ());

        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setCustomName(Component.literal(name));
        armorStand.setCustomNameVisible(true);

        // Create packets
    }

    public void show(Player player) {
        ReflectPacket.send(new ClientboundAddEntityPacket(armorStand), player);
        ReflectPacket.send(
                new ClientboundSetEntityDataPacket(armorStand.getId(), Reflect.getDataWatcher(armorStand).getNonDefaultValues()),
                player
        );
    }

    public void hide(Player player) {
        ReflectPacket.send(new ClientboundRemoveEntitiesPacket(armorStand.getId()), player);
    }

    public void setLocation(Location location) {
        this.location = location;
        this.armorStand.absMoveTo(location.getX(), location.getY(), location.getZ());
    }

    public void updateLocation(Player... players) {
        ReflectPacket.send(new ClientboundTeleportEntityPacket(armorStand), players);
    }

    public Location getLocation() {
        return location;
    }

    public ArmorStand bukkitEntity() {
        return bukkit;
    }
}
