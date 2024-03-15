package me.hapyl.spigotutils.module.reflect.npc;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.registry.Registry;

import javax.annotation.Nonnull;
import java.util.Map;

public class HumanNPCRegistry extends Registry<Integer, HumanNPC> {

    public HumanNPCRegistry(EternaPlugin plugin) {
        super(plugin);
    }

    @Nonnull
    public Map<Integer, HumanNPC> getRegistered() {
        return getRegistryCopy();
    }

    public void register(@Nonnull HumanNPC npc) {
        register(npc.getId(), npc);
    }

    public void removeAll() {
        registry.values().forEach(HumanNPC::remove0);
        registry.clear();
    }

}
