package me.hapyl.spigotutils.module.reflect;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import me.hapyl.spigotutils.module.entity.packet.NMSEntityType;
import me.hapyl.spigotutils.module.reflect.packet.Packets;
import me.hapyl.spigotutils.module.util.EternaEntity;
import me.hapyl.spigotutils.module.util.TeamHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntitySquid;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Creates a laser (Guardian beam) between start and end.
 */
@TestedOn(version = Version.V1_20_4)
public class Laser implements EternaEntity {

    private final Location start;
    private final Location end;
    private final Set<Player> showingTo;

    private EntityGuardian guardian;
    private EntitySquid squid;

    public Laser(Location start, Location end) {
        this.start = start;
        this.end = end;
        this.showingTo = Sets.newHashSet();
    }

    @Override
    public void show(@Nonnull Player player) {
        this.spawn(player);
    }

    @Override
    public void hide(@Nonnull Player player) {
        this.remove(player);
    }

    @Nonnull
    @Override
    public Set<Player> getShowingTo() {
        return showingTo;
    }

    /**
     * Spawns laser for player.
     *
     * @param player - Players who will see the laser.
     */
    public void spawn(@Nonnull Player player) {
        create();

        // spawn entity
        Packets.Server.spawnEntityLiving(guardian, player);
        Packets.Server.spawnEntityLiving(squid, player);

        // guardian-squid collision
        guardian.collidableExemptions.add(squid.getBukkitEntity().getUniqueId());
        squid.collidableExemptions.add(guardian.getBukkitEntity().getUniqueId());

        // remove player collision
        removeCollision(squid, player);
        removeCollision(guardian, player);

        // make entities invisible and set guardian's beam target
        Reflect.setDataWatcherValue(squid, DataWatcherType.BYTE, 0, (byte) 0x20);
        Reflect.setDataWatcherValue(guardian, DataWatcherType.BYTE, 0, (byte) 0x20);
        Reflect.setDataWatcherValue(guardian, DataWatcherType.INT, 17, Reflect.getEntityId(squid));

        Reflect.updateMetadata(squid, player);
        Reflect.updateMetadata(guardian, player);

        showingTo.add(player);
    }

    /**
     * Removes laser.
     *
     * @param player - Player, who will see remove.
     */
    public void remove(Player player) {
        if (this.guardian == null || this.squid == null) {
            return;
        }

        Reflect.destroyEntity(this.guardian, player);
        Reflect.destroyEntity(this.squid, player);

        showingTo.remove(player);
    }

    /**
     * Moves the laser to the new position.
     *
     * @param start - New start location. Keep null to keep previous location.
     * @param end   - New end location. Keep null to keep previous location.
     */
    public void move(@Nullable Location start, @Nullable Location end) {
        Reflect.setEntityLocation(this.guardian, start == null ? this.start : start);
        Reflect.setEntityLocation(this.squid, end == null ? this.end : end);

        showingTo.forEach(player -> {
            Reflect.updateEntityLocation(this.guardian, player);
            Reflect.updateEntityLocation(this.squid, player);
        });

    }

    private void create() {
        if (guardian != null && squid != null) {
            return;
        }

        guardian = new EntityGuardian(NMSEntityType.GUARDIAN, Reflect.getMinecraftWorld(this.start.getWorld()));
        Reflect.setEntityLocation(guardian, start);

        squid = new EntitySquid(NMSEntityType.SQUID, Reflect.getMinecraftWorld(this.end.getWorld()));
        Reflect.setEntityLocation(squid, end);
    }

    private void removeCollision(EntityLiving entity, Player player) {
        final Entity bukkitEntity = entity.getBukkitEntity();

        TeamHelper.FAKE_ENTITY.addToTeam(player.getScoreboard(), bukkitEntity);
    }

}
