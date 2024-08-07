package me.hapyl.eterna.module.parkour;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Parkour implements Startable<Player>, Finishable<Player> {

    private static final Location DEFAULT_QUIT_LOCATION;

    private final Set<Hologram> holograms;
    private final String name;
    private final Position start;
    private final Position finish;
    private final List<Position> checkpoints;
    private final Set<FailType> allowedFails;

    @Nonnull private ParkourFormatter formatter;
    private Difficulty difficulty;
    private Location quitLocation;
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
        this.formatter = ParkourFormatter.DEFAULT;
        this.spawnHolograms = true;
    }

    public Parkour(String name, Location start, Location finish) {
        this(name, new Position(Position.Type.START_OR_FINISH, start), new Position(Position.Type.START_OR_FINISH, finish));
    }

    @Override
    public void start(Player player) {
        Eterna.getRegistry().parkourRegistry.startParkour(player, this);
    }

    @Override
    public void finish(Player player) {
        Eterna.getRegistry().parkourRegistry.finishParkour(player);
    }

    /**
     * Changes parkour formatter.
     *
     * @param formatter - New formatter. Use {@link ParkourFormatter#EMPTY} to clear formatter.
     */
    public void setFormatter(@Nonnull ParkourFormatter formatter) {
        Validate.notNull(formatter, "formatter cannot be null, use ParkourFormatter.EMPTY to remove formatter");
        this.formatter = formatter;
    }

    /**
     * Gets current parkour formatter.
     *
     * @return current parkour formatter.
     * @throws NullPointerException if formatter is null.
     */
    @Nonnull
    public ParkourFormatter getFormatter() {
        Validate.notNull(formatter, "formatter cannot be null");
        return formatter;
    }

    /**
     * Gets quit location of this parkour. Quit location is used to teleport
     * player upon finishing or quitting the parkour.
     *
     * @return quit the location of this parkour.
     */
    public Location getQuitLocation() {
        return quitLocation;
    }

    /**
     * Returns true, if specific fail is allows for this parkour.
     *
     * @param type - Fail type.
     * @return true, if specific fail is allows for this parkour.
     */
    public boolean isFailAllowed(FailType type) {
        return allowedFails.contains(type);
    }

    /**
     * Makes this parkour use empty formatter.
     *
     * @param silent - redundant.
     */
    public void setSilent(boolean silent) {
        setFormatter(ParkourFormatter.EMPTY);
    }

    /**
     * Changes if parkour should summon holograms upon load.
     *
     * @param spawnHolograms - true if parkour should summon holograms.
     */
    public void setSpawnHolograms(boolean spawnHolograms) {
        this.spawnHolograms = spawnHolograms;
    }

    /**
     * Returns if parkour should summon holograms upon load.
     *
     * @return if parkour should summon holograms upon load.
     */
    public boolean isSpawnHolograms() {
        return spawnHolograms;
    }

    /**
     * Adds a fail type to be allowed for this parkour.
     * Note that plugin must implement custom FailType.
     *
     * @param type - fail type.
     */
    public void addAllowedFail(FailType type) {
        allowedFails.add(type);
    }

    /**
     * Removes a fail type from allowed for this parkour.
     *
     * @param type - fail type.
     */
    public void removeAllowedFail(FailType type) {
        allowedFails.remove(type);
    }

    /**
     * Sets new quit location for this parkour. Quit location is used
     * to teleport player upon quit or finish.
     *
     * @param quitLocation - New quit location.
     */
    public void setQuitLocation(@Nonnull Location quitLocation) {
        this.quitLocation = quitLocation;
    }

    /**
     * Removes all holograms if spawned.
     */
    public void removeHolograms() {
        this.holograms.forEach(Hologram::destroy);
        this.holograms.clear();
    }

    /**
     * Creates holograms for this parkour. This is called
     * automatically on a load if {@link this#isSpawnHolograms()} is true.
     */
    public void createHolograms() {
        createHologram("&6&l" + name, "&aStart").create(this.getStart().toLocation(0.5d, 0.0d, 0.5d));
        createHologram("&6&l" + name, "&aFinish").create(this.getFinish().toLocation(0.5d, 0.0d, 0.5d));

        for (int i = 0; i < checkpoints.size(); i++) {
            final Position current = checkpoints.get(i);
            createHologram("&aCheckpoint (%s/%s)".formatted(i + 1, checkpoints.size())).create(current.toLocation(0.5d, 0.0d, 0.5d));
        }

        showHolograms();
    }

    /**
     * Shows spawned holograms for player.
     *
     * @param player - Player to show to.
     */
    public void showHolograms(Player player) {
        this.holograms.forEach(holo -> {
            holo.show(player);
        });
    }

    /**
     * Hides spawned holograms from player.
     *
     * @param player - Player to hide for.
     */
    public void hideHolograms(Player player) {
        this.holograms.forEach(holo -> {
            holo.hide(player);
        });
    }

    /**
     * Shows spawned holograms for each online player.
     */
    public void showHolograms() {
        this.holograms.forEach(Hologram::showAll);
    }

    /**
     * Hides spawned holograms for each online player.
     */
    public void hideHolograms() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            this.holograms.forEach(hologram -> hologram.hide(player));
        });
    }

    /**
     * Returns difficulty of this parkour.
     *
     * @return difficulty of this parkour.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets new difficulty for this parkour.
     *
     * @param difficulty - Difficulty.
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns name of parkour.
     *
     * @return name of parkour.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a checkpoint for parkour using bukkit location.
     *
     * @param location - location.
     */
    public void addCheckpoint(Location location) {
        this.checkpoints.add(new Position(Position.Type.CHECKPOINT, location));
    }

    /**
     * Adds a checkpoint for parkour using raw coordinates.
     *
     * @param world - World.
     * @param x     - X.
     * @param y     - Y.
     * @param z     - Z.
     * @param yaw   - Yaw.
     * @param pitch - Pitch.
     */
    public void addCheckpoint(World world, int x, int y, int z, float yaw, float pitch) {
        this.checkpoints.add(new Position(Position.Type.CHECKPOINT, world, x, y, z, yaw, pitch));
    }

    /**
     * Returns start position of parkour.
     *
     * @return start position of parkour.
     */
    public Position getStart() {
        return start;
    }

    /**
     * Returns finish position of parkour.
     *
     * @return finish position of parkour.
     */
    public Position getFinish() {
        return finish;
    }

    /**
     * Returns all checkpoint of parkour.
     * Might be empty.
     *
     * @return all checkpoints of parkour.
     */
    @Nonnull
    public List<Position> getCheckpoints() {
        return checkpoints;
    }

    /**
     * Checks if specific location is parkour checkpoint.
     *
     * @param location - Location.
     * @return if specific location is parkour checkpoint.
     */
    public boolean isCheckpoint(Location location) {
        for (Position checkpoint : getCheckpoints()) {
            if (checkpoint.compare(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Spawns all world entities, which includes pressure plates and holograms.
     */
    public void spawnWorldEntities() {
        this.start.setBlock();
        this.finish.setBlock();
        this.checkpoints.forEach(Position::setBlock);
        if (spawnHolograms) {
            createHolograms();
        }
    }

    /**
     * Removes all world entities, which includes pressure plates and holograms.
     */
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
        return Objects.equals(name, parkour.name) && start.compare(parkour.start) && Objects.equals(finish, parkour.finish) &&
                Objects.equals(difficulty, parkour.difficulty) && Objects.equals(checkpoints, parkour.checkpoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, start, finish, difficulty, checkpoints);
    }

    protected Hologram createHologram(String... strings) {
        final Hologram hologram = new Hologram().addLines(strings);
        this.holograms.add(hologram);
        return hologram;
    }

    static {
        final World world = Bukkit.getWorlds().get(0);
        DEFAULT_QUIT_LOCATION = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 1, 0);
    }

    public Set<Hologram> getHolograms() {
        return holograms;
    }
}
