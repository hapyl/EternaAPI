package me.hapyl.eterna.module.block.display;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.entity.Entities;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a parsed data for {@link DisplayEntity}.
 */
public class DisplayData {
    
    /**
     * The default delay before teleporting the entity.
     */
    public static final int TELEPORTATION_DELAY = 2;
    
    private final List<DisplayObject<?>> objects;
    
    /**
     * Creates a new {@link DisplayData}.
     */
    public DisplayData() {
        this.objects = Lists.newArrayList();
    }
    
    /**
     * Appends the given {@link DisplayObject}.
     *
     * @param object - The object to append.
     */
    public void append(@NotNull DisplayObject<?> object) {
        objects.add(object);
    }
    
    /**
     * Removes the given {@link DisplayObject}.
     *
     * @param object - The object to remove.
     * @return {@code true} if removed; {@code false} otherwise.
     */
    public boolean remove(@NotNull DisplayObject<?> object) {
        return objects.remove(object);
    }
    
    /**
     * Gets the {@link DisplayObject} by its {@code index}; or {@code null} if it doesn't index.
     *
     * @param index - The index.
     * @return the element.
     */
    @Nullable
    public DisplayObject<?> get(int index) {
        if (index < 0 || index >= objects.size()) {
            return null;
        }
        
        return objects.get(index);
    }
    
    /**
     * Gets the size of this display data.
     *
     * @return the size of this display data.
     */
    public int size() {
        return objects.size();
    }
    
    /**
     * Spawns the displays according to the {@link #objects}.
     *
     * @param location - The location to spawn at.
     * @param consumer - The consumer to apply to each {@link BlockDisplay}, including the head.
     * @return the spawned display entity.
     */
    @NotNull
    public DisplayEntity spawn(@NotNull Location location, @Nullable Consumer<Display> consumer) {
        final DisplayEntity displayEntity = new DisplayEntity(Entities.BLOCK_DISPLAY.spawn(
                location, self -> {
                    if (consumer != null) {
                        consumer.accept(self);
                    }
                }
        ));
        
        if (size() == 0) {
            return displayEntity;
        }
        
        for (DisplayObject<?> object : objects) {
            displayEntity.append(object.create(location, consumer));
        }
        
        return displayEntity;
    }
    
    /**
     * Spawns the displays according to the {@link #objects}.
     *
     * @param location - The location to spawn at.
     * @return the spawned display entity.
     */
    @NotNull
    public DisplayEntity spawn(@NotNull Location location) {
        return spawn(location, null);
    }
    
    /**
     * Spawns the displays at the given location with teleport duration of {@code 1}, meaning the entities will interpolate on teleport.
     *
     * @param location - The location to spawn at.
     * @return the spawned display entity.
     */
    @NotNull
    public DisplayEntity spawnInterpolated(@NotNull Location location) {
        final DisplayEntity entity = spawn(location);
        entity.setTeleportDuration(1);
        
        return entity;
    }
    
}
