package me.hapyl.eterna.module.util;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * A {@link BlockFace} extension.
 */
public enum Direction {

    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    EAST(1, 0, 0),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NONE(0, 0, 0);

    private final int[] offset;

    Direction(int x, int y, int z) {
        this.offset = new int[] { x, y, z };
    }

    /**
     * Gets the offset multiplied by the magnitude of the given index.
     *
     * @param index     - Index.
     * @param magnitude - Magnitude
     * @return the offset multiplied by the magnitude.
     */
    public double getOffset(int index, double magnitude) {
        index = Math.clamp(index, 0, offset.length - 1);

        final int value = offset[index];
        return value * magnitude;
    }

    /**
     * Returns the given value if the offset at the given index is not <code>0</code>.
     *
     * @param index - Index.
     * @param value - Value
     * @param def   - Default.
     * @return the value if the offset at the given index is not <code>0</code>, def otherwise.
     */
    public double getValue(int index, double value, double def) {
        final int val = offset[index];

        if (val == 0) {
            return def;
        }

        return value;
    }

    /**
     * Modifies the given {@link Location} with offset multiplied by the distance.
     * <br>
     * This method modifies the original location before returning it to the original values:
     * <pre><code>
     *     location.add(x, y, z);
     *     consumer.accept(location);
     *     location.subtract(x, y, z);
     * </code></pre>
     *
     * @param location - Location.
     * @param distance - Distance.
     * @param consumer - Consumer of the modified location.
     */
    public void modifyLocation(@Nonnull Location location, double distance, @Nonnull Consumer<Location> consumer) {
        final double x = offset[0] * distance;
        final double y = offset[1] * distance;
        final double z = offset[2] * distance;

        location.add(x, y, z);
        consumer.accept(location);
        location.subtract(x, y, z);
    }

    /**
     * Gets the offsets of this {@link Direction}.
     *
     * @return the offsets of this direction.
     */
    public int[] getOffset() {
        return CollectionUtils.arrayCopy(offset);
    }

