package me.hapyl.spigotutils.module.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Position {

    private final Type type;
    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private final float yaw;
    private final float pitch;

    private Material oldBlock;

    public Position(Type type, World world, int x, int y, int z, float yaw, float pitch) {
        this.type = type;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Position(World world, int x, int y, int z, float yaw, float pitch) {
        this(Type.START_OR_FINISH, world, x, y, z, yaw, pitch);
    }

    public Position(Type type, World world, int x, int y, int z) {
        this(type, world, x, y, z, 0.0f, 0.0f);
    }

    public Position(World world, int x, int y, int z) {
        this(Type.START_OR_FINISH, world, x, y, z, 0.0f, 0.0f);
    }

    public Position(Type type, Location location) {
        this(
                type,
                location.getWorld(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public Position(Location location) {
        this(
                Type.START_OR_FINISH,
                location.getWorld(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public boolean isStartOrFinish() {
        return !isCheckpoint();
    }

    public boolean isCheckpoint() {
        return this.type == Type.CHECKPOINT;
    }

    public Type getType() {
        return type;
    }

    public Location getLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location toLocationCentered() {
        return toLocation(0.5d, 0.0d, 0.5d);
    }

    public Location toLocation(double x, double y, double z) {
        final Location location = getLocation();
        location.add(x, y, z);
        return location;
    }

    public void restoreBlock() {
        if (oldBlock == null) {
            return;
        }
        getLocation().getBlock().setType(oldBlock, false);
    }

    public void setBlock() {
        final Location location = getLocation();
        oldBlock = location.getBlock().getType();
        location.getBlock().setType(this.type.material(), false);
    }

    public boolean compare(Location location) {
        return location.getBlockX() == this.x && location.getBlockY() == this.y && location.getBlockZ() == this.z;
    }

    public boolean compare(Position position) {
        return position.x == this.x && position.y == this.y && position.z == this.z;
    }

    public enum Type {
        START_OR_FINISH, CHECKPOINT;

        public Material material() {
            return this == CHECKPOINT ? Material.HEAVY_WEIGHTED_PRESSURE_PLATE : Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
        }

    }

}
