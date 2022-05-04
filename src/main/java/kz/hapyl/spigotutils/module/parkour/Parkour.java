package kz.hapyl.spigotutils.module.parkour;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.hologram.Hologram;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Parkour implements Startable<Player>, Finishable<Player> {

    private static final Location DEFAULT_QUIT_LOCATION;

    static {
        final World world = Bukkit.getWorlds().get(0);
        DEFAULT_QUIT_LOCATION = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 1, 0);
    }

    private final Set<Hologram> holograms;
    private final String name;
    private final Position start;
    private final Position finish;
    private final List<Position> checkpoints;
    private final Set<FailType> allowedFails;

    private Difficulty difficulty;
    private Location quitLocation;
    private boolean silent;
    private boolean spawnHolograms;

    public Parkour(String name, Position start, Position finish) {
        Validate.isTrue(start.isStartOrFinish(), "must be start, not checkpoint!");
        Validate.isTrue(finish.isStartOrFinish(), "must be finish, not checkpoint!");
        this.name = name;
        this.start = start;
        this.finish = finish;
        this.holograms = Sets.newHashSet();
        this.difficulty = Difficulty.NORMAL;
        this.quitLocation = DEFAULT_QUIT_LOCATION;
        this.checkpoints = Lists.newArrayList();
        this.allowedFails = Sets.newHashSet();
        this.silent = false; // silent parkours will not send messages nor play sfx
        this.spawnHolograms = true;
    }

    public Parkour(String name, Location start, Location finish) {
        this(
                name,
                new Position(Position.Type.START_OR_FINISH, start),
                new Position(Position.Type.START_OR_FINISH, finish)
        );
    }

    @Override
    public void start(Player player) {
        SpigotUtilsPlugin.getPlugin().getParkourManager().startParkour(player, this);
    }

    @Override
    public void finish(Player player) {
        SpigotUtilsPlugin.getPlugin().getParkourManager().finishParkour(player);
    }

    public Location getQuitLocation() {
        return quitLocation;
    }

    public boolean isFailAllowed(FailType type) {
        return allowedFails.contains(type);
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public void setSpawnHolograms(boolean spawnHolograms) {
        this.spawnHolograms = spawnHolograms;
    }

    public boolean isSpawnHolograms() {
        return spawnHolograms;
    }

    public void addAllowedFail(FailType type) {
        allowedFails.add(type);
    }

    public void removeAllowedFail(FailType type) {
        allowedFails.remove(type);
    }

    public void setQuitLocation(Location quitLocation) {
        this.quitLocation = quitLocation;
    }

    public void removeHolograms() {
        this.holograms.forEach(Hologram::destroy);
        this.holograms.clear();
    }

    public void createHolograms() {
        createHologram("&6&l" + name, "&aStart").create(this.getStart().toLocation(0.5d, -2.0d, 0.5d));
        createHologram("&6&l" + name, "&aFinish").create(this.getFinish().toLocation(0.5d, -2.0d, 0.5d));

        for (int i = 0; i < checkpoints.size(); i++) {
            final Position current = checkpoints.get(i);
            createHologram("&aCheckpoint (%s/%s)".formatted(i + 1, checkpoints.size())).create(current.toLocation(
                    0.5d,
                    -2.0d,
                    0.5d
            ));
        }

        showHolograms();
    }

    public void showHolograms(Player player) {
        this.holograms.forEach(holo -> {
            holo.show(player);
        });
    }

    public void hideHolograms(Player player) {
        this.holograms.forEach(holo -> {
            holo.hide(player);
        });
    }

    public void showHolograms() {
        this.holograms.forEach(Hologram::showAll);
    }

    public void hideHolograms() {
        this.holograms.forEach(Hologram::hide);
    }

    private Hologram createHologram(String... strings) {
        final Hologram hologram = new Hologram().addLines(strings);
        this.holograms.add(hologram);
        return hologram;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void addCheckpoint(Location location) {
        this.checkpoints.add(new Position(Position.Type.CHECKPOINT, location));
    }

    public void addCheckpoint(World world, int x, int y, int z, float yaw, float pitch) {
        this.checkpoints.add(new Position(Position.Type.CHECKPOINT, world, x, y, z, yaw, pitch));
    }

    public Position getStart() {
        return start;
    }

    public Position getFinish() {
        return finish;
    }

    public List<Position> getCheckpoints() {
        return checkpoints;
    }

    public void spawnWorldEntities() {
        this.start.setBlock();
        this.finish.setBlock();
        this.checkpoints.forEach(Position::setBlock);
        if (spawnHolograms) {
            createHolograms();
        }
    }

    public void removeWorldEntities() {
        this.start.restoreBlock();
        this.finish.restoreBlock();
        this.checkpoints.forEach(Position::restoreBlock);
        removeHolograms();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parkour parkour = (Parkour) o;
        return Objects.equals(name, parkour.name) && start.compare(parkour.start) && Objects.equals(
                finish,
                parkour.finish
        ) && Objects.equals(difficulty, parkour.difficulty) && Objects.equals(
                checkpoints,
                parkour.checkpoints
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, start, finish, difficulty, checkpoints);
    }

    public boolean isCheckpoint(Location clickedBlockLocation) {
        for (Position checkpoint : getCheckpoints()) {
            if (checkpoint.compare(clickedBlockLocation)) {
                return true;
            }
        }
        return false;
    }
}
