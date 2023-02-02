package me.hapyl.spigotutils.config;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.registry.Registry;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PlayerConfigManager extends Registry<UUID, PlayerConfig> {

    protected final Map<UUID, PlayerConfig> playerConfig;

    public PlayerConfigManager(EternaPlugin plugin) {
        super(plugin);
        playerConfig = Maps.newHashMap();
    }

    // Returns PlayerConfig instance is present, else creates a new one.
    public PlayerConfig getConfig(Player player) {
        PlayerConfig config = playerConfig.get(player.getUniqueId());
        if (config == null) {
            config = new PlayerConfig(player);
            playerConfig.put(player.getUniqueId(), config);
        }
        return config;
    }

    public void saveAllData() {
        playerConfig.forEach((a, b) -> b.save());
    }

}
