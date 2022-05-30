package me.hapyl.spigotutils.module.reflect.npc;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.HashRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;

public class HumanNPCRegistry extends HashRegistry<Integer, HumanNPC> {
    private final HashMap<Integer, HumanNPC> byId;

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

    public HashMap<Integer, HumanNPC> getRegistered() {
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
        for (HumanNPC value : byId.values()) {
            value.remove();
        }
    }
}
