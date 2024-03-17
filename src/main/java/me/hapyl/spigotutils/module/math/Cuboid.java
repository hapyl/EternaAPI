package me.hapyl.spigotutils.module.math;

import me.hapyl.spigotutils.module.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A class that allows to create and manipulate a cuboid region.
 *
 * @author <a href="https://www.spigotmc.org/threads/region-cuboid.329859/">Tristiisch74</a>
 * @author hapyl
 */
public class Cuboid {

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;
    private final double xMinCentered;
    private final double xMaxCentered;
    private final double yMinCentered;
    private final double yMaxCentered;
    private final double zMinCentered;
    private final double zMaxCentered;
    private final World world;

    /**
     * Creates a cuboid between two points.
     * This will use the first world (usually 'world') as the location world.
     *
     * @param x  - Start x.
     * @param y  - Start y.
     * @param z  - Start z.
     * @param x2 - End x.
     * @param y2 - End y.
     * @param z2 - End z.
     */
    public Cuboid(double x, double y, double z, double x2, double y2, double z2) {
        this(BukkitUtils.defWorld(), x, y, z, x2, y2, z2);
    }

    /**
     * Creates a cuboid between two points.
     *
     * @param world - World.
     * @param x     - Start x.
     * @param y     - Start y.
     * @param z     - Start z.
     * @param x2    - End x.
     * @param y2    - End y.
     * @param z2    - End z.
     */
    public Cuboid(@Nonnull World world, double x, double y, double z, double x2, double y2, double z2) {
        this(new Location(world, x, y, z), new Location(world, x2, y2, z2));
    }

    /**
     * Creates a cuboid between two points.
     *
     * @param point1 - Point 1.
     * @param point2 - Point 2.
     */
    public Cuboid(final Location point1, final Location point2) {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
        this.world = point1.getWorld();
        this.xMinCentered = this.xMin + 0.5;
        this.xMaxCentered = this.xMax + 0.5;
        this.yMinCentered = this.yMin + 0.5;
        this.yMaxCentered = this.yMax + 0.5;
        this.zMinCentered = this.zMin + 0.5;
        this.zMaxCentered = this.zMax + 0.5;
    }

    /**
     * Gets the blocks inside the area as iterator.
     *
     * @return Block iterator.
     */
    public Iterator<Block> blockList() {
        return getBlocks().iterator();
    }

    /**
     * Gets the blocks inside the area.
     *
     * @return list of blocks inside the area.
     */
    public List<Block> getBlocks() {
        return getBlocks(block -> true);
    }

    /**
     * Gets the blocks inside the area that matches the predicate.
     *
     * @param predicate - Predicate.
     * @return list of blocks inside the area that matches the predicate.
     */
    public List<Block> getBlocks(@Nonnull Predicate<Block> predicate) {
        final List<Block> bL = new ArrayList<>(this.getTotalBlockSize());
        for (int x = this.xMin; x <= this.xMax; ++x) {
            for (int y = this.yMin; y <= this.yMax; ++y) {
                for (int z = this.zMin; z <= this.zMax; ++z) {
                    final Block b = this.world.getBlockAt(x, y, z);
                    if (predicate.test(b)) {
                        bL.add(b);
                    }
                }
            }
        }
        return bL;
    }

    /**
     * Gets the blocks inside the area as a hash set.
     *
     * @return set of blocks inside the area.
     */
    public Set<Block> getBlockSet() {
        return new HashSet<>(this.getBlocks());
    }

    /**
     * Gets the center location of this cuboid.
     *
     * @return center location of this cuboid.
     */
    public Location getCenter() {
        return new Location(this.world, (this.xMax - this.xMin) / 2d + this.xMin, (this.yMax - this.yMin) / 2d + this.yMin,
                (this.zMax - this.zMin) / 2d + this.zMin
        );
    }

    /**
     * Gets the distance between point 1 and point 2.
     *
     * @return the distance between points.
     */
    public double getDistance() {
        return this.getPoint1().distance(this.getPoint2());
    }

    /**
     * Gets the squared distance between point 1 and point 2.
     *
     * @return the squared distance between points.
     */
    public double getDistanceSquared() {
        return this.getPoint1().distanceSquared(this.getPoint2());
    }

    /**
     * Gets the height of this cuboid.
     *
     * @return the height of this cuboid.
     */
    public int getHeight() {
        return this.yMax - this.yMin + 1;
    }

    /**
     * Gets the copy location of the first point.
     *
     * @return the copy location of the first point.
     */
    public Location getPoint1() {
        return new Location(this.world, this.xMin, this.yMin, this.zMin);
    }

    /**
     * Gets the copy location of the second point.
     *
     * @return the copy location of the second point.
     */
    public Location getPoint2() {
        return new Location(this.world, this.xMax, this.yMax, this.zMax);
    }

