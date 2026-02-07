package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an internal glowing runnable.
 */
@ApiStatus.Internal
public final class GlowingRunnable extends EternaKeyed implements Runnable {
    
    public GlowingRunnable(@NotNull EternaKey key) {
        super(key);
    }
    
    @Override
    public void run() {
        final GlowingHandler handler = GlowingHandler.handler;
        
        handler.emptyOrphans();
        handler.forEach(GlowingImpl::tick);
    }
}
