package me.hapyl.spigotutils.module.reflect;

import me.hapyl.spigotutils.module.annotate.TestedNMS;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.reflect.packet.Packets;
import me.hapyl.spigotutils.module.util.TeamHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntitySquid;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * Creates a laser (Guardian beam) between start and end.
 */
@TestedNMS(version = "1.19.2")
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
     * @param players - Players who will see the laser. <b>Keep null to make everyone a viewer.</b>
     */
    public void spawn(@Nullable Player... players) {
        players = insureViewers(players);
        create();

        // spawn entity
        Packets.Server.spawnEntityLiving(guardian, players);
        Packets.Server.spawnEntityLiving(squid, players);

        // guardian-squid collision
        guardian.collidableExemptions.add(squid.getBukkitEntity().getUniqueId());
        squid.collidableExemptions.add(guardian.getBukkitEntity().getUniqueId());

        // remove player collision
        removeCollision(squid, players);
        removeCollision(guardian, players);

        // make entities invisible and set guardian's beam target
        Reflect.setDataWatcherValue(squid, DataWatcherType.BYTE, 0, (byte) 0x20, players);
        Reflect.setDataWatcherValue(guardian, DataWatcherType.BYTE, 0, (byte) 0x20, players);
        Reflect.setDataWatcherValue(guardian, DataWatcherType.INT, 17, Reflect.getEntityId(squid), players);
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

        guardian = new EntityGuardian(Entities.NMS.GUARDIAN, Reflect.getMinecraftWorld(this.start.getWorld()));
        Reflect.setEntityLocation(guardian, start);

        squid = new EntitySquid(Entities.NMS.SQUID, Reflect.getMinecraftWorld(this.end.getWorld()));
        Reflect.setEntityLocation(squid, end);
    }

    private Player[] insureViewers(Player... b) {
        if (b == null || b.length == 0) {
            return Bukkit.getOnlinePlayers().toArray(new Player[] {});
        }
        return b;
    }

    private void removeCollision(EntityLiving entity, Player... players) {
        final CraftEntity bukkitEntity = entity.getBukkitEntity();
        for (Player player : players) {
            TeamHelper.FAKE_ENTITY.addToTeam(player.getScoreboard(), bukkitEntity);
        }
    }

}
