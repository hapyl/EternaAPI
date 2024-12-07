package me.hapyl.eterna.module.reflect;

import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.util.Runnables;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Create a visual bounding box between two points.
 * A bounding box size cannot exceed 48 blocks.
 */
@TestedOn(version = Version.V1_21_4)
public class BoundingBox {

    public static final net.minecraft.world.level.block.Block STRUCTURE_BLOCK = Blocks.STRUCTURE_BLOCK;
    public static final int MAX_DIST = 48;

    private final Player player;
    private Location start;
    private Location end;

    private Location fakeBlockLocation;

    public BoundingBox(@Nonnull Player player) {
        this(player, null, null);
    }

    public BoundingBox(@Nonnull Player player, @Nullable Location start) {
        this(player, start, null);
    }

    public BoundingBox(@Nonnull Player player, @Nullable Location start, @Nullable Location end) {
        this.player = player;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the player.
     *
     * @return the player.
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns start location if defined, null otherwise.
     *
     * @return start location if defined, null otherwise.
     */
    @Nullable
    public Location getStart() {
        return start;
    }

    /**
     * Sets the start location.
     *
     * @param start - New start location.
     */
    public void setStart(@Nullable Location start) {
        this.start = start;
    }

    /**
     * Sets the start location from the player's target block.
     *
     * @param player - Player.
     * @return true if location has been set, false otherwise.
     */
    public boolean setStart(Player player) {
        final Location targetLocation = getTargetLocation(player);
        if (targetLocation == null) {
            return false;
        }

        this.start = targetLocation;
        return true;
    }

    /**
     * Returns end location if defined, null otherwise.
     *
     * @return end location if defined, null otherwise.
     */
    @Nullable
    public Location getEnd() {
        return end;
    }

    /**
     * Sets the end location.
     *
     * @param end - New end location.
     */
    public void setEnd(@Nullable Location end) {
        this.end = end;
    }

    /**
     * Sets the end location from the player's target block.
     *
     * @param player - Player.
     * @return true if location has been set, false otherwise.
     */
    public boolean setEnd(Player player) {
        final Location targetLocation = getTargetLocation(player);
        if (targetLocation == null) {
            return false;
        }

        this.end = targetLocation;
        return true;
    }

    /**
     * Returns true if both start and end location are defined, false otherwise.
     *
     * @return true if both start and end location are defined, false otherwise.
     */
    public boolean isDefined() {
        return this.start != null && this.end != null;
    }

    /**
     * Returns the minimum X of the bounding box, or 0 if not {@link BoundingBox#isDefined()}
     *
     * @return the minimum X of the bounding box, or 0 if not {@link BoundingBox#isDefined()}
     */
    public int getMinX() {
        if (!isDefined()) {
            return 0;
        }
        return (int) Math.round(Math.min(start.getX(), end.getX()));
    }

    /**
     * Returns the minimum Y of the bounding box, or 0 if not {@link BoundingBox#isDefined()}
     *
     * @return the minimum Y of the bounding box, or 0 if not {@link BoundingBox#isDefined()}
     */
    public int getMinY() {
        if (!isDefined()) {
            return 0;
        }
        return (int) Math.round(Math.min(start.getY(), end.getY()));
    }

    /**
     * Returns the minimum Z of the bounding box, or 0 if not {@link BoundingBox#isDefined()}
     *
     * @return the minimum Z of the bounding box, or 0 if not {@link BoundingBox#isDefined()}
     */
    public int getMinZ() {
        if (!isDefined()) {
            return 0;
        }
        return (int) Math.round(Math.min(start.getZ(), end.getZ()));
    }

    /**
     * Returns the maximum X of the bounding box; or 0 if not {@link #isDefined()}
     *
     * @return the maximum X of the bounding box; or 0 if not {@link #isDefined()}
     */
    public int getMaxX() {
        if (!isDefined()) {
            return 0;
        }

        return (int) Math.round(Math.max(start.getX(), end.getX()));
    }

    /**
     * Returns the maximum Y of the bounding box; or 0 if not {@link #isDefined()}
     *
     * @return the maximum Y of the bounding box; or 0 if not {@link #isDefined()}
     */
    public int getMaxY() {
        if (!isDefined()) {
            return 0;
        }

        return (int) Math.round(Math.max(start.getY(), end.getY()));
    }

    /**
     * Returns the maximum Z of the bounding box; or 0 if not {@link #isDefined()}
     *
     * @return the maximum Z of the bounding box; or 0 if not {@link #isDefined()}
     */
    public int getMaxZ() {
        if (!isDefined()) {
            return 0;
        }

        return (int) Math.round(Math.max(start.getZ(), end.getZ()));
    }

    /**
     * Returns the size of the X axi, or 0 if not {@link BoundingBox#isDefined()}
     *
     * @return the size of the X axi, or 0 if not {@link BoundingBox#isDefined()}
     */
    public int getSizeX() {
        if (!isDefined()) {
            return 0;
        }

        return (int) (Math.max(start.getX(), end.getX()) - Math.min(start.getX(), end.getX())) + 1;
    }

    /**
     * Returns the size of the Y axis, or 0 if not {@link BoundingBox#isDefined()}
     *
     * @return the size of the Y axis, or 0 if not {@link BoundingBox#isDefined()}
     */
    public int getSizeY() {
        if (!isDefined()) {
            return 0;
        }

        return (int) (Math.max(start.getY(), end.getY()) - Math.min(start.getY(), end.getY())) + 1;
    }

    /**
     * Returns the size of the Z axi, or 0 if not {@link BoundingBox#isDefined()}
     *
     * @return the size of the Z axi, or 0 if not {@link BoundingBox#isDefined()}
     */
    public int getSizeZ() {
        if (!isDefined()) {
            return 0;
        }

        return (int) (Math.max(start.getZ(), end.getZ()) - Math.min(start.getZ(), end.getZ())) + 1;
    }


    /**
     * Draws a bounding box outline between two locations, if {@link BoundingBox#isDefined()}, does nothing otherwise.
     * <b>Note that this is limited to 48 blocks for each side due to structure block limitation. (duh)</b>
     *
     * @return true if an attempt of drawing was made, or false if size is greater than {@link BoundingBox#MAX_DIST} or {@link BoundingBox#isDefined()} is false.
     */
    public boolean show() {
        if (!isDefined()) {
            return false;
        }

        if (getSizeX() > MAX_DIST || getSizeY() > MAX_DIST || getSizeZ() > MAX_DIST) {
            return false;
        }

        hide();

        Runnables.runLater(() -> {
            final BlockState blockData = STRUCTURE_BLOCK.defaultBlockState();

            final int minX = getMinX();
            final int minY = getMinY() - 10; // -10 to offset structure block
            final int minZ = getMinZ();

            this.fakeBlockLocation = new Location(start.getWorld(), minX, minY, minZ);
            final BlockPos blockPosition = new BlockPos(minX, minY, minZ);
            final CompoundTag nbt = new CompoundTag();

            nbt.putString("name", "bb:" + player.getName());
            nbt.putString("author", player.getName());
            nbt.putInt("posX", 0);
            nbt.putInt("posY", 10); // compensate the block offset
            nbt.putInt("posZ", 0);
            nbt.putInt("sizeX", getSizeX());
            nbt.putInt("sizeY", getSizeY());
            nbt.putInt("sizeZ", getSizeZ());
            nbt.putString("rotation", "NONE");
            nbt.putString("mirror", "NONE");
            nbt.putString("mode", "SAVE");
            nbt.putByte("ignoreEntities", (byte) 1);
            nbt.putByte("showboundingbox", (byte) 1);

            final ClientboundBlockEntityDataPacket packetEntityData = new ClientboundBlockEntityDataPacket(
                    blockPosition,
                    BlockEntityType.STRUCTURE_BLOCK,
                    nbt
            );

            final ClientboundBlockUpdatePacket packetBlockData = new ClientboundBlockUpdatePacket(blockPosition, blockData);

            Reflect.sendPacket(player, packetBlockData);
            Reflect.sendPacket(player, packetEntityData);
        }, 2L);

        return true;
    }

    /**
     * Hides the bounding box selection.
     */
    public void hide() {
        if (fakeBlockLocation == null) {
            return;
        }
        fakeBlockLocation.getBlock().getState().update(false, false);
    }

    @Nullable
    private Location getTargetLocation(Player player) {
        final Block blockExact = player.getTargetBlockExact(48);
        return blockExact == null ? null : blockExact.getLocation();
    }

}
