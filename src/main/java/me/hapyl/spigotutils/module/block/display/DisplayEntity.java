package me.hapyl.spigotutils.module.block.display;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a display entity.
 * An entity consists of a head (always present) and children.
 */
public class DisplayEntity implements Iterable<Display> {

    private final BlockDisplay head;
    private final List<Display> children;

    public DisplayEntity(@Nonnull BlockDisplay head) {
        this.head = head;
        this.children = Lists.newArrayList();
        this.children.add(head);
    }

    /**
     * Teleports the entity to the given location.
     *
     * @param location - Location to teleport to.
     */
    public void teleport(@Nonnull Location location) {
        head.teleport(location);
        children.forEach(display -> display.teleport(location));
    }

    /**
     * Removes the entity, including the head and children.
     */
    public void remove() {
        head.remove();

        children.forEach(Display::remove);
        children.clear();
    }

    /**
     * Gets a copy of all entities, including head and children.
     *
     * @return Gets a copy of all entities, including head and children.
     */
    @Nonnull
    public List<Display> getEntities() {
        return Lists.newArrayList(children);
    }

    /**
     * Gets the head of this entity.
     *
     * @return the head of this entity.
     */
    @Nonnull
    public BlockDisplay getHead() {
        return head;
    }

    /**
     * Gets an iterator for this entity.
     *
     * @return an iterator.
     */
    @Override
    public Iterator<Display> iterator() {
        return children.iterator();
    }

    /**
     * Adds a display as a child.
     *
     * @param display  - Display to add.
     * @param consumer - Consumer if needed.
     */
    protected void append(@Nonnull Display display, @Nullable Consumer<Display> consumer) {
        head.addPassenger(display);
        children.add(display);

        if (consumer != null) {
            consumer.accept(display);
        }
    }
}
