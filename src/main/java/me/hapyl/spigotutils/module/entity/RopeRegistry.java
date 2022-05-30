package me.hapyl.spigotutils.module.entity;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.HashRegistry;

import javax.annotation.Nullable;
import java.util.Map;

public class RopeRegistry extends HashRegistry<Integer, Rope> {

    private final Map<Integer, Rope> byId;
    private int freeId;

    public RopeRegistry(EternaPlugin plugin) {
        super(plugin);
        byId = Maps.newHashMap();
        freeId = 0;
    }

    @Override
    public void register(Rope rope) {
        rope.setId(freeId);
        register(freeId, rope);
        freeId++;
    }

    @Override
    public void register(Integer integer, Rope rope) {
        byId.put(integer, rope);
    }

    @Override
    public void unregister(Integer integer, Rope rope) {
        byId.remove(integer);
    }

    @Nullable
    public Rope getById(int id) {
        return byId.get(id);
    }

    public void removeAll() {
        byId.forEach((id, rope) -> {
            if (rope.isRemoveAtRestart()) {
                rope.remove();
            }
        });
    }

}
