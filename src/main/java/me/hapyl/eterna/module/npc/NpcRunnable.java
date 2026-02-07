package me.hapyl.eterna.module.npc;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class NpcRunnable extends EternaKeyed implements Runnable {
    public NpcRunnable(@NotNull EternaKey key) {
        super(key);
    }
    
    @Override
    public void run() {
        NpcHandler.handler.forEach(Npc::tick);
    }
}
