package me.hapyl.eterna.module.block.display;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

/**
 * Represents a base display data object.
 * Display data objects hold key information on how to spawn display entity.
 */
public abstract class DisplayDataObject<T> {

    @Nonnull
    protected final T object;
    @Nonnull
    protected final Matrix4f matrix4f;

    public DisplayDataObject(@Nonnull T object, @Nonnull Matrix4f matrix4f) {
        this.object = object;
        this.matrix4f = matrix4f;
    }

    public abstract Display create(@Nonnull Location location);
}
