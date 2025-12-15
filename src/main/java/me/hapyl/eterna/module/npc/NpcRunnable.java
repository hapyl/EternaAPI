package me.hapyl.eterna.module.npc;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public class NpcRunnable extends EternaLock implements Runnable {
    public NpcRunnable(@Nonnull EternaKey key) {
        super(key);
    }
    
    @Override
    public void run() {
        Eterna.getManagers().npc.forEach(Npc::tick);
    }
}
