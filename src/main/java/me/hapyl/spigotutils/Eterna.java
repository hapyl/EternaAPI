package me.hapyl.spigotutils;

import me.hapyl.spigotutils.builtin.updater.Updater;
import me.hapyl.spigotutils.protocol.EternaProtocol;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    /**
     * Gets the {@link EternaProtocol} for the plugin.
     *
     * @return the protocol.
     */
    @Nonnull
    public static EternaProtocol getProtocol() {
        return plugin.protocol;
    }

    /**
     * Gets a {@link Set} of online {@link Player} who is a {@link Player#isOp()}.
     *
     * @return a set of operator players.
     */
    @Nonnull
    public static Set<Player> getOnlineOperators() {
        return Bukkit.getOnlinePlayers().stream().filter(Player::isOp).collect(Collectors.toSet());
    }
}
