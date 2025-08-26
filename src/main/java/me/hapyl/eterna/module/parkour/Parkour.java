package me.hapyl.eterna.module.parkour;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.util.list.StringList;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Named;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a parkour course.
 * <p>Remember to register the parkour!</p>
 *
 * @see me.hapyl.eterna.builtin.manager.ParkourManager#register(Parkour)
 */
public class Parkour implements Keyed, Named {
    
    public static final Material blockStartFinish;
    public static final Material blockCheckpoint;
    
    public static final PlayerTeleportEvent.TeleportCause reversedTeleportCauseForCheckpoint;
    
    private static final Location defaultQuitLocation;
    private static final int hologramViewDistance = 576;
    
    static {
        blockStartFinish = Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
        blockCheckpoint = Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
        
        reversedTeleportCauseForCheckpoint = PlayerTeleportEvent.TeleportCause.UNKNOWN;
        
        defaultQuitLocation = BukkitUtils.defLocation(0, BukkitUtils.defWorld().getHighestBlockYAt(0, 0) + 1, 0);
    }
    
    private final Key key;
    private final String name;
    private final ParkourPosition start;
    private final ParkourPosition finish;
    
    private final List<ParkourPosition> checkpoints;
    private final Set<Hologram> holograms;
    private final Set<FailType> allowedFails;
    
    private ParkourFormatter formatter;
    private Difficulty difficulty;
    private Location quitLocation;
    
    public Parkour(@Nonnull Key key, @Nonnull String name, @Nonnull ParkourPosition start, @Nonnull ParkourPosition finish) {
        this.key = key;
        this.name = name;
        this.start = start;
        this.finish = finish;
        this.checkpoints = Lists.newLinkedList();
        this.holograms = Sets.newHashSet();
        this.allowedFails = Sets.newHashSet();
        this.formatter = ParkourFormatter.DEFAULT;
        this.difficulty = Difficulty.NORMAL;
        this.quitLocation = defaultQuitLocation;
    }
    
    /**
     * Starts this parkour for the given player.
     *
     * @param player - The player who starts the parkour.
     */
    public void start(@Nonnull Player player) {
        Eterna.getManagers().parkour.start(player, this);
    }
    
    /**
     * Gets the current {@link ParkourFormatter}.
     *
     * @return the current {@link ParkourFormatter}.
     */
    @Nonnull
    public ParkourFormatter getFormatter() {
        return formatter;
    }
    
    /**
     * Sets the {@link ParkourFormatter}.
     *
     * @param formatter - The new formatter.
     */
    public void setFormatter(@Nonnull ParkourFormatter formatter) {
        this.formatter = Objects.requireNonNull(formatter, "Formatter must not be null!");
    }
    
    /**
     * Gets the quit location of this parkour.
     * <p>The location is used to teleport player upon quitting/finishing the parkour.</p>
     *
     * @return the quit location of this parkour.
     */
    @Nonnull
    public Location getQuitLocation() {
        return quitLocation;
    }
    
    /**
     * Sets the quit location of this parkour.
     * <p>The location is used to teleport player upon quitting/finishing the parkour.</p>
     *
     * @param quitLocation - The new quit location.
     */
    public void setQuitLocation(@Nonnull Location quitLocation) {
        this.quitLocation = quitLocation;
    }
    
    /**
     * Gets whether the given {@link FailType} is allowed by this parkour.
     * <p>This is really only useful for {@link FailType#EFFECT_CHANGE} when you want to add something along the lines of Jump Boost parkour.</p>
     *
     * @param type - The fail type to check.
     * @return {@code true} if the given {@link FailType} is permitted; {@code false} otherwise.
     */
    public boolean isFailAllowed(@Nonnull FailType type) {
        return allowedFails.contains(type);
    }
    
    /**
     * Sets the {@link FailType} that are permissted by this parkour.
     *
     * @param fails - The fails to set.
     */
    public void setAllowedFails(@Nonnull FailType... fails) {
        Validate.isTrue(fails.length > 0, "There must be at least one fail type!");
        
        allowedFails.clear();
        allowedFails.addAll(List.of(fails));
    }
    
    /**
     * Gets the text of the 'start' hologram.
     *
     * @return the text of the 'start' hologram.
     */
    @Nonnull
    public StringList hologramTextStart() {
        return StringList.of("&6&l" + name, "&aStart");
    }
    
    /**
     * Gets the text of the 'finish' hologram.
     *
     * @return the text of the 'finish' hologram.
     */
    @Nonnull
    public StringList hologramTextFinish() {
        return StringList.of("&6&l" + name, "&aFinish");
    }
    
    /**
     * Gets the text of the 'checkpoint' hologram.
     *
     * @param nth - The index of the checkpoint, starting at {@code 1}.
     * @param max - The max number of checkpoint.
     * @return the text of the 'checkpoint' hologram.
     */
    @Nonnull
    public StringList hologramTextCheckpoint(int nth, int max) {
        return StringList.of("&aCheckpoint &7(%s/%s)".formatted(nth, max));
    }
    
    /**
     * Creates the holograms for 'start', 'finish' and checkpoints.
     */
    public void createHolograms() {
        makeHologram(start.getLocationCentered(), hologramTextStart());
        makeHologram(finish.getLocationCentered(), hologramTextFinish());
        
        final int checkpointsSize = checkpoints.size();
        
        for (int i = 0; i < checkpointsSize; i++) {
            final ParkourPosition current = checkpoints.get(i);
            
            makeHologram(current.getLocationCentered(), hologramTextCheckpoint(i + 1, checkpointsSize));
        }
    }
    
