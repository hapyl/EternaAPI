package me.hapyl.eterna.module.block;

import me.hapyl.eterna.module.annotate.DefensiveCopy;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.Runnables;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Create a visual bounding box between two points.
 *
 * <p>A bounding box size cannot exceed 48 blocks.</p>
 */
@TestedOn(version = Version.V1_21_11)
public class BlockOutline {
    
    /**
     * Defines the maximum size of the bounding box.
     */
    public static final int MAXIMUM_SIZE = 48;
    
    private final Player player;
    private Location start;
    private Location end;
    
    private BlockPos blockPos;
    
    /**
     * Creates a new {@link BlockOutline} for the given {@link Player}.
     *
     * @param player - The player for whom to create the outline.
     */
    public BlockOutline(@NotNull Player player) {
        this(player, null, null);
    }
    
    /**
     * Creates a new {@link BlockOutline} for the given {@link Player} with a start point.
     *
     * @param player - The player for whom to create the bounding box.
     * @param start  - The initial start location.
     */
    public BlockOutline(@NotNull Player player, @Nullable Location start) {
        this(player, start, null);
    }
    
    /**
     * Creates a new {@link BlockOutline} for the given {@link Player} with a start and end point.
     *
     * @param player - The player for whom to create the outline.
     * @param start  - The initial start location.
     * @param end    - The initial end location.
     */
    public BlockOutline(@NotNull Player player, @Nullable Location start, @Nullable Location end) {
        this.player = player;
        this.start = start;
        this.end = end;
    }
    
    /**
     * Gets the {@link Player} this {@link BlockOutline} belongs to.
     *
     * @return the player this outline belongs to.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets a copy of the start {@link Location}.
     *
     * @return a copy of the start location, {@code null} if undefined.
     */
    @Nullable
    @DefensiveCopy
    public Location getStart() {
        return LocationHelper.copyOfNullable(start);
    }
    
    /**
     * Sets the start {@link Location}.
     *
     * @param start - The new start location.
     */
    public void setStart(@Nullable @DefensiveCopy Location start) {
        this.start = LocationHelper.copyOfNullable(start);
    }
    
    /**
     * Gets a copy of the end {@link Location}.
     *
     * @return a copy of the end location, {@code null} if undefined.
     */
    @Nullable
    @DefensiveCopy
    public Location getEnd() {
        return LocationHelper.copyOfNullable(end);
    }
    
    /**
     * Sets the end {@link Location}.
     *
     * @param end - The new start location.
     */
    public void setEnd(@Nullable @DefensiveCopy Location end) {
        this.end = LocationHelper.copyOfNullable(end);
    }
    
    /**
     * Gets whether this {@link BlockOutline} is defined, meaning both start and end locations are set.
     *
     * @return {@code true} if this outline is defined, {@code false} otherwise.
     */
    public boolean isDefined() {
        return this.start != null && this.end != null;
    }
    
    /**
     * Gets the minimum {@code X} of the {@link BlockOutline}, or {@code 0} if undefined.
     *
     * @return the minimum {@code X} of the {@link BlockOutline}, or {@code 0} if undefined.
     */
    public int getMinX() {
        if (!isDefined()) {
            return 0;
        }
        return (int) Math.round(Math.min(start.getX(), end.getX()));
    }
    
    /**
     * Gets the minimum {@code Y} of the {@link BlockOutline}, or {@code 0} if undefined.
     *
     * @return the minimum {@code Y} of the {@link BlockOutline}, or {@code 0} if undefined.
     */
    public int getMinY() {
        if (!isDefined()) {
            return 0;
        }
        return (int) Math.round(Math.min(start.getY(), end.getY()));
    }
    
    /**
     * Gets the minimum {@code Z} of the {@link BlockOutline}, or {@code 0} if undefined.
     *
     * @return the minimum {@code Z} of the {@link BlockOutline}, or {@code 0} if undefined.
     */
    public int getMinZ() {
        if (!isDefined()) {
            return 0;
        }
        return (int) Math.round(Math.min(start.getZ(), end.getZ()));
    }
    
    /**
     * Gets the maximum {@code X} of the {@link BlockOutline}, or {@code 0} if undefined.
     *
     * @return the maximum {@code X} of the {@link BlockOutline}, or {@code 0} if undefined.
     */
    public int getMaxX() {
        if (!isDefined()) {
            return 0;
        }
        
        return (int) Math.round(Math.max(start.getX(), end.getX()));
    }
    
    /**
     * Gets the maximum {@code Y} of the {@link BlockOutline}, or {@code 0} if undefined.
     *
     * @return the maximum {@code Y} of the {@link BlockOutline}, or {@code 0} if undefined.
     */
    public int getMaxY() {
        if (!isDefined()) {
            return 0;
        }
        
        return (int) Math.round(Math.max(start.getY(), end.getY()));
    }
    
