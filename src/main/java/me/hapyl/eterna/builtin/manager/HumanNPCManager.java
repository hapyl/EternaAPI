package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.util.Disposable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public final class HumanNPCManager extends EternaManager<Integer, HumanNPC> implements Disposable {

    HumanNPCManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
    }

    @Nonnull
    public Map<Integer, HumanNPC> getRegistered() {
        return new HashMap<>(managing);
    }

    public void register(@Nonnull HumanNPC npc) {
        register(npc.getId(), npc);
    }

    @Override
    public void dispose() {
        managing.values().forEach(HumanNPC::remove0);
        managing.clear();
    }

}