    /**
     * Gets the opposite {@link Direction}.
     *
     * @return the opposite direction
     */
    @Nonnull
    public Direction getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
            case UP -> DOWN;
            case DOWN -> UP;
            default -> NONE;
        };
    }

    /**
     * Gets the {@link BlockFace}.
     *
     * @return the block face.
     */
    @Nonnull
    public BlockFace getBlockFace() {
        return switch (this) {
            case NORTH -> BlockFace.NORTH;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case EAST -> BlockFace.EAST;
            case UP -> BlockFace.UP;
            case DOWN -> BlockFace.DOWN;
            default -> BlockFace.SELF;
        };
    }

    /**
     * Gets the opposite {@link BlockFace}.
     *
     * @return the opposite block face.
     */
    @Nonnull
    public BlockFace getOppositeBlockFace() {
        return getBlockFace().getOppositeFace();
    }

    /**
     * Returns true if this {@link Direction} points to the north.
     *
     * @return true if this direction points to the north.
     */
    public boolean isNorth() {
        return this == NORTH;
    }

    /**
     * Returns true if this {@link Direction} points to the south.
     *
     * @return true if this direction points to the south.
     */
    public boolean isSouth() {
        return this == SOUTH;
    }

    /**
     * Returns true if this {@link Direction} points to the west.
     *
     * @return true if this direction points to the west.
     */
    public boolean isWest() {
        return this == WEST;
    }

    /**
     * Returns true if this {@link Direction} points to the east.
     *
     * @return true if this direction points to the east.
     */
    public boolean isEast() {
        return this == EAST;
    }

    /**
     * Returns true if this {@link Direction} points up.
     *
     * @return true if this direction points up.
     */
    public boolean isUp() {
        return this == UP;
    }

    /**
     * Returns true if this {@link Direction} points down.
     *
     * @return true if this direction points down.
     */
    public boolean isDown() {
        return this == DOWN;
    }

    /**
     * Returns true if this {@link Direction} points to the west or east.
     *
     * @return true if this direction points to the west or east.
     */
    public boolean isAxisX() {
        return this == WEST || this == EAST;
    }

    /**
     * Returns true if this {@link Direction} points up or down.
     *
     * @return true if this direction points up or down.
     */
    public boolean isAxisY() {
        return this == UP || this == DOWN;
    }

    /**
     * Returns true if this {@link Direction} points to the north or south.
     *
     * @return true if this direction points to the north or south.
     */
    public boolean isAxisZ() {
        return this == NORTH || this == SOUTH;
    }

    /**
     * Returns true if this {@link Direction} points to the north or south.
     *
     * @return true if this direction points to the north or south.
     */
    public boolean isNorthOrSouth() {
        return isNorth() || isSouth();
    }

    /**
     * Returns true if this {@link Direction} points to the west or east.
     *
     * @return true if this direction points to the west or east.
     */
    public boolean isWestOrEast() {
        return isEast() || isWest();
    }

    /**
     * Returns true if this {@link Direction} points up or down.
     *
     * @return true if this direction points up or down.
     */
    public boolean isUpOrDown() {
        return this == UP || this == DOWN;
    }

    /**
     * Converts this {@link Direction} into a {@link EulerAngle}.
     * <p>
     * <b>This assumes that the armor stand is looking at south (default spawn direction)</b>
     *
     * @return the EulerAngle for this direction.
     */
    @Nonnull
    public EulerAngle toEulerAngle() {
        return switch (this) {
            case UP -> new EulerAngle(Math.toRadians(90), 0, 0);
            case DOWN -> new EulerAngle(Math.toRadians(-90), 0, 0);
            case SOUTH -> new EulerAngle(0, Math.toRadians(180), 0);
            case WEST -> new EulerAngle(0, Math.toRadians(90), 0);
            case EAST -> new EulerAngle(0, Math.toRadians(-90), 0);
            default -> new EulerAngle(0, 0, 0);
        };
    }

    /**
     * Converts this {@link Direction} into a {@link Transformation}.
     *
     * @return transformation.
     */
    @Nonnull
    public Transformation toTransformation() {
        return new Transformation(
                new Vector3f(0, 0, 0),
                new Quaternionf(),
                new Vector3f(1, 1, 1),
                new Quaternionf()
        );
    }

    /**
     * Gets the {@link Direction} that points towards positive X.
     *
     * @return the direction that points towards positive X.
     */
    @Nonnull
    public static Direction positiveX() {
        return EAST;
    }

    /**
     * Gets the {@link Direction} that points towards negative X.
     *
     * @return the direction that points towards negative X.
     */
    @Nonnull
    public static Direction negativeX() {
        return WEST;
    }

    /**
     * Gets the {@link Direction} that points towards positive Y.
     *
     * @return the direction that points towards positive Y.
     */
    @Nonnull
    public static Direction positiveY() {
        return UP;
    }

    /**
     * Gets the {@link Direction} that points towards negative Y.
     *
     * @return the direction that points towards negative Y.
     */
    @Nonnull
    public static Direction negativeY() {
        return DOWN;
    }

    /**
     * Gets the {@link Direction} that points towards positive Z.
     *
     * @return the direction that points towards positive Z.
     */
    @Nonnull
    public static Direction positiveZ() {
        return SOUTH;
    }

    /**
     * Gets the {@link Direction} that points towards negative Z.
     *
     * @return the direction that points towards negative Z.
     */
    @Nonnull
    public static Direction negativeZ() {
        return NORTH;
    }

    /**
     * Gets the {@link Direction} from the given {@link Location}.
     *
     * @param location - Location.
     * @return the direction from location's yaw.
     */
    @Nonnull
    public static Direction getDirection(@Nonnull Location location) {
        return getDirection(location.getYaw());
    }

    /**
     * Gets the {@link Direction} from the given <code>yaw</code>.
     *
     * @param yaw - Yaw.
     * @return the direction from the given yaw.
     */
    @Nonnull
    public static Direction getDirection(float yaw) {
        yaw = yaw < 0 ? yaw + 360 : yaw;

        if (yaw >= 315 || yaw < 45) {
            return SOUTH;
        }
        else if (yaw < 135) {
            return WEST;
        }
        else if (yaw < 225) {
            return NORTH;
        }
        else if (yaw < 315) {
            return EAST;
        }
        return NORTH;
    }

}
