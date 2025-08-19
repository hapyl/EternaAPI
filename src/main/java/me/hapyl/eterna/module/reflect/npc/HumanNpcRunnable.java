package me.hapyl.eterna.module.reflect.npc;

import me.hapyl.eterna.Eterna;

public class HumanNpcRunnable implements Runnable {
    
    @Override
    public void run() {
        Eterna.getManagers().npc.getRegistered().values().forEach(HumanNPC::tick);
    }
    
}
