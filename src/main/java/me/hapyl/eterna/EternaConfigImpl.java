package me.hapyl.eterna;

import me.hapyl.eterna.module.resource.YamlResourceLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public final class EternaConfigImpl extends YamlResourceLoader implements EternaConfig {
    
    private TickRate tickRate;
    
    EternaConfigImpl(@NotNull EternaKey key, @NotNull EternaPlugin plugin) {
        super(plugin, "config.yml");
        
        key.validateKey();
    }
    
    @Override
    public boolean checkForUpdates() {
        return get("check-for-updates").toBoolean();
    }
    
    @Override
    public boolean keepTestCommands() {
        return get("keep-test-commands").toBoolean();
    }
    
    @Override
    public boolean allowQuestJournal() {
        return get("allow-quest-journal").toBoolean();
    }
    
    @NotNull
    @Override
    public TickRate tickRate() {
        if (tickRate == null) {
            tickRate = new TickRate() {
                @Override
                public int npc() {
                    return Math.max(1, get("tick-rate.npc").toInt());
                }
                
                @Override
                public int parkour() {
                    return Math.max(1, get("tick-rate.parkour").toInt());
                }
            };
        }
        
        return tickRate;
    }
    
    @Override
    @NotNull
    public CompletableFuture<Void> reload() {
        tickRate = null; // Schedule tickRate reload
        
        return super.reload();
    }
}
