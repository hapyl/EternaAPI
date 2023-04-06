package me.hapyl.spigotutils.module.entity;

import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Bat;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * This class allows to create ropes between two areas.
 *
 * @deprecated Bats can't be leashed.
 */
@Deprecated
public class Rope {

    // FIXME (hapyl): 006, Apr 6, 2023: Bats can't be leashed.

    private boolean removeAtServerRestart;
    private int id;

    private final Bat[] bats = new Bat[2];
    private final Location startPoint;
    private final Location endPoint;

    /**
     * Creates a rope.
     *
     * @param startPoint - From.
     * @param endPoint   - To;
     */
    public Rope(Location startPoint, Location endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Rope(World world, double x, double y, double z, double x1, double y1, double z1) {
        this(new Location(world, x, y, z), new Location(world, x1, y1, z1));
    }

    public Rope(double x, double y, double z, double x1, double y1, double z1) {
        this(new Location(Bukkit.getWorlds().get(0), x, y, z), new Location(Bukkit.getWorlds().get(0), x1, y1, z1));
    }

    /**
     * If true, this rope will be removed on server restart.
     *
     * @param flag - flag.
     */
    public Rope setRemoveAtServerRestart(boolean flag) {
        this.removeAtServerRestart = flag;
        return this;
    }

    /**
     * Removed the rope.
     */
    public void remove() {
        for (final Bat bat : this.bats) {
            if (bat != null) {
                bat.setLeashHolder(null);
                bat.remove();
            }
        }

        EternaRegistry.getRopeRegistry().unregister(this.id, this);
    }

    public void spawn() {
        this.validateLocation();
        this.bats[0] = this.spawnBat(BukkitUtils.centerLocation(this.startPoint));
        this.bats[1] = this.spawnBat(BukkitUtils.centerLocation(this.endPoint));
        this.bats[0].setLeashHolder(this.bats[1]);
        EternaRegistry.getRopeRegistry().register(this);
    }

    public Location getStartPoint() {
        return this.startPoint;
    }

    public Location getEndPoint() {
        return this.endPoint;
    }

    public int getId() {
        return this.id;
    }

    // static members
    @Nullable
    public static Rope getById(int id) {
        return EternaRegistry.getRopeRegistry().getById(id);
    }

    private Bat spawnBat(Location location) {
        return Entities.BAT.spawn(location, me -> {
            me.setAwake(false);
            me.setAI(false);
            me.setGravity(false);
            me.setInvulnerable(true);
            me.setInvisible(true);
            me.setCollidable(false);
            me.setPersistent(true);
            me.setSilent(true);
            me.setPersistent(true);
            me.addScoreboardTag("RopeEntity");
        });
    }

    private void validateLocation() {
        if (!Objects.equals(startPoint.getWorld(), endPoint.getWorld())) {
            throw new IllegalArgumentException("Both locations must be in the same world!");
        }
    }

    public void setId(int freeId) {
        id = freeId;
    }

    public boolean isRemoveAtRestart() {
        return removeAtServerRestart;
    }
}
