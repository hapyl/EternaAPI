package me.hapyl.spigotutils.module.player.tablist;

import javax.annotation.Nonnull;

public interface EntryConsumer {

    void apply(@Nonnull TablistEntry entry);

}
