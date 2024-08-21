package me.hapyl.eterna.module.block.display;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.util.Runnables;
import org.bukkit.Location;
import org.bukkit.entity.Display;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents data for displays to later spawn.
 */
public class DisplayData {

    /**
     * The default delay before teleporting the entity.
     */
    public static final int TELEPORTATION_DELAY = 2;

    private final List<DisplayDataObject<?>> objects;

    public DisplayData() {
        this.objects = Lists.newArrayList();
    }

    /**
     * Appends {@link DisplayDataObject}.
     *
     * @param object - Object to append.
     */
    public void append(DisplayDataObject<?> object) {
        objects.add(object);
    }

    /**
     * Removes {@link DisplayDataObject}.
     *
     * @param object - Object to remove.
     * @return true if removed; false otherwise.
     */
    public boolean remove(DisplayDataObject<?> object) {
        return objects.remove(object);
    }

    /**
     * Gets the element by its index; or null, if not present, or index is >= size.
     *
     * @param index - Index
     * @return the element.
     */
    @Nullable
    public DisplayDataObject<?> get(int index) {
        if (index >= objects.size()) {
            return null;
        }

        return objects.get(index);
    }

    /**
     * Returns the size of objects.
     *
     * @return the size of objects.
     */
    public int size() {
        return objects.size();
    }

    /**
     * Spawns the displays according to the {@link #objects}.
     *
     * @param location - Location to spawn at.
     * @param consumer - Consumer to apply to each block display, including the head.
     * @return a display entity.
     */
    @Nonnull
    public DisplayEntity spawn(@Nonnull Location location, @Nullable Consumer<Display> consumer) {
        final DisplayEntity displayEntity = new DisplayEntity(Entities.BLOCK_DISPLAY.spawn(location, self -> {
            if (consumer != null) {
                consumer.accept(self);
            }
        }));

        if (size() == 0) {
            return displayEntity;
        }

        for (DisplayDataObject<?> object : objects) {
            displayEntity.append(object.create(location), consumer);
        }

        return displayEntity;
    }

    /**
     * Spawns the displays according to the {@link #objects}.
     *
     * @param location - Location to spawn at.
     * @return a display entity.
     */
    @Nonnull
    public DisplayEntity spawn(@Nonnull Location location) {
        return spawn(location, null);
    }

    /**
     * Spawns the displays at the given location with teleport duration of 1.
     * <p>
     * Meaning the entities will interpolate on teleport.
     *
     * @param location - Location.
     * @return a display entity.
     */
    @Nonnull
    public DisplayEntity spawnInterpolated(@Nonnull Location location) {
        final DisplayEntity entity = spawn(location);
        entity.setTeleportDuration(1);

        return entity;
    }

    /**
     * Spawns the displays at the given {@link Location}, accepts the {@link Consumer} and teleports to the {@link Location}.
     * <br>
     * <pre>{@code
     * final DisplayData data = BDEngine.parse(...);
     *
     * data.spawnInterpolated(
     *     player.getLocation(),
     *     player.getLocation().add(0, 5, 0),
     *     self -> {
     *         self.setTeleportDuration(20);
     *     }
     * );
     * }</pre>
     * <br>
     * Will spawn the entity at the player's location, and teleport it five blocks above after {@link #TELEPORTATION_DELAY} ticks over twenty ticks.
     *
     * @param location    - Location to spawn at.
     * @param endLocation - Location to teleport to.
     * @param consumer    - Consumer to accept.
     * @return a display entity.
     */
    @Nonnull
    public DisplayEntity spawnInterpolated(@Nonnull Location location, @Nonnull Location endLocation, @Nonnull Consumer<Display> consumer) {
        return spawnInterpolated(location, endLocation, consumer, TELEPORTATION_DELAY);
    }

    /**
     * Spawns the displays at the given {@link Location}, accepts the {@link Consumer} and teleports to the {@link Location}.
     * <br>
     * <pre>{@code
     * final DisplayData data = BDEngine.parse(...);
     *
     * data.spawnInterpolated(
     *     player.getLocation(),
     *     player.getLocation().add(0, 5, 0),
     *     self -> {
     *         self.setTeleportDuration(20);
     *     }, 5
     * );
     * }</pre>
     * <br>
     * Will spawn the entity at the player's location, and teleport it five blocks above after five ticks over twenty ticks.
     *
     * @param location      - Location to spawn at.
     * @param endLocation   - Location to teleport to.
     * @param consumer      - Consumer to accept.
     * @param teleportDelay - Delay before teleporting.
     *                      Ticks are weird, sometimes it works with <code>1</code> sometimes only with <code>2</code>, leaving this here for custom delay.
     * @return a display entity.
     */
    @Nonnull
    public DisplayEntity spawnInterpolated(@Nonnull Location location, @Nonnull Location endLocation, @Nonnull Consumer<Display> consumer, int teleportDelay) {
        final DisplayEntity entity = spawn(location, consumer);

        // Teleport a little later because it doesn't work at the same tick yep
        Runnables.runLater(() -> entity.teleport(endLocation), teleportDelay);

        return entity;
    }

}