    /**
     * Shows the holograms for the given player.
     *
     * @param player - The player to show for.
     */
    public void showHolograms(@Nonnull Player player) {
        this.holograms.forEach(holo -> holo.show(player));
    }
    
    /**
     * Hides the holograms for the given player.
     *
     * @param player - The player to hide for.
     */
    public void hideHolograms(@Nonnull Player player) {
        this.holograms.forEach(holo -> holo.hide(player));
    }
    
    /**
     * Gets the {@link Difficulty} of this parkour.
     *
     * @return the {@link Difficulty} of this parkour.
     */
    @Nonnull
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    
    /**
     * Sets the {@link Difficulty} of this parkour.
     *
     * @param difficulty - The new difficulty.
     */
    public void setDifficulty(@Nonnull Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    /**
     * Gets the name of this parkour.
     *
     * @return the name of this parkour.
     */
    @Nonnull
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Adds a checkpoint for this parkour.
     *
     * @param position - The checkpoint position.
     */
    public void addCheckpoint(@Nonnull ParkourPosition position) {
        this.checkpoints.stream()
                        .filter(position::equals)
                        .findFirst()
                        .ifPresent(e -> {
                            throw new IllegalArgumentException("Parkour already exists on position: %s!".formatted(position));
                        });
        
        this.checkpoints.add(position);
    }
    
    /**
     * Adds a checkpoint for this parkour.
     *
     * @param location - The location of the checkpoint.
     */
    public void addCheckpoint(@Nonnull Location location) {
        this.addCheckpoint(ParkourPosition.of(location));
    }
    
    /**
     * Adds a checkpoint for this parkour.
     *
     * @param world - The world.
     * @param x     - The {@code X} coordinate.
     * @param y     - The {@code Y} coordinate.
     * @param z     - The {@code Z} coordinate.
     * @param yaw   - The yaw.
     * @param pitch - The pitch.
     */
    public void addCheckpoint(@Nonnull World world, int x, int y, int z, float yaw, float pitch) {
        this.addCheckpoint(ParkourPosition.of(world, x, y, z, yaw, pitch));
    }
    
    /**
     * Adds a checkpoint for this parkour.
     * <p>This method uses the <i>default</i> world.</p>
     *
     * @param x     - The {@code X} coordinate.
     * @param y     - The {@code Y} coordinate.
     * @param z     - The {@code Z} coordinate.
     * @param yaw   - The yaw.
     * @param pitch - The pitch.
     */
    public void addCheckpoint(int x, int y, int z, float yaw, float pitch) {
        this.addCheckpoint(ParkourPosition.of(BukkitUtils.defWorld(), x, y, z, yaw, pitch));
    }
    
    /**
     * Gets the 'start' of this parkour.
     *
     * @return the 'start' of this parkour.
     */
    @Nonnull
    public ParkourPosition getStart() {
        return start;
    }
    
    /**
     * Gets the 'finish' of this parkour.
     *
     * @return the 'finish' of this parkour.
     */
    @Nonnull
    public ParkourPosition getFinish() {
        return finish;
    }
    
    /**
     * Gets a <b>mutable</b> copy of all the checkpoints.
     *
     * @return a <b>mutable</b> copy of all the checkpoints.
     */
    @Nonnull
    public LinkedList<ParkourPosition> getCheckpoints() {
        return Lists.newLinkedList(checkpoints);
    }
    
    public int checkpointCount() {
        return checkpoints.size();
    }
    
    public boolean isCheckpoint(Location location) {
        for (ParkourPosition checkpoint : getCheckpoints()) {
            if (checkpoint.compare(location)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Spawns all world entities, which includes pressure plates and holograms.
     */
    public void createWorldEntities() {
        this.start.setBlock(blockStartFinish);
        this.finish.setBlock(blockStartFinish);
        
        this.checkpoints.forEach(position -> position.setBlock(blockCheckpoint));
        
        this.createHolograms();
    }
    
    /**
     * Removes all world entities, which includes pressure plates and holograms.
     */
    public void removeWorldEntities() {
        this.start.restoreBlock();
        this.finish.restoreBlock();
        
        this.checkpoints.forEach(ParkourPosition::restoreBlock);
        
        this.holograms.forEach(Hologram::destroy);
    }
    
    @Nonnull
    public String formatTime(long millis) {
        if (millis >= 3600000) {
            return new SimpleDateFormat("HH:mm:ss.SSS").format(millis);
        }
        else if (millis >= 60000) {
            return new SimpleDateFormat("mm:ss.SSS").format(millis);
        }
        else {
            return new SimpleDateFormat("ss.SSS").format(millis);
        }
    }
    
    @Nonnull
    @Override
    public final Key getKey() {
        return key;
    }
    
    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final Parkour that = (Parkour) o;
        return Objects.equals(this.key, that.key);
    }
    
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.key);
    }
    public void tickHolograms() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            final Location location = player.getLocation();
            
            for (Hologram hologram : holograms) {
                final double distanceSquared = hologram.getLocation().distanceSquared(location);
                final boolean canBeSeen = distanceSquared <= hologramViewDistance;
                final boolean showingTo = hologram.isShowingTo(player);
                
                if (canBeSeen && !showingTo) {
                    hologram.show(player);
                }
                else if (!canBeSeen && showingTo) {
                    hologram.hide(player);
                }
            }
        });
    }
    
    private void makeHologram(Location location, StringList text) {
        final Hologram hologram = Hologram.ofArmorStand(location);
        hologram.setLines(text);
        
        this.holograms.add(hologram);
    }
}
