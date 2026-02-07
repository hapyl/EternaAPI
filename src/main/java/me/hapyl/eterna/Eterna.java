package me.hapyl.eterna;

import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Represents a utility class for {@link EternaAPI}.
 */
@UtilityClass
public final class Eterna {
    
    static EternaPlugin plugin;
    
    private Eterna() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets the {@link EternaPlugin} instance.
     *
     * @return the eterna plugin instance.
     */
    @NotNull
    public static EternaPlugin getPlugin() {
        return plugin;
    }
    
    /**
     * Gets the {@link EternaConfig} instance.
     *
     * @return the eterna config instance.
     */
    @NotNull
    public static EternaConfig getConfig() {
        return plugin.config;
    }
    
    /**
     * Gets the {@link Updater} instance.
     *
     * @return the updater instance.
     */
    @NotNull
    public static Updater getUpdater() {
        return plugin.updater;
    }
    
    /**
     * Gets the {@link Logger} for {@link EternaPlugin}.
     *
     * @return the logger for the plugin.
     */
    @NotNull
    public static Logger getLogger() {
        return plugin.getLogger();
    }
}
