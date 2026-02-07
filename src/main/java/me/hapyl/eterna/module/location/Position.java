package me.hapyl.eterna.module.location;

import me.hapyl.eterna.module.math.Numbers;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Represents an axis-aligned bounding box (AABB) that may exist in a given {@link World}.
 *
 * @author <a href="https://www.spigotmc.org/threads/region-position.329859/">Tristiisch74</a>
 */
public class Position {
    
    private final World world;
    
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    
    /**
     * Creates a new {@link Position} in the {@link LocationHelper#defaultWorld()}.
     *
     * @param minX - The {@code minX} point.
     * @param minY - The {@code minY} point.
     * @param minZ - The {@code minZ} point.
     * @param maxX - The {@code maxX} point.
     * @param maxY - The {@code maxY} point.
     * @param maxZ - The {@code maxZ} point.
     */
    public Position(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this(LocationHelper.defaultWorld(), minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    /**
     * Creates a new {@link Position} in the given {@link World}.
     *
     * @param world - The world of the position.
     * @param minX  - The {@code minX} point.
     * @param minY  - The {@code minY} point.
     * @param minZ  - The {@code minZ} point.
     * @param maxX  - The {@code maxX} point.
     * @param maxY  - The {@code maxY} point.
     * @param maxZ  - The {@code maxZ} point.
     */
    public Position(@NotNull World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this(new Location(world, minX, minY, minZ), new Location(world, maxX, maxY, maxZ));
    }
    
    /**
     * Creates a new {@link Position} from the given {@link Location}.
     *
     * @param point1 - The first point.
     * @param point2 - The second point.
     * @throws IllegalArgumentException if locations world doesn't match.
     */
    public Position(final Location point1, final Location point2) {
        final World world1 = point1.getWorld();
        final World world2 = point2.getWorld();
        
        if (!world1.equals(world2)) {
            throw new IllegalArgumentException("Location worlds must match! (%s != %s)".formatted(world1, world2));
        }
        
        this.world = world1;
        
        this.minX = Math.min(point1.getBlockX(), point2.getBlockX());
        this.minY = Math.min(point1.getBlockY(), point2.getBlockY());
        this.minZ = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.maxX = Math.max(point1.getBlockX(), point2.getBlockX());
        this.maxY = Math.max(point1.getBlockY(), point2.getBlockY());
        this.maxZ = Math.max(point1.getBlockZ(), point2.getBlockZ());
    }
    
    /**
     * Gets a {@link List} of blocks within this {@link Position}.
     *
     * @return a list of blocks within this position.
     */
    @NotNull
    public List<Block> getBlocks() {
        return getBlocks(block -> true);
    }
    
    /**
     * Gets a {@link List} of blocks within this {@link Position} that match the given {@link Predicate}.
     *
     * @param predicate - The predicate to match.
     * @return a list of blocks within this position that match the given predicate.
     */
    @NotNull
    public List<Block> getBlocks(@NotNull Predicate<Block> predicate) {
        final List<Block> blocks = new ArrayList<>(getTotalBlockSize());
        
        for (int x = this.minX; x <= this.maxX; ++x) {
            for (int y = this.minY; y <= this.maxY; ++y) {
                for (int z = this.minZ; z <= this.maxZ; ++z) {
                    final Block block = this.world.getBlockAt(x, y, z);
                    
                    if (predicate.test(block)) {
                        blocks.add(block);
                    }
                }
            }
        }
        
        return blocks;
    }
    
    /**
     * Gets the centre {@link Location}.
     *
     * @return the centre location.
     */
    @NotNull
    public Location getCentre() {
        return new Location(
                this.world,
                this.minX + (this.maxX - this.minX) / 2.0,
                this.minY + (this.maxY - this.minY) / 2.0,
                this.minZ + (this.maxZ - this.minZ) / 2.0
        );
    }
    
    /**
     * Gets the distance between the two points.
     *
     * @return the distance between the two pints.
     */
    public double getDistance() {
        return Math.sqrt(getDistanceSquared());
    }
    
    /**
     * Gets the squared distance between the two points.
     *
     * @return the squared distance between the two points.
     */
    public double getDistanceSquared() {
        return Numbers.square(this.maxX - this.minX) + Numbers.square(this.maxY - this.minY) + Numbers.square(this.maxZ - this.minZ);
    }
    
    /**
     * Gets the height of this {@link Position}.
     *
     * @return the height of this position.
     */
    public int getHeight() {
        return this.maxY - this.minY + 1;
    }
    
    /**
     * Gets the first point of this {@link Position}.
     *
     * @return the first point of this {@link Position}.
     */
    @NotNull
    public Location getPoint1() {
        return new Location(this.world, this.minX, this.minY, this.minZ);
    }
    
    /**
     * Gets the second point of this {@link Position}.
     *
     * @return the second point of this {@link Position}.
     */
    @NotNull
    public Location getPoint2() {
        return new Location(this.world, this.maxX, this.maxY, this.maxZ);
    }
    
    /**
     * Gets a random {@link Location} within this {@link Position}.
     *
     * @return a random location within this position.
     */
    @NotNull
    public Location getRandomLocation() {
        final Random random = new Random();
        
        final int x = random.nextInt(Math.abs(this.maxX - this.minX) + 1) + this.minX;
        final int y = random.nextInt(Math.abs(this.maxY - this.minY) + 1) + this.minY;
        final int z = random.nextInt(Math.abs(this.maxZ - this.minZ) + 1) + this.minZ;
        
        return new Location(this.world, x, y, z);
    }
    
    /**
     * Gets the total number of {@link Block} within this {@link Position}.
     *
     * @return the total number of blocks within this position.
     */
    public int getTotalBlockSize() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }
    
    /**
     * Gets the {@code x} width of this {@link Position}.
     *
     * @return the {@code x} width of this position.
     */
    public int getXWidth() {
        return this.maxX - this.minX + 1;
    }
    
    /**
     * Gets the {@code z} width of this {@link Position}.
     *
     * @return the {@code z} width of this position.
     */
    public int getZWidth() {
        return this.maxZ - this.minZ + 1;
    }
    
    /**
     * Gets the {@code minX} of this {@link Position}.
     *
     * @return the {@code minX} of this position.
     */
    public int getMinX() {
        return this.minX;
    }
    
    /**
     * Gets the {@code minY} of this {@link Position}.
     *
     * @return the {@code minY} of this position.
     */
    public int getMinY() {
        return minY;
    }
    
    /**
     * Gets the {@code minZ} of this {@link Position}.
     *
     * @return the {@code minZ} of this position.
     */
    public int getMinZ() {
        return this.minZ;
    }
    
    /**
     * Gets the {@code maxX} of this {@link Position}.
     *
     * @return the {@code maxX} of this position.
     */
    public int getMaxX() {
        return this.maxX;
    }
    
    /**
     * Gets the {@code maxY} of this {@link Position}.
     *
     * @return the {@code maxY} of this position.
     */
    public int getMaxY() {
        return this.maxY;
    }
    
    /**
     * Gets the {@code maxZ} of this {@link Position}.
     *
     * @return the {@code maxZ} of this position.
     */
    public int getMaxZ() {
        return this.maxZ;
    }
    
    public int getCentreX() {
        return this.minX + (this.maxX - this.minX) / 2;
    }
    
    public int getCentreY() {
        return this.minY + (this.maxY - this.minY) / 2;
    }
    
    public int getCentreZ() {
        return this.minZ + (this.maxZ - this.minZ) / 2;
    }
    
    /**
     * Gets whether the given {@link Location} is within this {@link Position}.
     *
     * @param location - The location to check.
     * @return {@code true} if the given location is within this position; {@code false} otherwise.
     */
    public boolean contains(final Location location) {
        return location.getWorld() == this.world
               && location.getBlockX() >= this.minX
               && location.getBlockX() <= this.maxX
               && location.getBlockY() >= this.minY
               && location.getBlockY() <= this.maxY
               && location.getBlockZ() >= this.minZ
               && location.getBlockZ() <= this.maxZ;
    }
    
    /**
     * Gets whether the given {@link Player} is within this {@link Position}.
     *
     * @param player - The player to check.
     * @return {@code true} if the given player is within this position; {@code false} otherwise.
     */
    public boolean contains(final Player player) {
        return contains(player.getLocation());
    }
    
    /**
     * Clones all the blocks within this {@link Position} to the given {@link Location}.
     *
     * <p>Blocks are cloned starting at {@code minX, minY, minZ} and pasted preserving the same relative coordinates.</p>
     *
     * @param location - The location to clone the blocks to.
     * @param skipAir  - {@code true} to skip {@code air} blocks; {@code false} to clone {@code air} blocks.
     */
    public void cloneBlocksTo(@NotNull Location location, boolean skipAir) {
        final World world = location.getWorld();
        
        final int destinationX = location.getBlockX();
        final int destinationY = location.getBlockY();
        final int destinationZ = location.getBlockZ();
        
        getBlocks().forEach(block -> {
            if (block.getType().isAir() && skipAir) {
                return;
            }
            
            final int x = block.getX();
            final int y = block.getY();
            final int z = block.getZ();
            
            final Block targetBlock = world.getBlockAt(
                    destinationX + (x - minX),
                    destinationY + (y - minY),
                    destinationZ + (z - minZ)
            );
            
            targetBlock.setBlockData(block.getBlockData(), false);
        });
    }
    
    /**
     * Fills this {@link Position} with the given {@link BlockData}.
     *
     * @param blockData - The block to fill with.
     * @param skipAir   - {@code true} to skip {@code air} blocks; {@code false} to fill {@code air} blocks.
     */
    public void fill(@NotNull BlockData blockData, boolean skipAir) {
        getBlocks().forEach(block -> {
            if (block.getType().isAir() && skipAir) {
                return;
            }
            
            block.setBlockData(blockData, false);
        });
    }
    
    /**
     * Gets a {@link String} representation of this {@link Position}, following the pattern:
     *
     * <pre>{@code [minX, minY, minZ, maxX, maxY, maxZ]}</pre>
     *
     * @return a string representation of this position.
     */
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
    
    /**
     * Gets a {@link String} representation of this {@link Position}, following the pattern:
     *
     * <pre>{@code [centreX, centreY, centreZ]}</pre>
     *
     * @return a string representation of this position.
     */
    @NotNull
    public String toStringCentre() {
        return "[%s, %s, %s]".formatted(getCentreX(), getCentreY(), getCentreZ());
    }
    
}