    /**
     * Gets the maximum {@code Z} of the {@link BlockOutline}, or {@code 0} if undefined.
     *
     * @return the maximum {@code Z} of the {@link BlockOutline}, or {@code 0} if undefined.
     */
    public int getMaxZ() {
        if (!isDefined()) {
            return 0;
        }
        
        return (int) Math.round(Math.max(start.getZ(), end.getZ()));
    }
    
    /**
     * Gets the size of the {@code X} axi, or {@code 0} if undefined.
     *
     * @return the size of the {@code X} axi, or {@code 0} if undefined.
     */
    public int getSizeX() {
        if (!isDefined()) {
            return 0;
        }
        
        return (int) (Math.max(start.getX(), end.getX()) - Math.min(start.getX(), end.getX())) + 1;
    }
    
    /**
     * Gets the size of the {@code Y} axi, or {@code 0} if undefined.
     *
     * @return the size of the {@code Y} axi, or {@code 0} if undefined.
     */
    public int getSizeY() {
        if (!isDefined()) {
            return 0;
        }
        
        return (int) (Math.max(start.getY(), end.getY()) - Math.min(start.getY(), end.getY())) + 1;
    }
    
    /**
     * Gets the size of the {@code Z} axi, or {@code 0} if undefined.
     *
     * @return the size of the {@code Z} axi, or {@code 0} if undefined.
     */
    public int getSizeZ() {
        if (!isDefined()) {
            return 0;
        }
        
        return (int) (Math.max(start.getZ(), end.getZ()) - Math.min(start.getZ(), end.getZ())) + 1;
    }
    
    
    /**
     * Shows the {@link BlockOutline}.
     * <p>Note that this is hard limited to {@link #MAXIMUM_SIZE} due to structure block limitations.</p>
     *
     * @return {@code true} if successfully shown; {@code false} if the size is greater than then {@link #MAXIMUM_SIZE}.
     * @throws IllegalStateException if the outline is undefined.
     */
    public boolean show() {
        if (!isDefined()) {
            throw new IllegalStateException("Undefined outline.");
        }
        
        final int sizeX = getSizeX();
        final int sizeY = getSizeY();
        final int sizeZ = getSizeZ();
        
        if (sizeX > MAXIMUM_SIZE || sizeY > MAXIMUM_SIZE || sizeZ > MAXIMUM_SIZE) {
            return false;
        }
        
        this.hide();
        
        Runnables.later(() -> {
            final BlockState blockData = Blocks.STRUCTURE_BLOCK.defaultBlockState();
            final World world = start.getWorld();
            
            final int minX = getMinX();
            final int minY = Math.max(getMinY() - 10, world.getMinHeight());
            final int minZ = getMinZ();
            
            final int offset = getMinY() - minY;
            
            this.blockPos = new BlockPos(minX, minY, minZ);
            
            final CompoundTag nbt = new CompoundTag();
            nbt.putString("name", "bb:" + player.getName());
            nbt.putString("author", player.getName());
            nbt.putInt("posX", 0);
            nbt.putInt("posY", offset); // Compensate Y offset
            nbt.putInt("posZ", 0);
            nbt.putInt("sizeX", sizeX);
            nbt.putInt("sizeY", sizeY);
            nbt.putInt("sizeZ", sizeZ);
            nbt.putString("rotation", "NONE");
            nbt.putString("mirror", "NONE");
            nbt.putString("mode", "SAVE");
            nbt.putByte("ignoreEntities", (byte) 1);
            nbt.putByte("showboundingbox", (byte) 1);
            
            final ClientboundBlockEntityDataPacket packetEntityData = new ClientboundBlockEntityDataPacket(
                    blockPos,
                    BlockEntityType.STRUCTURE_BLOCK,
                    nbt
            );
            
            final ClientboundBlockUpdatePacket packetBlockData = new ClientboundBlockUpdatePacket(blockPos, blockData);
            
            Reflect.sendPacket(player, packetBlockData);
            Reflect.sendPacket(player, packetEntityData);
        }, 2L);
        
        return true;
    }
    
    /**
     * Hides the outline.
     */
    public void hide() {
        if (start == null || blockPos == null) {
            return;
        }
        
        final Location location = new Location(start.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
        
        player.sendBlockChange(location, location.getBlock().getBlockData());
    }
    
    @Nullable
    private Location getTargetLocation(Player player) {
        final Block blockExact = player.getTargetBlockExact(48);
        return blockExact == null ? null : blockExact.getLocation();
    }
    
}