    /**
     * Gets a random location between two points.
     *
     * @return a random location between two points.
     */
    public Location getRandomLocation() {
        final Random rand = new Random();
        final int x = rand.nextInt(Math.abs(this.xMax - this.xMin) + 1) + this.xMin;
        final int y = rand.nextInt(Math.abs(this.yMax - this.yMin) + 1) + this.yMin;
        final int z = rand.nextInt(Math.abs(this.zMax - this.zMin) + 1) + this.zMin;
        return new Location(this.world, x, y, z);
    }

    /**
     * Gets the total block size.
     *
     * @return total block size.
     */
    public int getTotalBlockSize() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }

    /**
     * Gets the width in X axis.
     *
     * @return the width in X axis.
     */
    public int getXWidth() {
        return this.xMax - this.xMin + 1;
    }

    /**
     * Gets the width in Z axis.
     *
     * @return the width in Z axis.
     */
    public int getZWidth() {
        return this.zMax - this.zMin + 1;
    }

    /**
     * Gets the min X.
     *
     * @return min X.
     */
    public int getMinX() {
        return xMin;
    }

    /**
     * Gets the max X.
     *
     * @return max X.
     */
    public int getMaxX() {
        return xMax;
    }

    /**
     * Gets the min Y.
     *
     * @return min Y.
     */
    public int getMinY() {
        return yMin;
    }

    /**
     * Gets the max Y.
     *
     * @return max Y.
     */
    public int getMaxY() {
        return yMax;
    }

    /**
     * Gets the min Z.
     *
     * @return min Z.
     */
    public int getMinZ() {
        return zMin;
    }

    /**
     * Gets the max Z.
     *
     * @return max Z.
     */
    public int getMaxZ() {
        return zMax;
    }

    /**
     * Returns true if the location is within this cuboid.
     *
     * @param location - Location to check.
     * @return true if the location is within this cuboid; false otherwise.
     */
    public boolean isIn(final Location location) {
        return location.getWorld() == this.world && location.getBlockX() >= this.xMin && location.getBlockX() <= this.xMax &&
                location.getBlockY() >= this.yMin && location.getBlockY() <= this.yMax && location
                .getBlockZ() >= this.zMin && location.getBlockZ() <= this.zMax;
    }

    /**
     * Returns true if player is within this cuboid.
     *
     * @param player - Player to check.
     * @return true if a player is within this cuboid; false otherwise.
     */
    public boolean isIn(final Player player) {
        return this.isIn(player.getLocation());
    }

    /**
     * Returns true if the location is within this cuboid with a marge.
     *
     * @param location - Location to check.
     * @param marge    - Marge.
     * @return true if the location is within this cuboid; false otherwise.
     */
    public boolean isInWithMarge(final Location location, final double marge) {
        return location.getWorld() == this.world && location.getX() >= this.xMinCentered - marge &&
                location.getX() <= this.xMaxCentered + marge &&
                location.getY() >= this.yMinCentered - marge && location
                .getY() <= this.yMaxCentered + marge && location.getZ() >= this.zMinCentered - marge &&
                location.getZ() <= this.zMaxCentered + marge;
    }

    /**
     * Performs a block cloning from this cuboid to the destination.
     * <p>
     * <b>
     * Keep in mind that this method works the same as vanilla one.
     * Meaning cloning starts at min X, Y and Z and pastes relatively to their increment.
     * </b>
     *
     * @param location - Destination.
     * @param skipAir  - Should skip air blocks?
     */
    public void cloneBlocksTo(@Nonnull Location location, boolean skipAir) {
        final World world = location.getWorld();

        if (world == null) {
            throw new IllegalArgumentException("the world must be loaded");
        }

        final int destX = location.getBlockX();
        final int destY = location.getBlockY();
        final int destZ = location.getBlockZ();

        iterateBlocks(block -> {
            if (block.getType().isAir() && skipAir) {
                return;
            }

            final int x = block.getX();
            final int y = block.getY();
            final int z = block.getZ();

            final Block targetBlock = world.getBlockAt(destX + (x - xMin), destY + (y - yMin), destZ + (z - zMin));

            targetBlock.setType(block.getType(), false);
            targetBlock.setBlockData(block.getBlockData(), false);
        });
    }

    /**
     * Fills all the blocks within the cuboid with the given {@link BlockData}.
     *
     * @param blockData - Block data.
     * @param skipAir   - Should skip air?
     */
    public void fill(@Nonnull BlockData blockData, boolean skipAir) {
        iterateBlocks(block -> {
            if (block.getType().isAir() && skipAir) {
                return;
            }

            block.setBlockData(blockData, false);
        });
    }

    @Override
    public String toString() {
        return "[%s, %s, %s, %s, %s, %s]".formatted(
                getMinX(),
                getMinY(),
                getMinZ(),
                getMaxX(),
                getMaxY(),
                getMaxZ()
        );
    }

    protected void iterateBlocks(@Nonnull Consumer<Block> consumer) {
        for (int x = this.xMin; x <= this.xMax; ++x) {
            for (int y = this.yMin; y <= this.yMax; ++y) {
                for (int z = this.zMin; z <= this.zMax; ++z) {
                    final Block block = this.world.getBlockAt(x, y, z);

                    consumer.accept(block);
                }
            }
        }
    }

}

