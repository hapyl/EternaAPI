package me.hapyl.eterna.module.reflect.access;

import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class IAccess<Bukkit, Minecraft> {

    private final Map<Bukkit, Minecraft> cache;

    protected IAccess() {
        this.cache = Maps.newConcurrentMap();
    }

    @Nonnull
    public Minecraft access(@Nonnull Bukkit bukkit) {
        Minecraft minecraft = cache.get(bukkit);

        if (minecraft == null) {
            cache.put(bukkit, minecraft = createAccess(bukkit));
        }

        return minecraft;
    }

    @Nonnull
    protected abstract Minecraft createAccess(@Nonnull Bukkit bukkit);

}
