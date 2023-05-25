package me.hapyl.spigotutils.module.block.display;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.entity.Entities;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents data for block displays to later spawn.
 */
public class BlockDisplayData {

    private final List<BlockDisplayDataObject> objects;

    public BlockDisplayData() {
        this.objects = Lists.newArrayList();
    }

    /**
     * Appends {@link BlockDisplayDataObject}.
     *
     * @param object - Object to append.
     */
    public void append(BlockDisplayDataObject object) {
        objects.add(object);
    }

    /**
     * Removes {@link BlockDisplayDataObject}.
     *
     * @param object - Object to remove.
     * @return true if removed; false otherwise.
     */
    public boolean remove(BlockDisplayDataObject object) {
        return objects.remove(object);
    }

    /**
     * Gets the element by its index; or null, if not present, or index is >= size.
     *
     * @param index - Index
     * @return the element.
     */
    @Nullable
    public BlockDisplayDataObject get(int index) {
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
     * Spawns the block displays according to the {@link #objects}.
     *
     * @param location - Location to spawn at.
     * @param consumer - Consumer to apply to each block display, including the head.
     * @return a list of spawned block displays, where the first element is the head, and others are passengers of the head. The list will <b>always</b> contain at least the head.
     */
    @Nonnull
    public List<BlockDisplay> spawn(@Nonnull Location location, @Nullable Consumer<BlockDisplay> consumer) {
        final List<BlockDisplay> list = Lists.newArrayList();

        // Head
        final BlockDisplay head = Entities.BLOCK_DISPLAY.spawn(location);
        list.add(head);

        if (consumer != null) {
            consumer.accept(head);
        }

        if (size() == 0) {
            return list;
        }

        for (BlockDisplayDataObject object : objects) {
            list.add(Entities.BLOCK_DISPLAY.spawn(location, self -> {
                head.addPassenger(self);

                self.setBlock(object.getBlockData());
                self.setTransformationMatrix(object.getMatrix());

                if (consumer != null) {
                    consumer.accept(self);
                }
            }));
        }

        return list;
    }

    /**
     * Spawns the block displays according to the {@link #objects}.
     *
     * @param location - Location to spawn at.
     * @return a list of spawned block displays, where the first element is the head, and others are passengers of the head. The list will <b>always</b> contain at least the head.
     */
    @Nonnull
    public List<BlockDisplay> spawn(@Nonnull Location location) {
        return spawn(location, null);
    }

}
