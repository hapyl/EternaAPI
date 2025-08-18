package me.hapyl.eterna.module.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Represents a {@link Parkour} position.
 */
public class ParkourPosition {
    
    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private final float yaw;
    private final float pitch;
    
    private final BlockData restoreBlock;
    
    public ParkourPosition(@Nonnull World world, int x, int y, int z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.restoreBlock = getLocation().getBlock().getBlockData();
    }
    
    @Nonnull
    public Location getLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    @Nonnull
    public Location getLocationCentered() {
        return getLocation().add(0.5, 0.0, 0.5);
    }
    
    public boolean compare(@Nonnull Location location) {
        return location.getBlockX() == this.x
               && location.getBlockY() == this.y
               && location.getBlockZ() == this.z;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final ParkourPosition that = (ParkourPosition) o;
        return this.x == that.x && this.y == that.y && this.z == that.z && Objects.equals(this.world, that.world);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.world, this.x, this.y, this.z);
    }
    
    public void setBlock(@Nonnull Material material) {
        getLocation().getBlock().setType(material, false);
    }
    
    public void restoreBlock() {
        getLocation().getBlock().setBlockData(restoreBlock, false);
    }
    
    @Override
    public String toString() {
        return "ParkourPosition{" +
               "world=" + world +
               ", x=" + x +
               ", y=" + y +
               ", z=" + z +
               ", yaw=" + yaw +
               ", pitch=" + pitch +
               '}';
    }
    
    @Nonnull
    public static ParkourPosition of(@Nonnull World world, int x, int y, int z) {
        return new ParkourPosition(world, x, y, z, 0, 0);
    }
    
    @Nonnull
    public static ParkourPosition of(@Nonnull World world, int x, int y, int z, float yaw, float pitch) {
        return new ParkourPosition(world, x, y, z, yaw, pitch);
    }
    
    @Nonnull
    public static ParkourPosition of(@Nonnull Location location) {
        return new ParkourPosition(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }
    
}
