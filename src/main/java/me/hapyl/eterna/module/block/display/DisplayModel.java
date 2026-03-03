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
 * Represents a parsed {@link BDEngine} {@link Display} model that holds display entity data.
 */
public class DisplayModel {
    
    protected final List<DisplayObject<?>> objects;
    
    DisplayModel() {
        this.objects = Lists.newArrayList();
    }
    
    /**
     * Spawns this {@link DisplayModel} at the given {@link Location} after applying the given {@link Consumer} to each {@link Display}.
     *
     * @param location - The location where to spawn the display entity.
     * @param consumer - The optional consumer to apply to each display entity.
     * @return a spawned display entity.
     */
    @NotNull
    public DisplayEntity spawn(@NotNull Location location, @Nullable Consumer<Display> consumer) {
        return new DisplayEntity(createHead(location, consumer), objects.stream().map(object -> object.create(location, consumer)).toList());
    }
    
    /**
     * Spawns this {@link DisplayModel} at the given {@link Location}.
     *
     * @param location - The location where to spawn the display entity.
     * @return a spawned display entity.
     */
    @NotNull
    public DisplayEntity spawn(@NotNull Location location) {
        return spawn(location, null);
    }
    
    /**
     * Spawns this {@link DisplayModel} at the given {@link Location} with a {@code teleport_duration} set to {@code 1} for each {@link Display}.
     *
     * @param location - The location where to spawn the display entity.
     * @return a spawned display entity.
     */
    @NotNull
    public DisplayEntity spawnInterpolated(@NotNull Location location) {
        return this.spawn(location, self -> self.setTeleportDuration(1));
    }
    
    @NotNull
    private static BlockDisplay createHead(@NotNull Location location, @Nullable Consumer<Display> consumer) {
        return Entities.BLOCK_DISPLAY.spawn(location, self -> {
            if (consumer != null) {
                consumer.accept(self);
            }
        });
    }
    
}
