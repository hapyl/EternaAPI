package me.hapyl.spigotutils.module.math.geometry;

import org.bukkit.Location;

import javax.annotation.Nonnull;

/**
 * Represents drawable that is used in {@link me.hapyl.spigotutils.module.math.Geometry}.
 */
public interface Drawable {

    void draw(@Nonnull Location location);

}
