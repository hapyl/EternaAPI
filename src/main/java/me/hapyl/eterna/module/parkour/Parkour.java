package me.hapyl.eterna.module.parkour;

import com.google.common.collect.Sets;
import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.annotate.DefensiveCopy;
import me.hapyl.eterna.module.annotate.RequiresVarargs;
import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.component.Named;
import me.hapyl.eterna.module.util.Ticking;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a {@link Parkour} course.
 */
public class Parkour implements Keyed, Named, Ticking {
    
    /**
     * Defines the {@link Material} used for start and finish pressure plates.
     */
    @NotNull
    public static final Material MATERIAL_START_AND_FINISH;
    
    /**
     * Defines the {@link Material} used for checkpoint pressure plates.
     */
    @NotNull
    public static final Material MATERIAL_CHECKPOINT;
    
    /**
     * Defines the {@link TeleportCause} which is ignored for {@link FailType#TELEPORT}.
     */
    @NotNull
    public static final PlayerTeleportEvent.TeleportCause RESERVED_TELEPORT_CAUSE;
    
    @NotNull
    private static final Location DEFAULT_QUIT_LOCATION;
    
    private static final int HOLOGRAM_VIEW_DISTANCE = 576;
    
    static {
        MATERIAL_START_AND_FINISH = Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
        MATERIAL_CHECKPOINT = Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
        
        RESERVED_TELEPORT_CAUSE = PlayerTeleportEvent.TeleportCause.UNKNOWN;
        
        DEFAULT_QUIT_LOCATION = LocationHelper.defaultLocation(0, LocationHelper.defaultWorld().getHighestBlockYAt(0, 0) + 1, 0);
    }
    
    protected final List<ParkourPosition> checkpoints;
    
    private final Key key;
    private final Component name;
    private final ParkourPosition start;
    private final ParkourPosition finish;
    private final Set<Hologram> holograms;
    private final Set<FailType> allowedFails;
    
    private ParkourFormatter formatter;
    private Difficulty difficulty;
    private Location quitLocation;
    
    public Parkour(@NotNull Key key, @NotNull Component name, @NotNull ParkourPosition.Builder positionBuilder) {
        this.key = key;
        this.name = name;
        this.start = positionBuilder.start;
        this.finish = positionBuilder.finish;
        this.checkpoints = List.copyOf(positionBuilder.checkpoints);
        this.holograms = Sets.newHashSet();
        this.allowedFails = Sets.newHashSet();
        this.formatter = ParkourFormatter.DEFAULT;
        this.difficulty = Difficulty.NORMAL;
        this.quitLocation = DEFAULT_QUIT_LOCATION;
        
        // Register the parkour
        ParkourHandler.handler.register(key, this);
        
        // Create entities
        this.createWorldEntities();
    }
    
    /**
     * Starts this {@link Parkour} for the given {@link Player}.
     *
     * @param player - The player for whom to start the parkour.
     */
    public void start(@NotNull Player player) {
        ParkourHandler.handler.start(player, this);
    }
    
    /**
     * Gets the current {@link ParkourFormatter}.
     *
     * @return the current parkour formatter.
     */
    @NotNull
    public ParkourFormatter getFormatter() {
        return formatter;
    }
    
    /**
     * Sets the {@link ParkourFormatter}.
     *
     * @param formatter - The new formatter.
     */
    public void setFormatter(@NotNull ParkourFormatter formatter) {
        this.formatter = Objects.requireNonNull(formatter, "Formatter must not be null!");
    }
    
    /**
     * Gets the quit {@link Location} of this {@link Parkour}, which is used to teleport the {@link Player} upon quiting the parkour.
     *
     * @return the quit location of this parkour.
     */
    @NotNull
    @DefensiveCopy
    public Location getQuitLocation() {
        return LocationHelper.copyOf(this.quitLocation);
    }
    
    /**
     * Sets the quit {@link Location} of this {@link Parkour}, which is used to teleport the {@link Player} upon quting the parkour.
     *
     * @param quitLocation - The new quit location.
     */
    public void setQuitLocation(@NotNull @DefensiveCopy Location quitLocation) {
        this.quitLocation = LocationHelper.copyOf(quitLocation);
    }
    
    /**
     * Gets whether the given {@link FailType} is permitted by this {@link Parkour}.
     *
     * @param type - The fail type to check.
     * @return {@code true} if the given fail type is permitted; {@code false} otherwise.
     */
    public boolean isFailAllowed(@NotNull FailType type) {
        return this.allowedFails.contains(type);
    }
    
    /**
     * Sets the {@link FailType} that are permitted by this {@link Parkour}.
     *
     * @param failTypes - The fail types to set.
     * @throws IllegalArgumentException if no varargs are provided.
     */
    public void setAllowedFails(@NotNull @RequiresVarargs FailType... failTypes) {
        this.allowedFails.clear();
        this.allowedFails.addAll(Arrays.asList(Validate.varargs(failTypes)));
    }
    
    /**
     * Gets the {@link ComponentList} used for {@code start} {@link Hologram}.
     *
     * @return the component list used for {@code start} hologram.
     */
    @NotNull
    public ComponentList hologramTextStart() {
        return ComponentList.of(
                name.style(Style.style(EternaColors.AMBER, TextDecoration.BOLD)),
                Component.text("Start", EternaColors.GREEN)
        );
    }
    
