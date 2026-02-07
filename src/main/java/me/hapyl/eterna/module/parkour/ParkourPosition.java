package me.hapyl.eterna.module.parkour;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.location.Located;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class ParkourPosition implements Located {
    
    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private final float yaw;
    private final float pitch;
    
    private final BlockData restoreBlock;
    
    public ParkourPosition(@NotNull World world, int x, int y, int z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.restoreBlock = getLocation().getBlock().getBlockData();
    }
    
    @NotNull
    @Override
    public Location getLocation() {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }
    
    @NotNull
    public Location getLocationCentered() {
        return getLocation().add(0.5, 0.0, 0.5);
    }
    
    public boolean compare(@NotNull Location location) {
        return location.getBlockX() == this.x && location.getBlockY() == this.y && location.getBlockZ() == this.z;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.world, this.x, this.y, this.z);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final ParkourPosition that = (ParkourPosition) o;
        return this.x == that.x && this.y == that.y && this.z == that.z && Objects.equals(this.world, that.world);
    }
    
    public void setBlock(@NotNull Material material) {
        getLocation().getBlock().setType(material, false);
    }
    
    public void restoreBlock() {
        getLocation().getBlock().setBlockData(restoreBlock, false);
    }
    
    @NotNull
    public static ParkourPosition of(@NotNull World world, int x, int y, int z) {
        return new ParkourPosition(world, x, y, z, 0, 0);
    }
    
    @NotNull
    public static ParkourPosition of(@NotNull World world, int x, int y, int z, float yaw, float pitch) {
        return new ParkourPosition(world, x, y, z, yaw, pitch);
    }
    
    @NotNull
    public static ParkourPosition of(@NotNull Location location) {
        return new ParkourPosition(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }
    
    @NotNull
    public static ParkourPosition.Builder builder(@NotNull ParkourPosition start, @NotNull ParkourPosition finish) {
        return new Builder(start, finish);
    }
    
    public static class Builder {
        
        protected final ParkourPosition start;
        protected final ParkourPosition finish;
        protected final List<ParkourPosition> checkpoints;
        
        Builder(@NotNull ParkourPosition start, @NotNull ParkourPosition finish) {
            this.start = start;
            this.finish = finish;
            this.checkpoints = Lists.newArrayList();
        }
        
        @SelfReturn
        public Builder checkpoint(@NotNull ParkourPosition position) {
            this.checkpoints.add(position);
            return this;
        }
        
        @NotNull
        public static ParkourPosition.Builder builder(@NotNull ParkourPosition start, @NotNull ParkourPosition finish) {
            return new Builder(start, finish);
        }
    }
}
