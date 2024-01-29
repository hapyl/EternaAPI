package me.hapyl.spigotutils.module.reflect.npc;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

// FIXME (hapyl): 029, Jan 29: I'm so confused why this creates another map?
public class HumanNPCRegistry extends Registry<Integer, HumanNPC> {
    private final Map<Integer, HumanNPC> byId;

    public HumanNPCRegistry(EternaPlugin plugin) {
        super(plugin);
        byId = Maps.newHashMap();
    }

    @Override
    public void register(Integer integer, HumanNPC humanNPC) {
        byId.put(integer, humanNPC);
    }

    @Override
    public void unregister(Integer integer, HumanNPC humanNPC) {
        byId.remove(integer);
    }

    @Nonnull
    public Map<Integer, HumanNPC> getRegistered() {
        return byId;
    }

    public void remove(int id) {
        byId.remove(id);
    }

    public void register(HumanNPC npc) {
        register(npc.getId(), npc);
    }

    public boolean isRegistered(int id) {
        return byId.containsKey(id);
    }

    @Nullable
    public HumanNPC getById(int id) {
        return byId.get(id);
    }

    public void removeAll() {
        byId.values().forEach(HumanNPC::remove0);
        byId.clear();
    }
}
