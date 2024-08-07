package me.hapyl.eterna;

import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;

public class EternaConfig {

    private final EternaPlugin plugin;
    private final FileConfiguration configuration;

    EternaConfig(EternaPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
    }

    public boolean isTrue(@Nonnull EternaConfigValue value) {
        return configuration.getBoolean(value.key, (Boolean) value.defaultValue);
    }

}
