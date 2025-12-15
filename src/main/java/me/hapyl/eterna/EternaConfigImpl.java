package me.hapyl.eterna;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public final class EternaConfigImpl extends EternaLock implements EternaConfig {
    
    private final FileConfiguration cfg;
    private final TickRate tickRate;
    
    EternaConfigImpl(@Nonnull EternaKey key, @Nonnull EternaPlugin plugin) {
        super(key);
        
        this.cfg = plugin.getConfig();
        this.tickRate = new TickRate() {
            private static final String parent = "tick-rate.";
            
            @Override
            public int npc() {
                return Math.max(1, cfg.getInt(parent + "npc"));
            }
            
            @Override
            public int parkour() {
                return Math.max(1, cfg.getInt(parent + "parkour"));
            }
        };
    }
    
    @Override
    public boolean checkForUpdates() {
        return cfg.getBoolean("check-for-updates");
    }
    
    @Override
    public boolean keepTestCommands() {
        return cfg.getBoolean("keep-test-commands");
    }
    
    @Override
    public boolean printStackTraces() {
        return cfg.getBoolean("print-stack-traces");
    }
    
    @Nonnull
    @Override
    public TickRate tickRate() {
        return tickRate;
    }
}
