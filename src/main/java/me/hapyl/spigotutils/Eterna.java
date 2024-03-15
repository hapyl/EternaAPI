package me.hapyl.spigotutils;

import me.hapyl.spigotutils.builtin.updater.Updater;
import me.hapyl.spigotutils.registry.EternaRegistry;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * A helper class for {@link EternaPlugin}.
 */
public final class Eterna {

    static EternaPlugin plugin;

    /**
     * Gets the {@link EternaRegistry} singleton for the plugin.
     *
     * @return the registry.
     */
    @Nonnull
    public static EternaRegistry getRegistry() {
        return plugin.registry;
    }

    /**
     * Gets the {@link EternaConfig} singleton for the plugin.
     *
     * @return the config.
     */
    @Nonnull
    public static EternaConfig getConfig() {
        return plugin.config;
    }

    /**
     * Gets the {@link Updater} singleton for the plugin.
     *
     * @return the updater.
     */
    @Nonnull
    public static Updater getUpdater() {
        return plugin.updater;
    }

    /**
     * Gets the {@link Logger} for the plugin.
     *
     * @return the logger.
     */
    @Nonnull
    public static Logger getLogger() {
        return plugin.getLogger();
    }

}
