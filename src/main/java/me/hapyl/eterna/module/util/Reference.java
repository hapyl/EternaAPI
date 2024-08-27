package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.entity.Entities;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

/**
 * Represents a functional reference to an object, providing a way to retrieve or create an instance when needed.
 * <pre>{@code
 * Location location = ...
 *
 * example(() -> Entities.PIG.spawn(location));
 *
 * void example(@Nonnull Reference<Entity> reference) {
 *     Entity entity = getEntity();
 *
 *     if (entity == null) {
 *         entity = reference.refer();
 *     }
 * }
 * }</pre>
 * The above example will <b>only</b> create the pig when the entity doesn't exist already.
 *
 * @param <R>
 */
@FunctionalInterface
public interface Reference<R> {

    /**
     * Retrieves or creates an instance of the object.
     *
     * @return the object instance.
     */
    @Nonnull
    R refer();

}