    /**
     * Gets the {@link ComponentList} used for {@code finish} {@link Hologram}.
     *
     * @return the component list used for {@code finish} hologram.
     */
    @NotNull
    public ComponentList hologramTextFinish() {
        return ComponentList.of(
                name.style(Style.style(EternaColors.AMBER, TextDecoration.BOLD)),
                Component.text("Finish", NamedTextColor.GREEN)
        );
    }
    
    /**
     * Gets the {@link ComponentList} used for {@code checkpoint} {@link Hologram}.
     *
     * @param nth - The index of the checkpoint, starting at {@code 1}.
     * @param max - The max number of checkpoint.
     * @return the component list used for {@code checkpoint} hologram.
     */
    @NotNull
    public ComponentList hologramTextCheckpoint(int nth, int max) {
        return ComponentList.of(
                Component.empty()
                         .append(Component.text("Checkpoint ", EternaColors.GREEN))
                         .append(Component.text("(%s/%s)".formatted(nth, max), EternaColors.GRAY))
        );
    }
    
    /**
     * Gets the {@link Difficulty} of this {@link Parkour}.
     *
     * @return the difficulty of this parkour.
     */
    @NotNull
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * Sets the {@link Difficulty} of this {@link Parkour}.
     *
     * @param difficulty - The new difficulty.
     */
    public void setDifficulty(@NotNull Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    /**
     * Gets the name of this {@link Parkour}.
     *
     * @return the name of this parkour.
     */
    @Override
    @NotNull
    public Component getName() {
        return name;
    }
    
    /**
     * Gets the {@code start} {@link ParkourPosition} of this {@link Parkour}
     *
     * @return the {@code start} position of this parkour.
     */
    @NotNull
    public ParkourPosition getStart() {
        return this.start;
    }
    
    /**
     * Gets the {@code finish} {@link ParkourPosition} of this {@link Parkour}
     *
     * @return the {@code finish} position of this parkour.
     */
    @NotNull
    public ParkourPosition getFinish() {
        return this.finish;
    }
    
    /**
     * Gets an <b>immutable</b> copy of all checkpoints.
     *
     * @return an <b>immutable</b> copy of all checkpoints.
     */
    @NotNull
    public List<ParkourPosition> getCheckpoints() {
        return List.copyOf(this.checkpoints);
    }
    
    /**
     * Gets the number of total checkpoints for this {@link Parkour}.
     *
     * @return the number of total checkpoints for this parkour.
     */
    public int checkpointCount() {
        return checkpoints.size();
    }
    
    /**
     * Ticks this {@link Parkour}.
     */
    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            final Location location = player.getLocation();
            
            for (Hologram hologram : holograms) {
                final double distanceSquared = LocationHelper.distanceSquared(hologram.getLocation(), location);
                final boolean canBeSeen = distanceSquared <= HOLOGRAM_VIEW_DISTANCE;
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
    
    /**
     * Gets the {@link Key} of this {@link Parkour}.
     *
     * @return the key of this parkour.
     */
    @NotNull
    @Override
    public final Key getKey() {
        return key;
    }
    
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.key);
    }
    
    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final Parkour that = (Parkour) o;
        return Objects.equals(this.key, that.key);
    }
    
    @ApiStatus.Internal
    private void createWorldEntities() {
        this.start.setBlock(MATERIAL_START_AND_FINISH);
        this.finish.setBlock(MATERIAL_START_AND_FINISH);
        
        this.checkpoints.forEach(position -> position.setBlock(MATERIAL_CHECKPOINT));
        
        this.createHolograms();
    }
    
    @ApiStatus.Internal
    private void createHolograms() {
        makeHologram(start.getLocationCentered(), hologramTextStart());
        makeHologram(finish.getLocationCentered(), hologramTextFinish());
        
        final int checkpointsSize = checkpoints.size();
        
        for (int i = 0; i < checkpointsSize; i++) {
            final ParkourPosition current = checkpoints.get(i);
            
            makeHologram(current.getLocationCentered(), hologramTextCheckpoint(i + 1, checkpointsSize));
        }
    }
    
    @ApiStatus.Internal
    private void makeHologram(@NotNull Location location, @NotNull ComponentList text) {
        final Hologram hologram = Hologram.ofArmorStand(location);
        hologram.setLines(p -> text);
        
        this.holograms.add(hologram);
    }
    
    @ApiStatus.Internal
    void removeWorldEntities() {
        this.start.restoreBlock();
        this.finish.restoreBlock();
        
        this.checkpoints.forEach(ParkourPosition::restoreBlock);
        
        this.holograms.forEach(Hologram::dispose);
        this.holograms.clear();
    }
    
    @ApiStatus.Internal
    void showHolograms(@NotNull Player player) {
        this.holograms.forEach(holo -> holo.show(player));
    }
    
    @ApiStatus.Internal
    void hideHolograms(@NotNull Player player) {
        this.holograms.forEach(holo -> holo.hide(player));
    }
    
}
