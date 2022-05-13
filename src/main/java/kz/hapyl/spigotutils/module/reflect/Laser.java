package kz.hapyl.spigotutils.module.reflect;

import kz.hapyl.spigotutils.module.annotate.TestedNMS;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntitySquid;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * Creates a laser (Guardian beam) between start and end.
 */
@TestedNMS(version = "1.18")
public class Laser {

    private final Location start;
    private final Location end;

    private EntityGuardian guardian;
    private EntitySquid squid;

    public Laser(Location start, Location end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Spawns laser for players.
     *
     * @param viewers - Players who will see the laser. <b>Keep null to make everyone a viewer.</b>
     */
    public void spawn(@Nullable Player... viewers) {
        viewers = insureViewers(viewers);
        create();

        // spawn entity
        new ReflectPacket(new PacketPlayOutSpawnEntityLiving(this.guardian)).sendPackets(viewers);
        new ReflectPacket(new PacketPlayOutSpawnEntityLiving(this.squid)).sendPackets(viewers);

        // remove player collision
        for (Player player : viewers) {
            Reflect.setCollision(squid, player, false);
            Reflect.setCollision(guardian, player, false);
        }

        // make entities invisible and set guardian's beam target
        Reflect.setDataWatcherValue(squid, DataWatcherType.BYTE, 0, (byte) 0x20, viewers);
        Reflect.setDataWatcherValue(guardian, DataWatcherType.BYTE, 0, (byte) 0x20, viewers);
        Reflect.setDataWatcherValue(guardian, DataWatcherType.INT, 17, Reflect.getEntityId(squid), viewers);
    }

    /**
     * Removes laser.
     *
     * @param viewers - Player who will see remove. <b>Keep null to remove for everyone.</b>
     */
    public void remove(Player... viewers) {
        if (this.guardian == null || this.squid == null) {
            return;
        }

        viewers = insureViewers(viewers);
        Reflect.destroyEntity(this.guardian, viewers);
        Reflect.destroyEntity(this.squid, viewers);
    }

    /**
     * Moves the laser to the new position.
     *
     * @param start   - New start location. Keep null to keep previous location.
     * @param end     - New end location. Keep null to keep previous location.
     * @param viewers - Players who will see the move. <b>Provided players must see the laser. Keep null to make everyone a viewer.</b>
     */
    public void move(@Nullable Location start, @Nullable Location end, @Nullable Player... viewers) {
        viewers = insureViewers(viewers);
        Reflect.setEntityLocation(this.guardian, start == null ? this.start : start);
        Reflect.setEntityLocation(this.squid, end == null ? this.end : end);
        Reflect.updateEntityLocation(this.guardian, viewers);
        Reflect.updateEntityLocation(this.squid, viewers);
    }

    private void create() {
        if (guardian != null && squid != null) {
            return;
        }

        guardian = new EntityGuardian(EntityTypes.K, Reflect.getMinecraftWorld(this.start.getWorld()));
        Reflect.setEntityLocation(guardian, start);

        squid = new EntitySquid(EntityTypes.aJ, Reflect.getMinecraftWorld(this.end.getWorld()));
        Reflect.setEntityLocation(squid, end);

        // remove collision between guardian and squid
        Reflect.setCollision(squid, guardian.getBukkitEntity(), false);
        Reflect.setCollision(guardian, squid.getBukkitEntity(), false);
    }

    private Player[] insureViewers(Player... b) {
        if (b == null || b.length == 0) {
            return Bukkit.getOnlinePlayers().toArray(new Player[] {});
        }
        return b;
    }

}
