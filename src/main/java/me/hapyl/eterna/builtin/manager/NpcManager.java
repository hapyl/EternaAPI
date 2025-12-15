package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.util.Disposable;

import javax.annotation.Nonnull;

public class NpcManager extends EternaManager<Integer, Npc> implements Disposable {
    NpcManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
    }
    
    @Override
    public void dispose() {
        this.forEach(Npc::destroy);
    }
}
