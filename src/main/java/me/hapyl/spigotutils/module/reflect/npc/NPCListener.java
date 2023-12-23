package me.hapyl.spigotutils.module.reflect.npc;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface NPCListener {

    void onClick(@Nonnull Player player, @Nonnull ClickType type);

    default void onSpawn() {
        throw new NotImplementedException();
    }

}
