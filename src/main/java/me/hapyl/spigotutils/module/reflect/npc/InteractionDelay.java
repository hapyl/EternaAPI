package me.hapyl.spigotutils.module.reflect.npc;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public record InteractionDelay(@Nonnull Player player, long interactedAt, long delay) {

    public InteractionDelay(@Nonnull Player player, long delay) {
        this(player, System.currentTimeMillis(), delay);
    }

    public boolean isOver() {
        return System.currentTimeMillis() - interactedAt > delay;
    }

}
